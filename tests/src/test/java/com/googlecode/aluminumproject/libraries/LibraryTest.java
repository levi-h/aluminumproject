/*
 * Copyright 2009-2010 Levi Hoogenberg
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
import com.googlecode.aluminumproject.parsers.xml.XmlParser;
import com.googlecode.aluminumproject.resources.ClassPathTemplateFinderFactory;
import com.googlecode.aluminumproject.writers.NullWriter;
import com.googlecode.aluminumproject.writers.StringWriter;
import com.googlecode.aluminumproject.writers.TextWriter;

import org.testng.annotations.BeforeMethod;

@SuppressWarnings("all")
public abstract class LibraryTest {
	private Aluminum engine;

	private String templatePath;

	protected LibraryTest(String templatePath) {
		this.templatePath = templatePath;
	}

	@BeforeMethod
	public final void createTemplateEngine() {
		ConfigurationParameters configurationParameters = new ConfigurationParameters();
		configurationParameters.addParameter(ClassPathTemplateFinderFactory.TEMPLATE_PATH, templatePath);
		configurationParameters.addParameter(XmlParser.TEMPLATE_EXTENSION, "xml");

		engine = new Aluminum(new DefaultConfiguration(configurationParameters));
	}

	protected final String processTemplate(String name, Context context) {
		StringWriter stringWriter = new StringWriter();

		engine.processTemplate(name, "xml", context, new TextWriter(stringWriter, true));

		return stringWriter.getString();
	}

	protected final void processTemplateIgnoringOutput(String name, Context context) {
		engine.processTemplate(name, "xml", context, new NullWriter());
	}
}