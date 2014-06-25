/*
 * Copyright 2010-2014 Aluminum project
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
package com.googlecode.aluminumproject.libraries.xml.actions;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.DefaultContext;
import com.googlecode.aluminumproject.libraries.xml.XmlLibraryTest;

import java.util.Arrays;
import java.util.List;

import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
@Test(groups = {"libraries", "libraries-xml", "slow"})
public class SelectTest extends XmlLibraryTest {
	public void selectingNoElementShouldResultInNull() {
		Context context = new DefaultContext();

		processTemplate("select-no-elements", context);

		assert context.getVariableNames(Context.TEMPLATE_SCOPE).contains("pages");

		Object variable = context.getVariable(Context.TEMPLATE_SCOPE, "pages");
		assert variable == null;
	}

	public void elementsShouldBeSelectable() {
		Context context = new DefaultContext();

		processTemplate("select-elements", context);

		assert context.getVariableNames(Context.TEMPLATE_SCOPE).contains("pages");

		Object variable = context.getVariable(Context.TEMPLATE_SCOPE, "pages");
		assert variable instanceof List;

		List<?> elements = (List<?>) variable;
		assert elements.size() == 2;
		assert elements.get(0) instanceof com.googlecode.aluminumproject.libraries.xml.model.Element;
		assert elements.get(1) instanceof com.googlecode.aluminumproject.libraries.xml.model.Element;
	}

	@Test(dependsOnMethods = "elementsShouldBeSelectable")
	public void singleResultShouldNotBeWrappedInList() {
		Context context = new DefaultContext();

		processTemplate("select-element", context);

		assert context.getVariableNames(Context.TEMPLATE_SCOPE).contains("pages");

		Object variable = context.getVariable(Context.TEMPLATE_SCOPE, "pages");
		assert variable instanceof com.googlecode.aluminumproject.libraries.xml.model.Element;
	}

	@Test(dependsOnMethods = "elementsShouldBeSelectable")
	public void singleResultShouldBeWrappedInListWithSuitableSelectionType() {
		Context context = new DefaultContext();

		processTemplate("select-with-type", context);

		assert context.getVariableNames(Context.TEMPLATE_SCOPE).contains("text");

		Object text = context.getVariable(Context.TEMPLATE_SCOPE, "text");
		assert text != null;
		assert text.equals(Arrays.asList("This is the first page."));
	}

	public void namespacesShouldBeSelectable() {
		Context context = new DefaultContext();

		processTemplate("select-namespaces", context);

		assert context.getVariableNames(Context.TEMPLATE_SCOPE).contains("namespaces");

		Object variable = context.getVariable(Context.TEMPLATE_SCOPE, "namespaces");
		assert variable instanceof List;

		List<?> namespaces = (List<?>) variable;
		assert namespaces.size() == 3;
		assert namespaces.contains("http://aluminumproject.googlecode.com/core");
		assert namespaces.contains("http://aluminumproject.googlecode.com/test");
		assert namespaces.contains("http://www.w3.org/XML/1998/namespace");
	}

	public void namespacesShouldBeSelectableUsingReusableContext() {
		Context context = new DefaultContext();

		processTemplate("select-namespaces-with-reusable-context", context);

		assert context.getVariableNames(Context.TEMPLATE_SCOPE).contains("namespaces");

		Object variable = context.getVariable(Context.TEMPLATE_SCOPE, "namespaces");
		assert variable instanceof List;

		List<?> namespaces = (List<?>) variable;
		assert namespaces.size() == 3;
		assert namespaces.contains("http://aluminumproject.googlecode.com/core");
		assert namespaces.contains("http://aluminumproject.googlecode.com/test");
		assert namespaces.contains("http://www.w3.org/XML/1998/namespace");
	}

	@Test(expectedExceptions = AluminumException.class)
	public void supplyingDuplicateNamespacePrefixesAsXPathContextShouldCauseException() {
		processTemplate("select-namespaces-with-illegal-context");
	}

	@Test(dependsOnMethods = "singleResultShouldNotBeWrappedInList")
	public void attributeShouldBeSelectable() {
		Context context = new DefaultContext();

		processTemplate("select-attribute", context);

		assert context.getVariableNames(Context.TEMPLATE_SCOPE).contains("page");

		Object page = context.getVariable(Context.TEMPLATE_SCOPE, "page");
		assert page != null;
		assert page.equals("1");
	}

	@Test(dependsOnMethods = "singleResultShouldNotBeWrappedInList")
	public void textShouldBeSelectable() {
		Context context = new DefaultContext();

		processTemplate("select-text", context);

		assert context.getVariableNames(Context.TEMPLATE_SCOPE).contains("text");

		Object text = context.getVariable(Context.TEMPLATE_SCOPE, "text");
		assert text != null;
		assert text.equals("This is the last page.");
	}

	@Test(dependsOnMethods = "singleResultShouldNotBeWrappedInList")
	public void commentShouldBeSelectable() {
		Context context = new DefaultContext();

		processTemplate("select-comment", context);

		assert context.getVariableNames(Context.TEMPLATE_SCOPE).contains("comment");

		Object comment = context.getVariable(Context.TEMPLATE_SCOPE, "comment");
		assert comment != null;
		assert comment.equals("TODO: pages");
	}

	@Test(expectedExceptions = AluminumException.class)
	public void invalidExpressionShouldCauseException() {
		processTemplate("select-with-invalid-expression");
	}
}