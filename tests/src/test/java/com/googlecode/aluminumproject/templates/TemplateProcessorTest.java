/*
 * Copyright 2009-2012 Levi Hoogenberg
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
package com.googlecode.aluminumproject.templates;

import static com.googlecode.aluminumproject.configuration.DefaultConfiguration.CACHE_CLASS;
import static com.googlecode.aluminumproject.configuration.DefaultConfiguration.PARSER_PACKAGES;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.annotations.Named;
import com.googlecode.aluminumproject.cache.Cache;
import com.googlecode.aluminumproject.cache.MemoryCache;
import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.configuration.ConfigurationParameters;
import com.googlecode.aluminumproject.configuration.DefaultConfiguration;
import com.googlecode.aluminumproject.configuration.TestConfiguration;
import com.googlecode.aluminumproject.context.DefaultContext;
import com.googlecode.aluminumproject.context.TestContextEnricher;
import com.googlecode.aluminumproject.converters.ConverterRegistry;
import com.googlecode.aluminumproject.converters.TestConverterRegistry;
import com.googlecode.aluminumproject.libraries.Library;
import com.googlecode.aluminumproject.libraries.TestLibrary;
import com.googlecode.aluminumproject.parsers.Parser;
import com.googlecode.aluminumproject.parsers.xml.XmlParser;
import com.googlecode.aluminumproject.resources.ClassPathTemplateFinder;
import com.googlecode.aluminumproject.resources.TemplateFinder;
import com.googlecode.aluminumproject.utilities.ReflectionUtilities;
import com.googlecode.aluminumproject.writers.NullWriter;
import com.googlecode.aluminumproject.writers.StringWriter;
import com.googlecode.aluminumproject.writers.TextWriter;

import java.util.Collections;
import java.util.Map;

import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
@Test(groups = {"core", "fast"})
public class TemplateProcessorTest {
	@Named("name")
	public static class NameParser implements Parser {
		private Configuration configuration;

		public void initialise(Configuration configuration) {
			this.configuration = configuration;
		}

		public void disable() {
			configuration = null;
		}

		public Template parseTemplate(String name) throws AluminumException {
			Map<String, String> libraryUrlAbbreviations = Collections.emptyMap();

			TemplateBuilder templateBuilder = new TemplateBuilder();
			templateBuilder.addTemplateElement(
				configuration.getTemplateElementFactory().createTextElement(name, libraryUrlAbbreviations));

			return templateBuilder.build();
		}
	}

	public void cacheShouldBePreferredOverParser() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(CACHE_CLASS, MemoryCache.class.getName());
		parameters.addParameter(PARSER_PACKAGES, ReflectionUtilities.getPackageName(NameParser.class));

		Configuration configuration = new DefaultConfiguration(parameters);

		Map<String, String> libraryUrlAbbreviations = Collections.emptyMap();

		TemplateBuilder templateBuilder = new TemplateBuilder();
		templateBuilder.addTemplateElement(
			configuration.getTemplateElementFactory().createTextElement("cache", libraryUrlAbbreviations));

		configuration.getCache().storeTemplate(new Cache.Key("test", "name"), templateBuilder.build());

		StringWriter stringWriter = new StringWriter();

		new TemplateProcessor(configuration).processTemplate("test", "name",
			new DefaultContext(), new TextWriter(stringWriter, true));

		assert stringWriter.getString().equals("cache");

		configuration.close();
	}

	public void parsedTemplatesShouldBeCached() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(CACHE_CLASS, MemoryCache.class.getName());
		parameters.addParameter(PARSER_PACKAGES, ReflectionUtilities.getPackageName(NameParser.class));

		Configuration configuration = new DefaultConfiguration(parameters);

		StringWriter stringWriter = new StringWriter();

		new TemplateProcessor(configuration).processTemplate("test", "name",
			new DefaultContext(), new TextWriter(stringWriter, true));

		assert stringWriter.getString().equals("test");

		assert configuration.getCache().findTemplate(new Cache.Key("test", "name")) != null;

		configuration.close();
	}

	public void cacheShouldBeOptional() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(PARSER_PACKAGES, ReflectionUtilities.getPackageName(NameParser.class));

		Configuration configuration = new DefaultConfiguration(parameters);

		StringWriter stringWriter = new StringWriter();

		new TemplateProcessor(configuration).processTemplate("test", "name",
			new DefaultContext(), new TextWriter(stringWriter, true));

		assert stringWriter.getString().equals("test");

		configuration.close();
	}

	public void contextEnrichersShouldBeInvoked() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(ClassPathTemplateFinder.TEMPLATE_PATH, "templates");
		parameters.addParameter(XmlParser.TEMPLATE_EXTENSION, "xml");

		TestConfiguration configuration = new TestConfiguration(parameters);

		TemplateFinder templateFinder = new ClassPathTemplateFinder();
		templateFinder.initialise(configuration);
		configuration.setTemplateFinder(templateFinder);

		Parser parser = new XmlParser();
		parser.initialise(configuration);
		configuration.addParser("xml", parser);

		TemplateElementFactory templateElementFactory = new DefaultTemplateElementFactory();
		templateElementFactory.initialise(configuration);
		configuration.setTemplateElementFactory(templateElementFactory);

		Library library = new TestLibrary();
		library.initialise(configuration);
		configuration.addLibrary(library);

		ConverterRegistry converterRegistry = new TestConverterRegistry();
		converterRegistry.initialise(configuration);
		configuration.setConverterRegistry(converterRegistry);

		TestContextEnricher contextEnricher = new TestContextEnricher();
		contextEnricher.initialise(configuration);
		configuration.addContextEnricher(contextEnricher);

		assert !contextEnricher.isBeforeTemplateInvoked();
		assert !contextEnricher.isAfterTemplateInvoked();

		new TemplateProcessor(configuration).processTemplate("test", "xml", new DefaultContext(), new NullWriter());

		assert contextEnricher.isBeforeTemplateInvoked();
		assert contextEnricher.isAfterTemplateInvoked();
	}
}