/*
 * Copyright 2010 Levi Hoogenberg
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

import com.googlecode.aluminumproject.annotations.ActionContributionInformation;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.interceptors.WriterReplacer;
import com.googlecode.aluminumproject.libraries.actions.Action;
import com.googlecode.aluminumproject.libraries.actions.ActionContribution;
import com.googlecode.aluminumproject.libraries.actions.ActionContributionOptions;
import com.googlecode.aluminumproject.libraries.actions.ActionException;
import com.googlecode.aluminumproject.libraries.actions.ActionFactory;
import com.googlecode.aluminumproject.libraries.actions.ActionParameter;
import com.googlecode.aluminumproject.templates.ActionContext;
import com.googlecode.aluminumproject.writers.Writer;

/**
 * Makes sure that the {@link Action action} that it contributes to uses a certain {@link Writer writer}.
 *
 * @author levi_h
 */
@ActionContributionInformation(parameterType = "com.googlecode.aluminumproject.writers.Writer")
public class WithWriter implements ActionContribution {
	/**
	 * Creates a <em>with writer</em> action contribution.
	 */
	public WithWriter() {}

	public boolean canBeMadeTo(ActionFactory actionFactory) {
		return true;
	}

	public void make(Context context, Writer originalWriter,
			final ActionParameter parameter, ActionContributionOptions options) throws ActionException {
		options.addInterceptor(new WriterReplacer() {
			@Override
			protected Writer createWriter(ActionContext actionContext) {
				return (Writer) parameter.getValue(Writer.class, actionContext.getContext());
			}
		});
	}
}