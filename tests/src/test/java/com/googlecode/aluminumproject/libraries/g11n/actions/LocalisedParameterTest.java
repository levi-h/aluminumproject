/*
 * Copyright 2010-2011 Levi Hoogenberg
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

import static com.googlecode.aluminumproject.context.g11n.GlobalisationContextProvider.RESOURCE_BUNDLE_PROVIDER_CLASS;
import static com.googlecode.aluminumproject.parsers.aluscript.AluScriptParser.AUTOMATIC_NEWLINES;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.configuration.ConfigurationParameters;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.g11n.ResourceBundleProvider;
import com.googlecode.aluminumproject.libraries.g11n.GlobalisationLibraryTest;

import java.util.ListResourceBundle;
import java.util.ResourceBundle;

import org.testng.annotations.Test;

@SuppressWarnings("all")
@Test(groups = {"libraries", "libraries-g11n", "slow"})
public class LocalisedParameterTest extends GlobalisationLibraryTest {
	public static class ParameterResourceBundleProvider implements ResourceBundleProvider {
		private final static ResourceBundle PARAMETER_RESOURCE_BUNDLE = new ListResourceBundle() {
			protected Object[][] getContents() {
				return new Object[][] {
					new Object[] {"text", "five"},
					new Object[] {"number", 5},
				};
			}
		};

		public ResourceBundle provide(Context context) throws AluminumException {
			return PARAMETER_RESOURCE_BUNDLE;
		}
	}

	protected void addConfigurationParameters(ConfigurationParameters parameters) {
		parameters.addParameter(AUTOMATIC_NEWLINES, "false");

		parameters.addParameter(RESOURCE_BUNDLE_PROVIDER_CLASS, ParameterResourceBundleProvider.class.getName());
	}

	public void textualParameterShouldBeLocalisable() {
		assert processTemplate("localised-parameter").equals("five");
	}

	public void numericalParameterShouldBeLocalisable() {
		assert processTemplate("localised-parameter-with-numeric-type").equals("01234");
	}

	public void customResourceBundleAndLocaleShouldBeUsed() {
		String output = processTemplate("localised-parameter-with-custom-resource-bundle-and-locale");
		assert output.equals("een krachtige en flexibele sjabloonverwerker");
	}

	@Test(expectedExceptions = AluminumException.class)
	public void missingKeyShouldCauseException() {
		processTemplate("localised-parameter-with-missing-key");
	}
}