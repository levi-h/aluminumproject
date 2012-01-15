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
package com.googlecode.aluminumproject.serialisers.xml;

import com.googlecode.aluminumproject.serialisers.ElementNameTranslator;

/**
 * The default element name translator. It replaces all spaces in a name with hyphens.
 */
public class XmlElementNameTranslator implements ElementNameTranslator {
	/**
	 * Creates an XML element name translator.
	 */
	public XmlElementNameTranslator() {}

	public String translateActionContributionName(String name) {
		return replaceSpacesWithHyphens(name);
	}

	public String translateActionName(String name) {
		return replaceSpacesWithHyphens(name);
	}

	public String translateActionParameterName(String name) {
		return replaceSpacesWithHyphens(name);
	}

	private String replaceSpacesWithHyphens(String name) {
		return name.replace(' ', '-');
	}
}