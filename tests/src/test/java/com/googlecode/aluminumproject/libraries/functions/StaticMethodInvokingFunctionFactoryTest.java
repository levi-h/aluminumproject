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
package com.googlecode.aluminumproject.libraries.functions;

import com.googlecode.aluminumproject.configuration.ConfigurationParameters;
import com.googlecode.aluminumproject.configuration.TestConfiguration;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.DefaultContext;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("all")
@Test(groups = {"core", "fast"})
public class StaticMethodInvokingFunctionFactoryTest {
	private FunctionFactory unannotatedFunctionFactory;
	private FunctionFactory annotatedFunctionFactory;

	@BeforeMethod
	public void createFunctionFactories() throws NoSuchMethodException {
		TestConfiguration configuration = new TestConfiguration(new ConfigurationParameters());

		unannotatedFunctionFactory =
			new StaticMethodInvokingFunctionFactory(TestFunctions.class.getMethod("max", Integer.TYPE, Integer.TYPE));
		unannotatedFunctionFactory.initialise(configuration);

		annotatedFunctionFactory =
			new StaticMethodInvokingFunctionFactory(TestFunctions.class.getMethod("min", Integer.TYPE, Integer.TYPE));
		annotatedFunctionFactory.initialise(configuration);
	}

	public void unannotatedMethodShouldResultInMethodNameAsFunctionName() {
		FunctionInformation information = unannotatedFunctionFactory.getInformation();
		assert information != null;

		String functionName = information.getName();
		assert functionName != null;
		assert functionName.equals("max");
	}

	public void annotatedMethodShouldResultInAnnotationAttributeAsFunctionName() {
		FunctionInformation information = annotatedFunctionFactory.getInformation();
		assert information != null;

		String functionName = information.getName();
		assert functionName != null;
		assert functionName.equals("minimumValue");
	}

	@Test(dependsOnMethods = "unannotatedMethodShouldResultInMethodNameAsFunctionName")
	public void unannotatedMethodParametersShouldResultInUnnamedFunctionArguments() {
		FunctionInformation information = unannotatedFunctionFactory.getInformation();
		assert information != null;

		List<FunctionArgumentInformation> argumentInformation = information.getArgumentInformation();
		assert argumentInformation != null;
		assert argumentInformation.size() == 2;

		FunctionArgumentInformation firstArgumentInformation = argumentInformation.get(0);
		assert firstArgumentInformation != null;
		assert firstArgumentInformation.getName() == null;

		FunctionArgumentInformation secondArgumentInformation = argumentInformation.get(1);
		assert secondArgumentInformation != null;
		assert secondArgumentInformation.getName() == null;
	}

	@Test(dependsOnMethods = "annotatedMethodShouldResultInAnnotationAttributeAsFunctionName")
	public void annotatedMethodParametersShouldResultInAnnotationAttributesAsArgumentNames() {
		FunctionInformation information = annotatedFunctionFactory.getInformation();
		assert information != null;

		List<FunctionArgumentInformation> argumentInformation = information.getArgumentInformation();
		assert argumentInformation != null;
		assert argumentInformation.size() == 2;

		FunctionArgumentInformation firstArgumentInformation = argumentInformation.get(0);
		assert firstArgumentInformation != null;
		assert firstArgumentInformation.getName() != null;
		assert firstArgumentInformation.getName().equals("firstValue");

		FunctionArgumentInformation secondArgumentInformation = argumentInformation.get(1);
		assert secondArgumentInformation != null;
		assert secondArgumentInformation.getName() != null;
		assert secondArgumentInformation.getName().equals("secondValue");
	}

	public void returnTypeShouldBecomeResultType() {
		assert annotatedFunctionFactory.getInformation().getResultType() == Integer.TYPE;
		assert unannotatedFunctionFactory.getInformation().getResultType() == Integer.TYPE;
	}

	@Test(expectedExceptions = FunctionException.class)
	public void creatingFunctionWithTooFewArgumentsShouldCauseException() {
		unannotatedFunctionFactory.create(Collections.<FunctionArgument>emptyList(), new DefaultContext());
	}

	@Test(expectedExceptions = FunctionException.class)
	public void creatingFunctionWithTooManyArgumentsShouldCauseException() {
		annotatedFunctionFactory.create(Arrays.asList(new FunctionArgument() {
			public Object getValue(Type type, Context context) {
				return 1;
			}
		}, new FunctionArgument() {
			public Object getValue(Type type, Context context) {
				return 2;
			}
		}, new FunctionArgument() {
			public Object getValue(Type type, Context context) {
				return 3;
			}
		}), new DefaultContext());
	}

	public void usingCorrectNumberOfArgumentsShouldCreateFunction() {
		List<FunctionArgument> functionArguments = Arrays.asList(new FunctionArgument() {
			public Object getValue(Type type, Context context) throws FunctionException {
				return 1;
			}
		}, new FunctionArgument() {
			public Object getValue(Type type, Context context) throws FunctionException {
				return 2;
			}
		});

		DefaultContext context = new DefaultContext();

		Function min = annotatedFunctionFactory.create(functionArguments, context);
		assert min != null;
		assert min instanceof StaticMethodInvokingFunction;
	}
}