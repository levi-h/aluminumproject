/*
 * Copyright 2010-2012 Levi Hoogenberg
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

@SuppressWarnings("javadoc")
public interface Element {
	List<?> select(String expression, SelectionContext context) throws AluminumException;

	List<?> transform(Element styleSheet) throws AluminumException;

	void writeDocument(Writer writer, int indentation) throws AluminumException;
}