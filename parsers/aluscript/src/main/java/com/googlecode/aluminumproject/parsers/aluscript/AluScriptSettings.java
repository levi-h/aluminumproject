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
package com.googlecode.aluminumproject.parsers.aluscript;

import com.googlecode.aluminumproject.parsers.TemplateNameTranslator;
import com.googlecode.aluminumproject.parsers.aluscript.instructions.NewlineInstruction;
import com.googlecode.aluminumproject.utilities.Logger;

/**
 * Settings for AluScript templates.
 */
public class AluScriptSettings {
	private boolean automaticNewlines;

	private TemplateNameTranslator templateNameTranslator;

	private final Logger logger;

	/**
	 * Creates default AluScript settings.
	 */
	public AluScriptSettings() {
		templateNameTranslator = new AluScriptTemplateNameTranslator();

		automaticNewlines = true;

		logger = Logger.get(getClass());
	}

	/**
	 * Returns the template name translator. The default template name translator will not change any names.
	 *
	 * @return the template name translator used
	 */
	public TemplateNameTranslator getTemplateNameTranslator() {
		return templateNameTranslator;
	}

	/**
	 * Overrides the default <em>template name translator</em> setting.
	 *
	 * @param templateNameTranslator the template name translator that should be used
	 */
	public void setTemplateNameTranslator(TemplateNameTranslator templateNameTranslator) {
		logger.debug("using ", templateNameTranslator);

		this.templateNameTranslator = templateNameTranslator;
	}

	/**
	 * Determines whether newlines are added automatically ({@code true} by default) after text lines.
	 *
	 * @return {@code true} if newlines should be added by the parser, {@code false} if they have to be inserted by the
	 *         template author
	 * @see NewlineInstruction
	 */
	public boolean isAutomaticNewlines() {
		return automaticNewlines;
	}

	/**
	 * Overrides the default <em>automatic newlines</em> setting.
	 *
	 * @param automaticNewlines whether the parser ({@code true}, the default) or template authors ({@code false}) have
	 *                          to insert newlines
	 */
	public void setAutomaticNewlines(boolean automaticNewlines) {
		logger.debug("using ", automaticNewlines ? "automatic" : "manual", " newlines");

		this.automaticNewlines = automaticNewlines;
	}
}