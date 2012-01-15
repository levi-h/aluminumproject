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

import com.googlecode.aluminumproject.serialisers.ElementNameTranslator;
import com.googlecode.aluminumproject.templates.Template;
import com.googlecode.aluminumproject.templates.TextElement;
import com.googlecode.aluminumproject.utilities.Logger;

import java.io.PrintWriter;

/**
 * Serialises {@link TextElement text elements}.
 */
public class TextElementSerialiser implements TemplateElementSerialiser<TextElement> {
	private final Logger logger;

	/**
	 * Creates a text element serialiser.
	 */
	public TextElementSerialiser() {
		logger = Logger.get(getClass());
	}

	public void writeBeforeChildren(Template template,
			TextElement textElement, ElementNameTranslator elementNameTranslator, PrintWriter writer) {
		String text = textElement.getText();

		logger.debug("serialising text '", text, "'");

		writer.print(text);
	}

	public void writeAfterChildren(Template template,
		TextElement textElement, ElementNameTranslator elementNameTranslator, PrintWriter writer) {}
}