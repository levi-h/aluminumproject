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
package com.googlecode.aluminumproject.libraries.html;

import com.googlecode.aluminumproject.configuration.ConfigurationParameters;
import com.googlecode.aluminumproject.parsers.aluscript.AluScriptParser;
import com.googlecode.aluminumproject.templates.TemplateException;

import org.testng.annotations.Test;

@SuppressWarnings("all")
@Test(groups = {"libraries", "libraries-html", "slow"})
public class Html4Test extends HtmlLibraryTest {
	protected void addConfigurationParameters(ConfigurationParameters configurationParameters) {
		configurationParameters.addParameter(AluScriptParser.AUTOMATIC_NEWLINES, "false");
	}

	public void tagsShouldBeClosed() {
		assert processTemplate("html4-tag").equals("<html></html>");
	}

	public void openTagsShouldNotBeClosed() {
		assert processTemplate("html4-open-tag").equals("first line<br>second line");
	}

	@Test(dependsOnMethods = "openTagsShouldNotBeClosed")
	public void attributesShouldBeWritten() {
		assert processTemplate("html4-tag-with-attribute").equals("<hr size=\"1\">");
	}

	@Test(dependsOnMethods = "bodyShouldBeWritten", expectedExceptions = TemplateException.class)
	public void usingUnknownAttributesShouldCauseException() {
		processTemplate("html4-tag-with-unknown-attribute");
	}

	@Test(dependsOnMethods = "tagsShouldBeClosed")
	public void bodyShouldBeWritten() {
		assert processTemplate("html4-tag-with-body").equals("<strong>important</strong>");
	}
}