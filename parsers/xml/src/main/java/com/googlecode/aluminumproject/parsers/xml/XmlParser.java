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
package com.googlecode.aluminumproject.parsers.xml;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.configuration.ConfigurationElementFactory;
import com.googlecode.aluminumproject.configuration.ConfigurationParameters;
import com.googlecode.aluminumproject.parsers.Parser;
import com.googlecode.aluminumproject.parsers.TemplateNameTranslator;
import com.googlecode.aluminumproject.templates.Template;
import com.googlecode.aluminumproject.utilities.Logger;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * Parses templates from XML files.
 * <p>
 * By default, the names of templates should include their extensions. If all of the templates have the same extension,
 * it might be worth the while to configure this extension using the {@value #TEMPLATE_EXTENSION} configuration
 * parameter. If a template extension is given, it will be appended (separated by a dot) to all template names.
 * <p>
 * To support a custom naming strategy in templates, the XML parser allows a {@link TemplateNameTranslator template name
 * translator} to be configured (using the {@value #TEMPLATE_NAME_TRANSLATOR_CLASS} configuration property). If no
 * template name translator is configured, the {@link XmlTemplateNameTranslator default template name translator} will
 * be used.
 *
 * @author levi_h
 */
public class XmlParser implements Parser {
	private Configuration configuration;

	private String templateExtension;

	private TemplateNameTranslator templateNameTranslator;

	private final Logger logger;

	/**
	 * Creates an XML parser.
	 */
	public XmlParser() {
		logger = Logger.get(getClass());
	}

	public void initialise(Configuration configuration) throws AluminumException {
		this.configuration = configuration;

		ConfigurationParameters parameters = configuration.getParameters();

		templateExtension = parameters.getValue(TEMPLATE_EXTENSION, null);

		if (templateExtension != null) {
			logger.debug("using template extension '", templateExtension, "'");
		}

		createTemplateNameTranslator();
	}

	private void createTemplateNameTranslator() throws AluminumException {
		String templateNameTranslatorClassName = configuration.getParameters().getValue(
			TEMPLATE_NAME_TRANSLATOR_CLASS, XmlTemplateNameTranslator.class.getName());

		logger.debug("creating template name translator of type '", templateNameTranslatorClassName, "'");

		ConfigurationElementFactory configurationElementFactory = configuration.getConfigurationElementFactory();

		templateNameTranslator =
			configurationElementFactory.instantiate(templateNameTranslatorClassName, TemplateNameTranslator.class);
	}

	public void disable() {}

	public Template parseTemplate(String name) throws AluminumException {
		XMLReader parser = createParser();
		logger.debug("created XML parser");

		TemplateHandler templateHandler = new TemplateHandler(configuration, templateNameTranslator);
		parser.setContentHandler(templateHandler);
		logger.debug("using template handler ", templateHandler);

		InputStream templateStream = findTemplateStream(name);
		logger.debug("found template '", name, "'");

		parse(name, parser, templateStream);
		logger.debug("parsed template '", name, "'");

		Template template = templateHandler.getTemplate();
		logger.debug("resulting template element: ", template);

		return template;
	}

	private XMLReader createParser() throws AluminumException {
		SAXParserFactory parserFactory = SAXParserFactory.newInstance();
		parserFactory.setNamespaceAware(true);

		XMLReader parser;

		try {
			parser = parserFactory.newSAXParser().getXMLReader();
		} catch (SAXException exception) {
			throw new AluminumException(exception, "can't create an XML parser");
		} catch (ParserConfigurationException exception) {
			throw new AluminumException(exception, "can't configure the XML parser factory");
		}

		return parser;
	}

	private InputStream findTemplateStream(String name) throws AluminumException {
		if (templateExtension != null) {
			name = String.format("%s.%s", name, templateExtension);
		}

		return configuration.getTemplateFinder().find(name);
	}

	private void parse(String name, XMLReader parser, InputStream templateStream) throws AluminumException {
		try {
			parser.parse(new InputSource(templateStream));
		} catch (IOException exception) {
			throw new AluminumException(exception, "can't parse template '", name, "'");
		} catch (SAXException exception) {
			throw new AluminumException(exception, "can't parse template '", name, "'");
		}
	}

	/** The name of the configuration parameter that is used to set the default extensions of templates. */
	public final static String TEMPLATE_EXTENSION = "parser.xml.template_extension";

	/**
	 * The name of the configuration parameter that holds the class name of the template name translator that should be
	 * used.
	 */
	public final static String TEMPLATE_NAME_TRANSLATOR_CLASS = "parser.xml.template_name_translator.class";
}