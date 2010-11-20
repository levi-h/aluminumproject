/*
 * Copyright 2010 Levi Hoogenberg
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
package com.googlecode.aluminumproject.libraries.g11n.actions;

import com.googlecode.aluminumproject.configuration.ConfigurationParameters;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.DefaultContext;
import com.googlecode.aluminumproject.context.g11n.GlobalisationContextProvider;
import com.googlecode.aluminumproject.libraries.g11n.GlobalisationLibraryTest;

import java.util.Date;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("all")
@Test(groups = {"libraries", "libraries-g11n", "slow"})
public class FormatDateTest extends GlobalisationLibraryTest {
	private Context context;

	@BeforeMethod
	public void createContext() {
		context = new DefaultContext();
		context.setVariable("date", new Date(0L));
	}

	protected void addConfigurationParameters(ConfigurationParameters parameters) {
		parameters.addParameter(GlobalisationContextProvider.LOCALE, "en");
	}

	public void customDateFormatShouldBeUsedByDefault() {
		assert processTemplate("format-date", context).equals("1970-01-01, 00:00");
	}

	public void typeShouldBeUsed() {
		assert processTemplate("format-date-with-type", context).equals("Jan 1, 1970");
	}

	@Test(dependsOnMethods = "typeShouldBeUsed")
	public void customLocaleShouldBeUsed() {
		assert processTemplate("format-date-with-custom-locale", context).equals("1-gen-1970");
	}

	@Test(dependsOnMethods = "customDateFormatShouldBeUsedByDefault")
	public void customPatternShouldBeUsed() {
		assert processTemplate("format-date-with-custom-pattern", context).equals("197001010000");
	}
}