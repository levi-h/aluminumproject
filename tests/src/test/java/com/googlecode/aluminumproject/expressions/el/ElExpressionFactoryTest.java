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
import com.googlecode.aluminumproject.configuration.DefaultConfiguration;
import com.googlecode.aluminumproject.configuration.TestConfiguration;
import com.googlecode.aluminumproject.context.DefaultContext;
import com.googlecode.aluminumproject.expressions.ExpressionException;
import com.googlecode.aluminumproject.expressions.ExpressionFactory;
import com.googlecode.aluminumproject.expressions.ExpressionOccurrence;

import java.util.List;

import org.testng.annotations.Test;

@SuppressWarnings("all")
@Test(groups = {"expressions", "expressions-el", "fast"})
public class ElExpressionFactoryTest {
	public void expressionStartingWithDollarSignShouldBeRecognised() {
		List<ExpressionOccurrence> occurrences = createExpressionFactory().findExpressions("${a}");
		assert occurrences != null;
		assert occurrences.size() == 1;
		assert occurrences.contains(new ExpressionOccurrence(0, 4));
	}

	public void expressionStartingWithPoundSignShouldBeRecognised() {
		List<ExpressionOccurrence> occurrences = createExpressionFactory().findExpressions("#{a}");
		assert occurrences != null;
		assert occurrences.size() == 1;
		assert occurrences.contains(new ExpressionOccurrence(0, 4));
	}

	@Test(dependsOnMethods = {
		"expressionStartingWithDollarSignShouldBeRecognised",
		"expressionStartingWithPoundSignShouldBeRecognised"
	})
	public void seriesOfExpressionsShouldBeRecognised() {
		List<ExpressionOccurrence> occurrences = createExpressionFactory().findExpressions("${a} #{b} ${c}");
		assert occurrences != null;
		assert occurrences.size() == 3;
		assert occurrences.contains(new ExpressionOccurrence(0, 4));
		assert occurrences.contains(new ExpressionOccurrence(5, 9));
		assert occurrences.contains(new ExpressionOccurrence(10, 14));
	}

	public void expressionContainingQuotedEndingCharacterShouldBeRecognised() {
		List<ExpressionOccurrence> occurrences = createExpressionFactory().findExpressions("${'}'}");
		assert occurrences != null;
		assert occurrences.size() == 1;
		assert occurrences.contains(new ExpressionOccurrence(0, 6));
	}

	public void incompleteExpressionShouldNotBeRecognised() {
		List<ExpressionOccurrence> occurrences = createExpressionFactory().findExpressions("${a");
		assert occurrences != null;
		assert occurrences.isEmpty();
	}

	public void escapedExpressionShouldNotBeRecognised() {
		List<ExpressionOccurrence> occurrences = createExpressionFactory().findExpressions("\\${a}");
		assert occurrences != null;
		assert occurrences.isEmpty();
	}

	@Test(dependsOnMethods = "expressionStartingWithDollarSignShouldBeRecognised")
	public void recognisedExpressionShouldBeCreatable() {
		assert createExpressionFactory().create("${a}", new DefaultContext()) != null;
	}

	@Test(dependsOnMethods = "recognisedExpressionShouldBeCreatable", expectedExceptions = ExpressionException.class)
	public void expressionFactoryPropertiesShouldBeConfigurable() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(ElExpressionFactory.EXPRESSION_FACTORY_PROPERTIES, "javax.el.methodInvocations: false");

		createExpressionFactory(parameters).create("${''.isEmpty()}", new DefaultContext());
	}

	private ExpressionFactory createExpressionFactory() {
		return createExpressionFactory(new ConfigurationParameters());
	}

	private ExpressionFactory createExpressionFactory(ConfigurationParameters parameters) {
		ExpressionFactory expressionFactory = new ElExpressionFactory();
		expressionFactory.initialise(new TestConfiguration(parameters));
		return expressionFactory;
	}
}