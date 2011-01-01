/*
 * Copyright 2009-2011 Levi Hoogenberg
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
package com.googlecode.aluminumproject.cli.commands.alu;

import static com.googlecode.aluminumproject.configuration.DefaultConfiguration.TEMPLATE_FINDER_CLASS;
import static com.googlecode.aluminumproject.context.Context.TEMPLATE_SCOPE;

import com.googlecode.aluminumproject.Aluminum;
import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.cli.Command;
import com.googlecode.aluminumproject.cli.CommandException;
import com.googlecode.aluminumproject.cli.OptionEffect;
import com.googlecode.aluminumproject.configuration.ConfigurationParameters;
import com.googlecode.aluminumproject.configuration.DefaultConfiguration;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.DefaultContext;
import com.googlecode.aluminumproject.resources.FileSystemTemplateFinder;
import com.googlecode.aluminumproject.utilities.UtilityException;
import com.googlecode.aluminumproject.utilities.environment.EnvironmentUtilities;
import com.googlecode.aluminumproject.utilities.environment.PropertySetContainer;
import com.googlecode.aluminumproject.writers.OutputStreamWriter;
import com.googlecode.aluminumproject.writers.TextWriter;
import com.googlecode.aluminumproject.writers.Writer;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import joptsimple.OptionSet;
import joptsimple.OptionSpec;

/**
 * Processes a template.
 * <p>
 * The <em>alu</em> command accepts the following options:
 * <ul>
 * <li>The parser option ({@code -p} or {@code --parser}): which parser to use; the name of the parser is expected as an
 *     argument to the option and defaults to {@value #DEFAULT_PARSER_NAME}.
 * </ul>
 * The name of the template has to be given as a parameter to the command. A {@link List list} that contains the other
 * arguments (if any) are made available as a context variable named {@value #ARGUMENTS_VARIABLE_NAME}.
 * <p>
 * The template engine will use a {@link DefaultConfiguration default configuration} with configuration parameters which
 * make sure that it's template finder will be created by a {@link FileSystemTemplateFinder file system template finder}
 * that looks for templates in the current directory and the <em>templates</em> directory inside the directory in which
 * Aluminum was installed. Other configuration parameters may be specified in a properties file named {@code
 * alu.properties}, located in {@code ~/.aluminum}, where {@code ~} stands for the user's home directory.
 *
 * @author levi_h
 */
public class Alu extends Command {
	private String parser;

	/**
	 * Creates an <em>alu</em> command.
	 */
	public Alu() {
		parser = DEFAULT_PARSER_NAME;

		addOptions();
	}

	private void addOptions() {
		List<String> parserOptions = Arrays.asList("p", "parser");
		String parserDescription = "The name of the parser to use.";

		OptionSpec<String> parser =
			getOptionParser().acceptsAll(parserOptions, parserDescription).withRequiredArg().ofType(String.class);

		addOption(parser, new OptionEffect<String>() {
			public void apply(OptionSet options, OptionSpec<String> option) {
				Alu.this.parser = option.value(options);
			}
		});
	}

	@Override
	protected Map<String, String> getHelpInformation() {
		Map<String, String> helpInformation = new HashMap<String, String>();

		helpInformation.put("name", "alu - Aluminum");
		helpInformation.put("description", "Processes a template.");
		helpInformation.put("usage", "Usage: alu [options] [template file] [arguments]");

		return helpInformation;
	}

	@Override
	protected void execute(
			PrintStream outputStream, PrintStream errorStream, List<String> arguments) throws CommandException {
		if (arguments.size() >= 1) {
			ConfigurationParameters parameters = new ConfigurationParameters();
			addConfigurationParameters(parameters);
			addCustomConfigurationParameters(parameters);

			Aluminum aluminum = new Aluminum(new DefaultConfiguration(parameters));

			String template = arguments.get(0);

			Context context = new DefaultContext();
			context.setVariable(TEMPLATE_SCOPE, ARGUMENTS_VARIABLE_NAME, arguments.subList(1, arguments.size()));

			Writer writer = new TextWriter(new OutputStreamWriter(outputStream), true);

			try {
				aluminum.processTemplate(template, parser, context, writer);
			} catch (AluminumException exception) {
				errorStream.printf("The template '%s' can't be processed (%s).%n", template, exception.getMessage());

				handleThrowable(exception, errorStream);
			} finally {
				try {
					aluminum.stop();
				} catch (AluminumException exception) {
					errorStream.printf("The template engine can't be stopped.%n");

					handleThrowable(exception, errorStream);
				}
			}
		} else {
			displayHelp(outputStream, errorStream);
		}
	}

	private void addConfigurationParameters(ConfigurationParameters parameters) {
		parameters.addParameter(TEMPLATE_FINDER_CLASS, FileSystemTemplateFinder.class.getName());

		String templateDirectories = System.getProperty("user.dir");

		String aluminumHome = System.getenv("ALUMINUM_HOME");

		if (aluminumHome == null) {
			logger.debug("environment variable ALUMINUM_HOME is not set, ",
				"templates will only be found in the current directory");
		} else {
			templateDirectories = String.format("%s, %s/templates", templateDirectories, aluminumHome);
		}

		parameters.addParameter(FileSystemTemplateFinder.TEMPLATE_DIRECTORIES, templateDirectories);
	}

	private void addCustomConfigurationParameters(ConfigurationParameters parameters) throws CommandException {
		PropertySetContainer propertySetContainer = EnvironmentUtilities.getPropertySetContainer();

		if (propertySetContainer.containsPropertySet("alu")) {
			Properties configurationPropertySet;

			try {
				configurationPropertySet = propertySetContainer.readPropertySet("alu");
			} catch (UtilityException exception) {
				throw new CommandException(exception, "can't read configuration property set 'alu'");
			}

			for (Object key: Collections.list(configurationPropertySet.propertyNames())) {
				String parameterName = (String) key;

				parameters.addParameter(parameterName, configurationPropertySet.getProperty(parameterName));
			}
		}
	}

	/**
	 * The name of the context variable that will contain any command line arguments after the first one (which is the
	 * name of the template to process): {@value}.
	 */
	public final static String ARGUMENTS_VARIABLE_NAME = "arguments";

	/** The name of the parser that will be used when none is specified: {@value}. */
	public final static String DEFAULT_PARSER_NAME = "aluscript";

	/**
	 * Executes the <em>alu</em> command.
	 *
	 * @param arguments the command-line parameters
	 */
	public static void main(String... arguments) {
		new Alu().execute(System.out, System.err, arguments);
	}
}