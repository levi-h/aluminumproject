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
package com.googlecode.aluminumproject.libraries.core.actions;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.libraries.actions.AbstractAction;
import com.googlecode.aluminumproject.writers.Writer;

@SuppressWarnings("javadoc")
public class Scope extends AbstractAction {
	private String name;

	public void execute(Context context, Writer writer) throws AluminumException {
		addScope(context);

		logger.debug("invoking body");

		getBody().invoke(context, writer);

		removeScope(context);
	}

	private void addScope(Context context) throws AluminumException {
		boolean requireUniqueName = name != null;

		if (name == null) {
			logger.debug("no name given, using '", DEFAULT_NAME, "'");

			name = DEFAULT_NAME;
		}

		name = context.addScope(name, requireUniqueName);

		logger.debug("added scope '", name, "'");
	}

	private void removeScope(Context context) throws AluminumException {
		context.removeScope(name);

		logger.debug("removed scope");
	}

	public final static String DEFAULT_NAME = "block";
}