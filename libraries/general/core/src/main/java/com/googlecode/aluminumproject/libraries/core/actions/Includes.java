/*
 * Copyright 2011 Levi Hoogenberg
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
package com.googlecode.aluminumproject.libraries.core.actions;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.annotations.Injected;
import com.googlecode.aluminumproject.annotations.Required;
import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.ContextEnricher;
import com.googlecode.aluminumproject.libraries.actions.AbstractDynamicallyParameterisableAction;
import com.googlecode.aluminumproject.libraries.actions.Action;
import com.googlecode.aluminumproject.libraries.actions.ActionBody;
import com.googlecode.aluminumproject.libraries.actions.ActionParameter;
import com.googlecode.aluminumproject.libraries.core.actions.Blocks.Block;
import com.googlecode.aluminumproject.libraries.core.actions.Blocks.BlockContents;
import com.googlecode.aluminumproject.templates.TemplateElement;
import com.googlecode.aluminumproject.templates.TemplateInformation;
import com.googlecode.aluminumproject.templates.TemplateProcessor;
import com.googlecode.aluminumproject.writers.NullWriter;
import com.googlecode.aluminumproject.writers.Writer;

import java.util.Map;
import java.util.Stack;

/**
 * Contains two actions that include templates into another template.
 * <p>
 * By default, included templates are processed in a stand-alone fashion. By setting the <em>inherit ancestors</em>
 * parameter, this behaviour can be changed.
 *
 * @author levi_h
 */
public class Includes {
	/**
	 * Includes a template.
	 * <p>
	 * The action has two parameters: the name of the included template and the name of the parser that should be used
	 * for it. The parser parameter may be omitted; in that case, the parser that is used for the template that contains
	 * the include action will be used to parse the included template as well.
	 * <p>
	 * The include action supports dynamic parameters, all of which will be made available as variables in the included
	 * template's context. It's also possible to pass blocks to included templates by using the {@link Block block}
	 * action; these blocks can be used through the {@link BlockContents block contents} action.
	 *
	 * @author levi_h
	 */
	public static class Include extends AbstractDynamicallyParameterisableAction {
		private @Required String name;
		private String parser;

		private boolean inheritAncestors;

		private @Injected Configuration configuration;

		/**
		 * Creates an <em>include</em> action.
		 */
		public Include() {}

		public void execute(Context context, Writer writer) throws AluminumException {
			getBody().invoke(context, new NullWriter());

			Context subcontext = context.createSubcontext();

			TemplateInformation templateInformation = TemplateInformation.from(context);
			TemplateInformation subtemplateInformation = TemplateInformation.from(subcontext);

			if (inheritAncestors) {
				inheritTemplateElements(templateInformation, subtemplateInformation);
				inheritActions(templateInformation, subtemplateInformation);
			}

			for (Map.Entry<String, ActionParameter> variable: getDynamicParameters().entrySet()) {
				String variableName = variable.getKey();
				Object variableValue = variable.getValue().getValue(Object.class, context);

				logger.debug("setting variable '", variableName, "' to ", variableValue);

				subcontext.setVariable(variableName, variableValue);
			}

			String parser = this.parser;

			if (parser == null) {
				parser = templateInformation.getParser();
			}

			logger.debug("including template '", name, "' using parser '", parser, "'");

			new TemplateProcessor(configuration).processTemplate(name, parser, subcontext, writer);
		}
	}

	/**
	 * Includes a part of a template that was saved earlier. Upon execution, the <em>include local</em> action reads a
	 * {@link Context#TEMPLATE_SCOPE template-scoped} context variable that contains an {@link ActionBody action body}
	 * and invokes it.
	 * <p>
	 * There are two ways to pass information to the included template:
	 * <ul>
	 * <li>The action supports dynamic parameters, all of which will be available within the template;
	 * <li>It's possible to used {@link Block blocks} in the action body, these can be invoked inside the included
	 *     template using the {@link BlockContents block contents} action.
	 * </ul>
	 *
	 * @author levi_h
	 * @see Template
	 */
	public static class IncludeLocal extends AbstractDynamicallyParameterisableAction {
		private @Required String name;

		private boolean inheritAncestors;

		private @Injected Configuration configuration;

		/**
		 * Creates an <em>include local</em> action.
		 */
		public IncludeLocal() {}

		public void execute(Context context, Writer writer) throws AluminumException {
			Object body = context.getVariable(Context.TEMPLATE_SCOPE, name);

			if (body instanceof ActionBody) {
				getBody().invoke(context, new NullWriter());

				Context subcontext = context.createSubcontext();

				TemplateInformation templateInformation = TemplateInformation.from(context);
				TemplateInformation subtemplateInformation = TemplateInformation.from(subcontext);

				subtemplateInformation.setTemplate(
					templateInformation.getTemplate(), templateInformation.getName(), templateInformation.getParser());

				if (inheritAncestors) {
					inheritTemplateElements(templateInformation, subtemplateInformation);
					inheritActions(templateInformation, subtemplateInformation);
				}

				for (Map.Entry<String, ActionParameter> variable: getDynamicParameters().entrySet()) {
					String variableName = variable.getKey();
					Object variableValue = variable.getValue().getValue(Object.class, context);

					logger.debug("setting variable '", variableName, "' to ", variableValue);

					subcontext.setVariable(variableName, variableValue);
				}

				for (ContextEnricher contextEnricher: configuration.getContextEnrichers()) {
					contextEnricher.beforeTemplate(subcontext);
				}

				((ActionBody) body).invoke(subcontext, writer);

				for (ContextEnricher contextEnricher: configuration.getContextEnrichers()) {
					contextEnricher.afterTemplate(subcontext);
				}
			} else {
				throw new AluminumException("can't include '", name, "' (", body, ")");
			}
		}
	}

	private static void inheritTemplateElements(
			TemplateInformation templateInformation, TemplateInformation subtemplateInformation) {
		Stack<TemplateElement> templateElements = new Stack<TemplateElement>();

		com.googlecode.aluminumproject.templates.Template template = templateInformation.getTemplate();

		TemplateElement templateElement = template.getParent(templateInformation.getCurrentTemplateElement());

		while (templateElement != null) {
			templateElements.push(templateElement);

			templateElement = template.getParent(templateElement);
		}

		while (!templateElements.isEmpty()) {
			subtemplateInformation.addTemplateElement(templateElements.pop());
		}
	}

	private static void inheritActions(
			TemplateInformation templateInformation, TemplateInformation subtemplateInformation) {
		Stack<Action> actions = new Stack<Action>();

		Action action = templateInformation.getCurrentAction().getParent();

		while (action != null) {
			actions.push(action.getParent());

			action = action.getParent();
		}

		while (!actions.isEmpty()) {
			subtemplateInformation.addAction(actions.pop());
		}
	}
}