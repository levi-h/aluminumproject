/*
 * Copyright 2012 Aluminum project
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

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.DefaultContext;
import com.googlecode.aluminumproject.libraries.ds.DsLibraryTest;

import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
@Test(groups = {"libraries", "libraries-ds", "slow"})
public class SortTest extends DsLibraryTest {
	public void omittingComparatorShouldResultInNaturalOrdering() {
		Context context = new DefaultContext();
		context.setVariable("list", Arrays.asList("d", "b", "c", "a"));

		processTemplate("sort", context);

		context.getVariableNames(Context.TEMPLATE_SCOPE).contains("sorted list");

		Object sortedList = context.getVariable(Context.TEMPLATE_SCOPE, "sorted list");
		assert sortedList instanceof List;
		assert sortedList.equals(Arrays.asList("a", "b", "c", "d"));
	}

	@Test(expectedExceptions = AluminumException.class)
	public void sortingListWithNonComparableElementsShouldCauseException() {
		Context context = new DefaultContext();
		context.setVariable("list", Arrays.<Class<?>>asList(Short.class, Integer.class, Long.class));

		processTemplate("sort", context);
	}

	public void suppliedComparatorShouldBeUsed() {
		Context context = new DefaultContext();
		context.setVariable("list", Arrays.<Number>asList((short) 10, 10, 10L));

		processTemplate("sort-by-class-name", context);

		context.getVariableNames(Context.TEMPLATE_SCOPE).contains("sorted list");

		Object sortedList = context.getVariable(Context.TEMPLATE_SCOPE, "sorted list");
		assert sortedList instanceof List;
		assert sortedList.equals(Arrays.<Number>asList(10, 10L, (short) 10));
	}
}