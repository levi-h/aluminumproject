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
package com.googlecode.aluminumproject.libraries;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.annotations.Injected;
import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.utilities.Injector;
import com.googlecode.aluminumproject.utilities.Logger;

/**
 * Reduces the effort it takes to implement subinterfaces of {@link LibraryElement the library element interface}.
 */
public abstract class AbstractLibraryElement implements LibraryElement {
	private Configuration configuration;

	private Injector injector;

	private Library library;

	/** The logger to use. */
	protected final Logger logger;

	/**
	 * Creates an abstract library element.
	 */
	public AbstractLibraryElement() {
		logger = Logger.get(getClass());
	}

	public void initialise(Configuration configuration) throws AluminumException {
		this.configuration = configuration;

		injector = new Injector();
		injector.addValueProvider(new Injector.ClassBasedValueProvider(configuration));
		injector.addValueProvider(new Injector.ClassBasedValueProvider(this));
		addValueProviders(injector);
	}

	/**
	 * Adds value providers to the injector that is used by the {@link #injectFields(Object) injectFields method}. The
	 * method does nothing by default; subclasses are encouraged to override it if they have value providers to add.
	 *
	 * @param injector the injector to add value providers to
	 */
	protected void addValueProviders(Injector injector) {}

	public void disable() {}

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
	 * Subclasses may extend the list of supported types by overriding the {@link #addValueProviders(Injector)
	 * addValueProviders method}.
	 *
	 * @param injectable the object whose fields should be injected
	 * @throws AluminumException when the object's fields can't be injected
	 */
	protected void injectFields(Object injectable) throws AluminumException {
		injector.inject(injectable);
	}
}