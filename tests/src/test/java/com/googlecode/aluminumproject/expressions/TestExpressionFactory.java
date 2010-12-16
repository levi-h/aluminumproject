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
package com.googlecode.aluminumproject.expressions;

import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.context.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * An expression factory that can be used in tests.
 *
 * @author levi_h
 */
public class TestExpressionFactory implements ExpressionFactory {
	private Configuration configuration;

	/**
	 * Creates a test expression factory.
	 */
	public TestExpressionFactory() {}

	public void initialise(Configuration configuration) {
		this.configuration = configuration;
	}

	public void disable() {
		configuration = null;
	}

	/**
	 * Returns the configuration that this expression factory was initialised with.
	 *
	 * @return this expression factory's configuration
	 */
	public Configuration getConfiguration() {
		return configuration;
	}

	public List<ExpressionOccurrence> findExpressions(String text) {
		List<ExpressionOccurrence> occurrences = new ArrayList<ExpressionOccurrence>();

		int b = 0;

		while ((b = text.indexOf("<<", b)) >= 0) {
			int e = text.indexOf(">>", b);

			if (e > b) {
				occurrences.add(new ExpressionOccurrence(b, b = e + 2));
			} else {
				b += 2;
			}
		}

		return occurrences;
	}

	public TestExpression create(String value, Context context) {
		return new TestExpression(value);
	}
}