/*
 * Copyright 2012 Aluminum project
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
package com.googlecode.aluminumproject.libraries.functions;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.libraries.AbstractLibraryElementCreation;
import com.googlecode.aluminumproject.libraries.actions.Action;
import com.googlecode.aluminumproject.libraries.actions.ActionInformation;
import com.googlecode.aluminumproject.writers.ListWriter;

import java.util.List;

/**
 * Executes an {@link Action action} when it is called. Whatever is written by the action will become the result of the
 * function.
 */
public class ActionExecutingFunction extends AbstractLibraryElementCreation<FunctionFactory> implements Function {
	private Action action;
	private ActionInformation actionInformation;

	/**
	 * Creates an action executing function.
	 *
	 * @param action the action to execute
	 * @param actionInformation information about the action
	 */
	public ActionExecutingFunction(Action action, ActionInformation actionInformation) {
		this.action = action;
		this.actionInformation = actionInformation;
	}

	public Object call(Context context) throws AluminumException {
		ListWriter writer = new ListWriter(true);

		action.execute(context, writer);

		List<?> list = writer.getList();

		if (list.isEmpty()) {
			throw new AluminumException("nothing was written by action '", actionInformation.getName(), "'");
		} else {
			return (list.size() == 1) ? list.get(0) : list;
		}
	}
}