/*
 * Copyright 2009-2012 Levi Hoogenberg
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
package com.googlecode.aluminumproject.parsers.xml;

import com.googlecode.aluminumproject.parsers.TemplateNameTranslator;

/**
 * The default template name translator. It replaces all hypens in a name with spaces.
 */
public class XmlTemplateNameTranslator implements TemplateNameTranslator {
	/**
	 * Creates an XML template name translator.
	 */
	public XmlTemplateNameTranslator() {}

	public String translateActionContributionName(String name) {
		return replaceHyphensWithSpaces(name);
	}

	public String translateActionName(String name) {
		return replaceHyphensWithSpaces(name);
	}

	public String translateActionParameterName(String name) {
		return replaceHyphensWithSpaces(name);
	}

	private String replaceHyphensWithSpaces(String name) {
		return name.replace('-', ' ');
	}
}