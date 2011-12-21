/*
 * Copyright 2010-2011 Levi Hoogenberg
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
package com.googlecode.aluminumproject.libraries.xml.model;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.writers.Writer;

import java.util.List;
import java.util.Map;

/**
 * An XML element.
 *
 * @author levi_h
 */
public interface Element {
	/**
	 * Selects elements and/or namespace, attribute, text, or comment values with an XPath query.
	 *
	 * @param expression the XPath expression to use
	 * @param context a set of namespaces that form the context for the XPath expression
	 * @return a list of results
	 * @throws AluminumException when the given expression is invalid or when one of the results is of an unsupported
	 *                           type
	 */
	List<?> select(String expression, Map<String, String> context) throws AluminumException;

	/**
	 * Transforms this element using a style sheet.
	 *
	 * @param styleSheet the root element of the style sheet to use
	 * @return the text and/or elements that the transformation resulted in
	 * @throws AluminumException when something goes wrong during the transformation
	 */
	List<?> transform(Element styleSheet) throws AluminumException;

	/**
	 * Writes a document with this element as root element to a writer.
	 *
	 * @param writer the writer to use
	 * @param indentation the number of spaces to use for indentation
	 * @throws AluminumException when the document can't be written
	 */
	void writeDocument(Writer writer, int indentation) throws AluminumException;
}