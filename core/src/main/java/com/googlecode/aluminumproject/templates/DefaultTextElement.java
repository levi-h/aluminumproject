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
package com.googlecode.aluminumproject.templates;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.writers.Writer;

import java.util.Map;

/**
 * The default {@link TextElement text element} implementation.
 */
public class DefaultTextElement extends AbstractTemplateElement implements TextElement {
	private String text;

	/**
	 * Creates a default text element.
	 *
	 * @param text the text to write
	 * @param libraryUrlAbbreviations the text element's library URL abbreviations
	 * @param lineNumber the line number of the text element
	 */
	public DefaultTextElement(String text, Map<String, String> libraryUrlAbbreviations, int lineNumber) {
		super(libraryUrlAbbreviations, lineNumber);

		this.text = text;
	}

	public String getText() {
		return text;
	}

	protected void processAsCurrent(Context context, Writer writer) throws AluminumException {
		writer.write(text);
	}
}