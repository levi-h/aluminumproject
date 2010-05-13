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
package com.googlecode.aluminumproject.libraries.core.actions;

import com.googlecode.aluminumproject.annotations.Ignored;
import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.configuration.ConfigurationParameters;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.ContextException;
import com.googlecode.aluminumproject.converters.ConverterException;
import com.googlecode.aluminumproject.converters.ConverterRegistry;
import com.googlecode.aluminumproject.libraries.actions.AbstractAction;
import com.googlecode.aluminumproject.libraries.actions.AbstractActionFactory;
import com.googlecode.aluminumproject.libraries.actions.Action;
import com.googlecode.aluminumproject.libraries.actions.ActionException;
import com.googlecode.aluminumproject.libraries.actions.ActionInformation;
import com.googlecode.aluminumproject.libraries.actions.ActionParameter;
import com.googlecode.aluminumproject.libraries.actions.ActionParameterInformation;
import com.googlecode.aluminumproject.writers.Writer;

import java.util.Arrays;
import java.util.Map;

/**
 * Converts a value and saves the converted value in a variable.
 *
 * @author levi_h
 */
@Ignored
public class Convert extends AbstractAction {
	private ConverterRegistry converterRegistry;

	private Object value;
	private Class<?> type;

	private String resultName;
	private String resultScope;

	/**
	 * Creates a <em>convert</em> action.
	 *
	 * @param converterRegistry the converter registry to use for converting values
	 * @param value the value to convert
	 * @param type the type to convert the value into
	 * @param resultName the name of the variable in which the converted value will be stored
	 * @param resultScope the name of the scope of the result variable (may be {@code null})
	 */
	public Convert(ConverterRegistry converterRegistry,
			Object value, Class<?> type, String resultName, String resultScope) {
		this.converterRegistry = converterRegistry;

		this.value = value;
		this.type = type;

		this.resultName = resultName;
		this.resultScope = resultScope;
	}

	public void execute(Context context, Writer writer) throws ActionException, ContextException {
		Object convertedValue;

		try {
			logger.debug("trying to convert ", value, " into type '", type.getName(), "'");

			convertedValue = converterRegistry.convert(value, type);
		} catch (ConverterException exception) {
			throw new ActionException(exception, "can't convert value");
		}

		if (resultScope == null) {
			logger.debug("setting variable '", resultName, "' in innermost scope ",
				"with converted value ", convertedValue);

			context.setVariable(resultName, convertedValue);
		} else {
			logger.debug("setting variable '", resultName, "' in scope '", resultScope, "' ",
				"with converted value ", convertedValue);

			context.setVariable(resultScope, resultName, convertedValue);
		}
	}

	/**
	 * Creates <em>convert</em> actions.
	 *
	 * @author levi_h
	 */
	public static class ConvertFactory extends AbstractActionFactory {
		private ActionInformation information;

		private Configuration configuration;

		/**
		 * Creates a convert factory.
		 */
		public ConvertFactory() {}

		public void initialise(Configuration configuration, ConfigurationParameters parameters) {
			this.configuration = configuration;

			information = new ActionInformation("convert", Arrays.asList(
				new ActionParameterInformation("value", Object.class, true),
				new ActionParameterInformation("type", Class.class, true),
				new ActionParameterInformation("result name", String.class, true),
				new ActionParameterInformation("result scope", String.class, false)
			), false);
		}

		public ActionInformation getInformation() {
			return information;
		}

		public Action create(Map<String, ActionParameter> parameters, Context context) throws ActionException {
			checkParameters(parameters);

			Object value = getParameter(parameters, context, "value", Object.class);
			Class<?> type = getParameter(parameters, context, "type", Class.class);
			String resultName = getParameter(parameters, context, "result name", String.class);
			String resultScope = getParameter(parameters, context, "result scope", String.class);

			return new Convert(configuration.getConverterRegistry(), value, type, resultName, resultScope);
		}
	}
}