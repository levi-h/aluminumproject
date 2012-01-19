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
package com.googlecode.aluminumproject.parsers.xml;

import com.googlecode.aluminumproject.AluminumException;
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

import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
@Test(groups = {"parsers", "parsers-xml", "slow"})
public class XmlParserTest {
	public void templateExtensionShouldBeConfigurable() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(ClassPathTemplateFinder.TEMPLATE_PATH, "templates");
		parameters.addParameter(XmlParser.TEMPLATE_EXTENSION, "xml");

		assert createParser(parameters).parseTemplate("test") != null;
	}

	public void templateNameTranslatorShouldBeConfigurable() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(
			XmlParser.TEMPLATE_NAME_TRANSLATOR_CLASS, UpperCaseTemplateNameTranslator.class.getName());

		assert createParser(parameters).parseTemplate("templates/upper-case.xml") != null;
	}

	public void templateShouldHaveName() {
		String name = createParser().parseTemplate("templates/test.xml").getName();
		assert name != null;
		assert name.equals("templates/test.xml");
	}

	public void libraryUrlAbbreviationsShouldBeSetOnTemplateElements() {
		Template template = createParser().parseTemplate("templates/test.xml");

		List<TemplateElement> rootElements = template.getChildren(null);
		assert rootElements != null;
		assert rootElements.size() == 1;

		Map<String, String> libraryUrlAbbreviations = rootElements.get(0).getLibraryUrlAbbreviations();
		assert libraryUrlAbbreviations != null;
		assert libraryUrlAbbreviations.containsKey("test");
		assert libraryUrlAbbreviations.get("test").equals("http://aluminumproject.googlecode.com/test");
	}

	@Test(dependsOnMethods = "libraryUrlAbbreviationsShouldBeSetOnTemplateElements")
	public void libraryUrlAbbreviationsShouldWorkWithoutNamespaces() {
		Template template = createParser().parseTemplate("templates/test-without-namespace.xml");

		List<TemplateElement> rootElements = template.getChildren(null);
		assert rootElements != null;
		assert rootElements.size() == 1;

		Map<String, String> libraryUrlAbbreviations = rootElements.get(0).getLibraryUrlAbbreviations();
		assert libraryUrlAbbreviations != null;
		assert libraryUrlAbbreviations.containsKey("");
		assert libraryUrlAbbreviations.get("").equals("http://aluminumproject.googlecode.com/test");
	}

	@Test(expectedExceptions = AluminumException.class)
	public void tagsWithoutLibraryShouldCauseException() {
		createParser().parseTemplate("templates/html.xml");
	}

	public void lineNumbersShouldBeSetOnTemplateElements() {
		Template template = createParser().parseTemplate("templates/test.xml");

		List<TemplateElement> rootElements = template.getChildren(null);
		assert rootElements != null;
		assert rootElements.size() == 1;

		TemplateElement rootElement = rootElements.get(0);
		assert rootElement.getLineNumber() == 17;

		List<TemplateElement> childElements = template.getChildren(rootElement);
		assert childElements != null;
		assert childElements.size() == 1;

		assert childElements.get(0).getLineNumber() == 19;
	}

	private XmlParser createParser() {
		return createParser(new ConfigurationParameters());
	}

	private XmlParser createParser(ConfigurationParameters parameters) {
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

		XmlParser parser = new XmlParser();
		parser.initialise(configuration);

		return parser;
	}
}