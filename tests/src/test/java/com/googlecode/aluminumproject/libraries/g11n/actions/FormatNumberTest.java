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

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("all")
@Test(groups = {"libraries", "libraries-g11n", "slow"})
public class FormatNumberTest extends GlobalisationLibraryTest {
	private Context context;

	@BeforeMethod
	public void createContext() {
		context = new DefaultContext();
		context.setVariable("number", 12.3);
	}

	protected void addConfigurationParameters(ConfigurationParameters parameters) {
		parameters.addParameter(GlobalisationContextProvider.LOCALE, "en_GB");
	}

	public void customNumberFormatShouldBeUsedByDefault() {
		assert processTemplate("format-number", context).equals("12.3");
	}

	public void numberFormatShouldBeUsed() {
		assert processTemplate("format-number-with-type", context).equals("£12.30");
	}
}