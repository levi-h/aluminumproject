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
package com.googlecode.aluminumproject.libraries.core.actions;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.annotations.Required;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.libraries.actions.AbstractAction;
import com.googlecode.aluminumproject.writers.Writer;

@SuppressWarnings("javadoc")
public class RemoveVariable extends AbstractAction {
	private String scope;
	private @Required String name;

	public void execute(Context context, Writer writer) throws AluminumException {
		if (scope == null) {
			logger.debug("removing variable '", name, "' from innermost scope");

			context.removeVariable(name);
		} else {
			logger.debug("removing variable '", name, "' from scope '", scope, "'");

			context.removeVariable(scope, name);
		}
	}
}