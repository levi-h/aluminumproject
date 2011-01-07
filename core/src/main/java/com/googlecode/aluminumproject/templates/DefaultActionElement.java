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

import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.ContextException;
import com.googlecode.aluminumproject.interceptors.AbstractActionInterceptor;
import com.googlecode.aluminumproject.interceptors.ActionInterceptor;
import com.googlecode.aluminumproject.interceptors.InterceptionException;
import com.googlecode.aluminumproject.libraries.actions.Action;
import com.googlecode.aluminumproject.libraries.actions.ActionBody;
import com.googlecode.aluminumproject.libraries.actions.ActionContribution;
import com.googlecode.aluminumproject.libraries.actions.ActionContributionFactory;
import com.googlecode.aluminumproject.libraries.actions.ActionException;
import com.googlecode.aluminumproject.libraries.actions.ActionFactory;
import com.googlecode.aluminumproject.libraries.actions.ActionParameter;
import com.googlecode.aluminumproject.libraries.actions.DefaultActionContributionOptions;
import com.googlecode.aluminumproject.utilities.Injector;
import com.googlecode.aluminumproject.utilities.UtilityException;
import com.googlecode.aluminumproject.utilities.Injector.ClassBasedValueProvider;
import com.googlecode.aluminumproject.writers.Writer;
import com.googlecode.aluminumproject.writers.WriterException;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * The default {@link ActionElement action element} implementation.
 *
 * @author levi_h
 */
public class DefaultActionElement extends AbstractTemplateElement implements ActionElement {
	private Configuration configuration;

	private ActionDescriptor actionDescriptor;
	private ActionFactory actionFactory;

	private Map<String, ActionParameter> parameters;
	private Map<ActionContributionDescriptor, ActionContributionFactory> actionContributionFactories;

	private List<ActionInterceptor> actionInterceptors;

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
		super(libraryUrlAbbreviations);

		this.configuration = configuration;

		this.actionDescriptor = actionDescriptor;
		this.actionFactory = actionFactory;

		this.parameters = parameters;
		this.actionContributionFactories = actionContributionFactories;

		this.actionInterceptors = actionInterceptors;
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

	public void process(Template template, TemplateContext templateContext, Context context, Writer writer)
			throws TemplateException {
		DefaultActionContext actionContext =
			new DefaultActionContext(configuration, actionDescriptor, actionFactory, context, writer);

		for (String parameterName: parameters.keySet()) {
			actionContext.addParameter(parameterName, parameters.get(parameterName));
		}

		for (ActionContributionDescriptor descriptor: actionContributionFactories.keySet()) {
			actionContext.addActionContribution(descriptor, actionContributionFactories.get(descriptor));
		}

		actionContext.addInterceptor(new ContributionInterceptor());
		actionContext.addInterceptor(new CreationInterceptor(template, templateContext));
		actionContext.addInterceptor(new ExecutionInterceptor(templateContext));

		for (ActionInterceptor actionInterceptor: actionInterceptors) {
			actionContext.addInterceptor(actionInterceptor);
		}

		for (ActionPhase phase: ActionPhase.values()) {
			actionContext.setPhase(phase);

			try {
				actionContext.proceed();
			} catch (InterceptionException exception) {
				throw new TemplateException(exception, "can't process action element");
			}
		}
	}

	private static class ContributionInterceptor extends AbstractActionInterceptor {
		public ContributionInterceptor() {
			super(ActionPhase.CONTRIBUTION);
		}

