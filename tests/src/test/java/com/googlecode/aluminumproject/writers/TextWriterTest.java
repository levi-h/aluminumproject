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

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
@Test(groups = {"core", "fast"})
public class TextWriterTest {
	private StringWriter writer;

	private Writer autoFlushWriter;
	private Writer nonAutoFlushWriter;

	@BeforeMethod
	public void createWriters() {
		writer = new StringWriter();

		autoFlushWriter = new TextWriter(writer, true);
		nonAutoFlushWriter = new TextWriter(writer);
	}

	public void autoFlushWriterShouldFlushAutomatically() {
		autoFlushWriter.write("contents");

		String string = writer.getString();
		assert string != null;
		assert string.equals("contents");
	}

	public void nonAutoFlushWriterShouldNotFlushAutomatically() {
		nonAutoFlushWriter.write("contents");

		String string = writer.getString();
		assert string != null;
		assert string.equals("");
	}

	@Test(dependsOnMethods = "nonAutoFlushWriterShouldNotFlushAutomatically")
	public void flushingWriterShouldWriteContentsToUnderlyingWriter() {
		nonAutoFlushWriter.write("contents");
		nonAutoFlushWriter.flush();

		String string = writer.getString();
		assert string != null;
		assert string.equals("contents");
	}

	@Test(dependsOnMethods = "flushingWriterShouldWriteContentsToUnderlyingWriter")
	public void flushingWriterWithEmptyBufferShouldBePossible() {
		nonAutoFlushWriter.write("contents");
		nonAutoFlushWriter.flush();
		nonAutoFlushWriter.flush();

		String string = writer.getString();
		assert string != null;
		assert string.equals("contents");
	}

	@Test(dependsOnMethods = "flushingWriterShouldWriteContentsToUnderlyingWriter")
	public void clearingWriterShouldClearBuffer() {
		nonAutoFlushWriter.write("content");
		nonAutoFlushWriter.clear();

		nonAutoFlushWriter.write("contents");
		nonAutoFlushWriter.flush();

		String string = writer.getString();
		assert string != null;
		assert string.equals("contents");
	}
}