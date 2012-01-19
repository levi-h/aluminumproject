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

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Builds a {@link Template template}.
 * <p>
 * A template builder works from a {@link TemplateElement template element}. Initially, this template element is {@code
 * null}, but {@link #addTemplateElement(TemplateElement) adding} a template element causes it to become the current
 * one. After adding children to that template element, the previous template element can be {@link
 * #restoreCurrentTemplateElement() made current again}.
 * <p>
 * When all template elements have been added to the template builder, a template can be {@link #build() built}. After
 * that, the template builder should no longer be used.
 */
public class TemplateBuilder {
	private String name;

	private TemplateElements templateElements;
	private TemplateElement currentTemplateElement;

	private boolean built;

	/**
	 * Creates a template builder.
	 *
	 * @param name the name of the template to build
	 */
	public TemplateBuilder(String name) {
		this.name = name;

		templateElements = new TemplateElements();
	}

	/**
	 * Adds a template element to the current template element and makes the new template element the current one.
	 *
	 * @param templateElement the template element to add
	 * @throws AluminumException when this template builder should no longer be used
	 */
	public void addTemplateElement(TemplateElement templateElement) throws AluminumException {
		ensureNotBuilt();

		templateElements.addTemplateElement(currentTemplateElement, templateElement);

		currentTemplateElement = templateElement;
	}

	/**
	 * Makes the template element that was the current one before the current template element current again.
	 *
	 * @throws AluminumException when there is no previous template element to restore or when this template builder
	 *                           should no longer be used
	 */
	public void restoreCurrentTemplateElement() throws AluminumException {
		ensureNotBuilt();

		currentTemplateElement = templateElements.getParent(currentTemplateElement);
	}

	/**
	 * Builds a template from the added template elements.
	 *
	 * @return a template that contains the template elements that were added to this builder
	 */
	public Template build() {
		ensureNotBuilt();

		built = true;

		return new BuiltTemplate(name, templateElements);
	}

	private void ensureNotBuilt() throws AluminumException {
		if (built) {
			throw new AluminumException("can't operate on builder - a template has already been built");
		}
	}

	/**
	 * Contains the template elements that were added to the {@link TemplateBuilder template builder}.
	 */
	private static class TemplateElements {
		private Map<TemplateElement, List<TemplateElement>> templateElements;

		/**
		 * Creates template elements.
		 */
		public TemplateElements() {
			templateElements = new LinkedHashMap<TemplateElement, List<TemplateElement>>();

			addTemplateElements(null);
		}

		private void addTemplateElement(TemplateElement parentTemplateElement, TemplateElement templateElement) {
			templateElements.get(parentTemplateElement).add(templateElement);

			addTemplateElements(templateElement);
		}

		private void addTemplateElements(TemplateElement parentTemplateElement) {
			templateElements.put(parentTemplateElement, new LinkedList<TemplateElement>());
		}

		private boolean contains(TemplateElement templateElement) {
			return templateElements.containsKey(templateElement);
		}

		private TemplateElement getParent(TemplateElement templateElement) throws AluminumException {
			Iterator<Map.Entry<TemplateElement, List<TemplateElement>>> it = templateElements.entrySet().iterator();

			TemplateElement parentTemplateElement = null;
			boolean parentTemplateElementFound = false;

			while (it.hasNext() && !parentTemplateElementFound) {
				Map.Entry<TemplateElement, List<TemplateElement>> templateElementWithChildren = it.next();

				if (parentTemplateElementFound = templateElementWithChildren.getValue().contains(templateElement)) {
					parentTemplateElement = templateElementWithChildren.getKey();
				}
			}

			if (parentTemplateElementFound) {
				return parentTemplateElement;
			} else {
				throw new AluminumException("element ", templateElement, " could not be found");
			}
		}

		private List<TemplateElement> getChildren(TemplateElement templateElement) throws AluminumException {
			if (templateElements.containsKey(templateElement)) {
				return templateElements.get(templateElement);
			} else {
				throw new AluminumException("element ", templateElement, " could not be found");
			}
		}
	}

	/**
	 * The template that is built by the {@link TemplateBuilder template builder}.
	 */
	private static class BuiltTemplate implements Template {
		private String name;

		private TemplateElements templateElements;

		public BuiltTemplate(String name, TemplateElements templateElements) {
			this.name = name;

			this.templateElements = templateElements;
		}

		public String getName() {
			return name;
		}

		public boolean contains(TemplateElement templateElement) {
			return templateElements.contains(templateElement);
		}

		public TemplateElement getParent(TemplateElement templateElement) throws AluminumException {
			return templateElements.getParent(templateElement);
		}

		public List<TemplateElement> getChildren(TemplateElement templateElement) throws AluminumException {
			return templateElements.getChildren(templateElement);
		}
	}
}