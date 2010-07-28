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
package com.googlecode.aluminumproject.libraries.actions;

/**
 * Provides information about the parameter of an {@link Action action}.
 *
 * @author levi_h
 */
public class ActionParameterInformation {
	private String name;
	private Class<?> type;
	private boolean required;

	/**
	 * Creates action parameter information.
	 *
	 * @param name the name of the parameter
	 * @param type the type of the parameter
	 * @param required whether the parameter is required or not
	 */
	public ActionParameterInformation(String name, Class<?> type, boolean required) {
		this.name = name;
		this.type = type;
		this.required = required;
	}

	/**
	 * Returns the name of the parameter. It should be unique for an {@link Action action}.
	 *
	 * @return the parameter's name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the type of the parameter.
	 *
	 * @return the parameter's type
	 */
	public Class<?> getType() {
		return type;
	}

	/**
	 * Returns whether the parameter is required or not.
	 *
	 * @return {@code true} if the parameter is required or {@code false} if it's an optional parameter
	 */
	public boolean isRequired() {
		return required;
	}
}