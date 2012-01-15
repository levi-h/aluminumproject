/*
 * Copyright 2010-2012 Aluminum project
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
import com.googlecode.aluminumproject.libraries.xml.model.Element;
import com.googlecode.aluminumproject.writers.StringWriter;

import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
@Test(groups = {"libraries", "libraries-xml", "slow"})
public class ParseTest extends XmlLibraryTest {
	public void parsingShouldResultInRootElement() {
		Context context = new DefaultContext();

		processTemplate("parse", context);

		assert context.getVariableNames(Context.TEMPLATE_SCOPE).contains("document");

		Object element = context.getVariable(Context.TEMPLATE_SCOPE, "document");
		assert element instanceof Element;

		StringWriter writer = new StringWriter();

		((Element) element).writeDocument(writer, 4);

		assert writer.getString().equals(createXml(
			"<document>",
			"    <page number=\"1\"/>",
			"    <page number=\"2\"/>",
			"</document>"
		)): writer.getString();
	}

	@Test(expectedExceptions = AluminumException.class)
	public void parsingInvalidDocumentShouldCauseException() {
		processTemplate("parse-invalid-document");
	}
}