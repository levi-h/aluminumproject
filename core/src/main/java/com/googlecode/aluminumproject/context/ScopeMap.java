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
package com.googlecode.aluminumproject.context;

import java.util.AbstractMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * A {@link Map map} that contains variables in a certain {@link Scope scope}.
 */
public class ScopeMap extends AbstractMap<String, Object> {
	private Scope scope;

	/**
	 * Creates a scope map.
	 *
	 * @param scope the scope to use
	 */
	public ScopeMap(Scope scope) {
		this.scope = scope;
	}

	@Override
	public Set<Map.Entry<String, Object>> entrySet() {
		Set<Map.Entry<String, Object>> variables = new LinkedHashSet<Map.Entry<String, Object>>();

		for (String name: scope.getVariableNames()) {
			variables.add(new Variable(scope, name));
		}

		return variables;
	}

	@Override
	public Object put(String name, Object value) {
		return scope.setVariable(name, value);
	}

	@Override
	public Object remove(Object name) {
		return (name instanceof String) ? scope.removeVariable((String) name) : null;
	}

	private static class Variable implements Map.Entry<String, Object> {
		private Scope scope;

		private String name;

		/**
		 * Creates a variable.
		 *
		 * @param scope the scope that contains the variable
		 * @param name the name of the variable
		 */
		public Variable(Scope scope, String name) {
			this.scope = scope;

			this.name = name;
		}

		public String getKey() {
			return name;
		}

		public Object getValue() {
			return scope.getVariable(name);
		}

		public Object setValue(Object value) {
			return scope.setVariable(name, value);
		}

		@Override
		public int hashCode() {
			Object value = getValue();

			return name.hashCode() ^ ((value == null) ? 0 : getValue().hashCode());
		}

		@Override
		public boolean equals(Object object) {
			return (object instanceof Variable) && equals((Variable) object);
		}

		private boolean equals(Variable otherVariable) {
			Object value = getValue();
			Object otherValue = otherVariable.getValue();

			return name.equals(otherVariable.name)
				&& ((value == null) ? (otherValue == null) : value.equals(otherValue));
		}
	}
}