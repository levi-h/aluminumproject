/*
 * Copyright 2011-2012 Levi Hoogenberg
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

import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.DefaultContext;
import com.googlecode.aluminumproject.libraries.xml.XmlLibraryTest;

import java.util.Arrays;

import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
@Test(groups = {"libraries", "libraries-xml", "slow"})
public class IncludeTest extends XmlLibraryTest {
	public void includedElementShouldBeAddedToParentElement() {
		Context context = new DefaultContext();
		context.setVariable("animals", Arrays.asList("penguin", "polar bear"));

		assert processTemplate("include", context).equals(createXml(
			"<zoo>",
			"    <animal name=\"penguin\"/>",
			"    <animal name=\"polar bear\"/>",
			"</zoo>"
		));
	}

	public void includedLocalElementShouldBeAddedToParentElement() {
		Context context = new DefaultContext();
		context.setVariable("animals", Arrays.asList("monkey", "elephant"));

		assert processTemplate("include-local", context).equals(createXml(
			"<zoo>",
			"    <animal name=\"monkey\"/>",
			"    <animal name=\"elephant\"/>",
			"</zoo>"
		));
	}
}