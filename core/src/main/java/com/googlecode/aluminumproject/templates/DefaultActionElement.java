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
import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.interceptors.AbstractActionInterceptor;
import com.googlecode.aluminumproject.interceptors.ActionInterceptor;
import com.googlecode.aluminumproject.libraries.actions.Action;
import com.googlecode.aluminumproject.libraries.actions.ActionBody;
import com.googlecode.aluminumproject.libraries.actions.ActionContribution;
import com.googlecode.aluminumproject.libraries.actions.ActionContributionFactory;
import com.googlecode.aluminumproject.libraries.actions.ActionFactory;
import com.googlecode.aluminumproject.libraries.actions.ActionParameter;
import com.googlecode.aluminumproject.libraries.actions.DefaultActionContributionOptions;
import com.googlecode.aluminumproject.utilities.Injector;
import com.googlecode.aluminumproject.utilities.Injector.ClassBasedValueProvider;
import com.googlecode.aluminumproject.writers.Writer;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * The default {@link ActionElement action element} implementation.
 */
public class DefaultActionElement implements ActionElement {
	private Configuration configuration;

	private ActionDescriptor actionDescriptor;
	private ActionFactory actionFactory;

	private Map<String, ActionParameter> parameters;
	private Map<ActionContributionDescriptor, ActionContributionFactory> actionContributionFactories;

	private List<ActionInterceptor> actionInterceptors;

	private Map<String, String> libraryUrlAbbreviations;

	/**
	 * Creates a default action element.
	 *
	 * @param configuration the current configuration
	 * @param actionDescriptor the descriptor of the action
	 * @param actionFactory the factory that will create the action to execute
	 * @param parameters the parameters for the action
	 * @param actionContributionFactories the factories that will create the contributions for the action
	 * @param actionInterceptors the action interceptors to use
	 * @param libraryUrlAbbreviations the action element's library URL abbreviations
	 */
	public DefaultActionElement(Configuration configuration,
			ActionDescriptor actionDescriptor, ActionFactory actionFactory, Map<String, ActionParameter> parameters,
			Map<ActionContributionDescriptor, ActionContributionFactory> actionContributionFactories,
			List<ActionInterceptor> actionInterceptors, Map<String, String> libraryUrlAbbreviations) {
		this.configuration = configuration;

		this.actionDescriptor = actionDescriptor;
		this.actionFactory = actionFactory;

		this.parameters = parameters;
		this.actionContributionFactories = actionContributionFactories;

		this.actionInterceptors = actionInterceptors;

		this.libraryUrlAbbreviations = libraryUrlAbbreviations;
	}

	public Map<String, String> getLibraryUrlAbbreviations() {
		return Collections.unmodifiableMap(libraryUrlAbbreviations);
	}

	public ActionDescriptor getDescriptor() {
		return actionDescriptor;
	}

	public Map<String, ActionParameter> getParameters() {
		return Collections.unmodifiableMap(parameters);
	}

	public List<ActionContributionDescriptor> getContributionDescriptors() {
		return new LinkedList<ActionContributionDescriptor>(actionContributionFactories.keySet());
	}

	public void process(Context context, Writer writer) throws AluminumException {
		TemplateInformation templateInformation = TemplateInformation.from(context);
		templateInformation.addTemplateElement(this);

		DefaultActionContext actionContext =
			new DefaultActionContext(configuration, actionDescriptor, actionFactory, context, writer);

		for (String parameterName: parameters.keySet()) {
			actionContext.addParameter(parameterName, parameters.get(parameterName));
		}

		for (ActionContributionDescriptor descriptor: actionContributionFactories.keySet()) {
			actionContext.addActionContribution(descriptor, actionContributionFactories.get(descriptor));
		}

		actionContext.addInterceptor(new ContributionInterceptor());
		actionContext.addInterceptor(new CreationInterceptor());
		actionContext.addInterceptor(new ExecutionInterceptor());

		for (ActionInterceptor actionInterceptor: actionInterceptors) {
			actionContext.addInterceptor(actionInterceptor);
		}

		for (ActionPhase phase: ActionPhase.values()) {
			actionContext.setPhase(phase);

			actionContext.proceed();
		}

		templateInformation.removeCurrentTemplateElement();
	}

