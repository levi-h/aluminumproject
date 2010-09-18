/*
 * Copyright 2009-2010 Levi Hoogenberg
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

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("all")
@Test(groups = {"core", "fast"})
public class AbstractWriterTest {
	private TestWriter writer;

	@BeforeMethod
	public void createWriter() {
		writer = new TestWriter();
	}

	public void writingToOpenWriterShouldBePossible() {
		writer.write(new Object());
	}

	@Test(expectedExceptions = WriterException.class)
	public void clearingWriterShouldCauseException() {
		writer.clear();
	}

	public void flushingOpenWriterShouldBePossible() {
		writer.flush();
	}

	public void closingOpenWriterShouldBePossible() {
		writer.write(new Object());
	}

	public void closingWriterShouldFlushIt() {
		assert !writer.isFlushed();

		writer.close();
		assert writer.isFlushed();
	}

	@Test(dependsOnMethods = "writingToOpenWriterShouldBePossible", expectedExceptions = WriterException.class)
	public void writingToClosedWriterShouldCauseException() {
		writer.close();
		writer.write(new Object());
	}

	@Test(dependsOnMethods = "flushingOpenWriterShouldBePossible", expectedExceptions = WriterException.class)
	public void flushingClosedWriterShouldCauseException() {
		writer.close();
		writer.flush();
	}

	@Test(dependsOnMethods = "closingOpenWriterShouldBePossible", expectedExceptions = WriterException.class)
	public void closingClosedWriterShouldCauseException() {
		writer.close();
		writer.close();
	}
}