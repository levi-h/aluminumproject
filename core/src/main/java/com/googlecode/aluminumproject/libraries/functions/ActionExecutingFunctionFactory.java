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
import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.libraries.AbstractLibraryElement;
import com.googlecode.aluminumproject.libraries.actions.Action;
import com.googlecode.aluminumproject.libraries.actions.ActionBody;
import com.googlecode.aluminumproject.libraries.actions.ActionFactory;
import com.googlecode.aluminumproject.libraries.actions.ActionInformation;
import com.googlecode.aluminumproject.libraries.actions.ActionParameter;
import com.googlecode.aluminumproject.libraries.actions.ActionParameterInformation;
import com.googlecode.aluminumproject.writers.Writer;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Creates functions that execute an action.
 */
public class ActionExecutingFunctionFactory extends AbstractLibraryElement implements FunctionFactory {
	private ActionFactory actionFactory;

	private FunctionInformation information;

	/**
	 * Creates an action executing function factory.
	 *
	 * @param actionFactory the action factory that is able to create the executed action
	 */
	public ActionExecutingFunctionFactory(ActionFactory actionFactory) {
		this.actionFactory = actionFactory;
	}

	@Override
	public void initialise(Configuration configuration) throws AluminumException {
		super.initialise(configuration);

		ActionInformation actionInformation = actionFactory.getInformation();

		information = new FunctionInformation(actionInformation.getName(),
			actionInformation.getResultTypeWhenFunction(), getArgumentInformation(actionInformation));
	}

	private List<FunctionArgumentInformation> getArgumentInformation(ActionInformation actionInformation) {
		SortedMap<Integer, FunctionArgumentInformation> argumentInformation =
			new TreeMap<Integer, FunctionArgumentInformation>();

		for (ActionParameterInformation parameterInformation: actionInformation.getParameterInformation()) {
			Integer index = parameterInformation.getIndexWhenFunctionArgument();

			if (index != null) {
				argumentInformation.put(index,
					new FunctionArgumentInformation(parameterInformation.getName(), parameterInformation.getType()));
			}
		}

		return new LinkedList<FunctionArgumentInformation>(argumentInformation.values());
	}

	public FunctionInformation getInformation() {
		return information;
	}

	public Function create(List<FunctionArgument> arguments, Context context) throws AluminumException {
		Action action = actionFactory.create(createActionParameters(arguments), context);
		action.setBody(new EmptyActionBody());

		return new ActionExecutingFunction(action, actionFactory.getInformation());
	}

	private Map<String, ActionParameter> createActionParameters(List<FunctionArgument> arguments) {
		Map<String, ActionParameter> actionParameters = new HashMap<String, ActionParameter>();

		List<FunctionArgumentInformation> argumentInformation = information.getArgumentInformation();

		if (arguments.size() == argumentInformation.size()) {
			for (int i = 0; i < arguments.size(); i++) {
				actionParameters.put(
					argumentInformation.get(i).getName(), new ArgumentActionParameter(arguments.get(i)));
			}

			return actionParameters;
		} else {
			throw new AluminumException("expected ", argumentInformation.size(), " arguments, got ", arguments.size());
		}
	}

	private static class ArgumentActionParameter implements ActionParameter {
		private FunctionArgument argument;

		public ArgumentActionParameter(FunctionArgument argument) {
			this.argument = argument;
		}

		public String getText() {
			return null;
		}

		public Object getValue(Type type, Context context) throws AluminumException {
			return argument.getValue(type, context);
		}
	}

	private static class EmptyActionBody implements ActionBody {
		public ActionBody copy() {
			return this;
		}

		public void invoke(Context context, Writer writer) {}
	}
}