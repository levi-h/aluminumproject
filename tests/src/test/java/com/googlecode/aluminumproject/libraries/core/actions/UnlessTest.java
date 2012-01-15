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
package com.googlecode.aluminumproject.libraries.core.actions;

import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.DefaultContext;
import com.googlecode.aluminumproject.libraries.core.CoreLibraryTest;

import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
@Test(groups = {"libraries", "libraries-core", "slow"})
public class UnlessTest extends CoreLibraryTest {
	public void parameterWithConditionThatMeetsShouldSkipAction() {
		Context context = new DefaultContext();
		context.setVariable("value", 6);

		String output = processTemplate("unless", context);
		assert output != null;
		assert output.equals("");
	}

	public void parameterWithConditionThatDoesNotMeetShouldExecuteAction() {
		Context context = new DefaultContext();
		context.setVariable("value", 5);

		String output = processTemplate("unless", context);
		assert output != null;
		assert output.equals("executed");
	}
}