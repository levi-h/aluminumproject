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
package com.googlecode.aluminumproject.libraries.core.actions;

import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.DefaultContext;
import com.googlecode.aluminumproject.libraries.core.CoreLibraryTest;
import com.googlecode.aluminumproject.templates.TemplateException;

import org.testng.annotations.Test;

@SuppressWarnings("all")
@Test(groups = {"libraries", "libraries-core", "slow"})
public class ScopeTest extends CoreLibraryTest {
	public void variablesShouldNotBeAvailableOutsideScope() {
		Context context = new DefaultContext();

		processTemplate("scope", context);

		assert context.getVariableNames(Context.TEMPLATE_SCOPE).isEmpty();
	}

	@Test(dependsOnMethods = "variablesShouldNotBeAvailableOutsideScope", expectedExceptions = TemplateException.class)
	public void accessingVariablesOutsideScopeShouldCauseException() {
		processTemplate("scope-accessing-inaccessible-variable");
	}

	public void parentScopesShouldBeAvailable() {
		Context context = new DefaultContext();

		processTemplate("scope-nested", context);

		Object variable = context.getVariable(Context.TEMPLATE_SCOPE, "number");
		assert variable != null;
		assert variable.equals("10");
	}

	@Test(dependsOnMethods = "parentScopesShouldBeAvailable")
	public void namingScopeShouldBePossible() {
		Context context = new DefaultContext();

		processTemplate("scope-named", context);

		Object variable = context.getVariable(Context.TEMPLATE_SCOPE, "number");
		assert variable != null;
		assert variable.equals("10");

	}

	@Test(dependsOnMethods = "namingScopeShouldBePossible", expectedExceptions = TemplateException.class)
	public void addingDuplicateScopeShouldCauseException() {
		processTemplate("scope-duplication");
	}
}