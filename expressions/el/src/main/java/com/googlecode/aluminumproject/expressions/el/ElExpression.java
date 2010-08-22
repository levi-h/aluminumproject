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
package com.googlecode.aluminumproject.expressions.el;

import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.configuration.ConfigurationException;
import com.googlecode.aluminumproject.configuration.ConfigurationParameters;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.expressions.Expression;
import com.googlecode.aluminumproject.expressions.ExpressionException;

import javax.el.ELException;
import javax.el.ValueExpression;

/**
 * An expression that delegates to an {@link ValueExpression EL value expression}.
 *
 * @author levi_h
 */
public class ElExpression implements Expression {
	private ValueExpression expression;

	private Configuration configuration;
	private ConfigurationParameters parameters;

	/**
	 * Creates an EL expression.
	 *
	 * @param expression the underlying value expression
	 * @param configuration the configuration that the expression factory was initialised with
	 * @param parameters the configuration parameters used
	 */
	protected ElExpression(ValueExpression expression,
			Configuration configuration, ConfigurationParameters parameters) {
		this.expression = expression;

		this.configuration = configuration;
		this.parameters = parameters;
	}

	public Object evaluate(Context context) throws ExpressionException {
		try {
			return expression.getValue(new ElContext(context, configuration, parameters));
		} catch (ConfigurationException exception) {
			throw new ExpressionException(exception, "can't create EL context");
		} catch (ELException exception) {
			throw new ExpressionException(exception, "can't evaluate expression");
		}
	}
}