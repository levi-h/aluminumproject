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
import com.googlecode.aluminumproject.interceptors.WriterReplacer;
import com.googlecode.aluminumproject.libraries.actions.AbstractActionContribution;
import com.googlecode.aluminumproject.libraries.actions.ActionContributionOptions;
import com.googlecode.aluminumproject.libraries.actions.ActionParameter;
import com.googlecode.aluminumproject.templates.ActionContext;
import com.googlecode.aluminumproject.utilities.Logger;
import com.googlecode.aluminumproject.writers.PreserveWhitespaceWriter;
import com.googlecode.aluminumproject.writers.PreserveWhitespaceWriter.WhitespaceType;
import com.googlecode.aluminumproject.writers.Writer;

@SuppressWarnings("javadoc")
@Typed("int")
public abstract class WhitespaceToPreserve extends AbstractActionContribution {
	private WhitespaceType type;

	private final Logger logger;

	protected WhitespaceToPreserve(WhitespaceType type) {
		this.type = type;

		logger = Logger.get(getClass());
	}

	public void make(Context context, Writer writer, ActionParameter parameter, ActionContributionOptions options)
			throws AluminumException {
		final int amount = ((Integer) parameter.getValue(Integer.TYPE, context)).intValue();

		logger.debug("preserving ", amount, " ", type.name().toLowerCase(), (amount == 1) ? "" : "s");

		options.addInterceptor(new WriterReplacer() {
			@Override
			protected Writer createWriter(ActionContext actionContext) {
				return new PreserveWhitespaceWriter(actionContext.getWriter(), type, amount);
			}
		});
	}
}