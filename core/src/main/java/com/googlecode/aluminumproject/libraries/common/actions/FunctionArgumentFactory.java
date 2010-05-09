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
package com.googlecode.aluminumproject.libraries.common.actions;

import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.configuration.ConfigurationParameters;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.libraries.actions.AbstractActionFactory;
import com.googlecode.aluminumproject.libraries.actions.Action;
import com.googlecode.aluminumproject.libraries.actions.ActionException;
import com.googlecode.aluminumproject.libraries.actions.ActionInformation;
import com.googlecode.aluminumproject.libraries.actions.ActionParameter;
import com.googlecode.aluminumproject.libraries.actions.ActionParameterInformation;

import java.util.Collections;
import java.util.Map;

/**
 * Creates <i>function arguments</i> actions.
 *
 * @author levi_h
 */
public class FunctionArgumentFactory extends AbstractActionFactory {
	private ActionInformation information;

	private Configuration configuration;

	/**
	 * Creates a function argument factory.
	 */
	public FunctionArgumentFactory() {}

	public ActionInformation getInformation() {
		return information;
	}

	public void initialise(Configuration configuration, ConfigurationParameters parameters) {
		this.configuration = configuration;

		information = new ActionInformation("function argument",
			Collections.singletonList(new ActionParameterInformation("value", Object.class, true)), false);
	}

	public Action create(Map<String, ActionParameter> parameters, Context context) throws ActionException {
		checkParameters(parameters);

		Object value = getParameter(parameters, context, "value", Object.class);

		logger.debug("creating function argument with value ", value);

		return new FunctionArgument(getLibrary(), value, configuration.getConverterRegistry());
	}
}