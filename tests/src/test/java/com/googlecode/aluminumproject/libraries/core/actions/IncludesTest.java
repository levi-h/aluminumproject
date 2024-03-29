/*
 * Copyright 2012 Aluminum project
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
package com.googlecode.aluminumproject.libraries.core.actions;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.libraries.core.CoreLibraryTest;

import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
@Test(groups = {"libraries", "libraries-core", "slow"})
public class IncludesTest extends CoreLibraryTest {
	public void outputOfIncludedTemplateShouldBecomeOutput() {
		String output = processTemplate("include");
		assert output != null;
		assert output.equals("included");
	}

	public void omittingParserShouldFallBackToParserOfIncludingTemplate() {
		String output = processTemplate("include-without-parser");
		assert output != null;
		assert output.equals("included");
	}

	@Test(dependsOnMethods = "omittingParserShouldFallBackToParserOfIncludingTemplate")
	public void parameterShouldBeAvailableInIncludedTemplate() {
		String output = processTemplate("include-with-parameter");
		assert output != null;
		assert output.equals("included");
	}

	public void localTemplateShouldBeIncludable() {
		String output = processTemplate("include-local");
		assert output != null;
		assert output.equals("Hello, flowers!\nHello, trees!"): output;
	}

	@Test(expectedExceptions = AluminumException.class)
	public void includingNonexistentLocalTemplateShouldCauseException() {
		processTemplate("include-local-without-available-template");
	}
}