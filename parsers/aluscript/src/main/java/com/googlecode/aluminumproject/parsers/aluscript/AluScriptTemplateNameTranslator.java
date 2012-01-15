/*
 * Copyright 2010-2012 Levi Hoogenberg
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

/**
 * The default template name translator. It keeps all names as is.
 */
public class AluScriptTemplateNameTranslator implements TemplateNameTranslator {
	/**
	 * Creates an AluScript template name translator.
	 */
	public AluScriptTemplateNameTranslator() {}

	public String translateActionName(String name) {
		return name;
	}

	public String translateActionParameterName(String name) {
		return name;
	}

	public String translateActionContributionName(String name) {
		return name;
	}
}