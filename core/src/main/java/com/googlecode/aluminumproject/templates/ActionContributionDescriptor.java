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
package com.googlecode.aluminumproject.templates;

import com.googlecode.aluminumproject.libraries.actions.ActionContribution;
import com.googlecode.aluminumproject.libraries.actions.ActionParameter;

/**
 * Describes an {@link ActionContribution action contribution}.
 *
 * @author levi_h
 */
public class ActionContributionDescriptor {
	private String libraryUrl;
	private String name;
	private ActionParameter parameter;

	/**
	 * Creates an action contribution descriptor.
	 *
	 * @param libraryUrl the URL of the action contribution's library
	 * @param name the name of the action contribution
	 * @param parameter the parameter of the action contribution
	 */
	public ActionContributionDescriptor(String libraryUrl, String name, ActionParameter parameter) {
		this.libraryUrl = libraryUrl;
		this.name = name;
		this.parameter = parameter;
	}

	/**
	 * Returns the library URL of the action contribution.
	 *
	 * @return the URL of the action contribution's library
	 */
	String getLibraryUrl() {
		return libraryUrl;
	}

	/**
	 * Returns the name of the action contribution.
	 *
	 * @return the action contribution's name
	 */
	String getName() {
		return name;
	}

	/**
	 * Returns the parameter of the action contribution.
	 *
	 * @return the action contribution's parameter
	 */
	public ActionParameter getParameter() {
		return parameter;
	}
}