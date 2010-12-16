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

import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.converters.ConverterException;
import com.googlecode.aluminumproject.converters.ConverterRegistry;

import java.lang.reflect.Type;
import java.util.List;

/**
 * An action parameter that is composed of other action parameters.
 * <p>
 * The value of a compound action parameter is determined in the following way:
 * <ul>
 * <li>First, the values of all action parameters are determined;
 * <li>After that, the values are converted to strings;
 * <li>Then all of those strings are concatenated;
 * <li>Finally, that string is converted into the requested type.
 * </ul>
 *
 * @author levi_h
 */
public class CompoundActionParameter implements ActionParameter {
	private List<ActionParameter> parameters;

	private ConverterRegistry converterRegistry;

	/**
	 * Creates a compound action parameter.
	 *
	 * @param parameters the parameters that constitute the compound parameter
	 * @param converterRegistry the converter registry to use when converting the parameter values
	 */
	public CompoundActionParameter(List<ActionParameter> parameters, ConverterRegistry converterRegistry) {
		this.parameters = parameters;

		this.converterRegistry = converterRegistry;
	}

	public String getText() {
		StringBuilder textBuilder = new StringBuilder();

		for (ActionParameter parameter: parameters) {
			textBuilder.append(parameter.getText());
		}

		return textBuilder.toString();
	}

	public Object getValue(Type type, Context context) throws ActionException {
		StringBuilder valueBuilder = new StringBuilder();

		for (ActionParameter parameter: parameters) {
			valueBuilder.append(parameter.getValue(String.class, context));
		}

		try {
			return converterRegistry.convert(valueBuilder.toString(), type, context);
		} catch (ConverterException exception) {
			throw new ActionException(exception, "can't convert compound parameter value");
		}
	}
}