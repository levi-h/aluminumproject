/*
 * Copyright 2012 Levi Hoogenberg
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
package com.googlecode.aluminumproject.finders;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.configuration.ConfigurationElement;

import java.util.List;

/**
 * Finds types in the class path, based on a filter.
 */
public interface TypeFinder extends ConfigurationElement {
	/**
	 * The filter used by the {@link #find(TypeFilter, String...) find method}.
	 */
	interface TypeFilter {
		/**
		 * Determines whether a certain type is accepted by this filter.
		 *
		 * @param type the type to possibly include
		 * @return {@code true} if this filter matches the given type, {@code false} otherwise
		 */
		boolean accepts(Class<?> type);
	}

	/**
	 * Finds types matching a certain filter in a number of packages.
	 *
	 * @param filter the filter to apply
	 * @param packageNames the names of the packages to include in the search (subpackages will be included)
	 * @return a list with all types in the given packages that match the given filter (may be empty)
	 * @throws IllegalArgumentException when no package names are supplied
	 * @throws AluminumException when something goes wrong while trying to find matching types
	 */
	List<Class<?>> find(TypeFilter filter, String... packageNames) throws AluminumException;
}