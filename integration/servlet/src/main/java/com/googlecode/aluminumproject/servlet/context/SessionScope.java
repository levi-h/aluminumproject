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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * A scope that works with session attributes. This scope won't create a session if it's not necessary; a missing
 * session will be treated as a session without any attributes.
 */
public class SessionScope implements Scope {
	private String name;

	private HttpServletRequest request;

	/**
	 * Creates a session scope.
	 *
	 * @param name the name of the scope
	 * @param request the request that's bound to the session
	 */
	public SessionScope(String name, HttpServletRequest request) {
		this.name = name;

		this.request = request;
	}

	public String getName() {
		return name;
	}

	public Set<String> getVariableNames() {
		Set<String> variableNames;

		HttpSession session = request.getSession(false);

		if (session == null) {
			variableNames = Collections.emptySet();
		} else {
			Enumeration<String> attributeNames = Utilities.typed(session.getAttributeNames());

			variableNames = new HashSet<String>(Collections.list(attributeNames));
		}

		return variableNames;
	}

	public Object getVariable(String name) throws AluminumException {
		HttpSession session = request.getSession(false);

		Object value = (session == null) ? null : session.getAttribute(name);

		if (value == null) {
			throw new AluminumException("can't find a session attribute named '", name, "'");
		}

		return value;
	}

	public Object setVariable(String name, Object value) {
		HttpSession session = request.getSession();

		Object previousValue = session.getAttribute(name);

		session.setAttribute(name, value);

		return previousValue;
	}

	public Object removeVariable(String name) throws AluminumException {
		Object value = getVariable(name);

		request.getSession().removeAttribute(name);

		return value;
	}
}