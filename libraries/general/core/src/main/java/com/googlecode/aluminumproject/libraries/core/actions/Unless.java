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

import com.googlecode.aluminumproject.annotations.ActionContributionInformation;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.libraries.actions.ActionContribution;
import com.googlecode.aluminumproject.libraries.actions.ActionContributionOptions;
import com.googlecode.aluminumproject.libraries.actions.ActionFactory;
import com.googlecode.aluminumproject.libraries.actions.ActionParameter;
import com.googlecode.aluminumproject.writers.Writer;

/**
 * Executes an action, unless the value of its parameter is {@code true}. The contribution can be made to any action.
 *
 * @author levi_h
 * @see If
 */
@ActionContributionInformation(parameterType = "boolean")
public class Unless implements ActionContribution {
	/**
	 * Creates an <em>unless</em> action contribution.
	 */
	public Unless() {}

	public boolean canBeMadeTo(ActionFactory actionFactory) {
		return true;
	}

	public void make(Context context, Writer writer, ActionParameter parameter, ActionContributionOptions options) {
		if (((Boolean) parameter.getValue(Boolean.TYPE, context)).booleanValue()) {
			options.skipAction();
		}
	}
}