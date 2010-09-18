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
import com.googlecode.aluminumproject.interceptors.ActionInterceptor;
import com.googlecode.aluminumproject.interceptors.InterceptionException;
import com.googlecode.aluminumproject.libraries.actions.Action;
import com.googlecode.aluminumproject.libraries.actions.ActionContribution;
import com.googlecode.aluminumproject.libraries.actions.ActionContributionFactory;
import com.googlecode.aluminumproject.libraries.actions.ActionFactory;
import com.googlecode.aluminumproject.libraries.actions.ActionParameter;
import com.googlecode.aluminumproject.writers.Writer;

import java.util.Map;

/**
 * The context in which an {@link Action action} is created and executed. It will be used by {@link ActionElement action
 * elements}. Before an action is executed, its context can be modified by {@link ActionContribution action
 * contributions} and/or {@link ActionInterceptor action interceptors}.
 * <p>
 * An action context has a number of {@link ActionPhase phases}, which means that not every part of the context is
 * accessible all of the time (e.g. the {@link #getAction() action itself} is not available until it is {@link
 * ActionPhase#CREATION created}).
 *
 * @author levi_h
 */
public interface ActionContext {
	/**
	 * Returns the configuration of the template processor that executes the action.
	 *
	 * @return the current configuration
	 */
	Configuration getConfiguration();

	/**
	 * Returns the factory that will be used to create the action.
	 *
	 * @return the action's action factory
	 */
	ActionFactory getActionFactory();

	/**
	 * Returns the context in which the action will be executed.
	 *
	 * @return the context that will be used
	 */
	Context getContext();

	/**
	 * Returns the writer that the action will use.
	 *
	 * @return the writer that will be used
	 */
	Writer getWriter();

	/**
	 * Replaces the writer that the action will use.
	 *
	 * @param writer the writer to use
	 */
	void setWriter(Writer writer);

	/**
	 * Returns the parameters that will be used to create the action.
	 *
	 * @return the action parameters that will be used
	 */
	Map<String, ActionParameter> getParameters();

	/**
	 * Adds an action parameter to the set of parameters that the action will be created with.
	 *
	 * @param name the name of the parameter to add
	 * @param parameter the parameter to add
	 */
	void addParameter(String name, ActionParameter parameter);

	/**
	 * Returns the factories of the action contributions that will be made to the action.
	 *
	 * @return the action's contribution factories
	 */
	Map<ActionContributionFactory, ActionParameter> getActionContributionFactories();

	/**
	 * Adds an action contribution to the set of action contributions that will be made to the action.
	 *
	 * @param contributionFactory the factory that will create the action contribution
	 * @param parameter the parameter that should be supplied to the action contribution
	 */
	void addActionContribution(ActionContributionFactory contributionFactory, ActionParameter parameter);

	/**
	 * Returns the action that will be executed.
	 *
	 * @return the executed action
	 */
	Action getAction();

	/**
	 * Sets the action that will be executed.
	 *
	 * @param action the action to execute
	 */
	void setAction(Action action);

	/**
	 * Adds an interceptor to this action context. The later an action interceptor is added, the higher its priority.
	 *
	 * @param interceptor the interceptor to add
	 */
	void addInterceptor(ActionInterceptor interceptor);

	/**
	 * Returns the current phase of this action context.
	 *
	 * @return this action context's current phase
	 */
	ActionPhase getPhase();

	/**
	 * Proceeds this action context by running the next interceptor of the current phase. If there are no more
	 * interceptors to run, this method does nothing.
	 *
	 * @throws InterceptionException when the interceptor can't be run
	 */
	void proceed() throws InterceptionException;
}