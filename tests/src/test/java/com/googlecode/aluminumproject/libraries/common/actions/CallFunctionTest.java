/*
 * Copyright 2009-2012 Aluminum project
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
package com.googlecode.aluminumproject.libraries.common.actions;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.DefaultContext;
import com.googlecode.aluminumproject.libraries.common.CommonLibraryTest;

import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
@Test(groups = {"libraries", "slow"})
public class CallFunctionTest extends CommonLibraryTest {
	public void functionShouldBeCallable() {
		Context context = new DefaultContext();

		processTemplate("call-function", context);

		assert context.getVariableNames(Context.TEMPLATE_SCOPE).contains("max");

		Object result = context.getVariable(Context.TEMPLATE_SCOPE, "max");
		assert result != null;
		assert result instanceof Integer;
		assert ((Integer) result).intValue() == 10;
	}

	@Test(dependsOnMethods = "functionShouldBeCallable")
	public void dynamicFunctionShouldBeCallable() {
		Context context = new DefaultContext();

		processTemplate("call-dynamic-function", context);

		Object result = context.getVariable(Context.TEMPLATE_SCOPE, "sum");
		assert result != null;
		assert result instanceof Integer;
		assert ((Integer) result).intValue() == 10;
	}

	@Test(expectedExceptions = AluminumException.class)
	public void callingFunctionWithArgumentsThatDoNotMatchShouldCauseException() {
		processTemplate("call-function-with-arguments-that-do-not-match");
	}
}