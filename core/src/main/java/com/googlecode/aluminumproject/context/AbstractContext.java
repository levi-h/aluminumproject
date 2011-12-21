/*
 * Copyright 2009-2011 Levi Hoogenberg
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

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

/**
 * Convenience superclass for classes that would like to implement {@link Context}.
 * <p>
 * All of the work regarding variables is delegated to a number of {@link Scope scopes}, which subclasses have to
 * provide when constructing an abstract context. The scopes are prioritised, which can be seen when {@link
 * #findVariable(String) finding a variable}: the value in the scope that has the highest precedence will be used.
 * <p>
 * All scoped variables are made available as implicit objects using {@link ScopeMap scope maps}; the names that will be
 * used are obtained by suffixing the scope names with {@value #IMPLICIT_OBJECT_SCOPE_NAME_SUFFIX}. As a consequence, no
 * other implicit objects can have a name that ends with that suffix.
 * <p>
 * Subcontexts are supported, but {@link #createSubcontext() the createSubcontext method} throws an exception, since it
 * doesn't know which context class to instantiate. Subclasses that want to support subcontexts have to override this
 * method and delegate to a constructor that calls the {@link #AbstractContext(AbstractContext, Scope...) constructor
 * that accepts a parent}.
 *
 * @author levi_h
 */
public abstract class AbstractContext implements Context {
	private AbstractContext parent;

	private List<Scope> scopes;

	private Map<String, Object> implicitObjects;

	/**
	 * Creates an abstract context.
	 *
	 * @param scopes the scopes that are supported by the context; the first scope has the lowest priority, the last
	 *               scope the highest one
	 * @throws AluminumException when the list of scopes does not contain the {@link Context#TEMPLATE_SCOPE template
	 *                           scope} or when two scopes have the same name
	 */
	protected AbstractContext(Scope... scopes) throws AluminumException {
		initialise(scopes);
	}

	/**
	 * Creates an abstract context that's the subcontext of another context.
	 *
	 * @param parent the parent context
	 * @param scopes the scopes that are supported by the context; the first scope has the lowest priority, the last
	 *               scope the highest one
	 * @throws AluminumException when the list of scopes does not contain the {@link Context#TEMPLATE_SCOPE template
	 *                           scope} or when two scopes have the same name
	 */
	protected AbstractContext(
			AbstractContext parent, Scope... scopes) throws AluminumException {
		this.parent = parent;

		initialise(scopes);
	}

	private void initialise(Scope... scopes) throws AluminumException {
		this.scopes = new LinkedList<Scope>();

		implicitObjects = new LinkedHashMap<String, Object>();

		for (Scope scope: scopes) {
			addScope(scope);
		}

		getScope(TEMPLATE_SCOPE);
	}

	public Set<String> getScopeNames() {
		Set<String> scopeNames = new LinkedHashSet<String>();

		for (Scope scope: scopes) {
			scopeNames.add(scope.getName());
		}

		return scopeNames;
	}

	public String addScope(String name, boolean requireUniqueName) throws AluminumException {
		String scopeName = name;

		int suffix = 1;

		while (getScope(scopeName, true) != null) {
			scopeName = String.format("%s-%d", name, suffix++);
		}

		if (requireUniqueName && !scopeName.equals(name)) {
			throw new AluminumException("a scope with name '", name, "' already exists");
		}

		addScope(new DefaultScope(scopeName));

		return scopeName;
	}

	public void removeScope(String name) throws AluminumException {
		if (isScopeRemovable(name)) {
			ListIterator<Scope> scopesIterator = scopes.listIterator();

			boolean scopeRemoved = false;

			while (scopesIterator.hasNext() && !scopeRemoved) {
				if (scopeRemoved = scopesIterator.next().getName().equals(name)) {
					scopesIterator.remove();
				}
			}

			if (!scopeRemoved) {
				throw new AluminumException("can't find scope with name '", name, "' to remove");
			}
		} else {
			throw new AluminumException("scope '", name, "' can't be removed");
		}
	}

