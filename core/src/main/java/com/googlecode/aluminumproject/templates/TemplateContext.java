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

import com.googlecode.aluminumproject.Logger;
import com.googlecode.aluminumproject.libraries.actions.Action;

import java.util.LinkedList;
import java.util.List;

/**
 * The current state of a {@link Template template}. It contains two stacks: the {@link TemplateElement template
 * elements} that are being processed and the currently executing {@link Action actions}.
 *
 * @author levi_h
 */
public class TemplateContext {
	private List<TemplateElement> templateElements;

	private List<Action> actions;

	private final Logger logger;

	/**
	 * Creates a template context.
	 */
	public TemplateContext() {
		templateElements = new LinkedList<TemplateElement>();

		actions = new LinkedList<Action>();

		logger = Logger.get(getClass());
	}

	/**
	 * Creates a template context by copying another template context's state.
	 *
	 * @param templateContext the template context to copy
	 */
	public TemplateContext(TemplateContext templateContext) {
		templateElements = new LinkedList<TemplateElement>(templateContext.templateElements);

		actions = new LinkedList<Action>(templateContext.actions);

		logger = Logger.get(getClass());
	}

	/**
	 * Adds a template element to the stack of template elements that are being processed.
	 *
	 * @param templateElement the template element to add
	 */
	public void addTemplateElement(TemplateElement templateElement) {
		logger.debug("adding template element ", templateElement, " to template context");

		templateElements.add(0, templateElement);
	}

	/**
	 * Retrieves the template element that's being processed.
	 *
	 * @return the current template element or {@code null} if there is no template element being processed
	 */
	public TemplateElement getCurrentTemplateElement() {
		return templateElements.isEmpty() ? null : templateElements.get(0);
	}

	/**
	 * Removes the current template element.
	 *
	 * @throws TemplateException when there is no template element to remove
	 */
	public void removeCurrentTemplateElement() throws TemplateException {
		if (templateElements.isEmpty()) {
			throw new TemplateException("there is no current template element");
		} else {
			TemplateElement templateElement = templateElements.remove(0);

			logger.debug("removed template element ", templateElement, " from template context");
		}
	}

	/**
	 * Adds an action to the stack of currently excecuting actions.
	 *
	 * @param action the action to add
	 */
	public void addAction(Action action) {
		logger.debug("adding action ", action, " to template context");

		actions.add(0, action);
	}

	/**
	 * Retrieves the currently executing action.
	 *
	 * @return the current action or {@code null} if there is no action executing
	 */
	public Action getCurrentAction() {
		return actions.isEmpty() ? null : actions.get(0);
	}

	/**
	 * Removes the current action.
	 *
	 * @throws TemplateException when there is no action to remove
	 */
	public void removeCurrentAction() throws TemplateException {
		if (actions.isEmpty()) {
			throw new TemplateException("there is no current action");
		} else {
			Action action = actions.remove(0);

			logger.debug("removed action ", action, " from template context");
		}
	}
}