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
package com.googlecode.aluminumproject.libraries.g11n.actions;

import com.googlecode.aluminumproject.configuration.ConfigurationParameters;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.DefaultContext;
import com.googlecode.aluminumproject.context.g11n.GlobalisationContextProvider;
import com.googlecode.aluminumproject.libraries.g11n.GlobalisationLibraryTest;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
@Test(groups = {"libraries", "libraries-g11n", "slow"})
public class FormatNumberTest extends GlobalisationLibraryTest {
	private Context context;

	@BeforeMethod
	public void createContext() {
		context = new DefaultContext();
		context.setVariable("number", 1234.5);
	}

	protected void addConfigurationParameters(ConfigurationParameters parameters) {
		parameters.addParameter(GlobalisationContextProvider.LOCALE, "en_GB");
	}

	public void customNumberFormatShouldBeUsedByDefault() {
		assert processTemplate("format-number", context).equals("1,234.5");
	}

	public void typeShouldBeUsed() {
		assert processTemplate("format-number-with-type", context).equals("\u00a31,234.50");
	}

	public void optionsShouldBeUsed() {
		processTemplate("format-number-with-options", context);

		Object numberWithoutGrouping = context.getVariable(Context.TEMPLATE_SCOPE, "no grouping");
		assert numberWithoutGrouping != null;
		assert numberWithoutGrouping.equals("1234.5");

		Object numberWithoutFractionDigits = context.getVariable(Context.TEMPLATE_SCOPE, "no fraction digits");
		assert numberWithoutFractionDigits != null;
		assert numberWithoutFractionDigits.equals("1,234");

		Object numberWithTwoFractionDigits = context.getVariable(Context.TEMPLATE_SCOPE, "two fraction digits");
		assert numberWithTwoFractionDigits != null;
		assert numberWithTwoFractionDigits.equals("1,234.50");

		Object numberWithOneIntegerDigit = context.getVariable(Context.TEMPLATE_SCOPE, "one integer digit");
		assert numberWithOneIntegerDigit != null;
		assert numberWithOneIntegerDigit.equals("4.5");

		Object numberWithSevenIntegerDigits = context.getVariable(Context.TEMPLATE_SCOPE, "seven integer digits");
		assert numberWithSevenIntegerDigits != null;
		assert numberWithSevenIntegerDigits.equals("0,001,234.5");
	}

	@Test(dependsOnMethods = "customNumberFormatShouldBeUsedByDefault")
	public void customPatternShouldBeUsed() {
		assert processTemplate("format-number-with-custom-pattern", context).equals("1234.5");
	}
}