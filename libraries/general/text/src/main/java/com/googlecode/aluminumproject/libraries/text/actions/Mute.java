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
package com.googlecode.aluminumproject.libraries.text.actions;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.annotations.Typed;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.interceptors.AbstractActionInterceptor;
import com.googlecode.aluminumproject.libraries.actions.AbstractActionContribution;
import com.googlecode.aluminumproject.libraries.actions.ActionContributionOptions;
import com.googlecode.aluminumproject.libraries.actions.ActionParameter;
import com.googlecode.aluminumproject.templates.ActionContext;
import com.googlecode.aluminumproject.templates.ActionPhase;
import com.googlecode.aluminumproject.writers.DecorativeWriter;
import com.googlecode.aluminumproject.writers.ToggleableWriter;
import com.googlecode.aluminumproject.writers.Writer;

@SuppressWarnings("javadoc")
@Typed("boolean")
public class Mute extends AbstractActionContribution {
	public void make(Context context, Writer writer, ActionParameter parameter, ActionContributionOptions options)
			throws AluminumException {
		final boolean mute = ((Boolean) parameter.getValue(Boolean.TYPE, context)).booleanValue();

		options.addInterceptor(new AbstractActionInterceptor(ActionPhase.EXECUTION) {
			public void intercept(ActionContext actionContext) throws AluminumException {
				Writer originalWriter = actionContext.getWriter();

				ToggleableWriter toggleableWriter = findToggleableWriter(originalWriter);
				Boolean originalWrite = null;

				if (toggleableWriter == null) {
					actionContext.setWriter(new ToggleableWriter(originalWriter, !mute));
				} else {
					originalWrite = toggleableWriter.isWrite();

					toggleableWriter.setWrite(!mute);
				}

				try {
					actionContext.proceed();
				} finally {
					if (toggleableWriter == null) {
						actionContext.setWriter(originalWriter);
					} else {
						toggleableWriter.setWrite(originalWrite);
					}
				}
			}

			private ToggleableWriter findToggleableWriter(Writer writer) {
				ToggleableWriter toggleableWriter = null;

				Writer currentWriter = writer;

				do {
					if (currentWriter instanceof ToggleableWriter) {
						toggleableWriter = (ToggleableWriter) currentWriter;
					} else if (currentWriter instanceof DecorativeWriter) {
						currentWriter = ((DecorativeWriter) currentWriter).getWriter();
					} else {
						currentWriter = null;
					}
				} while ((toggleableWriter == null) && (currentWriter != null));

				return toggleableWriter;
			}
		});
	}
}