	private static class ContributionInterceptor extends AbstractActionInterceptor {
		public ContributionInterceptor() {
			super(ActionPhase.CONTRIBUTION);
		}

		public void intercept(ActionContext actionContext) throws AluminumException {
			DefaultActionContributionOptions options = new DefaultActionContributionOptions();

			Map<ActionContributionDescriptor, ActionContributionFactory> actionContributionFactories =
				actionContext.getActionContributionFactories();

			for (ActionContributionDescriptor descriptor: actionContributionFactories.keySet()) {
				ActionContribution actionContribution = actionContributionFactories.get(descriptor).create();
				ActionFactory actionFactory = actionContext.getActionFactory();

				if (actionContribution.canBeMadeTo(actionFactory)) {
					Injector injector = new Injector();
					injector.addValueProvider(new ClassBasedValueProvider(descriptor));
					injector.inject(actionContribution);

					ActionParameter parameter = descriptor.getParameter();

					logger.debug("making action contribution ", actionContribution, " with parameter ", parameter);

					actionContribution.make(actionContext.getContext(), actionContext.getWriter(), parameter, options);
				} else {
					throw new AluminumException("action contribution ", actionContribution, " can't be made to",
						" action '", actionFactory.getInformation().getName(), "'");
				}
			}

			options.apply(actionContext);

			actionContext.proceed();
		}
	}

	private static class CreationInterceptor extends AbstractActionInterceptor {
		public CreationInterceptor() {
			super(ActionPhase.CREATION);
		}

		public void intercept(ActionContext actionContext) throws AluminumException {
			if (actionContext.getAction() == null) {
				ActionFactory actionFactory = actionContext.getActionFactory();

				logger.debug("creating action using ", actionFactory);

				Action action = actionFactory.create(actionContext.getParameters(), actionContext.getContext());

				Injector injector = new Injector();
				injector.addValueProvider(new ClassBasedValueProvider(actionContext.getActionDescriptor()));
				injector.inject(action);

				TemplateInformation templateInformation = TemplateInformation.from(actionContext.getContext());

				action.setParent(templateInformation.getCurrentAction());
				action.setBody(new TemplateBody(templateInformation.getCurrentTemplateElement()));

				actionContext.setAction(action);

				actionContext.proceed();
			} else {
				logger.debug("not creating action, since it is already available in the action context");
			}
		}

		private static class TemplateBody implements ActionBody {
			private TemplateElement currentTemplateElement;

			public TemplateBody(TemplateElement currentTemplateElement) {
				this.currentTemplateElement = currentTemplateElement;
			}

			public ActionBody copy() {
				return new TemplateBody(currentTemplateElement);
			}

			public void invoke(Context context, Writer writer) throws AluminumException {
				Template template = findTemplate(context, currentTemplateElement);

				for (TemplateElement templateElement: template.getChildren(currentTemplateElement)) {
					templateElement.process(context, writer);
				}
			}

			private Template findTemplate(Context context, TemplateElement templateElement) {
				Template template = null;

				do {
					template = TemplateInformation.from(context).getTemplate();

					context = context.getParent();
				} while ((context != null) && !template.contains(templateElement));

				return template;
			}
		}
	}

	private static class ExecutionInterceptor extends AbstractActionInterceptor {
		public ExecutionInterceptor() {
			super(ActionPhase.EXECUTION);
		}

		public void intercept(ActionContext actionContext) throws AluminumException {
			Action action = actionContext.getAction();

			logger.debug("executing action ", action);

			TemplateInformation templateInformation = TemplateInformation.from(actionContext.getContext());
			templateInformation.addAction(action);

			action.execute(actionContext.getContext(), actionContext.getWriter());

			templateInformation.removeCurrentAction();

			actionContext.proceed();
		}
	}
}