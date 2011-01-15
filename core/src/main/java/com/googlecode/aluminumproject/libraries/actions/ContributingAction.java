/*
 * Copyright 2010 Levi Hoogenberg
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
package com.googlecode.aluminumproject.libraries.actions;

import com.googlecode.aluminumproject.annotations.Injected;
import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.configuration.ConfigurationElementFactory;
import com.googlecode.aluminumproject.configuration.ConfigurationException;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.ContextException;
import com.googlecode.aluminumproject.interceptors.ActionInterceptor;
import com.googlecode.aluminumproject.interceptors.ActionSkipper;
import com.googlecode.aluminumproject.interceptors.InterceptionException;
import com.googlecode.aluminumproject.templates.ActionContext;
import com.googlecode.aluminumproject.templates.ActionContributionDescriptor;
import com.googlecode.aluminumproject.templates.ActionDescriptor;
import com.googlecode.aluminumproject.templates.ActionPhase;
import com.googlecode.aluminumproject.utilities.Injector;
import com.googlecode.aluminumproject.utilities.Logger;
import com.googlecode.aluminumproject.utilities.UtilityException;
import com.googlecode.aluminumproject.writers.Writer;
import com.googlecode.aluminumproject.writers.WriterException;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

/**
 * An action that makes an action contribution.
 *
 * @author levi_h
 */
public class ContributingAction extends AbstractAction {
	private Class<? extends ActionContribution> actionContributionClass;

	private String parameterName;
	private ActionParameter parameter;

	private @Injected ActionFactory actionFactory;
	private @Injected ActionDescriptor actionDescriptor;

	private @Injected Configuration configuration;

	/**
	 * Creates a contributing action.
	 *
	 * @param actionContributionClass the class of the contribution to make
	 * @param parameterName the name of the parameter for the action contribution
	 * @param parameter the parameter for the action contribution
	 */
	public ContributingAction(Class<? extends ActionContribution> actionContributionClass,
			String parameterName, ActionParameter parameter) {
		this.actionContributionClass = actionContributionClass;

		this.parameterName = parameterName;
		this.parameter = parameter;
	}

	public void execute(Context context, Writer writer) throws ActionException, ContextException {
		ActionContribution actionContribution = createActionContribution();
		injectFields(actionContribution);

		logger.debug("making contribution ", actionContribution);
		makeContribution(actionContribution, context, writer);
	}

	private ActionContribution createActionContribution() throws ActionException {
		try {
			ConfigurationElementFactory configurationElementFactory = configuration.getConfigurationElementFactory();

			return configurationElementFactory.instantiate(actionContributionClass.getName(), ActionContribution.class);
		} catch (ConfigurationException exception) {
			throw new ActionException(exception, "can't create action contribution");
		}
	}

	private void injectFields(ActionContribution actionContribution) throws ActionException {
		ActionContributionDescriptor actionContributionDescriptor = new ActionContributionDescriptor(
			actionDescriptor.getLibraryUrlAbbreviation(), actionDescriptor.getName(), parameter);

		try {
			Injector injector = new Injector();
			injector.addValueProvider(new Injector.ClassBasedValueProvider(configuration));
			injector.addValueProvider(new Injector.ClassBasedValueProvider(actionContributionDescriptor));
			injector.inject(actionContribution);
		} catch (UtilityException exception) {
			throw new ActionException(exception, "can't inject information into action contribution");
		}
	}

	private void makeContribution(ActionContribution actionContribution, Context context, Writer writer)
			throws ActionException, ContextException {
		ActionContext actionContext = new ContributingActionContext(context, writer);
		actionContext.addInterceptor(new ActionInterceptor() {
			public Set<ActionPhase> getPhases() {
				return EnumSet.of(ActionPhase.EXECUTION);
			}

			public void intercept(ActionContext actionContext) throws InterceptionException {
				try {
					getBody().invoke(actionContext.getContext(), actionContext.getWriter());
				} catch (ActionException exception) {
					throw new InterceptionException(exception, "can't invoke contributing action body");
				} catch (ContextException exception) {
					throw new InterceptionException(exception, "can't invoke contributing action body");
				} catch (WriterException exception) {
					throw new InterceptionException(exception, "can't invoke contributing action body");
				}
			}
		});

		actionContribution.make(context, writer, parameter, new ContributingActionContextOptions(actionContext));

		actionContext.proceed();
	}

	private static class ContributingActionContextOptions implements ActionContributionOptions {
		private ActionContext actionContext;

		private Logger logger;

		public ContributingActionContextOptions(ActionContext actionContext) {
			this.actionContext = actionContext;

			logger = Logger.get(getClass());
		}

		public void setParameter(String name, ActionParameter parameter) {
			logger.warn("action contributions that are used as if they were actions cannot set parameters");
		}

		public void skipAction() {
			addInterceptor(new ActionSkipper());
		}

		public void addInterceptor(ActionInterceptor interceptor) {
			actionContext.addInterceptor(interceptor);
		}
	}

	private class ContributingActionContext implements ActionContext {
		private Context context;
		private Writer writer;

		private Stack<ActionInterceptor> interceptors;

		public ContributingActionContext(Context context, Writer writer) {
			this.context = context;
			this.writer = writer;

			interceptors = new Stack<ActionInterceptor>();
		}

		public Configuration getConfiguration() {
			return configuration;
		}

		public ActionDescriptor getActionDescriptor() {
			return actionDescriptor;
		}

		public ActionFactory getActionFactory() {
			return actionFactory;
		}

		public Context getContext() {
			return context;
		}

		public Writer getWriter() {
			logger.debug("setting writer ", writer);

			return writer;
		}

		public void setWriter(Writer writer) {
			this.writer = writer;
		}

		public Map<String, ActionParameter> getParameters() {
			return Collections.singletonMap(parameterName, parameter);
		}

		public void addParameter(String name, ActionParameter parameter) throws ActionException {
			throw new ActionException("can't add parameters to a contributing action context");
		}

		public Map<ActionContributionDescriptor, ActionContributionFactory> getActionContributionFactories() {
			return Collections.emptyMap();
		}

		public void addActionContribution(ActionContributionDescriptor descriptor,
				ActionContributionFactory contributionFactory) throws ActionException {
			throw new ActionException("can't add action contributions to a contribution action context");
		}

		public Action getAction() {
			return ContributingAction.this;
		}

		public void setAction(Action action) throws ActionException {
			throw new ActionException("can't set the action of a contribution action context");
		}

		public void addInterceptor(ActionInterceptor interceptor) throws ActionException {
			if (!interceptor.getPhases().contains(ActionPhase.EXECUTION)) {
				throw new ActionException("only action interceptors that intercept the execution phase ",
					"can be added to a contribution action context");
			}

			logger.debug("adding interceptor ", interceptor);

			interceptors.push(interceptor);
		}

		public ActionPhase getPhase() {
			return ActionPhase.EXECUTION;
		}

		public void proceed() throws InterceptionException {
			if (!interceptors.isEmpty()) {
				ActionInterceptor interceptor = interceptors.pop();

				logger.debug("running interceptor ", interceptor);

				interceptor.intercept(this);
			}
		}
	}
}