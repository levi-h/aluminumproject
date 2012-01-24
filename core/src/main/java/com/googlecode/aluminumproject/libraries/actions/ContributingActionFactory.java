/*
 * Copyright 2010-2012 Aluminum project
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
package com.googlecode.aluminumproject.libraries.actions;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.libraries.AbstractLibraryElement;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Creates actions that, when executed, make an action contribution.
 */
public class ContributingActionFactory extends AbstractLibraryElement implements ActionFactory {
	private Class<? extends ActionContribution> actionContributionClass;
	private ActionContributionInformation actionContributionInformation;

	private ActionInformation actionInformation;

	/**
	 * Creates a contributing action factory.
	 *
	 * @param actionContributionClass the class of the action contribution that will be made
	 * @param actionContributionInformation information about the action contribution
	 */
	public ContributingActionFactory(Class<? extends ActionContribution> actionContributionClass,
			ActionContributionInformation actionContributionInformation) {
		this.actionContributionClass = actionContributionClass;
		this.actionContributionInformation = actionContributionInformation;
	}

	@Override
	public void initialise(Configuration configuration) throws AluminumException {
		super.initialise(configuration);

		String actionName = actionContributionInformation.getName();
		String parameterName = actionContributionInformation.getParameterNameWhenAction();
		Type parameterType = actionContributionInformation.getParameterType();

		logger.debug("using action name '", actionName, "', parameter name '", parameterName, "', ",
			"and parameter type ", parameterType, " for action contribution class ", actionContributionClass.getName());

		List<ActionParameterInformation> actionParameterInformation =
			Collections.singletonList(new ActionParameterInformation(parameterName, parameterType, true, null));

		actionInformation = new ActionInformation(actionName, actionParameterInformation, false, null);
	}

	public ActionInformation getInformation() {
		return actionInformation;
	}

	public Action create(Map<String, ActionParameter> parameters, Context context) throws AluminumException {
		String parameterName = actionContributionInformation.getParameterNameWhenAction();

		if ((parameters.size() != 1) || !parameters.containsKey(parameterName)) {
			throw new AluminumException("expected single parameter named '", parameterName, "'");
		}

		Action action = new ContributingAction(actionContributionClass,
			actionContributionInformation.getParameterNameWhenAction(), parameters.get(parameterName));

		injectFields(action);

		logger.debug("created contributing action for action contribution class ", actionContributionClass.getName());

		return action;
	}
}