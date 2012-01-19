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

import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.configuration.ConfigurationParameters;
import com.googlecode.aluminumproject.configuration.TestConfiguration;
import com.googlecode.aluminumproject.expressions.ExpressionFactory;
import com.googlecode.aluminumproject.expressions.TestExpressionFactory;
import com.googlecode.aluminumproject.libraries.Library;
import com.googlecode.aluminumproject.libraries.TestLibrary;
import com.googlecode.aluminumproject.parsers.aluscript.instructions.Instruction;
import com.googlecode.aluminumproject.parsers.aluscript.instructions.TestInstruction;
import com.googlecode.aluminumproject.templates.DefaultTemplateElementFactory;
import com.googlecode.aluminumproject.templates.TemplateElement;
import com.googlecode.aluminumproject.templates.TemplateElementFactory;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * An AluScript context that can be used in tests.
 */
public class TestAluScriptContext extends AluScriptContext {
	private List<TemplateElement> templateElements;

	/**
	 * Creates a test AluScript context.
	 */
	public TestAluScriptContext() {
		super(createConfiguration(), "test",
			new AluScriptSettings(), Arrays.<Instruction>asList(new TestInstruction()));

		templateElements = new LinkedList<TemplateElement>();
	}

	private static Configuration createConfiguration() {
		TestConfiguration configuration = new TestConfiguration(new ConfigurationParameters());

		TemplateElementFactory templateElementFactory = new DefaultTemplateElementFactory();
		templateElementFactory.initialise(configuration);
		configuration.setTemplateElementFactory(templateElementFactory);

		Library library = new TestLibrary();
		library.initialise(configuration);
		configuration.addLibrary(library);

		ExpressionFactory expressionFactory = new TestExpressionFactory();
		expressionFactory.initialise(configuration);
		configuration.addExpressionFactory(expressionFactory);

		return configuration;
	}

	@Override
	public void addTemplateElement(TemplateElement templateElement) {
		super.addTemplateElement(templateElement);

		templateElements.add(templateElement);
	}

	/**
	 * Returns all added template elements.
	 *
	 * @return a list that contains all of the template elements that were added
	 */
	public List<TemplateElement> getTemplateElements() {
		return templateElements;
	}
}