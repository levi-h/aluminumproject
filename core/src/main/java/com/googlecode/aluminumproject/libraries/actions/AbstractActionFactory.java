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
package com.googlecode.aluminumproject.libraries.actions;

import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.converters.ConverterException;
import com.googlecode.aluminumproject.libraries.AbstractLibraryElement;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Abstract superclass of action factories. It provides some convenience methods.
 *
 * @author levi_h
 */
public abstract class AbstractActionFactory extends AbstractLibraryElement implements ActionFactory {
	/**
	 * Creates an abstract action factory.
	 */
	public AbstractActionFactory() {}

	/**
	 * Checks the parameter map. This method delegates to both the {@link #checkRequiredParameters(Map)
	 * checkRequiredParameters} and {@link #checkSuperfluousParameters(Map) checkSuperfluousParameters} methods.
	 *
	 * @param parameters the parameter map that was given to the {@link #create(Map, Context) create method}
	 * @throws ActionException when the parameter map is invalid given the action information's parameter information
	 */
	protected void checkParameters(Map<String, ActionParameter> parameters) throws ActionException {
		checkRequiredParameters(parameters);
		checkSuperfluousParameters(parameters);
	}

	/**
	 * Checks whether all required parameters are available in the parameter map.
	 *
	 * @param parameters the parameter map that was given to the {@link #create(Map, Context) create method}
	 * @throws ActionException when one of the required parameters is missing
	 */
	protected void checkRequiredParameters(Map<String, ActionParameter> parameters) throws ActionException {
		for (ActionParameterInformation parameterInformation: getInformation().getParameterInformation()) {
			String parameterName = parameterInformation.getName();

			if (parameterInformation.isRequired() && !parameters.containsKey(parameterName)) {
				throw new ActionException("missing required parameter '", parameterName, "'");
			}
		}
	}

	/**
	 * Checks whether the parameter map does not contain parameters that are not declared.
	 *
	 * @param parameters the parameter map that was given to the {@link #create(Map, Context) create method}
	 * @throws ActionException when the parameter map contains a parameter that does not match a parameter in the action
	 *                         information's parameter information
	 */
	protected void checkSuperfluousParameters(Map<String, ActionParameter> parameters) throws ActionException {
		ActionInformation information = getInformation();

		if (!information.isDynamicallyParameterisable()) {
			Set<String> parameterNames = new HashSet<String>(parameters.keySet());

			for (ActionParameterInformation parameterInformation: information.getParameterInformation()) {
				parameterNames.remove(parameterInformation.getName());
			}

			if (!parameterNames.isEmpty()) {
				throw new ActionException("got superfluous parameters: ", parameterNames);
			}
		}
	}

	/**
	 * Gets a parameter from the parameter map.
	 *
	 * @param <T> the type of the parameter
	 * @param parameters the parameter map that was given to the {@link #create(Map, Context) create method}
	 * @param context the context in which to convert the parameter to the requested type
	 * @param name the name of the parameter
	 * @param type the required type of the parameter
	 * @return the parameter with the given name with the type specified
	 * @throws ActionException when the parameter is required but not available in the parameter map or when it can't be
	 *                         converted into the requested type
	 */
	protected <T> T getParameter(Map<String, ActionParameter> parameters, Context context,
			String name, Class<T> type) throws ActionException {
		return getParameter(parameters, context, name, type, false);
	}

	/**
	 * Removes a parameter from the parameter map and returns it.
	 *
	 * @param <T> the type of the parameter
	 * @param parameters the parameter map that was given to the {@link #create(Map, Context) create method}
	 * @param context the context in which to convert the parameter to the requested type
	 * @param name the name of the parameter
	 * @param type the required type of the parameter
	 * @return the parameter with the given name with the type specified
	 * @throws ActionException when the parameter is required but not available in the parameter map or when it can't be
	 *                         converted into the requested type
	 */
	protected <T> T removeParameter(Map<String, ActionParameter> parameters, Context context,
			String name, Class<T> type) throws ActionException {
		return getParameter(parameters, context, name, type, true);
	}

	private <T> T getParameter(Map<String, ActionParameter> parameters, Context context,
			String name, Class<T> type, boolean remove) throws ActionException {
		T parameter;

		if (parameters.containsKey(name)) {
			try {
				ActionParameter actionParameter;

				if (remove) {
					actionParameter = parameters.remove(name);
				} else {
					actionParameter = parameters.get(name);
				}

				parameter = actionParameter.getValue(type, context);
			} catch (ConverterException exception) {
				throw new ActionException(exception, "can't convert parameter '", name, "'");
			}
		} else {
			Iterator<ActionParameterInformation> it = getInformation().getParameterInformation().iterator();
			Boolean required = null;

			while (it.hasNext() && (required == null)) {
				ActionParameterInformation parameterInformation = it.next();

				if (parameterInformation.getName().equals(name)) {
					required = Boolean.valueOf(parameterInformation.isRequired());
				}
			}

			if ((required == null) || !required.booleanValue()) {
				parameter = null;
			} else {
				throw new ActionException("parameter '", name, "' is required");
			}
		}

		return parameter;
	}
}