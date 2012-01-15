/*
 * Copyright 2009-2012 Levi Hoogenberg
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
package com.googlecode.aluminumproject.utilities.finders;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Find methods of a type, based on a filter. To find methods, supply a {@link MethodFilter method filter} and the type
 * of which methods should be found to the {@link #find(MethodFilter, Class) find method}.
 */
public class MethodFinder {
	private MethodFinder() {}

	/**
	 * The filter used by the {@link #find(MethodFilter, Class) find method}.
	 */
	public static interface MethodFilter {
		/**
		 * Determines whether a certain method is matched by this filter or not.
		 *
		 * @param method the method to possibly include
		 * @return {@code true} if this filter accepts the method, {@code false} otherwise
		 */
		boolean accepts(Method method);
	}

	/**
	 * Finds methods on a type that match a certain filter. All of the methods that can be invoked on instances of the
	 * type are offered to the filter (so including static, native, non-public, and supertype methods).
	 *
	 * @param filter the filter to apply to the found methods
	 * @param type the type of which the methods should be found
	 * @return all of the methods that can be invoked on {@code type} instances
	 */
	public static List<Method> find(MethodFilter filter, Class<?> type) {
		if (type == null) {
			throw new IllegalArgumentException("please supply a type");
		}

		List<Method> methods = new LinkedList<Method>();
		addMethods(filter, methods, type);
		return methods;
	}

	private static void addMethods(MethodFilter filter, List<Method> methods, Class<?> type) {
		if (type != null) {
			if (type.isInterface()) {
				for (Class<?> superinterface: type.getInterfaces()) {
					addMethods(filter, methods, superinterface);
				}
			} else {
				addMethods(filter, methods, type.getSuperclass());
			}

			for (Method method: type.getDeclaredMethods()) {
				if (filter.accepts(method) && !isAlreadyPresent(methods, method)) {
					methods.add(method);
				}
			}
		}
	}

	private static boolean isAlreadyPresent(List<Method> methods, Method method) {
		boolean alreadyPresent = false;

		Iterator<Method> it = methods.iterator();

		while (it.hasNext() && !alreadyPresent) {
			Method existingMethod = it.next();

			alreadyPresent = method.getName().equals(existingMethod.getName())
				&& Arrays.deepEquals(method.getParameterTypes(), existingMethod.getParameterTypes());
		}

		return alreadyPresent;
	}
}