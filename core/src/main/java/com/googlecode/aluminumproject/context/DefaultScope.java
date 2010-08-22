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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * The default, {@link Map map}-backed, {@link Scope scope} implementation.
 *
 * @author levi_h
 */
public class DefaultScope implements Scope {
	private String name;

	private Map<String, Object> variables;

	/**
	 * Creates a default scope.
	 *
	 * @param name the name of the scope
	 */
	public DefaultScope(String name) {
		this.name = name;

		variables = new HashMap<String, Object>();
	}

	public String getName() {
		return name;
	}

	public Set<String> getVariableNames() {
		return Collections.unmodifiableSet(variables.keySet());
	}

	public Object getVariable(String name) throws ContextException {
		if (variables.containsKey(name)) {
			return variables.get(name);
		} else {
			throw new ContextException("no such variable: ", name);
		}
	}

	public Object setVariable(String name, Object value) {
		return variables.put(name, value);
	}

	public Object removeVariable(String name) throws ContextException {
		if (variables.containsKey(name)) {
			return variables.remove(name);
		} else {
			throw new ContextException("no such variable: ", name);
		}
	}
}