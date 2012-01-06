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
package com.googlecode.aluminumproject.writers;

import com.googlecode.aluminumproject.writers.PreserveWhitespaceWriter.WhitespaceType;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
@Test(groups = {"libraries", "libraries-text", "fast"})
public class PreserveWhitespaceWriterTest {
	private StringWriter writer;

	@BeforeMethod
	public void createWriter() {
		writer = new StringWriter();
	}

	public void superflousSpacesShouldNotBeWritten() {
		new PreserveWhitespaceWriter(writer, WhitespaceType.SPACE, 1).write("Soft  drinks");

		assert writer.getString().equals("Soft drinks");
	}

	public void superflousTabsShouldNotBeWritten() {
		new PreserveWhitespaceWriter(writer, WhitespaceType.TAB, 1).write(
			"\t\t* Cream soda\n" +
			"\t\t* Ginger ale\n" +
			"\t\t* Root beer\n"
		);

		assert writer.getString().equals(
			"\t* Cream soda\n" +
			"\t* Ginger ale\n" +
			"\t* Root beer\n"
		);
	}

	public void superfluousNewlinesShouldNotBeWritten() {
		new PreserveWhitespaceWriter(writer, WhitespaceType.NEWLINE, 2).write(
			"Cola is a beverage usually containing caramel coloring, " +
			"caffeine and a sweetener such as sugar or high fructose corn syrup.\n" +
			"\n" +
			"\n" +
			"\n" +
			"Originally invented by the druggist John Pemberton, it has become popular worldwide."
		);

		assert writer.getString().equals(
			"Cola is a beverage usually containing caramel coloring, " +
			"caffeine and a sweetener such as sugar or high fructose corn syrup.\n" +
			"\n" +
			"Originally invented by the druggist John Pemberton, it has become popular worldwide."
		);
	}
}