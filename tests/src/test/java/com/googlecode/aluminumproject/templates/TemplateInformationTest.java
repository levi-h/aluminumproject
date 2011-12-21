/*
 * Copyright 2011 Levi Hoogenberg
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
import com.googlecode.aluminumproject.libraries.actions.Action;
import com.googlecode.aluminumproject.libraries.actions.TestAction;

import org.testng.annotations.Test;

@SuppressWarnings("all")
@Test(groups = {"core", "fast"})
public class TemplateInformationTest {
	public void obtainingTemplateInformationFromNewContextShouldCreateIt() {
		Context context = new DefaultContext();

		assert TemplateInformation.from(context) != null;
	}

	public void obtainingTemplateInformationShouldResultInSameInstance() {
		Context context = new DefaultContext();

		TemplateInformation firstTemplateInformation = TemplateInformation.from(context);
		TemplateInformation secondTemplateInformation = TemplateInformation.from(context);

		assert firstTemplateInformation == secondTemplateInformation;
	}

	@Test(dependsOnMethods = "obtainingTemplateInformationFromNewContextShouldCreateIt")
	public void templateShouldBeRemembered() {
		Template template = new TemplateBuilder().build();

		TemplateInformation templateInformation = TemplateInformation.from(new DefaultContext());
		templateInformation.setTemplate(template, "test", "code");

		assert templateInformation.getTemplate() == template;

		String name = templateInformation.getName();
		assert name != null;
		assert name.equals("test");

		String parser = templateInformation.getParser();
		assert parser != null;
		assert parser.equals("code");
	}

	@Test(dependsOnMethods = "obtainingTemplateInformationFromNewContextShouldCreateIt")
	public void newTemplateInformationShouldNotHaveCurrentTemplateElement() {
		assert TemplateInformation.from(new DefaultContext()).getCurrentTemplateElement() == null;

	}

	@Test(dependsOnMethods = "newTemplateInformationShouldNotHaveCurrentTemplateElement")
	public void addedTemplateElementShouldBecomeCurrentTemplateElement() {
		TestActionElement templateElement = new TestActionElement();

		TemplateInformation templateInformation = TemplateInformation.from(new DefaultContext());
		templateInformation.addTemplateElement(templateElement);

		assert templateInformation.getCurrentTemplateElement() == templateElement;
	}

	@Test(
		dependsOnMethods = "newTemplateInformationShouldNotHaveCurrentTemplateElement",
		expectedExceptions = AluminumException.class
	)
	public void removingCurrentTemplateElementWithoutPreviousCurrentTemplateElementShouldCauseException() {
		TemplateInformation.from(new DefaultContext()).removeCurrentTemplateElement();
	}

	@Test(dependsOnMethods = "addedTemplateElementShouldBecomeCurrentTemplateElement")
	public void removingTemplateElementShouldRevertToPreviousCurrentTemplateElement() {
		TemplateInformation templateInformation = TemplateInformation.from(new DefaultContext());
		templateInformation.addTemplateElement(new TestActionElement());
		templateInformation.removeCurrentTemplateElement();

		assert templateInformation.getCurrentTemplateElement() == null;
	}

	@Test(dependsOnMethods = "obtainingTemplateInformationFromNewContextShouldCreateIt")
	public void newTemplateInformationShouldNotHaveCurrentAction() {
		assert TemplateInformation.from(new DefaultContext()).getCurrentAction() == null;
	}

	@Test(dependsOnMethods = "newTemplateInformationShouldNotHaveCurrentAction")
	public void addedActionShouldBecomeCurrentAction() {
		Action action = new TestAction();

		TemplateInformation templateInformation = TemplateInformation.from(new DefaultContext());
		templateInformation.addAction(action);

		assert templateInformation.getCurrentAction() == action;
	}

	@Test(
		dependsOnMethods = "newTemplateInformationShouldNotHaveCurrentAction",
		expectedExceptions = AluminumException.class
	)
	public void removingCurrentActionWithoutPreviousCurrentActionShouldCauseException() {
		TemplateInformation.from(new DefaultContext()).removeCurrentAction();
	}

	@Test(dependsOnMethods = "addedActionShouldBecomeCurrentAction")
	public void removingCurrentActionShouldRevertToPreviousCurrentAction() {
		Action action = new TestAction();

		TemplateInformation templateInformation = TemplateInformation.from(new DefaultContext());
		templateInformation.addAction(action);
		templateInformation.addAction(new TestAction());
		templateInformation.removeCurrentAction();

		assert templateInformation.getCurrentAction() == action;
	}
}