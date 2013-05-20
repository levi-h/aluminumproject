/*
 * Copyright 2013 Aluminum project
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
package com.googlecode.aluminumproject.libraries.fragments;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.libraries.Library;
import com.googlecode.aluminumproject.libraries.actions.AbstractDynamicallyParameterisableAction;
import com.googlecode.aluminumproject.libraries.actions.Action;
import com.googlecode.aluminumproject.libraries.actions.ActionFactory;
import com.googlecode.aluminumproject.libraries.actions.ActionInformation;
import com.googlecode.aluminumproject.libraries.actions.ActionParameter;
import com.googlecode.aluminumproject.libraries.actions.ActionParameterInformation;
import com.googlecode.aluminumproject.templates.TemplateInformation;
import com.googlecode.aluminumproject.templates.TemplateProcessor;
import com.googlecode.aluminumproject.utilities.Logger;
import com.googlecode.aluminumproject.writers.Writer;

import java.util.Collections;
import java.util.Map;

/**
 * Includes a template by using an action name as its name. Context variables can be passed as dynamic parameters;
 * fragment actions can't contain a body.
 */
public class FragmentAction extends AbstractDynamicallyParameterisableAction {
	private Configuration configuration;

	private String name;
	private String parser;

	private FragmentAction(Configuration configuration, String name, String parser) {
		this.configuration = configuration;

		this.name = name;
		this.parser = parser;
	}

	public void execute(Context context, Writer writer) throws AluminumException {
		String parser = (this.parser == null) ? TemplateInformation.from(context).getParser() : this.parser;

		Context subcontext = context.createSubcontext();

		for (Map.Entry<String, ActionParameter> parameter: getDynamicParameters().entrySet()) {
			String variableName = parameter.getKey();
			Object variableValue = parameter.getValue().getValue(Object.class, context);

			logger.debug("setting variable '", variableName, "' to ", variableValue);

			subcontext.setVariable(variableName, variableValue);
		}

		logger.debug("including template '", name, "' using parser '", parser, "'");

		new TemplateProcessor(configuration).processTemplate(name, parser, subcontext, writer);
	}

	/**
	 * Creates fragment actions.
	 */
	public static class Factory implements ActionFactory {
		private String name;
		private String parser;

		private Configuration configuration;
		private Library library;

		private Logger logger;

		/**
		 * Instantiates a fragment action factory.
		 *
		 * @param name the (qualified) name of the fragment to include
		 * @param parser the name of the parser to use (may be {@code null})
		 */
		public Factory(String name, String parser) {
			this.name = name;
			this.parser = parser;

			logger = Logger.get(getClass());
		}

		public void initialise(Configuration configuration) {
			this.configuration = configuration;
		}

		public void setLibrary(Library library) {
			this.library = library;
		}

		public ActionInformation getInformation() {
			return new ActionInformation(name, Collections.<ActionParameterInformation>emptyList(), true, null);
		}

		public Library getLibrary() {
			return library;
		}

		public Action create(Map<String, ActionParameter> parameters, Context context) throws AluminumException {
			FragmentAction fragmentAction = new FragmentAction(configuration, name, parser);

			logger.debug("created fragment action ", fragmentAction);

			for (String parameterName: parameters.keySet()) {
				fragmentAction.setParameter(parameterName, parameters.get(parameterName));
			}

			logger.debug("added parameters to fragment action");

			return fragmentAction;
		}

		public void disable() {}
	}
}