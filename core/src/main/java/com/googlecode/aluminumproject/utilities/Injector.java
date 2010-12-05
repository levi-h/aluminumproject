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

import com.googlecode.aluminumproject.Logger;
import com.googlecode.aluminumproject.annotations.Injected;
import com.googlecode.aluminumproject.utilities.finders.FieldFinder;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Injects values into fields that are annotated with {@link Injected &#64;Injected}. Which values are injected by an
 * injector depends on the {@link ValueProvider value providers} that are {@link #addValueProvider(ValueProvider) added}
 * to it.
 * <p>
 * An injector is not required to inject all of an object's annotated fields; this allows multiple injectors to be used
 * for the same injectable object.
 *
 * @author levi_h
 */
public class Injector {
	private List<ValueProvider> valueProviders;

	private final Logger logger;

	/**
	 * Creates an injector.
	 */
	public Injector() {
		valueProviders = new LinkedList<ValueProvider>();

		logger = Logger.get(getClass());
	}

	/**
	 * Adds a value provider to this injector.
	 *
	 * @param valueProvider the value provider to add
	 */
	public void addValueProvider(ValueProvider valueProvider) {
		valueProviders.add(valueProvider);
	}

	/**
	 * Loops through an object's non-static fields and injects a value into each one that is annotated with {@link
	 * Injected &#64;Injected} and for which a value provider exists.
	 *
	 * @param injectable the object to fill
	 * @throws UtilityException when something goes wrong while injecting the values
	 */
	public void inject(Object injectable) throws UtilityException {
		List<Field> fields = FieldFinder.find(new FieldFinder.FieldFilter() {
			public boolean accepts(Field field) {
				return !Modifier.isStatic(field.getModifiers()) && field.isAnnotationPresent(Injected.class);
			}
		}, injectable.getClass());

		for (Field field: fields) {
			ValueProvider valueProvider = findValueProvider(field);

			if (valueProvider == null) {
				logger.debug("no value can be found for ", field, " of ", injectable);
			} else {
				Object value = valueProvider.getValue(field);

				logger.debug("injecting ", value, " into ", field, " of ", injectable);

				ReflectionUtilities.setFieldValue(injectable, field.getName(), value);
			}
		}
	}

	private ValueProvider findValueProvider(Field field) {
		Iterator<ValueProvider> it = valueProviders.iterator();
		ValueProvider valueProvider = null;

		while (it.hasNext() && (valueProvider == null)) {
			valueProvider = it.next();

			if (!valueProvider.hasValue(field)) {
				valueProvider = null;
			}
		}

		return valueProvider;
	}

	/**
	 * Provides a value for certain fields.
	 *
	 * @author levi_h
	 */
	public static interface ValueProvider {
		/**
		 * Determines whether this value provider can provide a value for a certain field.
		 *
		 * @param field the field to check
		 * @return {@code true} if this value provider can provide a value for the given field, {@code false} otherwise
		 */
		boolean hasValue(Field field);

		/**
		 * Provides a value for a field. This method should only be invoked with fields for which this value provider
		 * {@link #hasValue(Field) has a value}.
		 *
		 * @param field the field for which a value should be provided
		 * @return the value for the field
		 * @throws UtilityException when no value can be provided for the given field
		 */
		Object getValue(Field field) throws UtilityException;
	}

	/**
	 * Provides a constant value if it can be assigned to a field.
	 *
	 * @author levi_h
	 */
	public static class ClassBasedValueProvider implements ValueProvider {
		private Object value;

		/**
		 * Creates a class-based value provider.
		 *
		 * @param value the value to provide
		 */
		public ClassBasedValueProvider(Object value) {
			this.value = value;
		}

		public boolean hasValue(Field field) {
			return field.getType().isInstance(value);
		}

		public Object getValue(Field field) throws UtilityException {
			if (hasValue(field)) {
				return value;
			} else {
				throw new UtilityException("can't provide value",
					" for field ", field.getDeclaringClass().getSimpleName(), "#", field.getName(), ", since",
					" its type is not assignment-compatible with value type ", value.getClass().getSimpleName());
			}
		}
	}
}