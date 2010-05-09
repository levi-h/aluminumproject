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

import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.converters.ConverterRegistry;
import com.googlecode.aluminumproject.expressions.ExpressionFactory;
import com.googlecode.aluminumproject.expressions.ExpressionOccurrence;
import com.googlecode.aluminumproject.libraries.actions.ActionParameter;
import com.googlecode.aluminumproject.libraries.actions.ConstantActionParameter;
import com.googlecode.aluminumproject.libraries.actions.ExpressionActionParameter;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Contains a number of utility methods that can be used by parsers.
 *
 * @author levi_h
 */
public class ParserUtilities {
	private ParserUtilities() {}

	/**
	 * Creates either an {@link ExpressionActionParameter expression} or a {@link ConstantActionParameter constant}
	 * action parameter from a textual value, depending on whether the text consists of a single expression or not.
	 *
	 * @param value the textual parameter value
	 * @param configuration the configuration that contains all expression factories
	 * @return the new action parameter
	 */
	public static ActionParameter createParameter(String value, Configuration configuration) {
		ActionParameter parameter;

		List<ExpressionFactory> expressionFactories = new ArrayList<ExpressionFactory>();

		for (ExpressionFactory expressionFactory: configuration.getExpressionFactories()) {
			List<ExpressionOccurrence> occurrences = expressionFactory.findExpressions(value);

			if (occurrences.size() == 1) {
				ExpressionOccurrence occurrence = occurrences.get(0);

				if ((occurrence.getBeginIndex() == 0) && (occurrence.getEndIndex() == value.length())) {
					expressionFactories.add(expressionFactory);
				}
			}
		}

		ConverterRegistry converterRegistry = configuration.getConverterRegistry();

		if (expressionFactories.size() == 1) {
			parameter = new ExpressionActionParameter(expressionFactories.get(0), value, converterRegistry);
		} else {
			parameter = new ConstantActionParameter(value, converterRegistry);
		}

		return parameter;
	}

	/**
	 * Analyses a text and splits it in into contiguous blocks of characters with optional expression factories attached
	 * to them. The occurrences that are not connected with an expression factory are not expressions, but should be
	 * treated as text.
	 *
	 * @param text the text to find occurrences of expressions in
	 * @param configuration the configuration that contains all expression factories
	 * @return a map that contains a number of blocks; each block is either text or an expression
	 * @throws UtilityException when the given text contains overlapping expressions
	 */
	public static SortedMap<ExpressionOccurrence, ExpressionFactory> getExpressionOccurrences(
			String text, Configuration configuration) throws UtilityException {
		SortedMap<ExpressionOccurrence, ExpressionFactory> occurrences =
			new TreeMap<ExpressionOccurrence, ExpressionFactory>();

		for (ExpressionFactory expressionFactory: configuration.getExpressionFactories()) {
			for (ExpressionOccurrence occurrence: expressionFactory.findExpressions(text)) {
				for (ExpressionOccurrence existingOccurrence: occurrences.keySet()) {
					if ((existingOccurrence.getBeginIndex() < occurrence.getEndIndex())
						&& (existingOccurrence.getEndIndex() > occurrence.getBeginIndex())) {
						throw new UtilityException("found overlapping expressions");
					}
				}

				occurrences.put(occurrence, expressionFactory);
			}
		}

		int beginIndex = 0;

		for (ExpressionOccurrence occurrence: new TreeSet<ExpressionOccurrence>(occurrences.keySet())) {
			addOccurrence(occurrences, beginIndex, occurrence.getBeginIndex());

			beginIndex = occurrence.getEndIndex();
		}

		addOccurrence(occurrences, beginIndex, text.length());

		return occurrences;
	}

	private static void addOccurrence(
			SortedMap<ExpressionOccurrence, ExpressionFactory> occurrences, int beginIndex, int endIndex) {
		if (endIndex > beginIndex) {
			occurrences.put(new ExpressionOccurrence(beginIndex, endIndex), null);
		}
	}
}