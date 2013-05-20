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
package com.googlecode.aluminumproject.libraries;

import com.googlecode.aluminumproject.Aluminum;
import com.googlecode.aluminumproject.configuration.ConfigurationParameters;
import com.googlecode.aluminumproject.configuration.DefaultConfiguration;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.DefaultContext;
import com.googlecode.aluminumproject.finders.ClassPathTemplateFinder;
import com.googlecode.aluminumproject.parsers.aluscript.AluScriptParser;
import com.googlecode.aluminumproject.parsers.xml.XmlParser;
import com.googlecode.aluminumproject.writers.StringWriter;
import com.googlecode.aluminumproject.writers.TextWriter;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

@SuppressWarnings("javadoc")
public abstract class LibraryTest {
	private Aluminum engine;

	private String templatePath;
	private String parser;

	protected LibraryTest(String templatePath, String parser) {
		this.templatePath = templatePath;
		this.parser = parser;
	}

	@BeforeClass
	public final void createTemplateEngine() {
		ConfigurationParameters configurationParameters = new ConfigurationParameters();
		configurationParameters.addParameter(ClassPathTemplateFinder.TEMPLATE_PATH, templatePath);
		configurationParameters.addParameter(XmlParser.TEMPLATE_EXTENSION, "xml");
		configurationParameters.addParameter(AluScriptParser.TEMPLATE_EXTENSION, "alu");

		addConfigurationParameters(configurationParameters);

		engine = new Aluminum(new DefaultConfiguration(configurationParameters));
	}

	protected void addConfigurationParameters(ConfigurationParameters configurationParameters) {}

	@AfterClass
	public final void stopTemplateEngine() {
		engine.stop();
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