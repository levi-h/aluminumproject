/*
 * Copyright 2011-2012 Aluminum project
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
import com.googlecode.aluminumproject.libraries.actions.Action;
import com.googlecode.aluminumproject.utilities.Logger;

import java.util.Stack;

/**
 * Information about a template that is being processed. It is available as an implicit object in a {@link Context
 * context} and can be obtained using a {@link #from(Context) convenience method}.
 */
public class TemplateInformation {
	private Template template;

	private String name;
	private String parser;

	private Stack<TemplateElement> templateElements;
	private Stack<Action> actions;

	private final Logger logger;

	private TemplateInformation() {
		templateElements = new Stack<TemplateElement>();
		actions = new Stack<Action>();

		logger = Logger.get(getClass());
	}

	/**
	 * Sets the template that is being processed.
	 *
	 * @param template the template being processed
	 * @param name the name of the template
	 * @param parser the name of the parser that produced the template
	 */
	public void setTemplate(Template template, String name, String parser) {
		this.template = template;

		this.name = name;
		this.parser = parser;
	}

	/**
	 * Returns the processed template.
	 *
	 * @return the template that is being processed
	 */
	public Template getTemplate() {
		return template;
	}

	/**
	 * Returns the template name.
	 *
	 * @return the name of the processed template
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the parser name.
	 *
	 * @return the name of the parser that produced the processed template
	 */
	public String getParser() {
		return parser;
	}

	/**
	 * Adds a template element to the stack of template elements that are being processed.
	 *
	 * @param templateElement the template element to add
	 */
	public void addTemplateElement(TemplateElement templateElement) {
		logger.debug("adding template element ", templateElement, " to template information");

		templateElements.push(templateElement);
	}

	/**
	 * Retrieves the template element that's being processed.
	 *
	 * @return the current template element or {@code null} if there is no template element being processed
	 */
	public TemplateElement getCurrentTemplateElement() {
		return templateElements.isEmpty() ? null : templateElements.peek();
	}

	/**
	 * Removes the current template element.
	 *
	 * @throws AluminumException when there is no template element to remove
	 */
	public void removeCurrentTemplateElement() throws AluminumException {
		if (templateElements.isEmpty()) {
			throw new AluminumException("there is no current template element");
		} else {
			TemplateElement templateElement = templateElements.pop();

			logger.debug("removed template element ", templateElement, " from template information");
		}
	}

	/**
	 * Adds an action to the stack of currently excecuting actions.
	 *
	 * @param action the action to add
	 */
	public void addAction(Action action) {
		logger.debug("adding action ", action, " to template information");

		actions.push(action);
	}

	/**
	 * Retrieves the currently executing action.
	 *
	 * @return the current action or {@code null} if there is no action executing
	 */
	public Action getCurrentAction() {
		return actions.isEmpty() ? null : actions.peek();
	}

	/**
	 * Removes the current action.
	 *
	 * @throws AluminumException when there is no action to remove
	 */
	public void removeCurrentAction() throws AluminumException {
		if (actions.isEmpty()) {
			throw new AluminumException("there is no current action");
		} else {
			Action action = actions.pop();

			logger.debug("removed action ", action, " from template information");
		}
	}

	/**
	 * Finds a template information implicit object in a certain context and creates it if it does not yet exist.
	 *
	 * @param context the context to search in
	 * @return the (possibly new) template information in the given context
	 * @throws AluminumException when the template information can't be obtained
	 */
	public static TemplateInformation from(Context context) throws AluminumException {
		if (!context.getImplicitObjectNames().contains(TEMPLATE_INFORMATION)) {
			context.addImplicitObject(TEMPLATE_INFORMATION, new TemplateInformation());
		}

		return (TemplateInformation) context.getImplicitObject(TEMPLATE_INFORMATION);
	}

	private final static String TEMPLATE_INFORMATION =
		Context.RESERVED_IMPLICIT_OBJECT_NAME_PREFIX + ".template_information";
}