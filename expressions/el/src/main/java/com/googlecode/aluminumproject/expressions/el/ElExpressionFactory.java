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
package com.googlecode.aluminumproject.expressions.el;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.expressions.Expression;
import com.googlecode.aluminumproject.expressions.ExpressionFactory;
import com.googlecode.aluminumproject.expressions.ExpressionOccurrence;
import com.googlecode.aluminumproject.utilities.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.el.ELException;
import javax.el.ValueExpression;

/**
 * An {@link ExpressionFactory expression factory} that uses the Unified Expression Language to create expressions.
 * <p>
 * {@link ExpressionFactory Expression factories} may be constructed with a number of implementation-specific
 * properties. The properties to supply can be configured by providing a parameter named {@value
 * #EXPRESSION_FACTORY_PROPERTIES}; by default, the expression factory is created without any parameters.
 */
public class ElExpressionFactory implements ExpressionFactory {
	private Configuration configuration;

	private javax.el.ExpressionFactory expressionFactory;

	private final Logger logger;

	/**
	 * Creates an EL expression factory.
	 */
	public ElExpressionFactory() {
		logger = Logger.get(getClass());
	}

	public void initialise(Configuration configuration) throws AluminumException {
		this.configuration = configuration;

		Map<String, String> emptyPropertyMap = Collections.emptyMap();

		Map<String, String> propertyMap =
			configuration.getParameters().getValueMap(EXPRESSION_FACTORY_PROPERTIES, emptyPropertyMap);

		if (propertyMap.equals(emptyPropertyMap)) {
			logger.debug("creating expression factory without properties");

			expressionFactory = javax.el.ExpressionFactory.newInstance();
		} else {
			logger.debug("creating expression factory with properties ", propertyMap);

			Properties properties = new Properties();
			properties.putAll(propertyMap);
			expressionFactory = javax.el.ExpressionFactory.newInstance(properties);
		}

		FunctionDelegateFactory.addConfiguration(configuration);
	}

	public void disable() {
		FunctionDelegateFactory.removeConfiguration(configuration);
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

	public Expression create(String value, Context context) throws AluminumException {
		try {
			ElContext elContext = new ElContext(context, configuration);

			ValueExpression expression =
				expressionFactory.createValueExpression(elContext, value, Object.class);

			return new ElExpression(expression, configuration);
		} catch (ELException exception) {
			throw new AluminumException(exception, "can't create expression");
		}
	}

	/**
	 * The name of the configuration parameter that contains a {@link Map map} of properties that the EL expression
	 * factory should be created with.
	 */
	public final static String EXPRESSION_FACTORY_PROPERTIES = "expression_factory.el.expression_factory_properties";
}