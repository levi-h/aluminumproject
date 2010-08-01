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
package com.googlecode.aluminumproject.libraries.core.actions;

import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.ContextException;
import com.googlecode.aluminumproject.libraries.actions.AbstractAction;
import com.googlecode.aluminumproject.libraries.actions.ActionException;
import com.googlecode.aluminumproject.writers.Writer;

/**
 * Adds a scope to the context, executes its body, and removes the scope from the context.
 *
 * @author levi_h
 */
public class Scope extends AbstractAction {
	private String name;

	/**
	 * Creates a <em>scope</em> action.
	 */
	public Scope() {}

	/**
	 * Sets the name of the new scope. If no name is given, {@value #DEFAULT_NAME} (with a suffix) will be used.
	 *
	 * @param name the name of the scope to add
	 */
	public void setName(String name) {
		this.name = name;
	}

	public void execute(Context context, Writer writer) throws ActionException {
		addScope(context);

		logger.debug("invoking body");

		getBody().invoke(context, writer);

		removeScope(context);
	}

	private void addScope(Context context) throws ActionException {
		boolean requireUniqueName = name != null;

		if (name == null) {
			logger.debug("no name given, using '", DEFAULT_NAME, "'");

			name = DEFAULT_NAME;
		}

		try {
			name = context.addScope(name, requireUniqueName);

			logger.debug("added scope '", name, "'");
		} catch (ContextException exception) {
			throw new ActionException(exception, "can't add scope");
		}
	}

	private void removeScope(Context context) throws ContextException {
		context.removeScope(name);

		logger.debug("removed scope");
	}

	/** The name that will be used for the scope if none is given. */
	public final static String DEFAULT_NAME = "block";
}