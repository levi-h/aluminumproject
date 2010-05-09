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
import com.googlecode.aluminumproject.libraries.actions.ActionException;
import com.googlecode.aluminumproject.libraries.actions.ActionInformation;
import com.googlecode.aluminumproject.libraries.actions.ActionParameter;
import com.googlecode.aluminumproject.libraries.actions.ActionParameterInformation;

import java.util.Arrays;
import java.util.Map;

/**
 * Creates <i>call function</i> actions.
 *
 * @author levi_h
 */
public class CallFunctionFactory extends AbstractActionFactory {
	private ActionInformation information;

	/**
	 * Creates a <em>call function</em> factory.
	 */
	public CallFunctionFactory() {}

	public ActionInformation getInformation() {
		return information;
	}

	public void initialise(Configuration configuration, ConfigurationParameters parameters) {
		information = new ActionInformation("call function", Arrays.asList(
			new ActionParameterInformation("name", String.class, true),
			new ActionParameterInformation("result name", String.class, true),
			new ActionParameterInformation("result scope", String.class, false)
		), false);
	}

	public CallFunction create(Map<String, ActionParameter> parameters, Context context) throws ActionException {
		checkParameters(parameters);

		String name = getParameter(parameters, context, "name", String.class);

		String resultName = getParameter(parameters, context, "result name", String.class);
		String resultScope = getParameter(parameters, context, "result scope", String.class);

		logger.debug("creating action that calls function '", name, "' in library ",
			"'", getLibrary().getInformation().getUrl(), "' and will store the result in variable '", resultName, "'",
			" in the ", (resultScope == null) ? "innermost" : String.format("'%s'", resultScope), " scope");

		return new CallFunction(getLibrary(), name, resultName, resultScope);
	}
}