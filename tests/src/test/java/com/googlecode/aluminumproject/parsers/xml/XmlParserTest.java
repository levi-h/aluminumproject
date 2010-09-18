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
package com.googlecode.aluminumproject.parsers.xml;

import com.googlecode.aluminumproject.configuration.ConfigurationParameters;
import com.googlecode.aluminumproject.configuration.test.TestConfiguration;
import com.googlecode.aluminumproject.context.DefaultContext;
import com.googlecode.aluminumproject.libraries.test.TestLibrary;
import com.googlecode.aluminumproject.parsers.ParseException;
import com.googlecode.aluminumproject.parsers.test.UpperCaseTemplateNameTranslator;
import com.googlecode.aluminumproject.resources.ClassPathTemplateFinderFactory;
import com.googlecode.aluminumproject.resources.TemplateFinderFactory;
import com.googlecode.aluminumproject.templates.DefaultTemplateElementFactory;
import com.googlecode.aluminumproject.templates.Template;
import com.googlecode.aluminumproject.templates.TemplateContext;
import com.googlecode.aluminumproject.templates.TemplateElement;
import com.googlecode.aluminumproject.templates.TemplateElementFactory;
import com.googlecode.aluminumproject.writers.StringWriter;
import com.googlecode.aluminumproject.writers.TextWriter;

import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

@SuppressWarnings("all")
@Test(groups = {"parsers", "parsers-xml", "slow"})
public class XmlParserTest {
	public void templateExtensionShouldBeConfigurable() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(ClassPathTemplateFinderFactory.TEMPLATE_PATH, "templates/xml");
		parameters.addParameter(XmlParser.TEMPLATE_EXTENSION, "xml");

		assert createParser(parameters).parseTemplate("test") != null;
	}

	public void templateNameTranslatorShouldBeConfigurable() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(
			XmlParser.TEMPLATE_NAME_TRANSLATOR_CLASS, UpperCaseTemplateNameTranslator.class.getName());

		assert createParser(parameters).parseTemplate("templates/xml/upper-case.xml") != null;
	}

	public void libraryUrlAbbreviationsShouldBeSetOnTemplateElements() {
		Template template = createParser().parseTemplate("templates/xml/test.xml");

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
		Template template = createParser().parseTemplate("templates/xml/test-without-namespace.xml");

		List<TemplateElement> rootElements = template.getChildren(null);
		assert rootElements != null;
		assert rootElements.size() == 1;

		Map<String, String> libraryUrlAbbreviations = rootElements.get(0).getLibraryUrlAbbreviations();
		assert libraryUrlAbbreviations != null;
		assert libraryUrlAbbreviations.containsKey("");
		assert libraryUrlAbbreviations.get("").equals("http://aluminumproject.googlecode.com/test");
	}

	@Test(expectedExceptions = ParseException.class)
	public void tagsWithoutLibraryShouldCauseException() {
		createParser().parseTemplate("templates/xml/html.xml");
	}

	public void tagsWithoutLibraryShouldBePossibleWithTagsAllowed() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(XmlParser.ALLOW_NON_ACTION_TAGS, "true");

		Template template = createParser(parameters).parseTemplate("templates/xml/html.xml");
		assert template != null;

		StringWriter stringWriter = new StringWriter();
		template.processChildren(new TemplateContext(), new DefaultContext(), new TextWriter(stringWriter, true));
		assert stringWriter.getString().equals("<html id=\"document\"></html>");
	}

	private XmlParser createParser() {
		return createParser(new ConfigurationParameters());
	}

	private XmlParser createParser(ConfigurationParameters parameters) {
		TestConfiguration configuration = new TestConfiguration(parameters);

		TestLibrary library = new TestLibrary();
		library.initialise(configuration, parameters);
		configuration.addLibrary(library);

		TemplateElementFactory templateElementFactory = new DefaultTemplateElementFactory();
		templateElementFactory.initialise(configuration, parameters);
		configuration.setTemplateElementFactory(templateElementFactory);

		TemplateFinderFactory templateFinderFactory = new ClassPathTemplateFinderFactory();
		templateFinderFactory.initialise(configuration, parameters);
		configuration.setTemplateFinderFactory(templateFinderFactory);

		XmlParser parser = new XmlParser();
		parser.initialise(configuration, parameters);

		return parser;
	}
}