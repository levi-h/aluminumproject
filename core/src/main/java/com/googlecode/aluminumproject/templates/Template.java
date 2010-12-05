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
package com.googlecode.aluminumproject.templates;

import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.writers.Writer;

import java.util.List;

/**
 * A template. It consists of a tree of {@link TemplateElement template elements}. A template should be stateless; all
 * information regarding the processing of a template is kept in a {@link TemplateContext template context}.
 * <p>
 * Each time a template processes one of its children, it should keep the {@link Context#ALUMINUM_IMPLICIT_OBJECT
 * internal implicit object} in the {@link Context context} up to date. At the very least, it should provide the
 * following information:
 * <ul>
 * <li>The current template (i.e. itself) under key {@value #TEMPLATE_KEY};
 * <li>The template context under key {@value #TEMPLATE_CONTEXT_KEY}.
 * </ul>
 *
 * @author levi_h
 */
public interface Template {
	/**
	 * Processes the children of a template context's current template element. If the template context does not have a
	 * current template element, the elements at the root of this template will be processed.
	 *
	 * @param templateContext the context that contains the current template element
	 * @param context the context to process the template elements in
	 * @param writer the writer to use for the template elements
	 * @throws TemplateException when one of the template elements can't be processed
	 */
	public void processChildren(
		TemplateContext templateContext, Context context, Writer writer) throws TemplateException;

	/**
	 * Finds the parent of a certain template element.
	 *
	 * @param templateElement the template element to find the parent of
	 * @return the template element that's the parent of the given template element
	 * @throws TemplateException when this template does not contain the given template element
	 */
	TemplateElement getParent(TemplateElement templateElement) throws TemplateException;

	/**
	 * Finds the children of a certain template element.
	 *
	 * @param templateElement the template element to find the children of
	 * @return the template elements that have the given template element as their parent
	 * @throws TemplateException when this template does not contain the given template element
	 */
	List<TemplateElement> getChildren(TemplateElement templateElement) throws TemplateException;

	/** The key that is used to store the current template under in the {@link Context context}'s implicit map. */
	public final static String TEMPLATE_KEY = "template";

	/** The key under which the template context should be stored in the {@link Context context}'s implicit map. */
	public final static String TEMPLATE_CONTEXT_KEY = "template.context";
}