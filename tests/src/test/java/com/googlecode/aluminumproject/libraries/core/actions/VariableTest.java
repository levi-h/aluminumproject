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
package com.googlecode.aluminumproject.libraries.core.actions;

import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.DefaultContext;
import com.googlecode.aluminumproject.libraries.core.CoreLibraryTest;
import com.googlecode.aluminumproject.templates.TemplateException;

import java.util.Arrays;

import org.testng.annotations.Test;

@SuppressWarnings("all")
@Test(groups = {"libraries", "libraries-core", "slow"})
public class VariableTest extends CoreLibraryTest {
	public void actionThatDoesNotUseWriterShouldNotResultInVariable() {
		Context context = new DefaultContext();

		processTemplate("variable-without-value", context);

		assert !context.getVariableNames(Context.TEMPLATE_SCOPE).contains("value");
	}

	public void actionThatWritesSingleValueShouldResultInVariable() {
		Context context = new DefaultContext();

		processTemplate("variable-with-single-value", context);

		assert context.getVariableNames(Context.TEMPLATE_SCOPE).contains("value");

		Object value = context.getVariable(Context.TEMPLATE_SCOPE, "value");
		assert value != null;
		assert value.equals("value");
	}

	public void actionThatWritesMultipleValuesShouldResultInListVariable() {
		Context context = new DefaultContext();

		processTemplate("variable-with-multiple-values", context);

		assert context.getVariableNames(Context.TEMPLATE_SCOPE).contains("value");

		Object value = context.getVariable(Context.TEMPLATE_SCOPE, "value");
		assert value != null;
		assert value.equals(Arrays.asList("value", "value", "value"));
	}

	@Test(dependsOnMethods = "actionThatWritesSingleValueShouldResultInVariable")
	public void supplyingScopeShouldResultInVariableInGivenScope() {
		Context context = new DefaultContext();
		context.addScope("scope", true);

		processTemplate("variable-with-scope", context);

		assert context.getVariableNames("scope").contains("value");

		Object value = context.getVariable("scope", "value");
		assert value != null;
		assert value.equals("value");
	}

	@Test(
		dependsOnMethods = "supplyingScopeShouldResultInVariableInGivenScope",
		expectedExceptions = TemplateException.class
	)
	public void omittingNameWhileSupplyingScopeShouldCauseException() {
		Context context = new DefaultContext();
		context.addScope("scope", true);

		processTemplate("variable-with-scope-without-name", context);
	}
}