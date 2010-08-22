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
import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.configuration.ConfigurationElement;
import com.googlecode.aluminumproject.libraries.Library;
import com.googlecode.aluminumproject.libraries.LibraryInformation;
import com.googlecode.aluminumproject.utilities.finders.FieldFinder;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.List;

/**
 * Contains utility methods that help when working with {@link Configuration configurations} and {@link
 * ConfigurationElement configuration elements}.
 *
 * @author levi_h
 */
public class ConfigurationUtilities {
	private ConfigurationUtilities() {}

	/**
	 * Finds a library in a configuration by its URL or returns {@code null} if the configuration does not contain a
	 * library with that URL. Both regular and the versioned library URLs are recognised.
	 *
	 * @param configuration the configuration to find the library in
	 * @param url the (possibly versioned) URL of the library to find
	 * @return the library with the given URL or {@code null} if the given configuration does not contain a library with
	 *         the requested URL
	 */
	public static Library findLibrary(Configuration configuration, String url) {
		Iterator<Library> itLibraries = configuration.getLibraries().iterator();

		Library library = null;

		while ((library == null) && itLibraries.hasNext()) {
			library = itLibraries.next();

			LibraryInformation libraryInformation = library.getInformation();

			if (!libraryInformation.getUrl().equals(url) && !libraryInformation.getVersionedUrl().equals(url)) {
				library = null;
			}
		}

		return library;
	}

	/**
	 * Injects suitable values into all of an object's fields that are annotated with {@link Injected &#64;Injected}.
	 *
	 * @param injectable the object whose fields should be injected
	 * @param valueProvider the object that determines which values should be injected into each field
	 * @throws UtilityException when the fields can't be injected
	 */
	public static void injectFields(Object injectable, InjectedValueProvider valueProvider) throws UtilityException {
		List<Field> fields = FieldFinder.find(new FieldFinder.FieldFilter() {
			public boolean accepts(Field field) {
				return !Modifier.isStatic(field.getModifiers()) && field.isAnnotationPresent(Injected.class);
			}
		}, injectable.getClass());

		for (Field field: fields) {
			Object valueToInject = valueProvider.getValueToInject(field);

			logger.debug("injecting ", valueToInject, " into ", field, " of ", injectable);

			if (!field.isAccessible()) {
				try {
					field.setAccessible(true);
				} catch (SecurityException exception) {
					throw new UtilityException("can't make field ",
						field.getDeclaringClass().getSimpleName(), "#", field.getName(), " accessible");
				}
			}

			try {
				field.set(injectable, valueToInject);
			} catch (IllegalArgumentException exception) {
				throw new UtilityException(exception, "can't inject ", field);
			} catch (IllegalAccessException exception) {
				throw new UtilityException(exception, "may not inject ", field);
			}
		}
	}

	/**
	 * Provides values for {@link #injectFields(Object, InjectedValueProvider) injected} fields.
	 *
	 * @author levi_h
	 */
	public static interface InjectedValueProvider {
		/**
		 * Determines which value should be injected into a certain field.
		 *
		 * @param field the injected field
		 * @return the value that should be injected into the field (may be {@code null})
		 * @throws UtilityException when no suitable value to inject can be found
		 */
		Object getValueToInject(Field field) throws UtilityException;
	}

	private final static Logger logger = Logger.get(ConfigurationUtilities.class);
}