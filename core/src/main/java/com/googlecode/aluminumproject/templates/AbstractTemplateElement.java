/*
 * Copyright 2012 Aluminum project
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
 * Helps implementing {@link TemplateElement the template element interface}.
 */
public abstract class AbstractTemplateElement implements TemplateElement {
	private Map<String, String> libraryUrlAbbreviations;

	private int lineNumber;

	/**
	 * Creates an abstract template element.
	 *
	 * @param libraryUrlAbbreviations the library URL abbreviations of the template element
	 * @param lineNumber the line number of the element
	 */
	protected AbstractTemplateElement(Map<String, String> libraryUrlAbbreviations, int lineNumber) {
		this.libraryUrlAbbreviations = libraryUrlAbbreviations;

		this.lineNumber = lineNumber;
	}

	public Map<String, String> getLibraryUrlAbbreviations() {
		return Collections.unmodifiableMap(libraryUrlAbbreviations);
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public void process(Context context, Writer writer) throws AluminumException {
		TemplateInformation templateInformation = TemplateInformation.from(context);
		templateInformation.addTemplateElement(this);

		processAsCurrent(context, writer);

		templateInformation.removeCurrentTemplateElement();
	}

	/**
	 * Processes this template element, which is now the current template element.
	 *
	 * @param context the context to use
	 * @param writer the writer to use
	 * @throws AluminumException when something goes wrong while processing this template element
	 */
	protected abstract void processAsCurrent(Context context, Writer writer) throws AluminumException;
}