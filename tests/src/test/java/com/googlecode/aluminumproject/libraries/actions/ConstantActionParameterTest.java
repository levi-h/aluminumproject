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

import com.googlecode.aluminumproject.configuration.ConfigurationParameters;
import com.googlecode.aluminumproject.configuration.test.TestConfiguration;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.DefaultContext;
import com.googlecode.aluminumproject.converters.ConverterRegistry;
import com.googlecode.aluminumproject.converters.DefaultConverterRegistry;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("all")
@Test(groups = {"core", "fast"})
public class ConstantActionParameterTest {
	private ActionParameter parameter;

	private Context context;

	@BeforeMethod
	public void createParameter() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		TestConfiguration configuration = new TestConfiguration(parameters);

		ConverterRegistry converterRegistry = new DefaultConverterRegistry();
		converterRegistry.initialise(configuration, parameters);

		parameter = new ConstantActionParameter("128", converterRegistry);

		context = new DefaultContext();
	}

	public void valueShouldBeRetrievableAsObject() {
		Object value = parameter.getValue(Object.class, context);
		assert value != null;
		assert value.equals("128");
	}

	public void valueShouldBeRetrievableWithOriginalType() {
		String value = parameter.getValue(String.class, context);
		assert value != null;
		assert value.equals("128");
	}

	public void valueShouldBeRetrievableWithCompatibleType() {
		Integer value = parameter.getValue(Integer.class, context);
		assert value != null;
		assert value.equals(Integer.valueOf(128));
	}

	@Test(expectedExceptions = ActionException.class)
	public void retrievingValueWithIncompatibleTypeShouldCauseException() {
		parameter.getValue(Byte.TYPE, context);
	}
}