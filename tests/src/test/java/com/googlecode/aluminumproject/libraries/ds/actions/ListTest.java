/*
 * Copyright 2013 Aluminum project
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
package com.googlecode.aluminumproject.libraries.ds.actions;

import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.DefaultContext;
import com.googlecode.aluminumproject.libraries.ds.DsLibraryTest;
import com.googlecode.aluminumproject.utilities.Utilities;

import java.util.Arrays;

import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
@Test(groups = {"libraries", "libraries-ds", "slow"})
public class ListTest extends DsLibraryTest {
	public void emptyListShouldBeCreatable() {
		Context context = new DefaultContext();

		processTemplate("empty-list", context);

		java.util.List<?> list = Utilities.typed(context.getVariable(Context.TEMPLATE_SCOPE, "list"));
		assert list != null;
		assert list.isEmpty();
	}

	public void listShouldBeCreatable() {
		Context context = new DefaultContext();

		processTemplate("list", context);

		java.util.List<?> list = Utilities.typed(context.getVariable(Context.TEMPLATE_SCOPE, "list"));
		assert list != null;
		assert list.equals(Arrays.asList("a", "b"));
	}
}