/*
 * Copyright 2010-2012 Levi Hoogenberg
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

import com.googlecode.aluminumproject.libraries.xml.XmlLibraryTest;

import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
@Test(groups = {"libraries", "libraries-xml", "slow"})
public class AttributeTest extends XmlLibraryTest {
	public void attributesShouldBeAddedToElement() {
		assert processTemplate("element-with-attributes").equals(createXml(
			"<document name=\"Sales report\" date=\"November 12\"/>"
		));
	}

	public void prefixedAttributeShouldResultInAttributeInNamespace() {
		assert processTemplate("element-with-prefixed-attribute").equals(createXml(
			"<document external:id=\"X43\" xmlns:external=\"http://external.example.com/\"/>"
		));
	}
}