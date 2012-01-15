/*
 * Copyright 2010-2012 Levi Hoogenberg
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
import com.googlecode.aluminumproject.libraries.AbstractLibraryElement;

import java.util.Collections;
import java.util.List;

/**
 * A dynamic function that parses its name (which should have the form {@code addNandN[andN...]}, where <em>N</em> is a
 * number) and adds the terms that were found.
 */
public class Add implements Function {
	private int[] terms;

	private Add(int[] terms) {
		this.terms = terms;
	}

	public Integer call(Context context) {
		int sum = 0;

		for (int term: terms) {
			sum += term;
		}

		return sum;
	}

	/**
	 * Creates {@link Add add} functions.
	 */
	public static class Factory extends AbstractLibraryElement implements FunctionFactory {
		private String name;

		private FunctionInformation information;

		/**
		 * Creates a factory for the <em>add</em> function.
		 *
		 * @param name the name of the addition function
		 */
		public Factory(String name) {
			this.name = name;

			information = new FunctionInformation(
				name, Integer.TYPE, Collections.<FunctionArgumentInformation>emptyList());
		}

		public FunctionInformation getInformation() {
			return information;
		}

		public Function create(List<FunctionArgument> arguments, Context context) throws AluminumException {
			String[] termsAsText = name.substring("add".length()).split("and");
			int[] terms = new int[termsAsText.length];

			for (int i = 0; i < termsAsText.length; i++) {
				terms[i] = Integer.parseInt(termsAsText[i]);
			}

			return new Add(terms);
		}
	}
}