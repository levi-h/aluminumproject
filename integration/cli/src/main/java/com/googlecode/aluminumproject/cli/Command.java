/*
 * Copyright 2009-2010 Levi Hoogenberg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.googlecode.aluminumproject.cli;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.OutputStreamAppender;

import com.googlecode.aluminumproject.Logger;
import com.googlecode.aluminumproject.utilities.Utilities;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import joptsimple.OptionException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

import org.slf4j.LoggerFactory;

/**
 * A command that will be executed from a command line. <a href="http://jopt-simple.sourceforge.net/">JOpt Simple</a>
 * will be used to parse the command-line parameters.
 * <p>
 * Subclasses should {@link #addOption(OptionSpec, OptionEffect) add} all of the options that they support. The
 * following options are added by default:
 * <ul>
 * <li>The debug option ({@code --debug}), that logs debug statements to the output stream;
 * <li>The help option ({@code -h} or {@code --help}), that makes sure that when the command is executed, a help
 *     message is printed to the output stream;
 * <li>The stack trace option ({@code --print-stack-traces}), that causes exceptions to print their stack traces to the
 *     error stream.
 * </ul>
 * When a command is executed, its options are processed first. After that, the {@link #execute(PrintStream,
 * PrintStream, List) execute method} is invoked with the command-line arguments that do not belong to any option.
 *
 * @author levi_h
 */
public abstract class Command {
	private OptionParser optionParser;

	private Map<OptionSpec<?>, OptionEffect<?>> optionEffects;

	private Level logLevel;

	private boolean displayHelp;
	private boolean printStackTraces;

	/** The logger to use. */
	protected final Logger logger;

	/**
	 * Creates a command.
	 */
	protected Command() {
		optionParser = new OptionParser();

		optionEffects = new IdentityHashMap<OptionSpec<?>, OptionEffect<?>>();

		logLevel = Level.OFF;

		logger = Logger.get(getClass());

		addCommonOptions();
	}

	private void addCommonOptions() {
		OptionSpec<Object> debug =
			Utilities.typed(optionParser.accepts("debug", "Prints debug statements."));
		addOption(debug, new PropertyEffect<Level>(this, Level.class, "logLevel", Level.DEBUG));

		OptionSpec<Object> displayHelp =
			Utilities.typed(optionParser.acceptsAll(Arrays.asList("h", "help"), "Displays this help message."));
		addOption(displayHelp, new PropertyEffect<Boolean>(this, Boolean.TYPE, "displayHelp", true));

		OptionSpec<Object> printStackTraces =
			Utilities.typed(optionParser.accepts("print-stack-traces", "Prints the stack traces of exceptions."));
		addOption(printStackTraces, new PropertyEffect<Boolean>(this, Boolean.TYPE, "printStackTraces", true));
	}

	/**
	 * Returns the option parser that should be used to add options to.
	 *
	 * @return the option parser to use
	 */
	protected OptionParser getOptionParser() {
		return optionParser;
	}

	/**
	 * Adds an option and the effect that it has when it is supplied.
	 *
	 * @param <T> the type of the option's argument
	 * @param option the option to accept
	 * @param effect the effect of the option
	 */
	protected <T> void addOption(OptionSpec<T> option, OptionEffect<T> effect) {
		optionEffects.put(option, effect);
	}

	/**
	 * Sets whether a help message should be displayed or not.
	 *
	 * @param displayHelp {@code true} to display a help message when this command is executed or {@code false} (the
	 *                    default) to actually execute it
	 */
	public void setDisplayHelp(boolean displayHelp) {
		this.displayHelp = displayHelp;
	}

	/**
	 * Sets the threshold at which log statements will be printed.
	 *
	 * @param logLevel the log level to use
	 */
	public void setLogLevel(Level logLevel) {
		this.logLevel = logLevel;
	}

	/**
	 * Sets whether stack traces of exceptions are printed. They aren't by default.
	 *
	 * @param printStackTraces {@code true} to print stack traces to the error stream, {@code false} to hide them from
	 *                         the user
	 */
	public void setPrintStackTraces(boolean printStackTraces) {
		this.printStackTraces = printStackTraces;
	}

