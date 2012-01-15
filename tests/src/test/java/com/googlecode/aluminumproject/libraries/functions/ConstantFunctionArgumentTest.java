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
import com.googlecode.aluminumproject.configuration.ConfigurationParameters;
import com.googlecode.aluminumproject.configuration.TestConfiguration;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.DefaultContext;
import com.googlecode.aluminumproject.converters.ConverterRegistry;
import com.googlecode.aluminumproject.converters.DefaultConverterRegistry;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
@Test(groups = {"core", "fast"})
public class ConstantFunctionArgumentTest {
	private FunctionArgument argument;

	private Context context;

	@BeforeMethod
	public void createArgument() {
		ConverterRegistry converterRegistry = new DefaultConverterRegistry();
		converterRegistry.initialise(new TestConfiguration(new ConfigurationParameters()));

		argument = new ConstantFunctionArgument("32768", converterRegistry);

		context = new DefaultContext();
	}

	public void valueShouldBeRetrievableAsObject() {
		Object value = argument.getValue(Object.class, context);
		assert value != null;
		assert value.equals("32768");
	}

	public void valueShouldBeRetrievableWithOriginalType() {
		Object value = argument.getValue(String.class, context);
		assert value instanceof String;
		assert value.equals("32768");
	}

	public void valueShouldBeRetrievableWithCompatibleType() {
		Object value = argument.getValue(Integer.class, context);
		assert value instanceof Integer;
		assert ((Integer) value).intValue() == 32768;
	}

	@Test(expectedExceptions = AluminumException.class)
	public void retrievingValueWithIncompatibleTypeShouldCauseException() {
		argument.getValue(Short.TYPE, context);
	}
}