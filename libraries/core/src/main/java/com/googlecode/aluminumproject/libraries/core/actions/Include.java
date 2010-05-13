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

import com.googlecode.aluminumproject.annotations.Ignored;
import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.configuration.ConfigurationParameters;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.ContextException;
import com.googlecode.aluminumproject.converters.ConverterException;
import com.googlecode.aluminumproject.libraries.actions.AbstractAction;
import com.googlecode.aluminumproject.libraries.actions.AbstractActionFactory;
import com.googlecode.aluminumproject.libraries.actions.Action;
import com.googlecode.aluminumproject.libraries.actions.ActionException;
import com.googlecode.aluminumproject.libraries.actions.ActionInformation;
import com.googlecode.aluminumproject.libraries.actions.ActionParameter;
import com.googlecode.aluminumproject.libraries.actions.ActionParameterInformation;
import com.googlecode.aluminumproject.templates.TemplateException;
import com.googlecode.aluminumproject.templates.TemplateProcessor;
import com.googlecode.aluminumproject.utilities.Utilities;
import com.googlecode.aluminumproject.writers.NullWriter;
import com.googlecode.aluminumproject.writers.Writer;
import com.googlecode.aluminumproject.writers.WriterException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Includes a template.
 * <p>
 * The action has two parameters: the name of the included template and the name of the parser that should be used for
 * it. The parser parameter may be omitted; in that case, the parser that is used for the template that contains the
 * include action will be used to parse the included template as well.
 * <p>
 * The include action supports dynamic parameters, all of which will be made available as variables in the included
 * template's context. It's also possible to pass blocks to included templates by using the {@link Block block} action;
 * these blocks can be used through the {@link BlockContents block contents} action.
 *
 * @author levi_h
 */
@Ignored
public class Include extends AbstractAction {
	private Configuration configuration;

	private String name;
	private String parser;

	private Map<String, Object> variables;

	/**
	 * Creates an <em>include</em> action.
	 *
	 * @param configuration the configuration used
	 * @param name the name of the template to include
	 * @param parser the name of the parser to use (may be {@code null})
	 * @param variables the variables that should be available in the included template
	 */
	public Include(Configuration configuration, String name, String parser, Map<String, Object> variables) {
		this.configuration = configuration;

		this.name = name;
		this.parser = parser;

		this.variables = variables;
	}

	public void execute(Context context, Writer writer) throws ActionException, ContextException, WriterException {
		String parser = this.parser;

		if (parser == null) {
			Map<String, Object> templateInformation =
				Utilities.typed(context.getImplicitObject(Context.ALUMINUM_IMPLICIT_OBJECT));

			parser = (String) templateInformation.get(TemplateProcessor.TEMPLATE_PARSER);
		}

		Context subcontext = context.createSubcontext();

		getBody().invoke(subcontext, new NullWriter());

		for (Map.Entry<String, Object> variable: variables.entrySet()) {
			String variableName = variable.getKey();
			Object variableValue = variable.getValue();

			logger.debug("setting variable '", variableName, "' to ", variableValue);

			subcontext.setVariable(variableName, variableValue);
		}

		logger.debug("including template '", name, "' using parser '", parser, "'");

		try {
			new TemplateProcessor(configuration).processTemplate(name, parser, subcontext, writer);
		} catch (TemplateException exception) {
			throw new ActionException(exception, "can't include template '", name, "'");
		}
	}

	/**
	 * Creates <em>include</em> actions.
	 *
	 * @author levi_h
	 */
	public static class IncludeFactory extends AbstractActionFactory {
		private Configuration configuration;

		private ActionInformation information;

		/**
		 * Creates an include factory.
		 */
		public IncludeFactory() {}

		public void initialise(Configuration configuration, ConfigurationParameters parameters) {
			this.configuration = configuration;

			information = new ActionInformation("include", Arrays.asList(
				new ActionParameterInformation("name", String.class, true),
				new ActionParameterInformation("parser", String.class, false)
			), true);
		}

		public ActionInformation getInformation() {
			return information;
		}

		public Action create(Map<String, ActionParameter> parameters, Context context) throws ActionException {
			parameters = new HashMap<String, ActionParameter>(parameters);

			checkParameters(parameters);

			String name = removeParameter(parameters, context, "name", String.class);
			String parser = removeParameter(parameters, context, "parser", String.class);

			return new Include(configuration, name, parser, convertParameters(parameters, context));
		}

		private Map<String, Object> convertParameters(
				Map<String, ActionParameter> parameters, Context context) throws ActionException {
			Map<String, Object> convertedParameters = new HashMap<String, Object>();

			for (Map.Entry<String, ActionParameter> parameter: parameters.entrySet()) {
				try {
					convertedParameters.put(parameter.getKey(), parameter.getValue().getValue(Object.class, context));
				} catch (ConverterException exception) {
					throw new ActionException(exception, "can't convert parameter '", parameter.getKey(), "'");
				}
			}

			return convertedParameters;
		}
	}
}