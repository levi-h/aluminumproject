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
package com.googlecode.aluminumproject.serialisers.xml;

import com.googlecode.aluminumproject.serialisers.ElementNameTranslator;
import com.googlecode.aluminumproject.templates.Template;
import com.googlecode.aluminumproject.templates.TemplateElement;

import java.io.PrintWriter;

/**
 * Serialises {@link TemplateElement template elements} of a certain type.
 *
 * @author levi_h
 * @param <E> the type of the template elements that are serialised by the template element serialiser
 */
public interface TemplateElementSerialiser<E extends TemplateElement> {
	/**
	 * Writes the part of a template element that should be written before its children are serialised.
	 *
	 * @param template the template that contains the serialised template element
	 * @param templateElement the serialised template element
	 * @param elementNameTranslator the element name translator to use
	 * @param writer the writer to use
	 */
	void writeBeforeChildren(Template template,
		E templateElement, ElementNameTranslator elementNameTranslator, PrintWriter writer);

	/**
	 * Writes the part of a template element that should be written before its children are serialised.
	 *
	 * @param template the template that contains the serialised template element
	 * @param templateElement the serialised template element
	 * @param elementNameTranslator the element name translator to use
	 * @param writer the writer to use
	 */
	void writeAfterChildren(Template template,
		E templateElement, ElementNameTranslator elementNameTranslator, PrintWriter writer);
}