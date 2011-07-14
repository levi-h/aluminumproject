/*
 * Copyright 2011 Levi Hoogenberg
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
package com.googlecode.aluminumproject.libraries.scripting.actions;

import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.DefaultContext;
import com.googlecode.aluminumproject.libraries.scripting.ScriptingLibraryTest;
import com.googlecode.aluminumproject.templates.TemplateException;

import org.testng.annotations.Test;

@SuppressWarnings("all")
@Test(groups = {"libraries", "libraries-scripting", "slow"})
public class ScriptTest extends ScriptingLibraryTest {
	@Test(expectedExceptions = TemplateException.class)
	public void usingUnavailableScriptTypeShouldCauseException() {
		processTemplate("script-with-unavailable-type");
	}

	public void textShouldBeWritable() {
		String output = processTemplate("script");
		assert output != null;
		assert output.equals("Hello!");
	}

	@Test(dependsOnMethods = "textShouldBeWritable")
	public void contextVariablesShouldBeAvailable() {
		Context context = new DefaultContext();
		context.setVariable("greeting", "Hello!");

		String output = processTemplate("script-with-variable", context);
		assert output != null;
		assert output.equals("Hello!"): output;
	}

	public void newScriptVariablesShouldBeAddedToContext() {
		Context context = new DefaultContext();

		processTemplate("script-with-new-variable", context);

		assert context.getVariableNames(Context.TEMPLATE_SCOPE).contains("greeting");

		Object variable = context.getVariable(Context.TEMPLATE_SCOPE, "greeting");
		assert variable != null;
		assert variable.equals("Hello!");
	}
}