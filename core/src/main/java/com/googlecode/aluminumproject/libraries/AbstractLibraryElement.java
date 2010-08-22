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
package com.googlecode.aluminumproject.libraries;

import com.googlecode.aluminumproject.Logger;
import com.googlecode.aluminumproject.annotations.Injected;
import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.configuration.ConfigurationParameters;
import com.googlecode.aluminumproject.utilities.ConfigurationUtilities;
import com.googlecode.aluminumproject.utilities.UtilityException;

import java.lang.reflect.Field;

/**
 * Reduces the effort it takes to implement subinterfaces of {@link LibraryElement the library element interface}.
 *
 * @author levi_h
 */
public abstract class AbstractLibraryElement implements LibraryElement {
	private Configuration configuration;

	private Library library;

	/** The logger to use. */
	protected final Logger logger;

	/**
	 * Creates an abstract library element.
	 */
	public AbstractLibraryElement() {
		logger = Logger.get(getClass());
	}

	public void initialise(Configuration configuration, ConfigurationParameters parameters) {
		this.configuration = configuration;
	}

	/**
	 * Returns the configuration that this library element was initialised with.
	 *
	 * @return the current configuration
	 */
	protected Configuration getConfiguration() {
		return configuration;
	}

	public Library getLibrary() {
		return library;
	}

	public void setLibrary(Library library) {
		this.library = library;
	}

	/**
	 * Injects all of an object's fields that are annotated with {@link Injected &#64;Injected}. The following field
	 * types are supported:
	 * <ul>
	 * <li>The current {@link Configuration configuration};
	 * <li>This library element's type.
	 * </ul>
	 * If an annotated field with a different type is encountered, this method will throw an exception.
	 *
	 * @param injectable the object whose fields should be injected
	 * @throws LibraryException when the object's fields can't be injected
	 */
	protected void injectFields(Object injectable) throws LibraryException {
		try {
			ConfigurationUtilities.injectFields(injectable, new ConfigurationUtilities.InjectedValueProvider() {
				public Object getValueToInject(Field field) throws UtilityException {
					try {
						return AbstractLibraryElement.this.getValueToInject(field);
					} catch (LibraryException exception) {
						throw new UtilityException(exception, "can't determine value to inject");
					}
				}
			});
		} catch (UtilityException exception) {
			Throwable cause = exception.getCause();

			throw (cause instanceof LibraryException)
				? (LibraryException) cause : new LibraryException(exception, "can't inject fields");
		}
	}

	/**
	 * Determines which value should be injected into a certain field. This diagnosis can be based on the field's type,
	 * one of its annotations, or some other characteristic of the field.
	 *
	 * @param field the field that is declared to be {@link Injected injected}
	 * @return the value that should be injected into the field (may be {@code null})
	 * @throws LibraryException when no value can be found to inject
	 */
	protected Object getValueToInject(Field field) throws LibraryException {
		Object valueToInject;

		if (field.getType() == Configuration.class) {
			valueToInject = configuration;
		} else if (getClass().isAssignableFrom(field.getType())) {
			valueToInject = this;
		} else {
			throw new LibraryException("can't determine value to inject into field '", field.getName(), "'",
				" that is annotated with @", Injected.class.getSimpleName());
		}

		return valueToInject;
	}
}