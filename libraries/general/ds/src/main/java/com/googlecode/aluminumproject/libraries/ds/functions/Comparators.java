/*
 * Copyright 2011-2012 Levi Hoogenberg
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
package com.googlecode.aluminumproject.libraries.ds.functions;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.annotations.FunctionClass;
import com.googlecode.aluminumproject.annotations.Named;
import com.googlecode.aluminumproject.utilities.ReflectionUtilities;
import com.googlecode.aluminumproject.utilities.Utilities;

import java.util.Collections;
import java.util.Comparator;

@FunctionClass
@SuppressWarnings("javadoc")
public class Comparators {
	public static Comparator<Object> naturalOrder() {
		return new NaturalOrderComparator();
	}

	private static class NaturalOrderComparator implements Comparator<Object> {
		public int compare(Object a, Object b) throws AluminumException {
			Comparable<Object> comparableA;
			Comparable<Object> comparableB;

			try {
				comparableA = Utilities.typed(a);
				comparableB = Utilities.typed(b);
			} catch (ClassCastException exception) {
				throw new AluminumException(exception, "can't compare objects that do not implement Comparable");
			}

			return Comparators.compare(comparableA, comparableB);
		}

		@Override
		public String toString() {
			return "by natural order";
		}
	}

	public static Comparator<Object> byProperty(@Named("propertyName") String propertyName) {
		return new PropertyComparator(propertyName);
	}

	private static class PropertyComparator implements Comparator<Object> {
		private String propertyName;

		public PropertyComparator(String propertyName) {
			this.propertyName = propertyName;
		}

		public int compare(Object a, Object b) {
			Comparable<Object> propertyA =
				Utilities.typed(ReflectionUtilities.getNestedProperty(a, Comparable.class, propertyName));
			Comparable<Object> propertyB =
				Utilities.typed(ReflectionUtilities.getNestedProperty(b, Comparable.class, propertyName));

			return Comparators.compare(propertyA, propertyB);
		}

		@Override
		public String toString() {
			return String.format("by property '%s'", propertyName);
		}
	}

	public static Comparator<Object> reverse(@Named("comparator") Comparator<Object> comparator) {
		return Collections.reverseOrder(comparator);
	}

	private static int compare(Comparable<Object> a, Comparable<Object> b) throws AluminumException {
		try {
			return (a == null) ? (b == null) ? 0 : -1 : (b == null) ? 1 : a.compareTo(b);
		} catch (ClassCastException exception) {
			throw new AluminumException(exception, "can't compare ", a, " and ", b);
		}
	}
}