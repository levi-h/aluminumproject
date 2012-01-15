/*
 * Copyright 2009-2012 Levi Hoogenberg
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

import com.googlecode.aluminumproject.AluminumException;

import java.util.Set;

/**
 * A context that is used throughout the processing of one template.
 * <p>
 * A context can contain variables. Each variable is stored in a certain scope. At the very minimum, implementations are
 * required to support the <i>template</i> scope, though it's possible to add scopes. Scopes that are added can be
 * removed as well.
 * <p>
 * A context can have a number of implicit objects, which are not scoped. Implicit objects can be {@link
 * #addImplicitObject(String, Object) added} and {@link #removeImplicitObject(String) removed} freely, although their
 * names should not start with {@value #RESERVED_IMPLICIT_OBJECT_NAME_PREFIX}. Subclasses can further restrict the names
 * that may be used for implicit names.
 * <p>
 * When a context supports it, it's possible to create one or more child contexts. The parent of such a child context
 * can be {@link #getParent() obtained} and will be used when {@link #findVariable(String) finding variables}.
 */
public interface Context {
	/**
	 * Returns all scope names, ordered by priority (from highest to lowest).
	 *
	 * @return a set that contains the names of all scopes in this context
	 */
	Set<String> getScopeNames();

	/**
	 * Adds a scope.
	 *
	 * @param name the name of the scope to add
	 * @param requireUniqueName whether the name is expected to be unique; if it isn't, this context is allowed to
	 *                          change the name to ensure its uniqueness
	 * @return the actual name of the new scope: this will be the same name as was requested if it should be unique, it
	 *         could be different if a scope with the requested name already exists and the name is not required to be
	 *         unique
	 * @throws AluminumException when the name has to be unique and there already is a scope with the given name
	 */
	String addScope(String name, boolean requireUniqueName) throws AluminumException;

	/**
	 * Removes a scope.
	 *
	 * @param name the name of the scope to remove
	 * @throws AluminumException when there's no scope with the given name or when the scope can't be removed
	 */
	void removeScope(String name) throws AluminumException;

	/**
	 * Returns all variable names within a certain scope.
	 *
	 * @param scope the name of the scope to retrieve the variable names of
	 * @return a set that contains the names of all variables in the given scope (may be empty but not {@code null})
	 * @throws AluminumException when there's no scope with the given name
	 */
	Set<String> getVariableNames(String scope) throws AluminumException;

	/**
	 * Retrieves a variable by name.
	 *
	 * @param scope the name of the scope to get the variable from
	 * @param name the name of the variable to retrieve
	 * @return the value of the variable with the given name in the specified scope
	 * @throws AluminumException when there's no scope with the given name or when there's no variable with the given
	 *                           name within the scope
	 */
	Object getVariable(String scope, String name) throws AluminumException;

	/**
	 * Sets a variable in the innermost scope (i.e. the scope that was added last).
	 *
	 * @param name the name of the variable to set
	 * @param value the value to set
	 * @return the previous value of the variable (or {@code null} if the variable did not exist)
	 */
	Object setVariable(String name, Object value);

	/**
	 * Sets a variable.
	 *
	 * @param scope the name of the scope to set the variable in
	 * @param name the name of the variable to set
	 * @param value the value to set
	 * @return the previous value of the variable (or {@code null} if the variable did not exist)
	 * @throws AluminumException when there's no scope with the given name
	 */
	Object setVariable(String scope, String name, Object value) throws AluminumException;

	/**
	 * Removes a variable from the innermost scope (i.e. the scope that was added last).
	 *
	 * @param name the name of the variable to remove
	 * @return the previous value of the variable
	 * @throws AluminumException when there's no variable with the given name within the innermost scope
	 */
	Object removeVariable(String name) throws AluminumException;

	/**
	 * Removes a variable.
	 *
	 * @param scope the name of the scope to remove the variable from
	 * @param name the name of the variable to remove
	 * @return the previous value of the variable
	 * @throws AluminumException when there's no scope with the given name or when there's no variable with the given
	 *                           name within the scope
	 */
	Object removeVariable(String scope, String name) throws AluminumException;

	/**
	 * Finds a variable across all scopes. When multiple scopes have a variable with the given name, it's up to the
	 * context implementation which variable will be returned. When none of the scopes in this context contain a
	 * variable with the specified name and a parent context is available, the variable will be tried to be found in
	 * that context.
	 *
	 * @param name the name of the variable to find
	 * @return the value of the variable with the given name
	 * @throws AluminumException when not a scope in either this context or one of its ancestor contexts contains a
	 *                           variable with the given name
	 */
	Object findVariable(String name) throws AluminumException;

	/**
	 * Returns all implicit object names.
	 *
	 * @return a set that contains the names of all implicit objects
	 */
	Set<String> getImplicitObjectNames();

	/**
	 * Retrieves a implicit object by name.
	 *
	 * @param name the name of the implicit object to return
	 * @return the implicit object with the given name
	 * @throws AluminumException when no implicit object with the given name exists in this context
	 */
	Object getImplicitObject(String name) throws AluminumException;

 	/**
	 * Adds an implicit object.
	 *
	 * @param name the name of the implicit object
	 * @param implicitObject the implicit object to add
	 * @throws AluminumException when there already is an implicit object with the given name or when the name is
	 *                           reserved
	 */
	void addImplicitObject(String name, Object implicitObject) throws AluminumException;

	/**
	 * Removes an implicit object with a certain name.
	 *
	 * @param name the name of the implicit object to remove
	 * @return the removed implicit object
	 * @throws AluminumException when there's no implicit object with the given name
	 */
	Object removeImplicitObject(String name) throws AluminumException;

	/**
	 * Returns the parent of this context.
	 *
	 * @return this context's parent, {@code null} if this context does not have a parent
	 */
	Context getParent();

	/**
	 * Creates a child context of this context.
	 *
	 * @return the new subcontext
	 * @throws AluminumException when this context does not support subcontexts
	 */
	Context createSubcontext() throws AluminumException;

	/** The name of the default scope. */
	String TEMPLATE_SCOPE = "template";

	/** The prefix of names of implicit objects that are reserved for internal use: {@value}. */
	String RESERVED_IMPLICIT_OBJECT_NAME_PREFIX = "aluminum";
}