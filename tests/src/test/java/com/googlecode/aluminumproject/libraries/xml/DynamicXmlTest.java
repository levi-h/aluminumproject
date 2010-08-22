/*
 * Copyright 2010 Levi Hoogenberg
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
package com.googlecode.aluminumproject.libraries.xml;

import com.googlecode.aluminumproject.templates.TemplateException;

import org.testng.annotations.Test;

@SuppressWarnings("all")
@Test(groups = {"libraries", "libraries-xml", "slow"})
public class DynamicXmlTest extends XmlLibraryTest {
	public void singleElementShouldResultInDocument() {
		assert processTemplate("single-element").equals(createXml("<document/>"));
	}

	public void elementsShouldBeNestable() {
		assert processTemplate("nested-elements").equals(createXml(
			"<document>",
			"    <page/>",
			"</document>"
		));
	}

	@Test(dependsOnMethods = "singleElementShouldResultInDocument")
	public void attributesShouldBeSupported() {
		assert processTemplate("element-with-attributes").equals(createXml(
			"<document name=\"Sales report\" date=\"November 12\"/>"
		));
	}

	@Test(dependsOnMethods = "singleElementShouldResultInDocument")
	public void namespacesShouldBeSupported() {
		assert processTemplate("element-in-namespace").equals(createXml(
			"<d:document xmlns:d=\"http://documents.example.com/\"/>"
		));
	}

	@Test(dependsOnMethods = "singleElementShouldResultInDocument")
	public void attributesInNamespaceShouldBeSupported() {
		assert processTemplate("element-with-attribute-in-namespace").equals(createXml(
			"<document external:id=\"X43\" xmlns:external=\"http://external.example.com/\"/>"
		));
	}

	@Test(dependsOnMethods = "attributesInNamespaceShouldBeSupported", expectedExceptions = TemplateException.class)
	public void usingPrefixedAttributeWithoutNamespaceShouldCauseException() {
		processTemplate("element-with-prefixed-attribute-without-namespace");
	}

	private String createXml(String... lines) {
		StringBuilder xmlBuilder = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");

		for (String line: lines) {
			xmlBuilder.append("\n").append(line);
		}

		return xmlBuilder.toString();
	}
}