/*
 * Copyright 2009-2013 Aluminum project
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
package com.googlecode.aluminumproject.libraries;

import com.googlecode.aluminumproject.Aluminum;
import com.googlecode.aluminumproject.configuration.ConfigurationParameters;
import com.googlecode.aluminumproject.configuration.DefaultConfiguration;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.DefaultContext;
import com.googlecode.aluminumproject.parsers.aluscript.AluScriptParser;
import com.googlecode.aluminumproject.parsers.xml.XmlParser;
import com.googlecode.aluminumproject.writers.StringWriter;
import com.googlecode.aluminumproject.writers.TextWriter;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;

@SuppressWarnings("javadoc")
@UseAluScriptParser
@UseConfigurationParameters({
	@UseConfigurationParameter(name = XmlParser.TEMPLATE_EXTENSION, value = "xml"),
	@UseConfigurationParameter(name = AluScriptParser.TEMPLATE_EXTENSION, value = "alu"),
})
public abstract class LibraryTest {
	private static Map<Map<String, String>, Aluminum> engines = new HashMap<Map<String, String>, Aluminum>();

	private Aluminum engine;
	private String parser;

	@BeforeClass
	public final void selectEngine() {
		Map<String, String> configurationParametersToUse = new HashMap<String, String>();
		addConfigurationParametersToUse(getClass(), configurationParametersToUse);

		if (!engines.containsKey(configurationParametersToUse)) {
			ConfigurationParameters configurationParameters = new ConfigurationParameters();

			for (String name: configurationParametersToUse.keySet()) {
				configurationParameters.addParameter(name, configurationParametersToUse.get(name));
			}

			engines.put(configurationParametersToUse, new Aluminum(new DefaultConfiguration(configurationParameters)));
		}

		engine = engines.get(configurationParametersToUse);
	}

	private void addConfigurationParametersToUse(Class<?> testClass, Map<String, String> configurationParametersToUse) {
		Class<?> superclass = testClass.getSuperclass();

		if (superclass != null) {
			addConfigurationParametersToUse(superclass, configurationParametersToUse);
		}

		if (testClass.isAnnotationPresent(UseConfigurationParameters.class)) {
			for (UseConfigurationParameter annotation:
					testClass.getAnnotation(UseConfigurationParameters.class).value()) {
				configurationParametersToUse.put(annotation.name(), annotation.value());
			}
		}

		if (testClass.isAnnotationPresent(UseConfigurationParameter.class)) {
			UseConfigurationParameter annotation = testClass.getAnnotation(UseConfigurationParameter.class);

			configurationParametersToUse.put(annotation.name(), annotation.value());
		}
	}

	@BeforeClass
	public final void selectParser() {
		String parser = null;

		Class<?> testClass = getClass();

		while ((parser == null) && (testClass != null)) {
			if (testClass.isAnnotationPresent(UseAluScriptParser.class)) {
				parser = "aluscript";
			} else if (testClass.isAnnotationPresent(UseXmlParser.class)) {
				parser = "xml";
			} else {
				testClass = testClass.getSuperclass();
			}
		}

		this.parser = parser;
	}

	@AfterSuite
	public final static void stopTemplateEngines() {
		for (Aluminum engine: engines.values()) {
			engine.stop();
		}
	}

	protected final String processTemplate(String name) {
		return processTemplate(name, new DefaultContext());
	}

	protected final String processTemplate(String name, Context context) {
		StringWriter stringWriter = new StringWriter();

		engine.processTemplate(name, parser, context, new TextWriter(stringWriter, true));

		return stringWriter.getString();
	}
}