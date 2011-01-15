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
import com.googlecode.aluminumproject.writers.WriterException;

/**
 * An action that serves as a container for other actions.
 * <p>
 * Normally, the template action does nothing more than invoking its body. The processing of the template can be
 * deferred, however, by using the <em>name</em> parameter. In that case, the body is stored in a context variable and
 * can be invoked later through the {@link IncludeLocal include local} action.
 *
 * @author levi_h
 */
public class Template extends AbstractAction {
	private String name;

	/**
	 * Creates a template action.
	 */
	public Template() {}

	public void execute(Context context, Writer writer) throws ActionException, ContextException, WriterException {
		if (name == null) {
			logger.debug("invoking body");

			getBody().invoke(context, writer);
		} else {
			logger.debug("storing body in context variable '", name, "'");

			context.setVariable(Context.TEMPLATE_SCOPE, name, getBody().copy());
		}
	}
}