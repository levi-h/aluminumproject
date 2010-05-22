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
package com.googlecode.aluminumproject.cli.commands.alu;

import static com.googlecode.aluminumproject.configuration.DefaultConfiguration.TEMPLATE_FINDER_FACTORY_CLASS;

import com.googlecode.aluminumproject.Aluminum;
import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.cli.Command;
import com.googlecode.aluminumproject.cli.CommandException;
import com.googlecode.aluminumproject.cli.OptionEffect;
import com.googlecode.aluminumproject.configuration.ConfigurationParameters;
import com.googlecode.aluminumproject.configuration.DefaultConfiguration;
import com.googlecode.aluminumproject.context.DefaultContext;
import com.googlecode.aluminumproject.resources.FileSystemTemplateFinderFactory;
import com.googlecode.aluminumproject.writers.OutputStreamWriter;
import com.googlecode.aluminumproject.writers.TextWriter;
import com.googlecode.aluminumproject.writers.Writer;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import joptsimple.OptionSet;
import joptsimple.OptionSpec;

/**
 * Processes a template.
 * <p>
 * The <em>alu</em> command accepts the following options:
 * <ul>
 * <li>The parser option ({@code -p} or {@code --parser}): which parser to use; the name of the parser is expected as an
 *     argument to the option and defaults to {@code "xml"}.
 * </ul>
 * The name of the template has to be given as a parameter to the command.
 * <p>
 * The template engine will use a {@link DefaultConfiguration default configuration} with a single exception: the
 * template finder will be created by a {@link FileSystemTemplateFinderFactory file system template finder factory} that
 * looks for templates in the current directory and the <em>templates</em> directory inside the directory in which
 * Aluminum was installed.
 *
 * @author levi_h
 */
public class Alu extends Command {
	private String parser;

	/**
	 * Creates an <em>alu</em> command.
	 */
	public Alu() {
		parser = "xml";

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
		helpInformation.put("usage", "Usage: alu [options] [template file]");

		return helpInformation;
	}

	@Override
	protected void execute(
			PrintStream outputStream, PrintStream errorStream, List<String> arguments) throws CommandException {
		if (arguments.size() == 1) {
			ConfigurationParameters parameters = new ConfigurationParameters();
			parameters.addParameter(TEMPLATE_FINDER_FACTORY_CLASS, FileSystemTemplateFinderFactory.class.getName());

			String templateDirectories = System.getProperty("user.dir");

			String aluminumHome = System.getenv("ALUMINUM_HOME");

			if (aluminumHome == null) {
				logger.debug("environment variable ALUMINUM_HOME is not set, ",
					"templates will only be found in the current directory");
			} else {
				templateDirectories = String.format("%s, %s/templates", templateDirectories, aluminumHome);
			}

			parameters.addParameter(FileSystemTemplateFinderFactory.TEMPLATE_DIRECTORIES, templateDirectories);

			Aluminum aluminum = new Aluminum(new DefaultConfiguration(parameters));

			String template = arguments.get(0);
			DefaultContext context = new DefaultContext();
			Writer writer = new TextWriter(new OutputStreamWriter(outputStream), true);

			try {
				aluminum.processTemplate(template, parser, context, writer);
			} catch (AluminumException exception) {
				errorStream.printf("The template '%s' can't be processed (%s).%n", template, exception.getMessage());

				handleThrowable(exception, errorStream);
			}
		} else {
			displayHelp(outputStream, errorStream);
		}
	}

	/**
	 * Executes the <em>alu</em> command.
	 *
	 * @param arguments the command-line parameters
	 */
	public static void main(String... arguments) {
		new Alu().execute(System.out, System.err, arguments);
	}
}