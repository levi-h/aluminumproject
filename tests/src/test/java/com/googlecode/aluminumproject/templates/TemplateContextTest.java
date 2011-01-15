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
package com.googlecode.aluminumproject.templates;

import com.googlecode.aluminumproject.libraries.actions.Action;
import com.googlecode.aluminumproject.libraries.actions.TestAction;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("all")
@Test(groups = {"core", "fast"})
public class TemplateContextTest {
	private TemplateContext context;

	@BeforeMethod
	public void createTemplateContext() {
		context = new TemplateContext();
	}

	public void newTemplateContextShouldBeEmpty() {
		assert context.getCurrentTemplateElement() == null;
		assert context.getCurrentAction() == null;
	}

	@Test(dependsOnMethods = "newTemplateContextShouldBeEmpty",
		expectedExceptions = TemplateException.class)
	public void removingCurrentTemplateElementFromEmptyTemplateContextShouldCauseException() {
		context.removeCurrentTemplateElement();
	}

	public void addingTemplateElementShouldMakeItTheCurrentOne() {
		TemplateElement templateElement = new TestActionElement();
		context.addTemplateElement(templateElement);

		assert context.getCurrentTemplateElement() == templateElement;
	}

	@Test(dependsOnMethods = {"newTemplateContextShouldBeEmpty", "addingTemplateElementShouldMakeItTheCurrentOne"})
	public void removingCurrentTemplateElementShouldActuallyRemoveIt() {
		context.addTemplateElement(new TestActionElement());

		context.removeCurrentTemplateElement();

		assert context.getCurrentTemplateElement() == null;
	}

	@Test(dependsOnMethods = "addingTemplateElementShouldMakeItTheCurrentOne")
	public void removingCurrentTemplateElementShouldRestorePreviousCurrentTemplateElement() {
		TestActionElement templateElement = new TestActionElement();
		context.addTemplateElement(templateElement);
		context.addTemplateElement(new TestActionElement());

		context.removeCurrentTemplateElement();

		assert context.getCurrentTemplateElement() == templateElement;
	}

	@Test(dependsOnMethods = "newTemplateContextShouldBeEmpty",
		expectedExceptions = TemplateException.class)
		public void removingCurrentActionFromEmptyTemplateContextShouldCauseException() {
		context.removeCurrentAction();
	}

	public void addingActionShouldMakeItTheCurrentOne() {
		Action action = new TestAction();
		context.addAction(action);

		assert context.getCurrentAction() == action;
	}

	@Test(dependsOnMethods = {"newTemplateContextShouldBeEmpty", "addingActionShouldMakeItTheCurrentOne"})
	public void removingCurrentActionShouldActuallyRemoveIt() {
		context.addAction(new TestAction());

		context.removeCurrentAction();

		assert context.getCurrentAction() == null;
	}

	@Test(dependsOnMethods = "addingActionShouldMakeItTheCurrentOne")
	public void removingCurrentActionShouldRestorePreviousCurrentAction() {
		TestAction action = new TestAction();
		context.addAction(action);
		context.addAction(new TestAction());

		context.removeCurrentAction();

		assert context.getCurrentAction() == action;
	}

	@Test(dependsOnMethods = "addingTemplateElementShouldMakeItTheCurrentOne")
	public void copyingTemplateContextShouldCopyTemplateElements() {
		TestActionElement templateElement = new TestActionElement();
		context.addTemplateElement(templateElement);

		assert new TemplateContext(context).getCurrentTemplateElement() == templateElement;
	}

	@Test(dependsOnMethods = "addingActionShouldMakeItTheCurrentOne")
	public void copyingTemplateContextShouldCopyActions() {
		TestAction action = new TestAction();
		context.addAction(action);

		assert new TemplateContext(context).getCurrentAction() == action;
	}

	@Test(dependsOnMethods = {
		"removingCurrentTemplateElementShouldRestorePreviousCurrentTemplateElement",
		"copyingTemplateContextShouldCopyTemplateElements",
		"copyingTemplateContextShouldCopyActions"
	})
	public void changingTemplateContextShouldNotAffectCopy() {
		TestActionElement templateElement = new TestActionElement();
		TestAction action = new TestAction();

		context.addTemplateElement(templateElement);
		context.addAction(action);

		TemplateContext copy = new TemplateContext(context);

		context.removeCurrentTemplateElement();
		context.addAction(new TestAction());

		assert copy.getCurrentTemplateElement() == templateElement;
		assert copy.getCurrentAction() == action;
	}
}