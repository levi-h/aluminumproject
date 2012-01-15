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

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.writers.Writer;

import java.util.Map;

/**
 * A part of a template.
 */
public interface TemplateElement {
	/**
	 * Returns a map of abbreviations for library URLs that are effective for this template element. It includes the URL
	 * abbreviations of this element's ancestors.
	 *
	 * @return all library URL abbreviations of this template element (may be empty, but not {@code null})
	 */
	Map<String, String> getLibraryUrlAbbreviations();

	/**
	 * Processes this template element, keeping the current template element in the {@link TemplateInformation template
	 * information} up to date.
	 *
	 * @param context the context to use
	 * @param writer the writer to use
	 * @throws AluminumException when something goes wrong while processing this template element
	 */
	void process(Context context, Writer writer) throws AluminumException;
}