	/**
	 * Determines whether a scope can be removed. This implementation checks whether the scope is the template scope.
	 *
	 * @param name the name of the scope of which should be determined whether or not it may be removed
	 * @return {@code true} if it's permitted to remove the scope with the given name, {@code false} if it isn't
	 */
	protected boolean isScopeRemovable(String name) {
		return !name.equals(TEMPLATE_SCOPE);
	}

	public Set<String> getVariableNames(String scope) throws AluminumException {
		return getScope(scope).getVariableNames();
	}

	public Object getVariable(String scope, String name) throws AluminumException {
		return getScope(scope).getVariable(name);
	}

	public Object setVariable(String name, Object value) {
		return setVariable(scopes.get(0).getName(), name, value);
	}

	public Object setVariable(String scope, String name, Object value) throws AluminumException {
		return getScope(scope).setVariable(name, value);
	}

	public Object removeVariable(String name) throws AluminumException{
		return removeVariable(scopes.get(0).getName(), name);
	}

	public Object removeVariable(String scope, String name) throws AluminumException {
		return getScope(scope).removeVariable(name);
	}

	public Object findVariable(String name) throws AluminumException {
		Iterator<Scope> scopesIterator = scopes.iterator();
		Scope scope = null;

		while (scopesIterator.hasNext() && (scope == null)) {
			scope = scopesIterator.next();

			if (!scope.getVariableNames().contains(name)) {
				scope = null;
			}
		}

		if (scope == null) {
			if (parent == null) {
				throw new AluminumException("variable '", name, "' can't be found in any scope");
			} else {
				return parent.findVariable(name);
			}
		} else {
			return scope.getVariable(name);
		}
	}

	private Scope getScope(String name) throws AluminumException {
		return getScope(name, false);
	}

	private Scope getScope(String name, boolean allowNull) throws AluminumException {
		ListIterator<Scope> scopesIterator = scopes.listIterator();
		Scope scope = null;

		while (scopesIterator.hasNext() && (scope == null)) {
			scope = scopesIterator.next();

			if (!scope.getName().equals(name)) {
				scope = null;
			}
		}

		if ((scope == null) && !allowNull) {
			throw new AluminumException("no scope with name '", name, "' can be found");
		} else {
			return scope;
		}
	}

	private void addScope(Scope scope) throws AluminumException {
		String name = scope.getName();

		if (getScope(name, true) == null) {
			scopes.add(0, scope);

			implicitObjects.put(name + IMPLICIT_OBJECT_SCOPE_NAME_SUFFIX, new ScopeMap(scope));
		} else {
			throw new AluminumException("a scope with name '", name, "' is already present");
		}
	}

	public Set<String> getImplicitObjectNames() {
		return Collections.unmodifiableSet(implicitObjects.keySet());
	}

	public Object getImplicitObject(String name) throws AluminumException {
		if (implicitObjects.containsKey(name)) {
			return implicitObjects.get(name);
		} else {
			throw new AluminumException("can't find implicit object with name '", name, "'");
		}
	}

	public void addImplicitObject(String name, Object implicitObject) throws AluminumException {
		if (name.endsWith(IMPLICIT_OBJECT_SCOPE_NAME_SUFFIX)) {
			throw new AluminumException("the name of an implicit object ",
				"can't end with '", IMPLICIT_OBJECT_SCOPE_NAME_SUFFIX, "'");
		} else if (implicitObjects.containsKey(name)) {
			throw new AluminumException("an implicit object with name '", name, "' already exists");
		}

		implicitObjects.put(name, implicitObject);
	}

	public Object removeImplicitObject(String name) throws AluminumException {
		if (implicitObjects.containsKey(name)) {
			return implicitObjects.remove(name);
		} else {
			throw new AluminumException("can't find implicit object with name '", name, "' to remove");
		}
	}

	public AbstractContext getParent() {
		return parent;
	}

	public AbstractContext createSubcontext() throws AluminumException {
		throw new AluminumException("subcontexts are not supported");
	}

	/**
	 * The text that is appended to the name of a scope to get to the name that will be used for the implicit object for
	 * the scope.
	 */
	public final static String IMPLICIT_OBJECT_SCOPE_NAME_SUFFIX = "Scope";
}