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

import com.googlecode.aluminumproject.Logger;
import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.configuration.ConfigurationElementFactory;
import com.googlecode.aluminumproject.configuration.ConfigurationException;
import com.googlecode.aluminumproject.configuration.ConfigurationParameters;
import com.googlecode.aluminumproject.parsers.ParseException;
import com.googlecode.aluminumproject.parsers.Parser;
import com.googlecode.aluminumproject.parsers.TemplateNameTranslator;
import com.googlecode.aluminumproject.resources.ResourceException;
import com.googlecode.aluminumproject.templates.Template;
import com.googlecode.aluminumproject.utilities.UtilityException;

import java.io.IOException;
import java.net.URL;

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
 * <p>
 * All tags in the XML templates are expected to be actions - any tag that is not an action causes an exception. It's
 * possible to configure the XML parser to allow these tags by using the boolean parameter {@value
 * #ALLOW_NON_ACTION_TAGS}. When used, it causes all unrecognised tags to be parsed into text elements. Note that
 * whether a tag is an action or not is determined by looking at the prefix only; if a tag's prefix maps to a library,
 * it's considered to be an action tag.
 *
 * @author levi_h
 */
public class XmlParser implements Parser {
	private Configuration configuration;

	private String templateExtension;

	private TemplateNameTranslator templateNameTranslator;

	private boolean allowNonActionTags;

	private final Logger logger;

	/**
	 * Creates an XML parser.
	 */
	public XmlParser() {
		logger = Logger.get(getClass());
	}

	public void initialise(Configuration configuration) throws ConfigurationException {
		this.configuration = configuration;

		ConfigurationParameters parameters = configuration.getParameters();

		templateExtension = parameters.getValue(TEMPLATE_EXTENSION, null);

		if (templateExtension != null) {
			logger.debug("using template extension '", templateExtension, "'");
		}

		createTemplateNameTranslator();

		allowNonActionTags = Boolean.parseBoolean(parameters.getValue(ALLOW_NON_ACTION_TAGS, "false"));

		logger.debug("non-action tags are ", allowNonActionTags ? "" : "not ", "allowed");
	}

	private void createTemplateNameTranslator() throws ConfigurationException {
		String templateNameTranslatorClassName = configuration.getParameters().getValue(
			TEMPLATE_NAME_TRANSLATOR_CLASS, XmlTemplateNameTranslator.class.getName());

		logger.debug("creating template name translator of type '", templateNameTranslatorClassName, "'");

		ConfigurationElementFactory configurationElementFactory = configuration.getConfigurationElementFactory();

		templateNameTranslator =
			configurationElementFactory.instantiate(templateNameTranslatorClassName, TemplateNameTranslator.class);
	}

	public Template parseTemplate(String name) throws ParseException {
		XMLReader parser = createParser();
		logger.debug("created XML parser");

		TemplateHandler templateHandler =
			new TemplateHandler(configuration, templateNameTranslator, allowNonActionTags);
		parser.setContentHandler(templateHandler);
		logger.debug("using template handler ", templateHandler);

		URL templateUrl = findTemplateUrl(name);
		logger.debug("found template '", name, "': ", templateUrl);

		parse(name, parser, templateUrl);
		logger.debug("parsed template '", name, "'");

		Template template = templateHandler.getTemplate();
		logger.debug("resulting template element: ", template);

		return template;
	}

	private XMLReader createParser() throws ParseException {
		SAXParserFactory parserFactory = SAXParserFactory.newInstance();
		parserFactory.setNamespaceAware(true);

		XMLReader parser;

		try {
			parser = parserFactory.newSAXParser().getXMLReader();
		} catch (SAXException exception) {
			throw new ParseException(exception, "can't create an XML parser");
		} catch (ParserConfigurationException exception) {
			throw new ParseException(exception, "can't configure the XML parser factory");
		}

		return parser;
	}

	private URL findTemplateUrl(String name) throws ParseException {
		if (templateExtension != null) {
			name = String.format("%s.%s", name, templateExtension);
		}

		URL templateUrl;

		try {
			templateUrl = configuration.getTemplateFinderFactory().createTemplateFinder().find(name);
		} catch (ResourceException exception) {
			throw new ParseException(exception, "can't create template finder");
		} catch (UtilityException exception) {
			throw new ParseException(exception, "can't find template");
		}

		return templateUrl;
	}

	private void parse(String name, XMLReader parser, URL templateUrl) throws ParseException {
		try {
			parser.parse(new InputSource(templateUrl.openStream()));
		} catch (IOException exception) {
			throw new ParseException(exception, "can't parse template '", name, "'");
		} catch (SAXException exception) {
			throw new ParseException(exception, "can't parse template '", name, "'");
		}
	}

	/** The name of the configuration parameter that is used to set the default extensions of templates. */
	public final static String TEMPLATE_EXTENSION = "parser.xml.template_extension";

	/**
	 * The name of the configuration parameter that holds the class name of the template name translator that should be
	 * used.
	 */
	public final static String TEMPLATE_NAME_TRANSLATOR_CLASS = "parser.xml.template_name_translator.class";

	/**
	 * The name of the configuration parameter that determines whether tags that are not actions should be added as
	 * text elements. By default, non-action tags are disallowed and will cause a parse exception.
	 */
	public final static String ALLOW_NON_ACTION_TAGS = "parser.xml.allow_non_action_tags";
}