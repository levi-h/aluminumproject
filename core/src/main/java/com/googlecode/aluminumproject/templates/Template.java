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

import java.util.List;

/**
 * A template. It consists of a tree of {@link TemplateElement template elements}. A template should be stateless; all
 * information regarding the processing of a template is kept in {@link TemplateInformation template information}.
 */
public interface Template {
	/**
	 * Determines whether this template contains a certain template element.
	 *
	 * @param templateElement the template element to check
	 * @return {@code true} if this template contains the given template element; {@code false} if it doesn't
	 */
	boolean contains(TemplateElement templateElement);

	/**
	 * Finds the parent of a certain template element.
	 *
	 * @param templateElement the template element to find the parent of
	 * @return the template element that's the parent of the given template element
	 * @throws AluminumException when this template does not contain the given template element
	 */
	TemplateElement getParent(TemplateElement templateElement) throws AluminumException;

	/**
	 * Finds the children of a certain template element.
	 *
	 * @param templateElement the template element to find the children of
	 * @return the template elements that have the given template element as their parent
	 * @throws AluminumException when this template does not contain the given template element
	 */
	List<TemplateElement> getChildren(TemplateElement templateElement) throws AluminumException;
}