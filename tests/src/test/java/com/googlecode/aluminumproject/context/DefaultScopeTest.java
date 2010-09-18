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
package com.googlecode.aluminumproject.context;

import java.util.Set;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("all")
@Test(groups = {"core", "fast"})
public class DefaultScopeTest {
	private Scope scope;

	@BeforeMethod
	public void createScope() {
		scope = new DefaultScope(Context.TEMPLATE_SCOPE);
	}

	public void scopeShouldHaveAName() {
		String name = scope.getName();
		assert name != null;
		assert name.equals("template");
	}

	public void variableNamesShouldInitiallyBeEmpty() {
		Set<String> variableNames = scope.getVariableNames();
		assert variableNames != null;
		assert variableNames.isEmpty();
	}

	@Test(expectedExceptions = ContextException.class)
	public void retrievingNonexistingVariableShouldCauseException() {
		scope.getVariable("nonexisting");
	}

	public void newVariableShouldBeRetrievable() {
		scope.setVariable("new", true);

		Object value = scope.getVariable("new");
		assert value != null;
		assert value instanceof Boolean;
		assert ((Boolean) value).booleanValue();
	}

	public void newVariableShouldBeIncludedInVariableNames() {
		scope.setVariable("new", true);

		Set<String> variableNames = scope.getVariableNames();
		assert variableNames != null;
		assert variableNames.contains("new");
	}

	public void replacingVariableShouldYieldPreviousValue() {
		scope.setVariable("value", "old");

		Object previousValue = scope.setVariable("value", "new");
		assert previousValue != null;
		assert previousValue.equals("old");
	}

	@Test(expectedExceptions = ContextException.class)
	public void removingNonexistingVariableShouldCauseException() {
		scope.removeVariable("nonexisting");
	}

	@Test(dependsOnMethods = "newVariableShouldBeRetrievable")
	public void removingVariableShouldYieldValue() {
		scope.setVariable("removed", true);

		Object value = scope.removeVariable("removed");
		assert value != null;
		assert value instanceof Boolean;
		assert ((Boolean) value).booleanValue();
	}

	@Test(dependsOnMethods = "newVariableShouldBeIncludedInVariableNames")
	public void removedVariableShouldNotBeIncludedInVariableNames() {
		scope.setVariable("removed", false);
		scope.removeVariable("removed");

		Set<String> variableNames = scope.getVariableNames();
		assert variableNames != null;
		assert !variableNames.contains("removed");
	}
}