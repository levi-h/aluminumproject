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
package com.googlecode.aluminumproject.configuration;

import java.util.Arrays;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("all")
@Test(groups = {"core", "fast"})
public class ConfigurationParametersTest {
	private ConfigurationParameters parameters;

	@BeforeMethod
	public void createParameters() {
		parameters = new ConfigurationParameters();
	}

	@Test(expectedExceptions = ConfigurationException.class)
	public void addingNullShouldCauseException() {
		parameters.addParameter("name", null);
	}

	@Test(expectedExceptions = ConfigurationException.class)
	public void addingEmptyValueShouldCauseException() {
		parameters.addParameter("name", "");
	}

	public void valueShouldBeRetrievable() {
		parameters.addParameter("name", "value");

		String value = parameters.getValue("name", "default");
		assert value != null;
		assert value.equals("value");
	}

	public void valueShouldFallBackToDefaultWhenParameterWasNotAdded() {
		String value = parameters.getValue("name", "default");
		assert value != null;
		assert value.equals("default");
	}

	public void valuesShouldBeRetrievable() {
		parameters.addParameter("name", "value, value");

		assert Arrays.equals(parameters.getValues("name", "default"), new String[] {"value", "value"});
	}

	public void valuesShouldFallBackToDefaultWhenParameterWasNotAdded() {
		assert Arrays.equals(parameters.getValues("name", "default", "default"), new String[] {"default", "default"});
	}
}