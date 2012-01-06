/*
 * Copyright 2009-2012 Levi Hoogenberg
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

import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
@Test(groups = {"libraries", "libraries-core", "slow"})
public class EachTest extends CoreLibraryTest {
	public void collectionShouldBeIterable() {
		Context context = new DefaultContext();
		context.setVariable("numbers", Arrays.asList(1, 2, 3, 4));

		processTemplate("each", context);

		Object variable = context.getVariable(Context.TEMPLATE_SCOPE, "sum");
		assert variable instanceof Long;
		assert ((Long) variable).longValue() == 10L;
	}

	public void arrayShouldBeIterable() {
		Context context = new DefaultContext();
		context.setVariable("numbers", new int[] {1, 2, 3, 4});

		processTemplate("each", context);

		Object variable = context.getVariable(Context.TEMPLATE_SCOPE, "sum");
		assert variable instanceof Long;
		assert ((Long) variable).longValue() == 10L;
	}

	@Test(dependsOnMethods = "collectionShouldBeIterable")
	public void elementNameShouldBeAssignable() {
		Context context = new DefaultContext();
		context.setVariable("numbers", Arrays.asList(1, 2, 3, 4));

		processTemplate("each-with-element-name", context);

		Object variable = context.getVariable(Context.TEMPLATE_SCOPE, "sum");
		assert variable instanceof Long;
		assert ((Long) variable).longValue() == 10L;
	}

	@Test(dependsOnMethods = "collectionShouldBeIterable")
	public void notSupplyingInformationNameShouldResultInDefaultInformationNameToBeUsed() {
		Context context = new DefaultContext();
		context.setVariable("numbers", Arrays.asList(1, 2, 3, 4));

		processTemplate("each-with-default-information-name", context);

		Object variable = context.getVariable(Context.TEMPLATE_SCOPE, "sum");
		assert variable instanceof Long;
		assert ((Long) variable).longValue() == 5L;
	}

	@Test(dependsOnMethods = "collectionShouldBeIterable")
	public void informationNameShouldBeAssignable() {
		Context context = new DefaultContext();
		context.setVariable("numbers", Arrays.asList(1, 2, 3, 4));

		processTemplate("each-with-information-name", context);

		Object variable = context.getVariable(Context.TEMPLATE_SCOPE, "sum");
		assert variable instanceof Long;
		assert ((Long) variable).longValue() == 5L;
	}

	@Test(dependsOnMethods = "arrayShouldBeIterable")
	public void informationShouldBeCorrect() {
		Context context = new DefaultContext();
		context.setVariable("numbers", new int[] {1, 2, 3});

		processTemplate("each-with-stored-information", context);

		Object variable = context.getVariable(Context.TEMPLATE_SCOPE, "loops");
		assert variable instanceof List;

		List<?> loops = (List<?>) variable;
		assert loops.size() == 3;
		assert loops.get(0) instanceof LoopInformation;
		assert loops.get(1) instanceof LoopInformation;
		assert loops.get(2) instanceof LoopInformation;

		LoopInformation firstLoop = (LoopInformation) loops.get(0);
		assert firstLoop.getIndex() == 0;
		assert firstLoop.getCount() == 3;
		assert firstLoop.isFirst();
		assert !firstLoop.isLast();

		LoopInformation secondLoop = (LoopInformation) loops.get(1);
		assert secondLoop.getIndex() == 1;
		assert secondLoop.getCount() == 3;
		assert !secondLoop.isFirst();
		assert !secondLoop.isLast();

		LoopInformation thirdLoop = (LoopInformation) loops.get(2);
		assert thirdLoop.getIndex() == 2;
		assert thirdLoop.getCount() == 3;
		assert !thirdLoop.isFirst();
		assert thirdLoop.isLast();
	}
}