/*
 * Copyright 2012 Levi Hoogenberg
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
package com.googlecode.aluminumproject.libraries.beans.actions;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.configuration.ConfigurationParameters;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.DefaultContext;
import com.googlecode.aluminumproject.libraries.beans.BeansLibraryTest;
import com.googlecode.aluminumproject.parsers.aluscript.AluScriptParser;

import java.util.Calendar;

import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
@Test(groups = {"libraries", "libraries-beans", "slow"})
public class SetPropertyTest extends BeansLibraryTest {
	protected void addConfigurationParameters(ConfigurationParameters configurationParameters) {
		configurationParameters.addParameter(AluScriptParser.AUTOMATIC_NEWLINES, "false");
	}

	public void valueShouldBeSet() {
		Calendar calendar = Calendar.getInstance();

		Context context = new DefaultContext();
		context.setVariable("bean", calendar);
		context.setVariable("name", "timeInMillis");
		context.setVariable("value", 1000000000000L);

		processTemplate("set-property", context);

		assert calendar.get(Calendar.YEAR) == 2001;
	}

	@Test(dependsOnMethods = "valueShouldBeSet")
	public void valueShouldBeConverted() {
		Calendar calendar = Calendar.getInstance();

		Context context = new DefaultContext();
		context.setVariable("bean", calendar);
		context.setVariable("name", "timeInMillis");
		context.setVariable("value", "500000000000");

		processTemplate("set-property", context);

		assert calendar.get(Calendar.YEAR) == 1985;
	}

	public void bodyShouldBeUsedWhenValueParameterIsOmitted() {
		Calendar calendar = Calendar.getInstance();

		Context context = new DefaultContext();
		context.setVariable("bean", calendar);
		context.setVariable("name", "timeInMillis");
		context.setVariable("value", 250000000000L);

		processTemplate("set-property-with-body", context);

		assert calendar.get(Calendar.YEAR) == 1977;
	}

	@Test(expectedExceptions = AluminumException.class)
	public void tryingToSetPropertyOnNullBeanShouldCauseException() {
		Context context = new DefaultContext();
		context.setVariable("bean", null);
		context.setVariable("name", "type");
		context.setVariable("value", "Null");

		processTemplate("set-property", context);
	}
}