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
import com.googlecode.aluminumproject.annotations.Typed;
import com.googlecode.aluminumproject.annotations.UsableAsAction;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.libraries.actions.AbstractActionContribution;
import com.googlecode.aluminumproject.libraries.actions.ActionContributionOptions;
import com.googlecode.aluminumproject.libraries.actions.ActionParameter;
import com.googlecode.aluminumproject.writers.Writer;

@SuppressWarnings("javadoc")
@Typed("boolean")
@UsableAsAction(parameterName = "condition")
public class If extends AbstractActionContribution {
	public void make(Context context, Writer writer, ActionParameter parameter, ActionContributionOptions options)
			throws AluminumException {
		if (!((Boolean) parameter.getValue(Boolean.TYPE, context)).booleanValue()) {
			options.skipAction();
		}
	}
}