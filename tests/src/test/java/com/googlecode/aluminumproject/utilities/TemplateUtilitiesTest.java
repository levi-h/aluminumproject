/*
 * Copyright 2012 Aluminum project
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
package com.googlecode.aluminumproject.utilities;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.DefaultContext;
import com.googlecode.aluminumproject.templates.AbstractTemplateElement;
import com.googlecode.aluminumproject.templates.Template;
import com.googlecode.aluminumproject.templates.TemplateBuilder;
import com.googlecode.aluminumproject.templates.TemplateElement;
import com.googlecode.aluminumproject.templates.TemplateInformation;
import com.googlecode.aluminumproject.writers.Writer;

import java.util.Collections;

import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
@Test(groups = {"utilities", "fast"})
public class TemplateUtilitiesTest {
	public void templateShouldBeFindableInContext() {
		TemplateElement templateElement = new AbstractTemplateElement(Collections.<String, String>emptyMap(), -1) {
			protected void processAsCurrent(Context context, Writer writer) {}
		};

		TemplateBuilder templateBuilder = new TemplateBuilder();
		templateBuilder.addTemplateElement(templateElement);
		Template template = templateBuilder.build();

		Context context = new DefaultContext();
		TemplateInformation.from(context).setTemplate(template, "test", "test");

		assert TemplateUtilities.findTemplate(templateElement, context) == template;
	}

	public void templateShouldBeFindableInParentContext() {
		TemplateElement templateElement = new AbstractTemplateElement(Collections.<String, String>emptyMap(), -1) {
			protected void processAsCurrent(Context context, Writer writer) {}
		};

		TemplateBuilder templateBuilder = new TemplateBuilder();
		templateBuilder.addTemplateElement(templateElement);
		Template template = templateBuilder.build();

		Context context = new DefaultContext();
		TemplateInformation.from(context).setTemplate(template, "test", "test");

		Context subcontext = context.createSubcontext();
		TemplateInformation.from(subcontext).setTemplate(new TemplateBuilder().build(), "subtest", "test");

		assert TemplateUtilities.findTemplate(templateElement, subcontext) == template;
	}

	@Test(expectedExceptions = AluminumException.class)
	public void tryingToFindNonContainingTemplateShouldCauseException() {
		TemplateElement templateElement = new AbstractTemplateElement(Collections.<String, String>emptyMap(), -1) {
			protected void processAsCurrent(Context context, Writer writer) {}
		};

		Context context = new DefaultContext();
		TemplateInformation.from(context).setTemplate(new TemplateBuilder().build(), "test", "test");

		TemplateUtilities.findTemplate(templateElement, context);
	}
}