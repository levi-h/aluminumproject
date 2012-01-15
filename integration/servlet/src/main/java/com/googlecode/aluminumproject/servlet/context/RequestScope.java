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
package com.googlecode.aluminumproject.servlet.context;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.context.Scope;
import com.googlecode.aluminumproject.utilities.Utilities;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

/**
 * A scope that works with request attributes.
 */
public class RequestScope implements Scope {
	private String name;

	private HttpServletRequest request;

	/**
	 * Creates a request scope.
	 *
	 * @param name the name of the scope
	 * @param request the current request object
	 */
	public RequestScope(String name, HttpServletRequest request) {
		this.name = name;

		this.request = request;
	}

	public String getName() {
		return name;
	}

	public Set<String> getVariableNames() {
		Enumeration<String> attributeNames = Utilities.typed(request.getAttributeNames());

		return new HashSet<String>(Collections.list(attributeNames));
	}

	public Object getVariable(String name) throws AluminumException {
		Object value = request.getAttribute(name);

		if (value == null) {
			throw new AluminumException("can't find a request attribute named '", name, "'");
		}

		return value;
	}

	public Object setVariable(String name, Object value) {
		Object previousValue = request.getAttribute(name);

		request.setAttribute(name, value);

		return previousValue;
	}

	public Object removeVariable(String name) throws AluminumException {
		Object value = getVariable(name);

		request.removeAttribute(name);

		return value;
	}
}