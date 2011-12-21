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
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.converters.ConverterRegistry;

import java.lang.reflect.Type;

/**
 * An action parameter with a constant value.
 * <p>
 * Conversion of the value will be delegated to a {@link ConverterRegistry converter registry}.
 *
 * @author levi_h
 */
public class ConstantActionParameter implements ActionParameter {
	private String text;

	private ConverterRegistry converterRegistry;

	/**
	 * Creates a constant action parameter.
	 *
	 * @param text the text of the parameter
	 * @param converterRegistry the converter registry to use to convert the text to other types
	 */
	public ConstantActionParameter(String text, ConverterRegistry converterRegistry) {
		this.text = text;

		this.converterRegistry = converterRegistry;
	}

	public String getText() {
		return text;
	}

	public Object getValue(Type type, Context context) throws AluminumException {
		return converterRegistry.convert(text, type);
	}
}