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
package com.googlecode.aluminumproject.libraries.text.actions;

import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.DefaultContext;
import com.googlecode.aluminumproject.libraries.text.TextLibraryTest;

import org.testng.annotations.Test;

@SuppressWarnings("all")
@Test(groups = {"libraries", "libraries-text", "slow"})
public class MuteTest extends TextLibraryTest {
	public void mutingTemplateShouldResultInEmptyOutput() {
		String output = processTemplate("mute");
		assert output != null;
		assert output.equals("");
	}

	public void muteShouldBeNestable() {
		String output = processTemplate("mute-nested");
		assert output != null;
		assert output.equals("This won't be ignored.");
	}

	@Test(dependsOnMethods = "muteShouldBeNestable")
	public void repeatingBehaviourShouldHaveNoEffect() {
		String output = processTemplate("mute-repeated");
		assert output != null;
		assert output.equals("");
	}

	@Test(dependsOnMethods = "mutingTemplateShouldResultInEmptyOutput")
	public void mutedActionsShouldStillBeExecuted() {
		Context context = new DefaultContext();

		processTemplate("mute-execution", context);

		assert context.getVariableNames(Context.TEMPLATE_SCOPE).contains("test");
		assert context.getVariable(Context.TEMPLATE_SCOPE, "test") != null;
		assert context.getVariable(Context.TEMPLATE_SCOPE, "test").equals("mute");
	}
}