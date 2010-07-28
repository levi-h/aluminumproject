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

import com.googlecode.aluminumproject.templates.test.TestActionElement;

import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("all")
@Test(groups = {"core", "fast"})
public class TemplateBuilderTest {
	private TemplateBuilder templateBuilder;

	@BeforeMethod
	public void createTemplateBuilder() {
		templateBuilder = new TemplateBuilder();
	}

	@Test(expectedExceptions = TemplateException.class)
	public void restoringCurrentTemplateElementWithoutHavingAddedOneShouldCauseException() {
		templateBuilder.restoreCurrentTemplateElement();
	}

	public void buildingEmptyTemplateShouldBePossible() {
		assert templateBuilder.build() != null;
	}

	@Test(dependsOnMethods = "buildingEmptyTemplateShouldBePossible", expectedExceptions = TemplateException.class)
	public void buildingTemplateMoreThanOnceShouldCauseException() {
		templateBuilder.build();
		templateBuilder.build();
	}

	@Test(dependsOnMethods = "buildingEmptyTemplateShouldBePossible", expectedExceptions = TemplateException.class)
	public void restoringCurrentTemplateElementAfterBuildingTemplateShouldCauseException() {
		templateBuilder.build();

		templateBuilder.restoreCurrentTemplateElement();
	}

	@Test(dependsOnMethods = "buildingEmptyTemplateShouldBePossible", expectedExceptions = TemplateException.class)
	public void addingTemplateElementAfterBuildingTemplateShouldCauseException() {
		templateBuilder.build();

		templateBuilder.addTemplateElement(new TestActionElement());
	}

	public void builtTemplateShouldBeAbleToRetrieveParent() {
		TemplateElement parent = new TestActionElement();
		TemplateElement child = new TestActionElement();

		templateBuilder.addTemplateElement(parent);
		templateBuilder.addTemplateElement(child);

		Template template = templateBuilder.build();
		assert template.getParent(parent) == null;
		assert template.getParent(child) == parent;
	}

	public void builtTemplateShouldBeAbleToRetrieveChildren() {
		TemplateElement parent = new TestActionElement();
		TemplateElement firstChild = new TestActionElement();
		TemplateElement secondChild = new TestActionElement();

		templateBuilder.addTemplateElement(parent);
		templateBuilder.addTemplateElement(firstChild);
		templateBuilder.restoreCurrentTemplateElement();
		templateBuilder.addTemplateElement(secondChild);

		List<TemplateElement> children = templateBuilder.build().getChildren(parent);
		assert children != null;
		assert children.size() == 2;
		assert children.contains(firstChild);
		assert children.contains(secondChild);
	}

	@Test(dependsOnMethods = "buildingEmptyTemplateShouldBePossible", expectedExceptions = TemplateException.class)
	public void retrievingParentOfUnknownTemplateElementShouldCauseException() {
		templateBuilder.build().getParent(new TestActionElement());
	}

	@Test(dependsOnMethods = "buildingEmptyTemplateShouldBePossible", expectedExceptions = TemplateException.class)
	public void retrievingChildrenOfUnknownTemplateElementShouldCauseException() {
		templateBuilder.build().getChildren(new TestActionElement());
	}
}