	/**
	 * Executes this command.
	 *
	 * @param outputStream the stream to write output to
	 * @param errorStream the stream to write errors to
	 * @param parameters the command-line parameters
	 */
	public final void execute(PrintStream outputStream, PrintStream errorStream, String... parameters) {
		try {
			OptionSet options = getOptionParser().parse(parameters);

			for (Map.Entry<OptionSpec<?>, OptionEffect<?>> entry: optionEffects.entrySet()) {
				OptionSpec<Object> option = Utilities.typed(entry.getKey());

				if (options.has(option)) {
					OptionEffect<Object> effect = Utilities.typed(entry.getValue());

					effect.apply(options, option);
				}
			}

			initialiseLogging(outputStream);

			if (displayHelp) {
				displayHelp(outputStream, errorStream);
			} else {
				execute(outputStream, errorStream, options.nonOptionArguments());
			}
		} catch (OptionException exception) {
			errorStream.printf("%s.%n", exception.getMessage());
		} catch (CommandException exception) {
			errorStream.println(exception.getMessage());

			handleThrowable(exception.getCause(), errorStream);
		}
	}

	/**
	 * Executes this command.
	 *
	 * @param outputStream the output stream to use
	 * @param errorStream the error stream to use
	 * @param arguments the command-line arguments that do not belong to any option
	 * @throws CommandException when this command can't be executed
	 */
	protected abstract void execute(
		PrintStream outputStream, PrintStream errorStream, List<String> arguments) throws CommandException;

	private void initialiseLogging(final PrintStream outputStream) {
		LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

		loggerContext.reset();

		PatternLayout layout = new PatternLayout();
		layout.setPattern("%p: %m%n");
		layout.setContext(loggerContext);
		layout.start();

		OutputStreamAppender<ILoggingEvent> appender = new OutputStreamAppender<ILoggingEvent>() {
			@Override
			public void start() {
				setOutputStream(outputStream);

				super.start();
			}

			@Override
			protected void closeOutputStream() {
				// the underlying output stream is either System.out (when running standalone), which we can't close, or
				// a byte array output stream (when testing), which we don't need to close
			}
		};
		appender.setContext(loggerContext);
		appender.setLayout(layout);
		appender.start();

		for (ch.qos.logback.classic.Logger logger: loggerContext.getLoggerList()) {
			logger.setAdditive(false);
			logger.setLevel(logLevel);

			logger.addAppender(appender);
		}
	}

	/**
	 * Displays a help message.
	 *
	 * @param outputStream the output stream to print the help message to
	 * @param errorStream the output stream to use when the help message can't be displayed
	 */
	protected void displayHelp(PrintStream outputStream, PrintStream errorStream) {
		Map<String, String> helpInformation = getHelpInformation();

		print(helpInformation.get("name"), outputStream);
		print(helpInformation.get("description"), outputStream);
		print(helpInformation.get("usage"), outputStream);

		print("The following options are accepted:", outputStream);

		try {
			optionParser.printHelpOn(outputStream);
		} catch (IOException exception) {
			outputStream.println("The options can't be displayed.");

			handleThrowable(exception, errorStream);
		}
	}

	/**
	 * Returns information that will be used in the help message. It can contain the following keys, all of which are
	 * optional:
	 * <dl>
	 * <dt>name</dt>
	 * <dd>The name of the command;</dd>
	 * <dt>description</dt>
	 * <dd>A description of the command;</dd>
	 * <dt>usage</dt>
	 * <dd>A description of how the command can be used.</dd>
	 * </dl>
	 *
	 * @return help information about this command
	 */
	protected abstract Map<String, String> getHelpInformation();

	private void print(String information, PrintStream outputStream) {
		if (information != null) {
			outputStream.println(information);
			outputStream.println();
		}
	}

	/**
	 * Handles a throwable by printing its stack trace when the option to do so was supplied.
	 *
	 * @param throwable the throwable to handle (may be {@code null})
	 * @param errorStream the error stream to use
	 */
	protected void handleThrowable(Throwable throwable, PrintStream errorStream) {
		if ((throwable != null) && printStackTraces) {
			throwable.printStackTrace(errorStream);
		}
	}
}