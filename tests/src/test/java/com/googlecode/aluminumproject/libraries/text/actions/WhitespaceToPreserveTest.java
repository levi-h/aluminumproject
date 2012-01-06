/*
 * Copyright 2009-2012 Levi Hoogenberg
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

import com.googlecode.aluminumproject.libraries.text.TextLibraryTest;

import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
@Test(groups = {"libraries", "libraries-text", "slow"})
public class WhitespaceToPreserveTest extends TextLibraryTest {
	public void spacesShouldBePreserved() {
		String output = processTemplate("preserve-spaces");
		assert output != null;
		assert output.equals("* * *");
	}

	public void tabsShouldBePreserved() {
		String output = processTemplate("preserve-tabs");
		assert output != null;
		assert output.equals("*\n*\n*");
	}

	public void newlinesShouldBePreserved() {
		String output = processTemplate("preserve-newlines");
		assert output != null;
		assert output.equals("\n\n\n");
	}
}