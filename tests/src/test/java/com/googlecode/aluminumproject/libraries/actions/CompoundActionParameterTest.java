/*
 * Copyright 2010-2012 Levi Hoogenberg
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
import com.googlecode.aluminumproject.configuration.ConfigurationParameters;
import com.googlecode.aluminumproject.configuration.TestConfiguration;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.DefaultContext;
import com.googlecode.aluminumproject.converters.ConverterRegistry;
import com.googlecode.aluminumproject.converters.DefaultConverterRegistry;
import com.googlecode.aluminumproject.expressions.ExpressionFactory;
import com.googlecode.aluminumproject.expressions.TestExpressionFactory;

import java.util.Arrays;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
@Test(groups = {"core", "fast"})
public class CompoundActionParameterTest {
	private ActionParameter parameter;

	private Context context;

	@BeforeMethod
	public void createParameterAndContext() {
		TestConfiguration configuration = new TestConfiguration(new ConfigurationParameters());

		ConverterRegistry converterRegistry = new DefaultConverterRegistry();
		converterRegistry.initialise(configuration);

		ExpressionFactory expressionFactory = new TestExpressionFactory();
		expressionFactory.initialise(configuration);

		parameter = new CompoundActionParameter(Arrays.<ActionParameter>asList(
			new ExpressionActionParameter(expressionFactory, "<<number>>", converterRegistry),
			new ConstantActionParameter(" items", converterRegistry)
		), converterRegistry);

		context = new DefaultContext();
	}

	public void textShouldBeComposedOfTextOfParameters() {
		String text = parameter.getText();
		assert text != null;
		assert text.equals("<<number>> items");
	}

	public void valueShouldBeRetrievableWithCompatibleType() {
		context.setVariable("number", 5);

		Object value = parameter.getValue(String.class, context);
		assert value instanceof String;
		assert value.equals("5 items");
	}

	@Test(expectedExceptions = AluminumException.class)
	public void retrievingValueWithIncompatibleTypeShouldCauseException() {
		context.setVariable("number", 10);

		parameter.getValue(Integer.TYPE, context);
	}
}