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

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.configuration.ConfigurationParameters;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.DefaultContext;
import com.googlecode.aluminumproject.context.g11n.GlobalisationContextProvider;
import com.googlecode.aluminumproject.libraries.g11n.GlobalisationLibraryTest;

import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
@Test(groups = {"libraries", "libraries-g11n", "slow"})
public class LocaliseTest extends GlobalisationLibraryTest {
	protected void addConfigurationParameters(ConfigurationParameters parameters) {
		parameters.addParameter(GlobalisationContextProvider.LOCALE, "en");
	}

	public void localisedResourceShouldBeFindableWithExistingKey() {
		assert processTemplate("localise").equals("a powerful and flexible template engine");
	}

	@Test(expectedExceptions = AluminumException.class)
	public void tryingToFindLocalisedResourceWithMissingKeyShouldCauseException() {
		Context context = new DefaultContext();
		context.setVariable("allowMissingKey", false);

		processTemplate("localise-with-missing-key", context);
	}

	public void tryingToFindLocalisedResourceWithMissingKeyAndDefaultShouldResultInDefaultResource() {
		assert processTemplate("localise-with-default").equals("the framework that makes generating reports fun");
	}

	public void tryingToFindLocalisedResourceWithAllowedMissingKeyShouldResultInKeyInsideQuestionMarks() {
		Context context = new DefaultContext();
		context.setVariable("allowMissingKey", true);

		assert processTemplate("localise-with-missing-key", context).equals("??rivoli??");
	}

	@Test(dependsOnMethods = "localisedResourceShouldBeFindableWithExistingKey")
	public void customResourceBundleShouldBeUsed() {
		String output = processTemplate("localise-with-custom-resource-bundle");
		assert output.equals("the framework that makes generating reports fun");
	}

	@Test(dependsOnMethods = "localisedResourceShouldBeFindableWithExistingKey")
	public void customLocaleShouldBeUsed() {
		assert processTemplate("localise-with-custom-locale").equals("een krachtige en flexibele sjabloonverwerker");
	}
}