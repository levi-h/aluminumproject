/*
 * Copyright 2010 Levi Hoogenberg
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
package com.googlecode.aluminumproject.utilities;

import com.googlecode.aluminumproject.configuration.ConfigurationParameters;
import com.googlecode.aluminumproject.configuration.TestConfiguration;
import com.googlecode.aluminumproject.expressions.ExpressionFactory;
import com.googlecode.aluminumproject.expressions.ExpressionOccurrence;
import com.googlecode.aluminumproject.expressions.TestExpressionFactory;
import com.googlecode.aluminumproject.expressions.el.ElExpressionFactory;
import com.googlecode.aluminumproject.libraries.actions.CompoundActionParameter;
import com.googlecode.aluminumproject.libraries.actions.ConstantActionParameter;
import com.googlecode.aluminumproject.libraries.actions.ExpressionActionParameter;

import java.util.Set;
import java.util.SortedMap;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("all")
@Test(groups = {"utilities", "fast"})
public class ParserUtilitiesTest {
	private TestConfiguration configuration;

	private ExpressionFactory elExpressionFactory;
	private ExpressionFactory testExpressionFactory;

	@BeforeMethod
	public void createExpressionFactories() {
		configuration = new TestConfiguration(new ConfigurationParameters());

		elExpressionFactory = new ElExpressionFactory();
		elExpressionFactory.initialise(configuration);
		configuration.addExpressionFactory(elExpressionFactory);

		testExpressionFactory = new TestExpressionFactory();
		testExpressionFactory.initialise(configuration);
		configuration.addExpressionFactory(testExpressionFactory);
	}

	public void nonExpressionShouldResultInConstantActionParameter() {
		assert ParserUtilities.createParameter("name", configuration) instanceof ConstantActionParameter;
	}

	public void expressionShouldResultInExpressionActionParameter() {
		assert ParserUtilities.createParameter("<<name>>", configuration) instanceof ExpressionActionParameter;
	}

	public void multipleExpressionsShouldResultInCompoundActionParameter() {
		assert ParserUtilities.createParameter("<<name>><<name>>", configuration) instanceof CompoundActionParameter;
	}

	public void textWithoutExpressionsShouldResultInSingleExpressionOccurrenceWithoutExpressionFactory() {
		SortedMap<ExpressionOccurrence, ExpressionFactory> expressionOccurrencesWithExpressionFactories =
			ParserUtilities.getExpressionOccurrences("name", configuration);
		assert expressionOccurrencesWithExpressionFactories != null;

		Set<ExpressionOccurrence> expressionOccurrences = expressionOccurrencesWithExpressionFactories.keySet();
		assert expressionOccurrences != null;
		assert expressionOccurrences.size() == 1;

		ExpressionOccurrence textOccurrence = new ExpressionOccurrence(0, 4);
		assert expressionOccurrences.contains(textOccurrence);
		assert expressionOccurrencesWithExpressionFactories.get(textOccurrence) == null;
	}

	public void textWithExpressionsShouldResultInExpressionOccurrencesWithExpressionFactories() {
		SortedMap<ExpressionOccurrence, ExpressionFactory> expressionOccurrencesWithExpressionFactories =
			ParserUtilities.getExpressionOccurrences("<<name>> = ${name}", configuration);
		assert expressionOccurrencesWithExpressionFactories != null;

		Set<ExpressionOccurrence> expressionOccurrences = expressionOccurrencesWithExpressionFactories.keySet();
		assert expressionOccurrences != null;
		assert expressionOccurrences.size() == 3;

		ExpressionOccurrence testOccurrence = new ExpressionOccurrence(0, 8);
		assert expressionOccurrences.contains(testOccurrence);
		assert expressionOccurrencesWithExpressionFactories.get(testOccurrence) instanceof TestExpressionFactory;

		ExpressionOccurrence textOccurrence = new ExpressionOccurrence(8, 11);
		assert expressionOccurrences.contains(textOccurrence);
		assert expressionOccurrencesWithExpressionFactories.get(textOccurrence) == null;

		ExpressionOccurrence elOccurrence = new ExpressionOccurrence(11, 18);
		assert expressionOccurrences.contains(elOccurrence);
		assert expressionOccurrencesWithExpressionFactories.get(elOccurrence) instanceof ElExpressionFactory;
	}

	@Test(expectedExceptions = UtilityException.class)
	public void textWithOverlappingExpressionsShouldCauseException() {
		ParserUtilities.getExpressionOccurrences("${'<<text>>'}", configuration);
	}
}