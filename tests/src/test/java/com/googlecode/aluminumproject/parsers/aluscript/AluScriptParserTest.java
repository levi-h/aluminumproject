/*
 * Copyright 2010-2012 Aluminum project
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

import static com.googlecode.aluminumproject.finders.ClassPathTemplateFinder.TEMPLATE_PATH;
import static com.googlecode.aluminumproject.parsers.aluscript.AluScriptParser.AUTOMATIC_NEWLINES;
import static com.googlecode.aluminumproject.parsers.aluscript.AluScriptParser.TEMPLATE_EXTENSION;
import static com.googlecode.aluminumproject.parsers.aluscript.AluScriptParser.TEMPLATE_NAME_TRANSLATOR_CLASS;

import com.googlecode.aluminumproject.configuration.ConfigurationParameters;
import com.googlecode.aluminumproject.configuration.TestConfiguration;
import com.googlecode.aluminumproject.finders.ClassPathTemplateFinder;
import com.googlecode.aluminumproject.finders.TemplateFinder;
import com.googlecode.aluminumproject.libraries.TestLibrary;
import com.googlecode.aluminumproject.parsers.UpperCaseTemplateNameTranslator;
import com.googlecode.aluminumproject.templates.DefaultTemplateElementFactory;
import com.googlecode.aluminumproject.templates.Template;
import com.googlecode.aluminumproject.templates.TemplateElement;
import com.googlecode.aluminumproject.templates.TemplateElementFactory;
import com.googlecode.aluminumproject.templates.TextElement;

import java.util.List;

import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
@Test(groups = {"parsers", "parsers-aluscript", "slow"})
public class AluScriptParserTest {
	public void templateExtensionShouldBeConfigurable() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(TEMPLATE_PATH, "templates");
		parameters.addParameter(TEMPLATE_EXTENSION, "alu");

		assert createParser(parameters).parseTemplate("test") != null;
	}

	public void templateNameTranslatorShouldBeConfigurable() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(TEMPLATE_NAME_TRANSLATOR_CLASS, UpperCaseTemplateNameTranslator.class.getName());

		assert createParser(parameters).parseTemplate("templates/upper-case.alu") != null;
	}

	public void newlinesShouldBeAddedAutomaticallyByDefault() {
		Template template = createParser().parseTemplate("templates/text.alu");

		List<TemplateElement> rootElements = template.getChildren(null);
		assert rootElements != null;
		assert rootElements.size() == 2;

		TemplateElement textElement = rootElements.get(0);
		assert textElement != null;
		assert textElement instanceof TextElement;

		String text = ((TextElement) textElement).getText();
		assert text != null;
		assert text.equals("test");

		TemplateElement newlineElement = rootElements.get(1);
		assert newlineElement != null;
		assert newlineElement instanceof TextElement;

		String newline = ((TextElement) newlineElement).getText();
		assert newline != null;
		assert newline.equals("\n");
	}

	@Test(dependsOnMethods = "newlinesShouldBeAddedAutomaticallyByDefault")
	public void automaticNewlinesSettingShouldBeConfigurable() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(AUTOMATIC_NEWLINES, "false");

		Template template = createParser(parameters).parseTemplate("templates/text.alu");

		List<TemplateElement> rootElements = template.getChildren(null);
		assert rootElements != null;
		assert rootElements.size() == 1;

		TemplateElement textElement = rootElements.get(0);
		assert textElement != null;
		assert textElement instanceof TextElement;

		String text = ((TextElement) textElement).getText();
		assert text != null;
		assert text.equals("test");
	}

	@Test(dependsOnMethods = "newlinesShouldBeAddedAutomaticallyByDefault")
	public void lineNumbersShouldBeSetOnTemplateElements() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(AUTOMATIC_NEWLINES, "true");

		Template template = createParser(parameters).parseTemplate("templates/test.alu");

		List<TemplateElement> rootElements = template.getChildren(null);
		assert rootElements != null;
		assert rootElements.size() == 1;

		TemplateElement rootElement = rootElements.get(0);
		assert rootElement != null;
		assert rootElement.getLineNumber() == 17;

		List<TemplateElement> childElements = template.getChildren(rootElement);
		assert childElements != null;
		assert childElements.size() == 2;

		TemplateElement firstChildElement = childElements.get(0);
		assert firstChildElement != null;
		assert firstChildElement.getLineNumber() == 18;

		TemplateElement secondChildElement = childElements.get(1);
		assert secondChildElement != null;
		assert secondChildElement.getLineNumber() == 18;
	}

	private AluScriptParser createParser() {
		return createParser(new ConfigurationParameters());
	}

	private AluScriptParser createParser(ConfigurationParameters parameters) {
		TestConfiguration configuration = new TestConfiguration(parameters);

		TestLibrary library = new TestLibrary();
		library.initialise(configuration);
		configuration.addLibrary(library);

		TemplateElementFactory templateElementFactory = new DefaultTemplateElementFactory();
		templateElementFactory.initialise(configuration);
		configuration.setTemplateElementFactory(templateElementFactory);

		TemplateFinder templateFinder = new ClassPathTemplateFinder();
		templateFinder.initialise(configuration);
		configuration.setTemplateFinder(templateFinder);

		AluScriptParser parser = new AluScriptParser();
		parser.initialise(configuration);

		return parser;
	}
}