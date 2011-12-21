/*
 * Copyright 2009-2011 Levi Hoogenberg
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

import com.googlecode.aluminumproject.AluminumException;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("all")
@Test(groups = {"core", "fast"})
public class StringWriterTest {
	private StringWriter writer;

	@BeforeMethod
	public void createWriter() {
		writer = new StringWriter();
	}

	public void initialWriterShouldBeEmpty() {
		String string = writer.getString();
		assert string != null;
		assert string.equals("");
	}

	public void stringShouldBeWritable() {
		writer.write("5");
	}

	@Test(expectedExceptions = AluminumException.class)
	public void writingNonStringShouldCauseException() {
		writer.write(5);
	}

	@Test(dependsOnMethods = "stringShouldBeWritable")
	public void concatenatedStringShouldBeRetrievable() {
		writer.write("to");
		writer.write("get");
		writer.write("her");

		String string = writer.getString();
		assert string != null;
		assert string.equals("together");
	}

	@Test(expectedExceptions = AluminumException.class)
	public void clearingWriterShouldCauseException() {
		writer.clear();
	}
}