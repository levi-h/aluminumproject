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

import com.googlecode.aluminumproject.annotations.ActionParameterInformation;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.ContextException;
import com.googlecode.aluminumproject.libraries.actions.AbstractAction;
import com.googlecode.aluminumproject.writers.Writer;

/**
 * Removes a variable from the context.
 *
 * @author levi_h
 */
public class RemoveVariable extends AbstractAction {
	private String scope;

	private String name;

	/**
	 * Creates a <em>remove variable</em> action.
	 */
	public RemoveVariable() {}

	/**
	 * Sets the name of the scope from which the variable will be removed. If no scope is given, the innermost scope
	 * will be used.
	 *
	 * @param scope the scope of the removed variable
	 */
	public void setScope(String scope) {
		this.scope = scope;
	}

	/**
	 * Sets the name of the variable.
	 *
	 * @param name the name of the variable to remove
	 */
	@ActionParameterInformation(required = true)
	public void setName(String name) {
		this.name = name;
	}

	public void execute(Context context, Writer writer) throws ContextException {
		if (scope == null) {
			logger.debug("removing variable '", name, "' from innermost scope");

			context.removeVariable(name);
		} else {
			logger.debug("removing variable '", name, "' from scope '", scope, "'");

			context.removeVariable(scope, name);
		}
	}
}