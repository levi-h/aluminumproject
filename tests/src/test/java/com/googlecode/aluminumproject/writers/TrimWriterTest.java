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
package com.googlecode.aluminumproject.writers;

import com.googlecode.aluminumproject.writers.TrimWriter.TrimType;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
@Test(groups = {"libraries", "libraries-text", "fast"})
public class TrimWriterTest {
	private StringWriter writer;

	@BeforeMethod
	public void createWriter() {
		writer = new StringWriter();
	}

	public void noTrimShouldNotTrimAnyWhitespace() {
		new TrimWriter(writer, TrimType.NONE, false).write(" * ");

		assert writer.getString().equals(" * ");
	}

	public void noTrimShouldNotTrimAnyWhitespaceInMultilineMode() {
		new TrimWriter(writer, TrimType.NONE, true).write(" * ");

		assert writer.getString().equals(" * ");
	}

	public void leftTrimShouldTrimLeadingWhitespace() {
		new TrimWriter(writer, TrimType.LEFT, false).write(" * ");

		assert writer.getString().equals("* ");
	}

	public void leftTrimShouldTrimLeadingWhitespaceInMultilineMode() {
		new TrimWriter(writer, TrimType.LEFT, true).write(" * \n * ");

		assert writer.getString().equals("* \n* ");
	}

	public void rightTrimShouldTrimTrailingWhitespace() {
		new TrimWriter(writer, TrimType.RIGHT, false).write(" * ");

		assert writer.getString().equals(" *");
	}

	public void rightTrimShouldTrimTrailingWhitespaceInMultilineMode() {
		new TrimWriter(writer, TrimType.RIGHT, true).write(" *  \n * ");

		assert writer.getString().equals(" *\n *");
	}

	public void leftAndRightTrimShouldTrimAllWhitespace() {
		new TrimWriter(writer, TrimType.BOTH, false).write(" * ");

		assert writer.getString().equals("*");
	}

	public void leftAndRightTrimShouldTrimAllWhitespaceInMultilineMode() {
		new TrimWriter(writer, TrimType.BOTH, true).write(" * \n * ");

		assert writer.getString().equals("*\n*");
	}
}