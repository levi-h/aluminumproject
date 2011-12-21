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
package com.googlecode.aluminumproject.templates;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.writers.Writer;

import java.util.Collections;
import java.util.Map;

/**
 * The default {@link TextElement text element} implementation.
 *
 * @author levi_h
 */
public class DefaultTextElement implements TextElement {
	private String text;

	private Map<String, String> libraryUrlAbbreviations;

	/**
	 * Creates a default text element.
	 *
	 * @param text the text to write
	 * @param libraryUrlAbbreviations the text element's library URL abbreviations
	 */
	public DefaultTextElement(String text, Map<String, String> libraryUrlAbbreviations) {
		this.text = text;

		this.libraryUrlAbbreviations = libraryUrlAbbreviations;
	}

	public Map<String, String> getLibraryUrlAbbreviations() {
		return Collections.unmodifiableMap(libraryUrlAbbreviations);
	}

	public String getText() {
		return text;
	}

	public void process(Context context, Writer writer) throws AluminumException {
		TemplateInformation templateInformation = TemplateInformation.from(context);
		templateInformation.addTemplateElement(this);

		writer.write(text);

		templateInformation.removeCurrentTemplateElement();
	}
}