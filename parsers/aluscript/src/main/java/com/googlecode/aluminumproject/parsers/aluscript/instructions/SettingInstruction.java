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
package com.googlecode.aluminumproject.parsers.aluscript.instructions;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.parsers.TemplateNameTranslator;
import com.googlecode.aluminumproject.parsers.aluscript.AluScriptContext;
import com.googlecode.aluminumproject.parsers.aluscript.AluScriptSettings;
import com.googlecode.aluminumproject.utilities.ReflectionUtilities;
import com.googlecode.aluminumproject.utilities.StringUtilities;
import com.googlecode.aluminumproject.utilities.Utilities;

import java.util.Map;

/**
 * Changes a parser setting. Please not that the change is not scoped, but will last for the rest of the template.
 * <p>
 * The name of the instruction is {@value #NAME}; the name of the parameters are treated as if they were action
 * parameters (i.e. run through the {@link TemplateNameTranslator template name translator} of the current AluScript
 * settings), then {@link StringUtilities#camelCase(String) transformed into camel-case} and then used as properties on
 * the AluScript settings. That means that with the default settings, to change the {@code automaticNewlines} setting,
 * {@code "automatic newlines"} should be used as setting parameter name.
 */
public class SettingInstruction extends AbstractInstruction {
	/**
	 * Creates a setting instruction.
	 */
	public SettingInstruction() {
		super(NAME);
	}

	public void execute(Map<String, String> parameters, AluScriptContext context) throws AluminumException {
		AluScriptSettings settings = context.getSettings();

		TemplateNameTranslator templateNameTranslator = settings.getTemplateNameTranslator();

		for (String setting: parameters.keySet()) {
			String propertyName =
				StringUtilities.camelCase(templateNameTranslator.translateActionParameterName(setting));

			Class<Object> propertyType =
				Utilities.typed(ReflectionUtilities.getPropertyType(AluScriptSettings.class, propertyName));

			Object value =
				context.getConfiguration().getConverterRegistry().convert(parameters.get(setting), propertyType);

			ReflectionUtilities.setProperty(settings, propertyType, propertyName, value);
		}
	}

	/** The name of the setting instruction: {@value}. */
	public final static String NAME = "setting";
}