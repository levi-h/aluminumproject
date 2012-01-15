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
package com.googlecode.aluminumproject.libraries.text.actions;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.libraries.text.TextLibraryTest;

import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
@Test(groups = {"libraries", "libraries-text", "slow"})
public class FormatTest extends TextLibraryTest {
	public void defaultFormatTypeShouldBeInterpolation() {
		assert processTemplate("format").equals("orange");
	}

	@Test(dependsOnMethods = "defaultFormatTypeShouldBeInterpolation")
	public void dynamicParametersShouldBeSupported() {
		assert processTemplate("format-with-dynamic-parameters").equals("mango and lemon");
	}

	@Test(dependsOnMethods = "dynamicParametersShouldBeSupported", expectedExceptions = AluminumException.class)
	public void usingParameterNameMoreThanOnceShouldCauseException() {
		processTemplate("format-with-duplicate-parameter-names");
	}

	public void usingInterpolationTypeShouldBePossible() {
		assert processTemplate("format-with-interpolation-type").equals("a yellow banana");
	}

	@Test(dependsOnMethods = "usingInterpolationTypeShouldBePossible", expectedExceptions = AluminumException.class)
	public void tryingIllegalFormatWithInterpolationTypeShouldCauseException() {
		processTemplate("format-with-interpolation-type-illegal");
	}

	public void usingPrintfTypeShouldBePossible() {
		assert processTemplate("format-with-printf-type").equals("apple");
	}

	@Test(dependsOnMethods = "usingPrintfTypeShouldBePossible", expectedExceptions = AluminumException.class)
	public void tryingIllegalFormatWithPrintfTypeShouldCauseException() {
		processTemplate("format-with-printf-type-illegal");
	}

	public void usingMessageFormatTypeShouldBePossible() {
		assert processTemplate("format-with-message-format-type").equals("grape");
	}

	@Test(dependsOnMethods = "usingMessageFormatTypeShouldBePossible", expectedExceptions = AluminumException.class)
	public void tryingIllegalFormatWithMessageFormatTypeTypeShouldCauseException() {
		processTemplate("format-with-message-format-type-illegal");
	}

	@Test(dependsOnMethods = "usingPrintfTypeShouldBePossible", expectedExceptions = AluminumException.class)
	public void namingParameterWhenItIsNotRequiredShouldCauseException() {
		processTemplate("format-with-unneeded-named-parameter");
	}

	@Test(dependsOnMethods = "defaultFormatTypeShouldBeInterpolation", expectedExceptions = AluminumException.class)
	public void notNamingParameterWhenItIsRequiredShouldCauseException() {
		processTemplate("format-with-missing-named-parameter");
	}
}