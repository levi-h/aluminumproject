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
import com.googlecode.aluminumproject.converters.ConverterRegistry;
import com.googlecode.aluminumproject.expressions.ExpressionException;
import com.googlecode.aluminumproject.expressions.ExpressionFactory;

import java.lang.reflect.Type;

/**
 * An action parameter with a value that is based on the evaluation of an expression.
 * <p>
 * Conversion of the value will be delegated to a {@link ConverterRegistry converter registry}.
 *
 * @author levi_h
 */
public class ExpressionActionParameter implements ActionParameter {
	private ExpressionFactory expressionFactory;
	private String text;

	private ConverterRegistry converterRegistry;

	/**
	 * Creates an expression action parameter.
	 *
	 * @param expressionFactory the factory of the expression that will be evaluated
	 * @param text the text of the expression
	 * @param converterRegistry the converter registry to use
	 */
	public ExpressionActionParameter(
			ExpressionFactory expressionFactory, String text, ConverterRegistry converterRegistry) {
		this.expressionFactory = expressionFactory;
		this.text = text;

		this.converterRegistry = converterRegistry;
	}

	public String getText() {
		return text;
	}

	public Object getValue(Type type, Context context) throws ActionException {
		try {
			return converterRegistry.convert(expressionFactory.create(text, context).evaluate(context), type, context);
		} catch (ExpressionException exception) {
			throw new ActionException(exception, "can't evaluate expression");
		} catch (ConverterException exception) {
			throw new ActionException(exception, "can't convert expression value");
		}
	}
}