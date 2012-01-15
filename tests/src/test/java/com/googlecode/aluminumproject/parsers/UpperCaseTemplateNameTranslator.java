/*
 * Copyright 2009-2012 Aluminum project
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
package com.googlecode.aluminumproject.parsers;

/**
 * Transforms all template names from upper to lower case.
 */
public class UpperCaseTemplateNameTranslator implements TemplateNameTranslator {
	/**
	 * Creates an upper case template name translator.
	 */
	public UpperCaseTemplateNameTranslator() {}

	public String translateActionName(String name) {
		return name.toLowerCase();
	}

	public String translateActionParameterName(String name) {
		return name.toLowerCase();
	}

	public String translateActionContributionName(String name) {
		return name.toLowerCase();
	}
}