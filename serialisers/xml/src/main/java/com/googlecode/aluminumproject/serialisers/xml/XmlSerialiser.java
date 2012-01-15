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
package com.googlecode.aluminumproject.serialisers.xml;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.configuration.ConfigurationElementFactory;
import com.googlecode.aluminumproject.serialisers.ElementNameTranslator;
import com.googlecode.aluminumproject.serialisers.Serialiser;
import com.googlecode.aluminumproject.templates.ActionElement;
import com.googlecode.aluminumproject.templates.ExpressionElement;
import com.googlecode.aluminumproject.templates.Template;
import com.googlecode.aluminumproject.templates.TemplateElement;
import com.googlecode.aluminumproject.templates.TextElement;
import com.googlecode.aluminumproject.utilities.Logger;
import com.googlecode.aluminumproject.utilities.Utilities;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Serialises templates as XML documents.
 * <p>
 * The serialisation is delegated to {@link TemplateElementSerialiser template element serialisers}; for each template
 * element type, a different template element serialiser is used. The XML serialiser uses the following template element
 * serialisers:
 * <ul>
 * <li>An {@link ActionElementSerialiser action element serialiser} for {@link ActionElement action elements};
 * <li>A {@link TextElementSerialiser text element serialiser} for {@link TextElement text elements};
 * <li>An {@link ExpressionElementSerialiser expression element serialiser} for {@link ExpressionElement expression
 *     elements}.
 * </ul>
 * Subclasses that want to support more template element types or would like to use different template element
 * serialisers can override the {@link #addTemplateElementSerialisers(Map) addTemplateElementSerialisers method} or
 * {@link #addTemplateElementSerialiser(Class, TemplateElementSerialiser) add} a template element serialiser from the
 * {@link #initialise(Configuration) initialise method}.
 * <p>
 * To support a custom naming strategy in templates, the XML serialiser allows an {@link ElementNameTranslator element
 * name translator} to be configured (using the {@value #ELEMENT_NAME_TRANSLATOR_CLASS} configuration property). If no
 * element name translator is configured, the {@link XmlElementNameTranslator default element name translator} will be
 * used.
 */
public class XmlSerialiser implements Serialiser {
	private Configuration configuration;

	private ElementNameTranslator elementNameTranslator;

	private Map<Class<? extends TemplateElement>, TemplateElementSerialiser<?>> templateElementSerialisers;

	/** The logger to use. */
	protected final Logger logger;

	/**
	 * Creates an XML serialiser.
	 */
	public XmlSerialiser() {
		templateElementSerialisers = new HashMap<Class<? extends TemplateElement>, TemplateElementSerialiser<?>>();

		logger = Logger.get(getClass());
	}

	public void initialise(Configuration configuration) throws AluminumException {
		this.configuration = configuration;

		createElementNameTranslator();

		logger.debug("adding template element serialisers");

		addTemplateElementSerialisers(templateElementSerialisers);
	}

	private void createElementNameTranslator() throws AluminumException {
		String elementNameTranslatorClass = configuration.getParameters().getValue(
			ELEMENT_NAME_TRANSLATOR_CLASS, XmlElementNameTranslator.class.getName());

		logger.debug("creating element name translator of type '", elementNameTranslatorClass, "'");

		ConfigurationElementFactory configurationElementFactory = configuration.getConfigurationElementFactory();

		elementNameTranslator =
			configurationElementFactory.instantiate(elementNameTranslatorClass, ElementNameTranslator.class);
	}

	/**
	 * Adds all template element serialisers.
	 *
	 * @param templateElementSerialisers the map that template element serialisers should be added to
	 */
	protected void addTemplateElementSerialisers(
			Map<Class<? extends TemplateElement>, TemplateElementSerialiser<?>> templateElementSerialisers) {
		addTemplateElementSerialiser(ActionElement.class, new ActionElementSerialiser());
		addTemplateElementSerialiser(TextElement.class, new TextElementSerialiser());
		addTemplateElementSerialiser(ExpressionElement.class, new ExpressionElementSerialiser());
	}

	/**
	 * Adds a template element serialiser for a certain template element type.
	 *
	 * @param <E> the type of the template element serialiser
	 * @param templateElementType the template element type for which the given template element serialiser should be
	 *                            used
	 * @param templateElementSerialiser the template element serialiser to use for template elements with the given type
	 */
	protected <E extends TemplateElement> void addTemplateElementSerialiser(
			Class<E> templateElementType, TemplateElementSerialiser<E> templateElementSerialiser) {
		logger.debug("adding template element serialiser ", templateElementSerialiser,
			" for template element type ", templateElementType.getName());

		templateElementSerialisers.put(templateElementType, templateElementSerialiser);
	}

	public void disable() {
		templateElementSerialisers.clear();
	}

	public void serialiseTemplate(Template template, String name) throws AluminumException {
		OutputStream out = configuration.getTemplateStoreFinder().find(name);

		PrintWriter writer = new PrintWriter(out);

		serialiseChildElements(template, null, writer);

		if (writer.checkError()) {
			throw new AluminumException("can't serialise template ", template);
		} else {
			writer.close();
		}
	}

	private void serialiseChildElements(
			Template template, TemplateElement templateElement, PrintWriter writer) throws AluminumException {
		for (TemplateElement childElement: template.getChildren(templateElement)) {
			serialiseTemplateElement(template, childElement, writer);
		}
	}

	private void serialiseTemplateElement(
			Template template, TemplateElement templateElement, PrintWriter writer) throws AluminumException {
		Class<? extends TemplateElement> templateElementType = templateElement.getClass();

		TemplateElementSerialiser<TemplateElement> templateElementSerialiser =
			Utilities.typed(findTemplateElementSerialiser(templateElement.getClass()));

		if (templateElementSerialiser == null) {
			logger.warn("no template element serialiser found for template element type ",
				templateElementType.getName());
		} else {
			templateElementSerialiser.writeBeforeChildren(template, templateElement, elementNameTranslator, writer);

			serialiseChildElements(template, templateElement, writer);

			templateElementSerialiser.writeAfterChildren(template, templateElement, elementNameTranslator, writer);
		}
	}

	/**
	 * Finds a template element serialiser for a certain template element type. This implementation checks all added
	 * template element serialisers and returns the first one that can serialise template elements with either the
	 * requested type or one of its supertypes.
	 *
	 * @param <E> the type of the returned template element serialiser
	 * @param templateElementType the type of the template element to find a template element serialiser for
	 * @return a template element serialiser for the given template element type or {@code null} when no template
	 *         element serialiser was added that can serialise template elements with the requested type
	 */
	protected <E extends TemplateElement> TemplateElementSerialiser<? super E> findTemplateElementSerialiser(
			Class<E> templateElementType) {
		TemplateElementSerialiser<? super E> templateElementSerialiser = null;

		for (Class<? extends TemplateElement> serialisedTemplateElementType: templateElementSerialisers.keySet()) {
			if (serialisedTemplateElementType.isAssignableFrom(templateElementType)) {
				templateElementSerialiser =
					Utilities.typed(templateElementSerialisers.get(serialisedTemplateElementType));
			}
		}

		return templateElementSerialiser;
	}

	/** The name of the configuration property that holds the class name of the element name translator to use. */
	public final static String ELEMENT_NAME_TRANSLATOR_CLASS = "serialiser.xml.element_name_translator.class";
}