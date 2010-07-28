/*
 * Copyright 2010 Levi Hoogenberg
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
package com.googlecode.aluminumproject.parsers.aluscript.instructions;

import com.googlecode.aluminumproject.libraries.actions.ActionParameter;
import com.googlecode.aluminumproject.parsers.TemplateNameTranslator;
import com.googlecode.aluminumproject.parsers.aluscript.AluScriptContext;
import com.googlecode.aluminumproject.parsers.aluscript.AluScriptException;
import com.googlecode.aluminumproject.templates.ActionContributionDescriptor;
import com.googlecode.aluminumproject.templates.ActionElement;
import com.googlecode.aluminumproject.templates.TemplateElement;
import com.googlecode.aluminumproject.templates.TemplateElementFactory;
import com.googlecode.aluminumproject.templates.TemplateException;
import com.googlecode.aluminumproject.utilities.ParserUtilities;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Creates an {@link ActionElement action element} and adds it to the context.
 *
 * @author levi_h
 */
public class ActionInstruction extends AbstractInstruction {
	private String namePrefix;
	private String name;

	/**
	 * Creates an action instruction.
	 *
	 * @param namePrefix the prefix of the action name (i.e. the abbreviation of the action library's URL)
	 * @param name the name of the action
	 */
	public ActionInstruction(String namePrefix, String name) {
		super("action");

		this.namePrefix = namePrefix;
		this.name = name;
	}

	public void execute(Map<String, String> parameters, AluScriptContext context) throws AluScriptException {
		TemplateElementFactory templateElementFactory = context.getConfiguration().getTemplateElementFactory();

		Map<String, String> libraryUrlAbbreviations = context.getLibraryUrlAbbreviations();

		String libraryUrl = getLibraryUrl(namePrefix, libraryUrlAbbreviations);

		Map<String, ActionParameter> actionParameters = new LinkedHashMap<String, ActionParameter>();
		List<ActionContributionDescriptor> contributionDescriptors = new LinkedList<ActionContributionDescriptor>();

		splitParameters(parameters, actionParameters, contributionDescriptors, context, libraryUrlAbbreviations);

		TemplateElement actionElement;

		try {
			String translatedName = context.getSettings().getTemplateNameTranslator().translateActionName(name);

			actionElement = templateElementFactory.createActionElement(
				libraryUrl, translatedName, actionParameters, contributionDescriptors, libraryUrlAbbreviations);
		} catch (TemplateException exception) {
			throw new AluScriptException(exception, "can't create action element for action ", namePrefix, ".", name);
		}

		context.addTemplateElement(actionElement);
	}

	private String getLibraryUrl(String abbreviation, Map<String, String> libraryUrlAbbreviations) {
		if (!libraryUrlAbbreviations.containsKey(abbreviation)) {
			throw new AluScriptException("can't find library URL with abbreviation '", abbreviation, "' ",
				"(available library URL abbreviations: ", libraryUrlAbbreviations, ")");
		}

		return libraryUrlAbbreviations.get(abbreviation);
	}

	private void splitParameters(Map<String, String> parameters,
			Map<String, ActionParameter> actionParameters, List<ActionContributionDescriptor> contributionDescriptors,
			AluScriptContext context, Map<String, String> libraryUrlAbbreviations) throws AluScriptException {
		TemplateNameTranslator templateNameTranslator = context.getSettings().getTemplateNameTranslator();

		for (String name: parameters.keySet()) {
			ActionParameter actionParameter =
				ParserUtilities.createParameter(parameters.get(name), context.getConfiguration());

			if (name.contains(".")) {
				String[] nameAndPrefix = name.split("\\.");

				String libraryUrl = getLibraryUrl(nameAndPrefix[0], libraryUrlAbbreviations);

				String translatedName = templateNameTranslator.translateActionContributionName(nameAndPrefix[1]);

				contributionDescriptors.add(
					new ActionContributionDescriptor(libraryUrl, translatedName, actionParameter));
			} else {
				actionParameters.put(templateNameTranslator.translateActionParameterName(name), actionParameter);
			}
		}
	}
}