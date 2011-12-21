/*
 * Copyright 2009-2011 Levi Hoogenberg
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

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.interceptors.ActionInterceptor;
import com.googlecode.aluminumproject.interceptors.ActionSkipper;
import com.googlecode.aluminumproject.templates.ActionContext;
import com.googlecode.aluminumproject.utilities.Logger;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * A default {@link ActionContributionOptions action contributions options} implementation that keeps track of all
 * chosen options, so that they can be applied to an {@link ActionContext action context}.
 *
 * @author levi_h
 */
public class DefaultActionContributionOptions implements ActionContributionOptions {
	private Map<String, ActionParameter> parameters;

	private List<ActionInterceptor> interceptors;

	private final Logger logger;

	/**
	 * Creates default action contribution options.
	 */
	public DefaultActionContributionOptions() {
		parameters = new HashMap<String, ActionParameter>();

		interceptors = new LinkedList<ActionInterceptor>();

		logger = Logger.get(getClass());
	}

	public void setParameter(String name, ActionParameter parameter) {
		parameters.put(name, parameter);

		logger.debug("set parameter '", name, "': ", parameter);
	}

	public void skipAction() {
		logger.debug("skipping action");

		addInterceptor(new ActionSkipper());
	}

	public void addInterceptor(ActionInterceptor interceptor) {
		interceptors.add(interceptor);
	}

	/**
	 * Applies all chosen options to an action context.
	 *
	 * @param actionContext the action context to apply these action contribution options to
	 * @throws AluminumException when the options can't be applied
	 */
	public void apply(ActionContext actionContext) throws AluminumException {
		for (Map.Entry<String, ActionParameter> parameter: parameters.entrySet()) {
			actionContext.addParameter(parameter.getKey(), parameter.getValue());
		}

		for (ActionInterceptor interceptor: interceptors) {
			actionContext.addInterceptor(interceptor);
		}
	}
}