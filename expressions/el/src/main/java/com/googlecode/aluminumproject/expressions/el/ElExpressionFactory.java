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
import com.googlecode.aluminumproject.expressions.ExpressionFactory;
import com.googlecode.aluminumproject.expressions.ExpressionOccurrence;

import java.util.ArrayList;
import java.util.List;

import javax.el.ELException;
import javax.el.ValueExpression;

/**
 * An {@link ExpressionFactory expression factory} that uses the Unified Expression Language to create expressions.
 *
 * @author levi_h
 */
public class ElExpressionFactory implements ExpressionFactory {
	private Configuration configuration;
	private ConfigurationParameters parameters;

	private javax.el.ExpressionFactory expressionFactory;

	/**
	 * Creates an EL expression factory.
	 */
	public ElExpressionFactory() {}

	public void initialise(Configuration configuration, ConfigurationParameters parameters) {
		this.configuration = configuration;
		this.parameters = parameters;

		expressionFactory = javax.el.ExpressionFactory.newInstance();

		FunctionDelegateFactory.addConfiguration(configuration);
	}

	public List<ExpressionOccurrence> findExpressions(String text) {
		List<ExpressionOccurrence> occurrences = new ArrayList<ExpressionOccurrence>();

		int beginIndex = 1;

		while ((beginIndex = text.indexOf('{', beginIndex)) >= 0) {
			if (((text.charAt(beginIndex - 1) == '$') || (text.charAt(beginIndex - 1) == '#'))) {
				int endIndex = beginIndex + 1;

				if ((beginIndex <= 1) || (text.charAt(beginIndex - 2) != '\\')) {
					boolean insideString = false;

					while ((endIndex < text.length()) && !(!insideString && (text.charAt(endIndex) == '}'))) {
						if (text.charAt(endIndex) == '\'') {
							insideString = !insideString;
						}

						endIndex++;
					}

					if (endIndex < text.length()) {
						occurrences.add(new ExpressionOccurrence(beginIndex - 1, ++endIndex));
					}
				}

				beginIndex = endIndex;
			} else {
				beginIndex++;
			}
		}

		return occurrences;
	}

	public Expression create(String value, Context context) throws ExpressionException {
		try {
			ElContext elContext = new ElContext(context, configuration, parameters);

			ValueExpression expression =
				expressionFactory.createValueExpression(elContext, value, Object.class);

			return new ElExpression(expression, configuration, parameters);
		} catch (ConfigurationException exception) {
			throw new ExpressionException(exception, "can't create EL context");
		} catch (ELException exception) {
			throw new ExpressionException(exception, "can't create expression");
		}
	}
}