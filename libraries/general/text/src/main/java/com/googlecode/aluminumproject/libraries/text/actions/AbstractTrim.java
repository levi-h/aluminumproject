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
package com.googlecode.aluminumproject.libraries.text.actions;

import com.googlecode.aluminumproject.annotations.Typed;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.interceptors.WriterReplacer;
import com.googlecode.aluminumproject.libraries.actions.ActionContribution;
import com.googlecode.aluminumproject.libraries.actions.ActionContributionOptions;
import com.googlecode.aluminumproject.libraries.actions.ActionException;
import com.googlecode.aluminumproject.libraries.actions.ActionFactory;
import com.googlecode.aluminumproject.libraries.actions.ActionParameter;
import com.googlecode.aluminumproject.templates.ActionContext;
import com.googlecode.aluminumproject.writers.TrimWriter;
import com.googlecode.aluminumproject.writers.Writer;
import com.googlecode.aluminumproject.writers.TrimWriter.TrimType;

/**
 * Abstract superclass of the trim action contributions.
 *
 * @author levi_h
 */
@Typed("com.googlecode.aluminumproject.writers.TrimWriter$TrimType")
abstract class AbstractTrim implements ActionContribution {
	private boolean multiline;

	/**
	 * Creates an abstract trim action contribution.
	 *
	 * @param multiline whether or not to trim each line individually
	 */
	public AbstractTrim(boolean multiline) {
		this.multiline = multiline;
	}

	public boolean canBeMadeTo(ActionFactory actionFactory) {
		return true;
	}

	public void make(Context context, Writer writer, ActionParameter parameter, ActionContributionOptions options)
			throws ActionException {
		final TrimType trimType = (TrimType) parameter.getValue(TrimType.class, context);

		options.addInterceptor(new WriterReplacer() {
			@Override
			protected Writer createWriter(ActionContext actionContext) {
				return new TrimWriter(actionContext.getWriter(), trimType, multiline);
			}
		});
	}
}