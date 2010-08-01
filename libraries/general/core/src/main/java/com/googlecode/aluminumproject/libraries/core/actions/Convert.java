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

import com.googlecode.aluminumproject.annotations.ActionParameterInformation;
import com.googlecode.aluminumproject.annotations.Injected;
import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.ContextException;
import com.googlecode.aluminumproject.converters.ConverterException;
import com.googlecode.aluminumproject.libraries.actions.AbstractAction;
import com.googlecode.aluminumproject.libraries.actions.ActionException;
import com.googlecode.aluminumproject.writers.Writer;

import java.lang.reflect.Type;

/**
 * Converts a value and saves the converted value in a variable.
 *
 * @author levi_h
 */
public class Convert extends AbstractAction {
	private Object value;
	private Type type;

	private String resultName;
	private String resultScope;

	private @Injected Configuration configuration;

	/**
	 * Creates a <em>convert</em> action.
	 */
	public Convert() {}

	/**
	 * Sets the value that should be converted.
	 *
	 * @param value the value to convert
	 */
	@ActionParameterInformation(required = true)
	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * Sets the type that the value should be converted into.
	 *
	 * @param type the desired type
	 */
	@ActionParameterInformation(required = true)
	public void setType(Type type) {
		this.type = type;
	}

	/**
	 * Sets the name of the variable that will contain the converted value.
	 *
	 * @param resultName the name of the result variable
	 */
	@ActionParameterInformation(required = true)
	public void setResultName(String resultName) {
		this.resultName = resultName;
	}

	/**
	 * Sets the (optional) scope of the variable that will contain the converted value.
	 *
	 * @param resultScope the scope of the result variable
	 */
	public void setResultScope(String resultScope) {
		this.resultScope = resultScope;
	}

	public void execute(Context context, Writer writer) throws ActionException, ContextException {
		Object convertedValue;

		try {
			logger.debug("trying to convert ", value, " into ", type);

			convertedValue = configuration.getConverterRegistry().convert(value, type);
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
}