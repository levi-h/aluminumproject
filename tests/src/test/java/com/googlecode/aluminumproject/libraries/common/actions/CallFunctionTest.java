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
package com.googlecode.aluminumproject.libraries.common.actions;

import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.DefaultContext;
import com.googlecode.aluminumproject.libraries.common.CommonLibraryTest;
import com.googlecode.aluminumproject.templates.TemplateException;

import org.testng.annotations.Test;

@SuppressWarnings("all")
@Test(groups = {"libraries", "slow"})
public class CallFunctionTest extends CommonLibraryTest {
	public void resultShouldBeStoredInContextVariable() {
		Context context = new DefaultContext();

		processTemplate("call-function", context);

		assert context.getVariableNames(Context.TEMPLATE_SCOPE).contains("max");

		Object result = context.getVariable(Context.TEMPLATE_SCOPE, "max");
		assert result != null;
		assert result instanceof Integer;
		assert ((Integer) result).intValue() == 10;
	}

	public void scopeOfResultVariableShouldBeConfigurable() {
		Context context = new DefaultContext();
		context.setVariable("a", 5);
		context.setVariable("b", 10);

		context.addScope("results", true);

		processTemplate("call-function-with-custom-result-scope", context);

		assert context.getVariableNames("results").contains("max");

		Object result = context.getVariable("results", "max");
		assert result != null;
		assert result instanceof Integer;
		assert ((Integer) result).intValue() == 10;
	}

	@Test(expectedExceptions = TemplateException.class)
	public void callingFunctionWithArgumentsThatDoNotMatchShouldCauseException() {
		processTemplate("call-function-with-arguments-that-do-not-match", new DefaultContext());
	}
}