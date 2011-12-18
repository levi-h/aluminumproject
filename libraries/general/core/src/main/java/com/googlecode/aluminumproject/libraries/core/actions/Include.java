/*
 * Copyright 2009-2011 Levi Hoogenberg
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

import com.googlecode.aluminumproject.annotations.Injected;
import com.googlecode.aluminumproject.annotations.Required;
import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.ContextException;
import com.googlecode.aluminumproject.libraries.actions.AbstractDynamicallyParameterisableAction;
import com.googlecode.aluminumproject.libraries.actions.ActionException;
import com.googlecode.aluminumproject.libraries.actions.ActionParameter;
import com.googlecode.aluminumproject.libraries.core.actions.Blocks.Block;
import com.googlecode.aluminumproject.libraries.core.actions.Blocks.BlockContents;
import com.googlecode.aluminumproject.templates.TemplateException;
import com.googlecode.aluminumproject.templates.TemplateInformation;
import com.googlecode.aluminumproject.templates.TemplateProcessor;
import com.googlecode.aluminumproject.writers.NullWriter;
import com.googlecode.aluminumproject.writers.Writer;
import com.googlecode.aluminumproject.writers.WriterException;

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
public class Include extends AbstractDynamicallyParameterisableAction {
	private @Required String name;
	private String parser;

	private @Injected Configuration configuration;

	/**
	 * Creates an <em>include</em> action.
	 */
	public Include() {}

	public void execute(Context context, Writer writer) throws ActionException, ContextException, WriterException {
		getBody().invoke(context, new NullWriter());

		String parser = this.parser;

		if (parser == null) {
			try {
				parser = TemplateInformation.from(context).getParser();
			} catch (TemplateException exception) {
				throw new ActionException(exception, "can't obtain parser");
			}
		}

		Context subcontext = context.createSubcontext();

		for (Map.Entry<String, ActionParameter> variable: getDynamicParameters().entrySet()) {
			String variableName = variable.getKey();
			Object variableValue = variable.getValue().getValue(Object.class, context);

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
}