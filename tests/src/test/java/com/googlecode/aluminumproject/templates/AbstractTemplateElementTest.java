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
package com.googlecode.aluminumproject.templates;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.DefaultContext;
import com.googlecode.aluminumproject.writers.NullWriter;
import com.googlecode.aluminumproject.writers.Writer;

import java.util.Collections;

import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
@Test(groups = {"core", "fast"})
public class AbstractTemplateElementTest {
	public void processExceptionShouldIncludeOrigin() {
		TemplateElement templateElement = new AbstractTemplateElement(Collections.<String, String>emptyMap(), 1) {
			protected void processAsCurrent(Context context, Writer writer) {
				throw new AluminumException("can't process element");
			}
		};

		TemplateBuilder templateBuilder = new TemplateBuilder("test");
		templateBuilder.addTemplateElement(templateElement);
		Template template = templateBuilder.build();

		Context context = new DefaultContext();

		TemplateInformation templateInformation = TemplateInformation.from(context);
		templateInformation.setTemplate(template, "test");
		templateInformation.addTemplateElement(templateElement);

		AluminumException exception;

		try {
			templateElement.process(context, new NullWriter());

			exception = null;
		} catch (AluminumException processException) {
			exception = processException;
		}

		assert exception != null;

		String origin = exception.getOrigin();
		assert origin != null;
		assert origin.equals("test, line 1");
	}
}