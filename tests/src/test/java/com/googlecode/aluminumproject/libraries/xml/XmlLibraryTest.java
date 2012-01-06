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
package com.googlecode.aluminumproject.libraries.xml;

import com.googlecode.aluminumproject.configuration.ConfigurationParameters;
import com.googlecode.aluminumproject.libraries.LibraryTest;
import com.googlecode.aluminumproject.parsers.aluscript.AluScriptParser;

@SuppressWarnings("javadoc")
public abstract class XmlLibraryTest extends LibraryTest {
	public XmlLibraryTest() {
		super("templates/xml", "aluscript");
	}

	@Override
	protected void addConfigurationParameters(ConfigurationParameters configurationParameters) {
		configurationParameters.addParameter(AluScriptParser.AUTOMATIC_NEWLINES, "false");
	}

	protected String createXml(String... lines) {
		StringBuilder xmlBuilder = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");

		for (String line: lines) {
			xmlBuilder.append("\n").append(line);
		}

		return xmlBuilder.toString();
	}
}