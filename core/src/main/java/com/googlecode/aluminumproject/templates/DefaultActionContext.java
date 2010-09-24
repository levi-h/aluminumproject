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
import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.interceptors.ActionInterceptor;
import com.googlecode.aluminumproject.interceptors.InterceptionException;
import com.googlecode.aluminumproject.libraries.actions.Action;
import com.googlecode.aluminumproject.libraries.actions.ActionContributionFactory;
import com.googlecode.aluminumproject.libraries.actions.ActionException;
import com.googlecode.aluminumproject.libraries.actions.ActionFactory;
import com.googlecode.aluminumproject.libraries.actions.ActionParameter;
import com.googlecode.aluminumproject.writers.Writer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The {@link ActionContext action context} implementation that is used by the {@link DefaultActionElement default
 * action element}.
 *
 * @author levi_h
 */
public class DefaultActionContext implements ActionContext {
	private Configuration configuration;

	private ActionFactory actionFactory;
	private Map<String, ActionParameter> parameters;
	private Map<ActionContributionFactory, ActionParameter> actionContributionFactories;

	private Context context;
	private Writer writer;

	private Action action;

	private Map<ActionPhase, List<ActionInterceptor>> interceptors;

	private ActionPhase phase;
	private int indexOfNextInterceptor;

	private final Logger logger;

	/**
	 * Creates a default action context.
	 *
	 * @param configuration the current configuration
	 * @param actionFactory the action factory that will create the action
	 * @param context the context that the action will use
	 * @param writer the writer that the writer will use
	 */
	public DefaultActionContext(
			Configuration configuration, ActionFactory actionFactory, Context context, Writer writer) {
		this.configuration = configuration;

		this.actionFactory = actionFactory;
		parameters = new HashMap<String, ActionParameter>();
		actionContributionFactories = new LinkedHashMap<ActionContributionFactory, ActionParameter>();

		this.context = context;
		this.writer = writer;

		interceptors = new EnumMap<ActionPhase, List<ActionInterceptor>>(ActionPhase.class);

		for (ActionPhase phase: ActionPhase.values()) {
			interceptors.put(phase, new ArrayList<ActionInterceptor>());
		}

		logger = Logger.get(getClass());
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public ActionFactory getActionFactory() {
		return actionFactory;
	}

	public Map<String, ActionParameter> getParameters() {
		return Collections.unmodifiableMap(parameters);
	}

	public void addParameter(String name, ActionParameter parameter) throws ActionException {
		if (action != null) {
			throw new ActionException("can't add parameter '", name, "': an action has already been created");
		}

		parameters.put(name, parameter);
	}

	public Map<ActionContributionFactory, ActionParameter> getActionContributionFactories() {
		return Collections.unmodifiableMap(actionContributionFactories);
	}

	public void addActionContribution(ActionContributionFactory contributionFactory, ActionParameter parameter)
			throws ActionException {
		if (EnumSet.of(ActionPhase.CREATION, ActionPhase.EXECUTION).contains(phase)) {
			throw new ActionException("can't add action contribution: all contributions have been made");
		}

		actionContributionFactories.put(contributionFactory, parameter);
	}

	public Context getContext() {
		return context;
	}

	public Writer getWriter() {
		return writer;
	}

	public void setWriter(Writer writer) {
		this.writer = writer;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) throws ActionException {
		if (this.action != null) {
			throw new ActionException("the action context already contains an action");
		}

		this.action = action;
	}

	/**
	 * Returns all action interceptors that should run for a certain phase.
	 *
	 * @param phase the intercepted phase
	 * @return the interceptors that intercept the given phase
	 */
	public List<ActionInterceptor> getInterceptors(ActionPhase phase) {
		return interceptors.get(phase);
	}

	public void addInterceptor(ActionInterceptor interceptor) throws ActionException {
		for (ActionPhase phase: interceptor.getPhases()) {
			if ((this.phase != null) && (this.phase.compareTo(phase) > 0)) {
				throw new ActionException("can't add interceptor for past phase ", phase);
			}

			logger.debug("adding interceptor ", interceptor, " for phase ", phase);

			interceptors.get(phase).add(0, interceptor);
		}
	}

	public ActionPhase getPhase() {
		return phase;
	}

	/**
	 * Sets the current phase.
	 *
	 * @param phase the current interception phase
	 */
	public void setPhase(ActionPhase phase) {
		logger.debug("next phase: ", phase);

		this.phase = phase;
		indexOfNextInterceptor = 0;
	}

	public void proceed() throws InterceptionException {
		List<ActionInterceptor> interceptorsForCurrentPhase = getInterceptors(phase);

		if (indexOfNextInterceptor < interceptorsForCurrentPhase.size()) {
			ActionInterceptor interceptor = interceptorsForCurrentPhase.get(indexOfNextInterceptor++);

			logger.debug("running interceptor ", interceptor);

			interceptor.intercept(this);
		}
	}
}