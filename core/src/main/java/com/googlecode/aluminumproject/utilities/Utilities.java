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
package com.googlecode.aluminumproject.utilities;

/**
 * Contains general utility methods.
 */
public class Utilities {
	private Utilities() {}

	/**
	 * Determines whether two objects are equal. This is the case when either both objects are {@code null} or when the
	 * {@link Object#equals(Object) equals method} of the first object returns {@code true} for the second object.
	 *
	 * @param oneObject the first object to compare
	 * @param otherObject the second object to compare
	 * @return {@code true} if both objects are equal, {@code false} if they aren't
	 */
	public static boolean equals(Object oneObject, Object otherObject) {
		return (oneObject == null) ? (otherObject == null) : oneObject.equals(otherObject);
	}

	/**
	 * Casts an object into a certain generic type. Note that the cast is <i>unchecked</i>, which means that it can fail
	 * at runtime.
	 *
	 * @param <T> the desired type
	 * @param object the object to cast
	 * @return {@code object} with type {@code T}
	 */
	@SuppressWarnings("unchecked")
	public static <T> T typed(Object object) {
		return (T) object;
	}

	/**
	 * Returns a value or, if it's {@code null}, a default value of the same type.
	 *
	 * @param <T> the type of the values
	 * @param value the value to use
	 * @param defaultValue the default value to use
	 * @return {@code value} or {@code defaultValue} if {@code value} is {@code null}
	 * @throws IllegalArgumentException when the default value is {@code null}
	 */
	public static <T> T withDefault(T value, T defaultValue) throws IllegalArgumentException {
		if (defaultValue == null) {
			throw new IllegalArgumentException("the default value should not be null");
		}

		return (value == null) ? defaultValue : value;
	}
}