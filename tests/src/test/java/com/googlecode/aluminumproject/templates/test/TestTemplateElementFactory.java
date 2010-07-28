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
package com.googlecode.aluminumproject.templates.test;

import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.configuration.ConfigurationParameters;
import com.googlecode.aluminumproject.expressions.ExpressionFactory;
import com.googlecode.aluminumproject.libraries.actions.ActionParameter;
import com.googlecode.aluminumproject.templates.ActionContributionDescriptor;
import com.googlecode.aluminumproject.templates.ActionElement;
import com.googlecode.aluminumproject.templates.ExpressionElement;
import com.googlecode.aluminumproject.templates.TemplateElementFactory;
import com.googlecode.aluminumproject.templates.TextElement;

import java.util.List;
import java.util.Map;

/**
 * A template element factory that can be used in tests.
 *
 * @author levi_h
 */
public class TestTemplateElementFactory implements TemplateElementFactory {
	private Configuration configuration;

	/**
	 * Creates a test template element factory.
	 */
	public TestTemplateElementFactory() {}

	public void initialise(Configuration configuration, ConfigurationParameters parameters) {
		this.configuration = configuration;
	}

	/**
	 * Returns the configuration that this template element factory was initialised with.
	 *
	 * @return this template element factory's configuration
	 */
	public Configuration getConfiguration() {
		return configuration;
	}

	public ActionElement createActionElement(String libraryUrl, String name, Map<String, ActionParameter> parameters,
			List<ActionContributionDescriptor> contributionDescriptors, Map<String, String> libraryUrlAbbreviations) {
		return new TestActionElement();
	}

	public TextElement createTextElement(String text, Map<String, String> libraryUrlAbbreviations) {
		return new TestTextElement();
	}

	public ExpressionElement createExpressionElement(
			ExpressionFactory expressionFactory, String text, Map<String, String> libraryUrlAbbreviations) {
		return new TestExpressionElement();
	}
}