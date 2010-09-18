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
package com.googlecode.aluminumproject.expressions;

import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.ContextException;

/**
 * An expression that can be used in tests.
 *
 * @author levi_h
 */
public class TestExpression implements Expression {
	private String value;

	/**
	 * Creates a test expression.
	 *
	 * @param value the value to use
	 */
	public TestExpression(String value) {
		this.value = value;
	}

	public Object evaluate(Context context) throws ExpressionException {
		int length = value.length();

		if ((length <= 2) || (value.charAt(0) != '[') || (value.charAt(length - 1) != ']')) {
			throw new ExpressionException("test expressions should be in [this form]");
		} else {
			String variableName = value.substring(1, length - 1);

			try {
				return context.findVariable(variableName);
			} catch (ContextException exception) {
				throw new ExpressionException(exception, "can't find variable '", variableName, "'");
			}
		}
	}
}