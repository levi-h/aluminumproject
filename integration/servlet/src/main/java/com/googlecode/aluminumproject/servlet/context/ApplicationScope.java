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
package com.googlecode.aluminumproject.servlet.context;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.context.Scope;
import com.googlecode.aluminumproject.utilities.Utilities;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

/**
 * A scope that works with context attributes.
 */
public class ApplicationScope implements Scope {
	private String name;

	private javax.servlet.ServletContext application;

	/**
	 * Creates an application scope.
	 *
	 * @param name the name of the scope
	 * @param application the application to use
	 */
	public ApplicationScope(String name, javax.servlet.ServletContext application) {
		this.name = name;

		this.application = application;
	}

	public String getName() {
		return name;
	}

	public Set<String> getVariableNames() {
		Enumeration<String> attributeNames = Utilities.typed(application.getAttributeNames());

		return new HashSet<String>(Collections.list(attributeNames));
	}

	public Object getVariable(String name) throws AluminumException {
		Object value = application.getAttribute(name);

		if (value == null) {
			throw new AluminumException("can't find an application attribute with the name '", name, "'");
		}

		return value;
	}

	public Object setVariable(String name, Object value) {
		Object previousValue = application.getAttribute(name);

		application.setAttribute(name, value);

		return previousValue;
	}

	public Object removeVariable(String name) throws AluminumException {
		Object value = getVariable(name);

		application.removeAttribute(name);

		return value;
	}
}