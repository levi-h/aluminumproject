/*
 * Copyright 2009-2012 Aluminum project
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
package com.googlecode.aluminumproject.libraries.functions;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.converters.ConverterRegistry;

import java.lang.reflect.Type;

/**
 * A function argument with a constant value.
 * <p>
 * Conversion of the value will be delegated to a {@link ConverterRegistry converter registry}.
 */
public class ConstantFunctionArgument implements FunctionArgument {
	private Object value;

	private ConverterRegistry converterRegistry;

	/**
	 * Creates a constant function argument.
	 *
	 * @param value the constant argument value
	 * @param converterRegistry the converter registry that can be used to convert the value to other types
	 */
	public ConstantFunctionArgument(Object value, ConverterRegistry converterRegistry) {
		this.value = value;

		this.converterRegistry = converterRegistry;
	}

	public Object getValue(Type type, Context context) throws AluminumException {
		return converterRegistry.convert(value, type);
	}
}