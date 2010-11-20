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

import com.googlecode.aluminumproject.annotations.Required;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.ContextException;
import com.googlecode.aluminumproject.libraries.actions.AbstractDynamicallyParameterisableAction;
import com.googlecode.aluminumproject.libraries.actions.ActionBody;
import com.googlecode.aluminumproject.libraries.actions.ActionException;
import com.googlecode.aluminumproject.libraries.actions.ActionParameter;
import com.googlecode.aluminumproject.writers.NullWriter;
import com.googlecode.aluminumproject.writers.Writer;
import com.googlecode.aluminumproject.writers.WriterException;

import java.util.Map;

/**
 * Includes a part of a template that was saved earlier. Upon execution, the <em>include local</em> action reads a
 * {@link Context#TEMPLATE_SCOPE template-scoped} context variable that contains an {@link ActionBody action body} and
 * invokes it.
 * <p>
 * There are two ways to pass information to the included template:
 * <ul>
 * <li>The action supports dynamic parameters, all of which will be available within the template;
 * <li>It's possible to used {@link Block blocks} in the action body, these can be invoked inside the included template
 *     using the {@link BlockContents block contents} action.
 * </ul>
 *
 * @author levi_h
 * @see Template
 */
public class IncludeLocal extends AbstractDynamicallyParameterisableAction {
	private @Required String name;

	/**
	 * Creates an <em>include local</em> action.
	 */
	public IncludeLocal() {}

	public void execute(Context context, Writer writer) throws ActionException, ContextException, WriterException {
		Object body = context.getVariable(Context.TEMPLATE_SCOPE, name);

		if (body instanceof ActionBody) {
			Context subcontext = context.createSubcontext();

			getBody().invoke(subcontext, new NullWriter());

			for (Map.Entry<String, ActionParameter> variable: getDynamicParameters().entrySet()) {
				String variableName = variable.getKey();
				Object variableValue = variable.getValue().getValue(Object.class, context);

				logger.debug("setting variable '", variableName, "' to ", variableValue);

				subcontext.setVariable(variableName, variableValue);
			}

			((ActionBody) body).invoke(subcontext, writer);
		} else {
			throw new ActionException("can't include '", name, "' (", body, ")");
		}
	}
}