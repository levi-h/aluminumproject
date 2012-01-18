/*
 * Copyright 2009-2012 Aluminum project
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
package com.googlecode.aluminumproject.parsers.aluscript;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.configuration.ConfigurationElementFactory;
import com.googlecode.aluminumproject.configuration.ConfigurationParameters;
import com.googlecode.aluminumproject.parsers.Parser;
import com.googlecode.aluminumproject.parsers.TemplateNameTranslator;
import com.googlecode.aluminumproject.parsers.aluscript.instructions.Instruction;
import com.googlecode.aluminumproject.parsers.aluscript.instructions.LibraryInstruction;
import com.googlecode.aluminumproject.parsers.aluscript.instructions.NewlineInstruction;
import com.googlecode.aluminumproject.parsers.aluscript.lines.LineParser;
import com.googlecode.aluminumproject.parsers.aluscript.lines.comments.CommentLineParser;
import com.googlecode.aluminumproject.parsers.aluscript.lines.instructions.InstructionLineParser;
import com.googlecode.aluminumproject.parsers.aluscript.lines.text.TextLineParser;
import com.googlecode.aluminumproject.templates.Template;
import com.googlecode.aluminumproject.utilities.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Parses AluScript templates.
 * <p>
 * An AluScript template consists of a number of lines. Each line is parsed by a suitable {@link LineParser line
 * parser}. There are three kinds of lines:
 * <ul>
 * <li>Comments: these lines start with a {@literal #} symbol and are parsed by a {@link CommentLineParser comment
 *     line parser};
 * <li>Instructions: these start with an {@literal @} symbol and are parsed by the {@link InstructionLineParser
 *     instruction line parser};
 * <li>Text and expressions: these lines are neither comments nor instructions and are parsed by a {@link TextLineParser
 *     text line parser}.
 * </ul>
 * Empty lines are ignored. Indentation can be used to express a parent/child relation between two lines. The only
 * indentation that is recognised as such is the tab character.
 * <p>
 * Each template is parsed in a new {@link AluScriptContext context}. This context contains everything that line parsers
 * need (e.g. all available instructions). It also contains a couple of {@link AluScriptSettings template settings}.
 * These settings have reasonable defaults, but they can be overridden:
 * <ul>
 * <li>The template name translator can be configured using the configuration parameter {@value
 *     #TEMPLATE_NAME_TRANSLATOR_CLASS};
 * <li>Whether the parser inserts newlines after text lines or not can be changed by supplying the {@value
 *     #AUTOMATIC_NEWLINES} parameter.
 * </ul>
 * <p>
 * By default, template names should include the template extension. If this extension is the same for every template
 * (<i>alu</i> is recommended), it can be configured using the {@value #TEMPLATE_EXTENSION} parameter.
 */
public class AluScriptParser implements Parser {
	private Configuration configuration;

	private String templateExtension;

	private AluScriptSettings settings;

	private List<LineParser> lineParsers;

	private List<Instruction> instructions;

	private final Logger logger;

	/**
	 * Creates an AluScript parser.
	 */
	public AluScriptParser() {
		logger = Logger.get(getClass());
	}

	public void initialise(Configuration configuration) throws AluminumException {
		this.configuration = configuration;

		templateExtension = configuration.getParameters().getValue(TEMPLATE_EXTENSION, null);

		if (templateExtension != null) {
			logger.debug("using template extension '", templateExtension, "'");
		}

		createSettings();

		lineParsers = Arrays.<LineParser>asList(
			new CommentLineParser(),
			new InstructionLineParser(),
			new TextLineParser()
		);

		instructions = Arrays.<Instruction>asList(
			new LibraryInstruction(),
			new NewlineInstruction()
		);
	}

	private void createSettings() throws AluminumException {
		ConfigurationParameters parameters = configuration.getParameters();

		settings = new AluScriptSettings();

		String templateNameTranslatorClassName = parameters.getValue(TEMPLATE_NAME_TRANSLATOR_CLASS, null);

		if (templateNameTranslatorClassName != null) {
			ConfigurationElementFactory configurationElementFactory = configuration.getConfigurationElementFactory();

			settings.setTemplateNameTranslator(
				configurationElementFactory.instantiate(templateNameTranslatorClassName, TemplateNameTranslator.class));
		}

		String automaticNewlines = parameters.getValue(AUTOMATIC_NEWLINES, null);

		if (automaticNewlines != null) {
			settings.setAutomaticNewlines(Boolean.parseBoolean(automaticNewlines));
		}
	}

	public void disable() {}

	public Template parseTemplate(String name) throws AluminumException {
		String templateContents = readStream(name, getTemplateStream(name));
		logger.debug("read template '", name, "': ", templateContents);

		AluScriptContext context = new AluScriptContext(configuration, settings, instructions);

		for (String line: templateContents.split("\r?\n")) {
			if (line.length() > 0) {
				findLineParser(line).parseLine(line, context);
			}

			context.incrementLineNumber();
		}

		return context.getTemplate();
	}

	private InputStream getTemplateStream(String name) throws AluminumException {
		if (templateExtension != null) {
			name = String.format("%s.%s", name, templateExtension);
		}

		return configuration.getTemplateFinder().find(name);
	}

	private String readStream(String name, InputStream in) throws AluminumException {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();

			try {
				byte[] buffer = new byte[4096];
				int bytesRead;

				while ((bytesRead = in.read(buffer)) > 0) {
					out.write(buffer, 0, bytesRead);
				}
			} finally {
				in.close();
			}

			return out.toString();
		} catch (IOException exception) {
			throw new AluminumException(exception, "can't read template '", name, "'");
		}
	}

	private LineParser findLineParser(String line) throws AluminumException {
		LineParser lineParser = null;

		Iterator<LineParser> it = lineParsers.iterator();

		while ((lineParser == null) && it.hasNext()) {
			lineParser = it.next();

			if (!lineParser.handles(line)) {
				lineParser = null;
			}
		}

		if (lineParser == null) {
			throw new AluminumException("can't find a line parser for '", line, "'");
		}

		return lineParser;
	}

	/** The name of the configuration parameter that is used to set the default extension of templates. */
	public final static String TEMPLATE_EXTENSION = "parser.aluscript.template_extension";

	/** The name of the configuration parameter that holds the class name of the template name translator to use. */
	public final static String TEMPLATE_NAME_TRANSLATOR_CLASS = "parser.aluscript.template_name_translator.class";

	/** The name of the configuration parameter that controls whether newlines are added automatically. */
	public final static String AUTOMATIC_NEWLINES = "parser.aluscript.automatic_newlines";
}