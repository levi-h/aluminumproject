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

import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.libraries.actions.ActionContributionFactory;
import com.googlecode.aluminumproject.libraries.actions.ActionFactory;
import com.googlecode.aluminumproject.libraries.actions.ActionParameter;
import com.googlecode.aluminumproject.libraries.test.actions.TestActionFactory;
import com.googlecode.aluminumproject.templates.ActionElement;
import com.googlecode.aluminumproject.templates.Template;
import com.googlecode.aluminumproject.templates.TemplateContext;
import com.googlecode.aluminumproject.writers.Writer;

import java.util.Collections;
import java.util.Map;

/**
 * Am action element that can be used in tests.
 *
 * @author levi_h
 */
public class TestActionElement implements ActionElement {
	private ActionFactory factory;

	/**
	 * Creates a test action element.
	 */
	public TestActionElement() {
		factory = new TestActionFactory();
	}

	public Map<String, String> getLibraryUrlAbbreviations() {
		return Collections.emptyMap();
	}

	public ActionFactory getFactory() {
		return factory;
	}

	public Map<String, ActionParameter> getParameters() {
		return Collections.emptyMap();
	}

	public Map<ActionContributionFactory, ActionParameter> getContributionFactories() {
		return Collections.emptyMap();
	}

	public void process(Template template, TemplateContext templateContext, Context context, Writer writer) {}
}