/*
 * Copyright 2010-2012 Levi Hoogenberg
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
package com.googlecode.aluminumproject.templates;

import com.googlecode.aluminumproject.libraries.actions.Action;

/**
 * Describes an {@link Action action}.
 */
public class ActionDescriptor {
	private String libraryUrlAbbreviation;
	private String name;

	/**
	 * Creates an action descriptor.
	 *
	 * @param libraryUrlAbbreviation the URL abbreviation of the library that contains the action
	 * @param name the name of the action
	 */
	public ActionDescriptor(String libraryUrlAbbreviation, String name) {
		this.libraryUrlAbbreviation = libraryUrlAbbreviation;
		this.name = name;
	}

	/**
	 * Returns the library URL abbreviation of the action.
	 *
	 * @return the action's library URL abbreviation
	 */
	public String getLibraryUrlAbbreviation() {
		return libraryUrlAbbreviation;
	}

	/**
	 * Returns the name of the action.
	 *
	 * @return the action name
	 */
	public String getName() {
		return name;
	}
}