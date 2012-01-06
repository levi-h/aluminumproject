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
package com.googlecode.aluminumproject.libraries.functions;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.DefaultContext;

import java.lang.reflect.Method;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
@Test(groups = {"core", "fast"})
public class StaticMethodInvokingFunctionTest {
	private Context context;

	@BeforeMethod
	public void createContext() {
		context = new DefaultContext();
	}

	@Test(expectedExceptions = AluminumException.class)
	public void callingInaccessibleFunctionShouldCauseException() throws NoSuchMethodException {
		new StaticMethodInvokingFunction(TestFunctions.class.getDeclaredMethod("inaccessible")).call(context);
	}

	@Test(expectedExceptions = AluminumException.class)
	public void callingFunctionThatThrowsExceptionShouldCauseException() throws NoSuchMethodException {
		Method divide = TestFunctions.class.getMethod("divide", Integer.TYPE, Integer.TYPE);

		new StaticMethodInvokingFunction(divide, 1, 0).call(context);
	}

	public void callingFunctionShouldInvokeMethod() throws NoSuchMethodException {
		Method divide = TestFunctions.class.getMethod("divide", Integer.TYPE, Integer.TYPE);

		Object result = new StaticMethodInvokingFunction(divide, 10, 5).call(context);
		assert result != null;
		assert result instanceof Integer;
		assert ((Integer) result).intValue() == 2;
	}
}