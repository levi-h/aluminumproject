/*
 * Copyright 2012 Aluminum project
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

import com.googlecode.aluminumproject.annotations.Ignored;
import com.googlecode.aluminumproject.utilities.Logger;

/**
 * Simplifies implementing the {@link LibraryElementCreation library element creation interface}.
 *
 * @param <T> the type of the factory that produces the abstract library element creations
 */
public abstract class AbstractLibraryElementCreation<T extends LibraryElement> implements LibraryElementCreation<T> {
	private @Ignored T factory;

	/** The logger to use. */
	protected final Logger logger;

	/**
	 * Creates an abstract library element creation.
	 */
	protected AbstractLibraryElementCreation() {
		logger = Logger.get(getClass());
	}

	public T getFactory() {
		return factory;
	}

	public void setFactory(T factory) {
		this.factory = factory;
	}
}