		public void intercept(ActionContext actionContext) throws InterceptionException {
			DefaultActionContributionOptions options = new DefaultActionContributionOptions();

			Map<ActionContributionDescriptor, ActionContributionFactory> actionContributionFactories =
				actionContext.getActionContributionFactories();

			for (ActionContributionDescriptor descriptor: actionContributionFactories.keySet()) {
				ActionContribution actionContribution;

				try {
					actionContribution = actionContributionFactories.get(descriptor).create();
				} catch (ActionException exception) {
					throw new InterceptionException(exception, "can't create action contribution");
				}

				ActionFactory actionFactory = actionContext.getActionFactory();

				if (actionContribution.canBeMadeTo(actionFactory)) {
					try {
						Injector injector = new Injector();
						injector.addValueProvider(new ClassBasedValueProvider(descriptor));
						injector.inject(actionContribution);
					} catch (UtilityException exception) {
						throw new InterceptionException(exception, "can't inject action contribution");
					}

					ActionParameter parameter = descriptor.getParameter();

					logger.debug("making action contribution ", actionContribution, " with parameter ", parameter);

					try {
						actionContribution.make(
							actionContext.getContext(), actionContext.getWriter(), parameter, options);
					} catch (ActionException exception) {
						throw new InterceptionException(exception, "can't make action contribution");
					} catch (ContextException exception) {
						throw new InterceptionException(exception, "can't make action contribution");
					}
				} else {
					throw new InterceptionException("action contribution ", actionContribution, " can't be made to",
						" action '", actionFactory.getInformation().getName(), "'");
				}
			}

			options.apply(actionContext);

			actionContext.proceed();
		}
	}

	private static class CreationInterceptor extends AbstractActionInterceptor {
		private Template template;
		private TemplateContext templateContext;

		public CreationInterceptor(Template template, TemplateContext templateContext) {
			super(ActionPhase.CREATION);

			this.template = template;
			this.templateContext = templateContext;
		}

		public void intercept(ActionContext actionContext) throws InterceptionException {
			if (actionContext.getAction() == null) {
				ActionFactory actionFactory = actionContext.getActionFactory();

				logger.debug("creating action using ", actionFactory);

				Action action;

				try {
					action = actionFactory.create(actionContext.getParameters(), actionContext.getContext());
				} catch (ActionException exception) {
					throw new InterceptionException(exception, "can't create action");
				}

				try {
					Injector injector = new Injector();
					injector.addValueProvider(new ClassBasedValueProvider(actionContext.getActionDescriptor()));
					injector.inject(action);
				} catch (UtilityException exception) {
					throw new InterceptionException(exception, "can't inject action");
				}

				action.setParent(templateContext.getCurrentAction());
				action.setBody(new TemplateBody(template, templateContext));

				actionContext.setAction(action);

				actionContext.proceed();
			} else {
				logger.debug("not creating action, since it is already available in the action context");
			}
		}

		private static class TemplateBody implements ActionBody {
			private Template template;
			private TemplateContext templateContext;

			public TemplateBody(Template template, TemplateContext templateContext) {
				this.template = template;
				this.templateContext = templateContext;
			}

			public ActionBody copy() {
				return new TemplateBody(template, new TemplateContext(templateContext));
			}

			public void invoke(Context context, Writer writer) throws ActionException {
				try {
					template.processChildren(templateContext, context, writer);
				} catch (TemplateException exception) {
					throw new ActionException(exception, "can't process body");
				}
			}
		}
	}

	private static class ExecutionInterceptor extends AbstractActionInterceptor {
		private TemplateContext templateContext;

		public ExecutionInterceptor(TemplateContext templateContext) {
			super(ActionPhase.EXECUTION);

			this.templateContext = templateContext;
		}

		public void intercept(ActionContext actionContext) throws InterceptionException {
			Action action = actionContext.getAction();

			logger.debug("executing action ", action);

			templateContext.addAction(action);

			try {
				action.execute(actionContext.getContext(), actionContext.getWriter());
			} catch (ActionException exception) {
				throw new InterceptionException(exception, "can't execute action");
			} catch (ContextException exception) {
				throw new InterceptionException(exception, "can't execute action");
			} catch (WriterException exception) {
				throw new InterceptionException(exception, "can't execute action");
			} finally {
				templateContext.removeCurrentAction();
			}

			actionContext.proceed();
		}
	}
}