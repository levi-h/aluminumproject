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
package com.googlecode.aluminumproject.libraries.actions;

import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.configuration.ConfigurationElementFactory;
import com.googlecode.aluminumproject.configuration.ConfigurationException;
import com.googlecode.aluminumproject.configuration.ConfigurationParameters;
import com.googlecode.aluminumproject.libraries.AbstractLibraryElement;
import com.googlecode.aluminumproject.utilities.StringUtilities;

/**
 * An {@link ActionContributionFactory action contribution factory} that's based on a {@link ActionContribution action
 * contribution} class and examines it to retrieve information about it. Normally, reflection is used to obtain the
 * action contribution information, though this can be influenced by annotating the action contribution class.
 * <p>
 * The name of an action contribution class annotated with {@link
 * com.googlecode.aluminumproject.annotations.ActionContributionInformation &#64;ActionContributionInformation} will be
 * the value of the {@code name} attribute. For unannotated action contribution classes, the lower cased {@link
 * StringUtilities#humanise(String) humanised} class name will become the name (this means the the action contribution
 * {@code OnDays} would get the name <em>on days</em>).
 * <p>
 * The action contribution will be created by the {@link ConfigurationElementFactory configuration element factory}
 * in the configuration.
 *
 * @author levi_h
 */
public class DefaultActionContributionFactory extends AbstractLibraryElement implements ActionContributionFactory {
	private Class<? extends ActionContribution> actionContributionClass;

	private Configuration configuration;

	private ActionContributionInformation information;

	/**
	 * Creates a default action contribution factory.
	 *
	 * @param actionContributionClass the class of the action contribution to create
	 */
	public DefaultActionContributionFactory(Class<? extends ActionContribution> actionContributionClass) {
		this.actionContributionClass = actionContributionClass;
	}

	public void initialise(Configuration configuration, ConfigurationParameters parameters) {
		this.configuration = configuration;

		information = new ActionContributionInformation(getActionContributionName());
	}

	private String getActionContributionName() {
		String name;

		Class<com.googlecode.aluminumproject.annotations.ActionContributionInformation> informationClass =
			com.googlecode.aluminumproject.annotations.ActionContributionInformation.class;

		if (actionContributionClass.isAnnotationPresent(informationClass)) {
			name = actionContributionClass.getAnnotation(informationClass).name();
		} else {
			name = StringUtilities.humanise(actionContributionClass.getSimpleName()).toLowerCase();
		}

		logger.debug("using name '", name, "' for action contribution class ", actionContributionClass.getName());

		return name;
	}

	public ActionContributionInformation getInformation() {
		return information;
	}

	public ActionContribution create() throws ActionException {
		ActionContribution actionContribution;

		try {
			ConfigurationElementFactory configurationElementFactory = configuration.getConfigurationElementFactory();

			actionContribution =
				configurationElementFactory.instantiate(actionContributionClass.getName(), ActionContribution.class);
		} catch (ConfigurationException exception) {
			throw new ActionException(exception, "can't create action contribution");
		}

		logger.debug("created action contribution ", actionContribution);

		return actionContribution;
	}
}