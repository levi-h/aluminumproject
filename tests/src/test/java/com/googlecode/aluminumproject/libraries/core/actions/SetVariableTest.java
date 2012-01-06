/*
 * Copyright 2009-2012 Levi Hoogenberg
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
package com.googlecode.aluminumproject.libraries.core.actions;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.DefaultContext;
import com.googlecode.aluminumproject.libraries.core.CoreLibraryTest;

import java.util.List;

import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
@Test(groups = {"libraries", "libraries-core", "slow"})
public class SetVariableTest extends CoreLibraryTest {
	public void scopeShouldBeUsed() {
		Context context = new DefaultContext();

		processTemplate("set-variable-in-specific-scope", context);

		Object variable = context.getVariable(Context.TEMPLATE_SCOPE, "project");
		assert variable != null;
		assert variable.equals("Aluminum");
	}

	public void noScopeShouldResultInInnermostScope() {
		Context context = new DefaultContext();
		context.addScope("block", true);

		processTemplate("set-variable-without-scope", context);

		Object variable = context.getVariable("block", "project");
		assert variable != null;
		assert variable.equals("Aluminum");
	}

	@Test(dependsOnMethods = "noScopeShouldResultInInnermostScope")
	public void settingVariableMoreThanOnceShouldReplaceIt() {
		Context context = new DefaultContext();

		processTemplate("set-variable-more-than-once", context);

		Object variable = context.getVariable(Context.TEMPLATE_SCOPE, "project");
		assert variable != null;
		assert variable.equals("Aluminum");
	}

	@Test(dependsOnMethods = "noScopeShouldResultInInnermostScope")
	public void bodyShouldBeUsable() {
		Context context = new DefaultContext();

		processTemplate("set-variable-using-body", context);

		Object variable = context.getVariable(Context.TEMPLATE_SCOPE, "project");
		assert variable != null;
		assert variable.equals("Aluminum");
	}

	@Test(dependsOnMethods = "noScopeShouldResultInInnermostScope")
	public void settingMoreThanOneValueInBodyShouldResultInList() {
		Context context = new DefaultContext();
		context.setVariable("name", "Aluminum");
		context.setVariable("creator", "levi_h");

		processTemplate("set-variable-using-body-with-multiple-values", context);

		Object variable = context.getVariable(Context.TEMPLATE_SCOPE, "projectInformation");
		assert variable instanceof List;

		List<?> elements = (List<?>) variable;
		assert elements.size() == 3;

		Object firstElement = elements.get(0);
		assert firstElement != null;
		assert firstElement.equals("Aluminum");

		Object secondElement = elements.get(1);
		assert secondElement != null;
		assert secondElement.equals(" ");

		Object thirdElement = elements.get(2);
		assert thirdElement != null;
		assert thirdElement.equals("levi_h");
	}

	@Test(expectedExceptions = AluminumException.class)
	public void omittingValueShouldCauseException() {
		processTemplate("set-variable-without-value");
	}
}