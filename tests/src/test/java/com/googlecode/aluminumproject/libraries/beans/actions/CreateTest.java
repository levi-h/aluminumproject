/*
 * Copyright 2012-2013 Aluminum project
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
package com.googlecode.aluminumproject.libraries.beans.actions;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.DefaultContext;
import com.googlecode.aluminumproject.libraries.beans.BeansLibraryTest;
import com.googlecode.aluminumproject.utilities.GenericsUtilities;

import java.util.GregorianCalendar;
import java.util.HashMap;

import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
@Test(groups = {"libraries", "libraries-beans", "slow"})
public class CreateTest extends BeansLibraryTest {
	public void beanShouldBeCreatable() {
		Context context = new DefaultContext();
		context.setVariable("type", "java.util.GregorianCalendar");

		processTemplate("create", context);

		assert context.getVariableNames(Context.TEMPLATE_SCOPE).contains("bean");
		assert context.getVariable(Context.TEMPLATE_SCOPE, "bean") instanceof GregorianCalendar;
	}

	@Test(expectedExceptions = AluminumException.class)
	public void tryingToCreateBeanWithNonClassAsTypeShouldCauseException() {
		Context context = new DefaultContext();
		context.setVariable("type", GenericsUtilities.getType("? extends java.util.Calendar"));

		processTemplate("create", context);
	}

	@Test(expectedExceptions = AluminumException.class)
	public void tryingToCreateBeanWithNonInstantiableClassAsTypeShouldCauseException() {
		Context context = new DefaultContext();
		context.setVariable("type", "java.util.Calendar");

		processTemplate("create", context);
	}

	public void beanShouldBeCreatableWithArguments() {
		Context context = new DefaultContext();
		context.setVariable("type", "java.util.HashMap");
		context.setVariable("firstArgument", 10);
		context.setVariable("secondArgument", 0.5F);

		processTemplate("create-with-arguments", context);

		assert context.getVariableNames(Context.TEMPLATE_SCOPE).contains("bean");
		assert context.getVariable(Context.TEMPLATE_SCOPE, "bean") instanceof HashMap;
	}

	@Test(expectedExceptions = AluminumException.class)
	public void tryingToCreateBeanWithIllegalArgumentsShouldCauseException() {
		Context context = new DefaultContext();
		context.setVariable("type", "java.util.HashMap");
		context.setVariable("firstArgument", "10");
		context.setVariable("secondArgument", "0.5");

		processTemplate("create-with-arguments", context);
	}
}