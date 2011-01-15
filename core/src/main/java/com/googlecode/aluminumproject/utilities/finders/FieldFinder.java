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
package com.googlecode.aluminumproject.utilities.finders;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

/**
 * Finds fields of a type, based on a filter. Fields can be found by creating a {@link FieldFilter filter} and passing
 * it together with a type in which should be looked for fields to {@link #find(FieldFilter, Class) the find method}.
 *
 * @author levi_h
 */
public class FieldFinder {
	private FieldFinder() {}

	/**
	 * The filter that is used by the {@link #find(FieldFilter, Class) find method}.
	 *
	 * @author levi_h
	 */
	public static interface FieldFilter {
		/**
		 * Determines whether this filter accepts a certain field.
		 *
		 * @param field the field to examine
		 * @return {@code true} if the given field is accepted by this filter or {@code false} if this filter denies it
		 */
		boolean accepts(Field field);
	}

	/**
	 * Finds all of a certain type's fields that match a particular filter. The filter is offered fields of both the
	 * type and all of its supertypes.
	 *
	 * @param filter the filter to apply to the fields that can be found on the type and its supertypes
	 * @param type the type to scan for fields
	 * @return all of the fields that were found on the given type and were accepted by the given filter
	 */
	public static List<Field> find(FieldFilter filter, Class<?> type) {
		if (type == null) {
			throw new IllegalArgumentException("please supply a type");
		}

		List<Field> fields = new LinkedList<Field>();
		addFields(type, fields, filter);
		return fields;
	}

	private static void addFields(Class<?> type, List<Field> fields, FieldFilter filter) {
		for (Class<?> implementedInterface: type.getInterfaces()) {
			addFields(implementedInterface, fields, filter);
		}

		Class<?> supertype = type.getSuperclass();

		if (supertype != null) {
			addFields(supertype, fields, filter);
		}

		for (Field field: type.getDeclaredFields()) {
			if (filter.accepts(field)) {
				fields.add(field);
			}
		}
	}
}