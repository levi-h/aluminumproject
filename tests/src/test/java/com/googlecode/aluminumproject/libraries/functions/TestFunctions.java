/*
 * Copyright 2009-2012 Aluminum project
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

import com.googlecode.aluminumproject.annotations.FunctionClass;
import com.googlecode.aluminumproject.annotations.Ignored;
import com.googlecode.aluminumproject.annotations.Named;
import com.googlecode.aluminumproject.libraries.AbstractLibrary;

/**
 * Contains methods that can be used as {@link Function functions}.
 */
@FunctionClass
public class TestFunctions {
	private TestFunctions() {}

	/**
	 * Finds the maximum of two integer values.
	 *
	 * @param a the first value to compare
	 * @param b the second value to compare
	 * @return the highest of the two values
	 */
	public static int max(int a, int b) {
		return (a > b) ? a : b;
	}

	/**
	 * Finds the minimum of two integer values.
	 *
	 * @param a the first value to compare
	 * @param b the second value to compare
	 * @return the lowest of the two values
	 */
	@Named("minimumValue")
	public static int min(@Named("firstValue") int a, @Named("secondValue") int b) {
		return (a < b) ? a : b;
	}

	@SuppressWarnings("unused")
	private static void inaccessible() {}

	/**
	 * Divides a value by another value.
	 *
	 * @param value the value to divide
	 * @param divisor the value to divide by
	 * @return {@code value / divisor}
	 */
	public static int divide(int value, int divisor) {
		return value / divisor;
	}

	/**
	 * Will be ignored by {@link AbstractLibrary abstract libraries} when they are looking for {@link
	 * StaticMethodInvokingFunction static method invoking functions}.
	 */
	@Ignored
	public static void ignored() {}
}