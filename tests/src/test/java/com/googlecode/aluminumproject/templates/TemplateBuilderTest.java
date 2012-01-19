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
package com.googlecode.aluminumproject.templates;

import com.googlecode.aluminumproject.AluminumException;

import java.util.List;

import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
@Test(groups = {"core", "fast"})
public class TemplateBuilderTest {
	@Test(expectedExceptions = AluminumException.class)
	public void restoringCurrentTemplateElementWithoutHavingAddedOneShouldCauseException() {
		new TemplateBuilder("test").restoreCurrentTemplateElement();
	}

	public void buildingEmptyTemplateShouldBePossible() {
		assert new TemplateBuilder("test").build() != null;
	}

	@Test(dependsOnMethods = "buildingEmptyTemplateShouldBePossible")
	public void builtTemplateShouldContainName() {
		String name = new TemplateBuilder("test").build().getName();
		assert name != null;
		assert name.equals("test");
	}

	@Test(dependsOnMethods = "buildingEmptyTemplateShouldBePossible", expectedExceptions = AluminumException.class)
	public void buildingTemplateMoreThanOnceShouldCauseException() {
		TemplateBuilder templateBuilder = new TemplateBuilder("test");
		templateBuilder.build();
		templateBuilder.build();
	}

	@Test(dependsOnMethods = "buildingEmptyTemplateShouldBePossible", expectedExceptions = AluminumException.class)
	public void restoringCurrentTemplateElementAfterBuildingTemplateShouldCauseException() {
		TemplateBuilder templateBuilder = new TemplateBuilder("test");
		templateBuilder.build();

		templateBuilder.restoreCurrentTemplateElement();
	}

	@Test(dependsOnMethods = "buildingEmptyTemplateShouldBePossible", expectedExceptions = AluminumException.class)
	public void addingTemplateElementAfterBuildingTemplateShouldCauseException() {
		TemplateBuilder templateBuilder = new TemplateBuilder("test");
		templateBuilder.build();

		templateBuilder.addTemplateElement(new TestActionElement());
	}

	public void builtTemplateShouldContainAddedTemplateElements() {
		TemplateElement templateElement = new TestActionElement();

		TemplateBuilder templateBuilder = new TemplateBuilder("test");
		templateBuilder.addTemplateElement(templateElement);

		assert templateBuilder.build().contains(templateElement);
	}

	public void builtTemplateShouldNotContainTemplateElementsThatWereNotAdded() {
		assert !new TemplateBuilder("test").build().contains(new TestActionElement());
	}

	public void builtTemplateShouldBeAbleToRetrieveParent() {
		TemplateElement parent = new TestActionElement();
		TemplateElement child = new TestActionElement();

		TemplateBuilder templateBuilder = new TemplateBuilder("test");
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

		TemplateBuilder templateBuilder = new TemplateBuilder("test");
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

	@Test(dependsOnMethods = "buildingEmptyTemplateShouldBePossible", expectedExceptions = AluminumException.class)
	public void retrievingParentOfUnknownTemplateElementShouldCauseException() {
		new TemplateBuilder("test").build().getParent(new TestActionElement());
	}

	@Test(dependsOnMethods = "buildingEmptyTemplateShouldBePossible", expectedExceptions = AluminumException.class)
	public void retrievingChildrenOfUnknownTemplateElementShouldCauseException() {
		new TemplateBuilder("test").build().getChildren(new TestActionElement());
	}
}