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
package com.googlecode.aluminumproject.interceptors;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.libraries.actions.Action;
import com.googlecode.aluminumproject.templates.ActionContext;
import com.googlecode.aluminumproject.templates.ActionPhase;
import com.googlecode.aluminumproject.writers.DecorativeWriter;
import com.googlecode.aluminumproject.writers.Writer;

/**
 * An {@link ActionInterceptor action interceptor} that will replace the {@link Writer writer} that will be used by the
 * {@link Action action} that it contributes to. If the new writer is not a decorative writer, then it will be closed
 * after it has been used.
 */
public abstract class WriterReplacer extends AbstractActionInterceptor {
	/**
	 * Creates a writer replacer.
	 */
	public WriterReplacer() {
		super(ActionPhase.EXECUTION);
	}

	public void intercept(ActionContext actionContext) throws AluminumException {
		Writer originalWriter = actionContext.getWriter();
		Writer writer = createWriter(actionContext);

		actionContext.setWriter(writer);

		try {
			actionContext.proceed();
		} finally {
			actionContext.setWriter(originalWriter);

			if (!(writer instanceof DecorativeWriter)) {
				writer.close();
			}
		}
	}

	/**
	 * Creates the writer that will be used by the action. The original writer can be obtained by {@link
	 * ActionContext#getWriter() asking} the supplied action context for it.
	 *
	 * @param actionContext the current action context
	 * @return the writer to use
	 * @throws AluminumException when the writer can't be created
	 */
	protected abstract Writer createWriter(ActionContext actionContext) throws AluminumException;
}