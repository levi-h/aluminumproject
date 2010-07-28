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
package com.googlecode.aluminumproject.context;

import java.util.Set;

/**
 * A variable scope in a {@link Context}.
 *
 * @author levi_h
 */
public interface Scope {
	/**
	 * Returns the name of this scope.
	 *
	 * @return this scope's name
	 */
	String getName();

	/**
	 * Returns all variable names in this scope.
	 *
	 * @return a set that contains the names of all variables in this scope (may be empty but not {@code null})
	 */
	Set<String> getVariableNames();

	/**
	 * Retrieves a variable by name.
	 *
	 * @param name the name of the variable to retrieve
	 * @return the value of the variable with the given name
	 * @throws ContextException when there's no variable with the given name within this scope
	 */
	Object getVariable(String name) throws ContextException;

	/**
	 * Sets a variable.
	 *
	 * @param name the name of the variable to set
	 * @param value the value to set
	 * @return the previous value of the variable (or {@code null} if the variable did not exist)
	 */
	Object setVariable(String name, Object value);

	/**
	 * Removes a variable.
	 *
	 * @param name the name of the variable to remove
	 * @return the previous value of the variable
	 * @throws ContextException when there's no variable with the given name within this scope
	 */
	Object removeVariable(String name) throws ContextException;
}