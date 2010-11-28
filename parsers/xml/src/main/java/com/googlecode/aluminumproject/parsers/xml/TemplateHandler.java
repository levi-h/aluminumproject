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
import com.googlecode.aluminumproject.expressions.ExpressionFactory;
import com.googlecode.aluminumproject.expressions.ExpressionOccurrence;
import com.googlecode.aluminumproject.libraries.actions.ActionParameter;
import com.googlecode.aluminumproject.parsers.TemplateNameTranslator;
import com.googlecode.aluminumproject.templates.ActionContributionDescriptor;
import com.googlecode.aluminumproject.templates.ActionDescriptor;
import com.googlecode.aluminumproject.templates.Template;
import com.googlecode.aluminumproject.templates.TemplateBuilder;
import com.googlecode.aluminumproject.templates.TemplateElement;
import com.googlecode.aluminumproject.templates.TemplateException;
import com.googlecode.aluminumproject.utilities.ParserUtilities;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * A {@link ContentHandler content handler} that builds a template.
 * <p>
 * This handler is meant to be used once and then discarded.
 *
 * @author levi_h
 */
public class TemplateHandler extends DefaultHandler {
	private Configuration configuration;
	private TemplateNameTranslator templateNameTranslator;

	private Stack<Map<String, String>> libraryUrlAbbreviationsStack;

	private TemplateBuilder templateBuilder;

	private StringBuilder textBuffer;

	private final Logger logger;

	/**
	 * Creates a template handler.
	 *
	 * @param configuration the configuration to use
	 * @param templateNameTranslator the template name translator to use
	 */
	public TemplateHandler(Configuration configuration, TemplateNameTranslator templateNameTranslator) {
		this.configuration = configuration;
		this.templateNameTranslator = templateNameTranslator;

		libraryUrlAbbreviationsStack = new Stack<Map<String, String>>();

		templateBuilder = new TemplateBuilder();

		textBuffer = new StringBuilder();

		logger = Logger.get(getClass());
	}

	@Override
	public void startPrefixMapping(String prefix, String uri) {
		Map<String, String> libraryUrlAbbreviations = new HashMap<String, String>();

		if (!libraryUrlAbbreviationsStack.isEmpty()) {
			libraryUrlAbbreviations.putAll(getLibraryUrlAbbreviations());
		}

		libraryUrlAbbreviations.put(prefix, uri);

		libraryUrlAbbreviationsStack.push(libraryUrlAbbreviations);
	}

	@Override
	public void endPrefixMapping(String prefix) {
		libraryUrlAbbreviationsStack.pop();
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		handleText();

		logger.debug("starting parsing element '", qName, "'");

		String library = qName.contains(":") ? qName.split(":")[0] : "";
		String actionName = templateNameTranslator.translateActionName(localName);

		ActionDescriptor action = new ActionDescriptor(library, actionName);
		Map<String, ActionParameter> parameters = new HashMap<String, ActionParameter>();
		List<ActionContributionDescriptor> contributions = new LinkedList<ActionContributionDescriptor>();

		processAttributes(attributes, parameters, contributions);

		logger.debug("parameters for action '", actionName, "': ", parameters);
		logger.debug("contributions for action '", actionName, "': ", contributions);

		TemplateElement actionElement = createActionElement(action, parameters, contributions);
		logger.debug("created action element ", actionElement);

		templateBuilder.addTemplateElement(actionElement);
		logger.debug("added action element");
	}

	private void processAttributes(Attributes attributes,
			Map<String, ActionParameter> parameters, List<ActionContributionDescriptor> contributions) {
		for (int i = 0; i < attributes.getLength(); i++) {
			String localName = attributes.getLocalName(i);
			ActionParameter parameter = ParserUtilities.createParameter(attributes.getValue(i), configuration);

			String prefix = getPrefix(attributes.getQName(i));

			if (prefix.equals("")) {
				parameters.put(templateNameTranslator.translateActionParameterName(localName), parameter);
			} else {
				String actionContributionName = templateNameTranslator.translateActionContributionName(localName);

				contributions.add(new ActionContributionDescriptor(prefix, actionContributionName, parameter));
			}
		}
	}

	private String getPrefix(String qName) {
		return qName.contains(":") ? qName.split(":")[0] : "";
	}

	@Override
	public void characters(char[] characters, int offset, int length) {
		String text = new String(characters, offset, length);

		textBuffer.append(text);
		logger.debug("added '", text, "' to text buffer");
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		handleText();

		templateBuilder.restoreCurrentTemplateElement();

		logger.debug("finished parsing element '", qName, "'");
	}

	private void handleText() throws SAXException {
		if (textBuffer.length() > 0) {
			String text = textBuffer.toString();

			SortedMap<ExpressionOccurrence, ExpressionFactory> occurrences =
				ParserUtilities.getExpressionOccurrences(text, configuration);

			for (Map.Entry<ExpressionOccurrence, ExpressionFactory> entry: occurrences.entrySet()) {
				TemplateElement element;

				ExpressionOccurrence occurrence = entry.getKey();
				String elementText = text.substring(occurrence.getBeginIndex(), occurrence.getEndIndex());

				ExpressionFactory expressionFactory = entry.getValue();

				if (expressionFactory == null) {
					element = createTextElement(elementText);
					logger.debug("created text element");
				} else {
					element = createExpressionElement(expressionFactory, elementText);
					logger.debug("created expression element");
				}

				templateBuilder.addTemplateElement(element);
				templateBuilder.restoreCurrentTemplateElement();
				logger.debug("added template element ", element);
			}

			textBuffer.delete(0, textBuffer.length());
		}
	}

	private TemplateElement createActionElement(ActionDescriptor action, Map<String, ActionParameter> parameters,
			List<ActionContributionDescriptor> contributions) throws SAXException {
		try {
			return configuration.getTemplateElementFactory().createActionElement(
				action, parameters, contributions, getLibraryUrlAbbreviations());
		} catch (TemplateException exception) {
			throw new SAXException("can't create action element", exception);
		}
	}

	private TemplateElement createTextElement(String text) throws SAXException {
		try {
			return configuration.getTemplateElementFactory().createTextElement(text, getLibraryUrlAbbreviations());
		} catch (TemplateException exception) {
			throw new SAXException("can't create text element", exception);
		}
	}

	private TemplateElement createExpressionElement(
			ExpressionFactory expressionFactory, String text) throws SAXException {
		try {
			return configuration.getTemplateElementFactory().createExpressionElement(
				expressionFactory, text, getLibraryUrlAbbreviations());
		} catch (TemplateException exception) {
			throw new SAXException("can't create expression element", exception);
		}
	}

	private Map<String, String> getLibraryUrlAbbreviations() {
		return libraryUrlAbbreviationsStack.isEmpty()
			? Collections.<String, String>emptyMap() : libraryUrlAbbreviationsStack.peek();
	}

	/**
	 * Returns the template that was built.
	 *
	 * @return the built template
	 */
	public Template getTemplate() {
		return templateBuilder.build();
	}
}