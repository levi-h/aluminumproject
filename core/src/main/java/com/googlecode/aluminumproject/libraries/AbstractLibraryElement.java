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

/**
 * Reduces the effort it takes to implement subinterfaces of {@link LibraryElement the library element interface}.
 *
 * @author levi_h
 */
public abstract class AbstractLibraryElement implements LibraryElement {
	private Library library;

	/** The logger to use. */
	protected final Logger logger;

	/**
	 * Creates an abstract library element.
	 */
	public AbstractLibraryElement() {
		logger = Logger.get(getClass());
	}

	public Library getLibrary() {
		return library;
	}

	public void setLibrary(Library library) {
		this.library = library;
	}
}