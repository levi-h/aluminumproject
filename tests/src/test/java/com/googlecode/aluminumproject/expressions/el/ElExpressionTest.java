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
package com.googlecode.aluminumproject.expressions.el;

import com.googlecode.aluminumproject.configuration.ConfigurationParameters;
import com.googlecode.aluminumproject.configuration.TestConfiguration;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.DefaultContext;
import com.googlecode.aluminumproject.expressions.ExpressionException;
import com.googlecode.aluminumproject.expressions.ExpressionFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.ListResourceBundle;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("all")
@Test(groups = {"expressions", "expressions-el", "fast"})
public class ElExpressionTest {
	private ExpressionFactory expressionFactory;

	private Context context;

	@BeforeMethod
	public void createExpressionFactoryAndContext() {
		expressionFactory = new ElExpressionFactory();
		expressionFactory.initialise(new TestConfiguration(new ConfigurationParameters()));

		context = new DefaultContext();
		context.setVariable("number", 10);
		context.setVariable("array", new Integer[] {5, 10});
		context.setVariable("list", Arrays.asList(5, 10));
		context.setVariable("map", Collections.singletonMap("number", 10));
		context.setVariable("thread", new Thread() {
			{
				setPriority(10);
			}
		});
		context.setVariable("numbers", new ListResourceBundle() {
			protected Object[][] getContents() {
				return new Object[][] {
					new Object[] {"five", 5},
					new Object[] {"ten", 10}
				};
			}
		});
	}

	public void expressionFactoryShouldCreateElExpressions() {
		assert expressionFactory.create("${number}", context) instanceof ElExpression;
	}

	@Test(dependsOnMethods = "expressionFactoryShouldCreateElExpressions")
	public void expressionShouldSupportImplicitObjects() {
		Object dotResult = expressionFactory.create("${templateScope.number}", context).evaluate(context);
		assert dotResult instanceof Integer;
		assert ((Integer) dotResult).intValue() == 10;

		Object bracketResult = expressionFactory.create("${templateScope['number']}", context).evaluate(context);
		assert bracketResult instanceof Integer;
		assert ((Integer) bracketResult).intValue() == 10;
	}

	@Test(dependsOnMethods = "expressionFactoryShouldCreateElExpressions")
	public void expressionShouldSupportContextVariables() {
		Object result = expressionFactory.create("${number}", context).evaluate(context);
		assert result instanceof Integer;
		assert ((Integer) result).intValue() == 10;
	}

	@Test(dependsOnMethods = "expressionShouldSupportContextVariables")
	public void expressionShouldSupportArrays() {
		Object result = expressionFactory.create("${array[1]}", context).evaluate(context);
		assert result instanceof Integer;
		assert ((Integer) result).intValue() == 10;

	}

	@Test(dependsOnMethods = "expressionShouldSupportContextVariables")
	public void expressionShouldSupportLists() {
		Object result = expressionFactory.create("${list[1]}", context).evaluate(context);
		assert result instanceof Integer;
		assert ((Integer) result).intValue() == 10;

	}

	@Test(dependsOnMethods = "expressionShouldSupportContextVariables")
	public void expressionShouldSupportMaps() {
		Object dotResult = expressionFactory.create("${map.number}", context).evaluate(context);
		assert dotResult instanceof Integer;
		assert ((Integer) dotResult).intValue() == 10;

		Object bracketResult = expressionFactory.create("${map['number']}", context).evaluate(context);
		assert bracketResult instanceof Integer;
		assert ((Integer) bracketResult).intValue() == 10;
	}

	@Test(dependsOnMethods = "expressionShouldSupportContextVariables")
	public void expressionShouldSupportResourceBundles() {
		Object dotResult = expressionFactory.create("${numbers.ten}", context).evaluate(context);
		assert dotResult instanceof Integer;
		assert ((Integer) dotResult).intValue() == 10;

		Object bracketResult = expressionFactory.create("${numbers['ten']}", context).evaluate(context);
		assert bracketResult instanceof Integer;
		assert ((Integer) bracketResult).intValue() == 10;
	}

	@Test(dependsOnMethods = "expressionShouldSupportContextVariables")
	public void expressionShouldSupportBeans() {
		Object dotResult = expressionFactory.create("${thread.priority}", context).evaluate(context);
		assert dotResult instanceof Integer;
		assert ((Integer) dotResult).intValue() == 10;

		Object bracketResult = expressionFactory.create("${thread['priority']}", context).evaluate(context);
		assert bracketResult instanceof Integer;
		assert ((Integer) bracketResult).intValue() == 10;
	}

	@Test(expectedExceptions = ExpressionException.class,
		dependsOnMethods = "expressionFactoryShouldCreateElExpressions")
	public void evaluatingInvalidExpressionShouldCauseException() {
		expressionFactory.create("${invalid}", context).evaluate(context);
	}
}