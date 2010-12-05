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
package com.googlecode.aluminumproject.libraries.common.actions;

import com.googlecode.aluminumproject.annotations.Injected;
import com.googlecode.aluminumproject.annotations.Required;
import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.libraries.Library;
import com.googlecode.aluminumproject.libraries.actions.AbstractAction;
import com.googlecode.aluminumproject.libraries.actions.Action;
import com.googlecode.aluminumproject.libraries.actions.ActionException;
import com.googlecode.aluminumproject.libraries.actions.DefaultActionFactory;
import com.googlecode.aluminumproject.libraries.functions.ConstantFunctionArgument;
import com.googlecode.aluminumproject.libraries.functions.Function;
import com.googlecode.aluminumproject.writers.Writer;

/**
 * Adds an argument to a {@link Function function} that's being called by the {@link CallFunction call function action}.
 *
 * @author levi_h
 */
public class FunctionArgument extends AbstractAction {
	private @Required Object value;

	private @Injected DefaultActionFactory factory;
	private @Injected Configuration configuration;

	/**
	 * Creates a <i>function argument</i> action.
	 */
	public FunctionArgument() {}

	public void execute(Context context, Writer writer) throws ActionException {
		findCallFunction().addArgument(new ConstantFunctionArgument(value, configuration.getConverterRegistry()));
	}

	private CallFunction findCallFunction() throws ActionException {
		CallFunction callFunction = null;

		Library library = factory.getLibrary();
		String libraryUrl = library.getInformation().getVersionedUrl();

		Action action = this;

		do {
			action = action.getParent();

			if (action instanceof CallFunction) {
				callFunction = (CallFunction) action;

				String callFunctionUrl = callFunction.getLibrary().getInformation().getVersionedUrl();

				if (!callFunctionUrl.equals(libraryUrl)) {
					callFunction = null;
				}
			}
		} while ((action != null) && (callFunction == null));

		if (callFunction == null) {
			throw new ActionException("can't find call function action",
				" for library '", library.getInformation().getUrl(), "'");
		} else {
			return callFunction;
		}
	}
}