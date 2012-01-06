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

import java.util.Arrays;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
@Test(groups = {"libraries", "libraries-text", "fast"})
public class ToggleableWriterTest {
	private ListWriter listWriter;

	@BeforeMethod
	public void createListWriter() {
		listWriter = new ListWriter(true);
	}

	public void objectsWrittenToWritingWriterShouldBeWrittenToUnderlyingWriter() {
		new ToggleableWriter(listWriter).write(10);

		assert listWriter.getList().equals(Arrays.asList(10));
	}

	public void objectsWrittenToNonWritingWriterShouldNotBeWrittenToUnderlyingWriter() {
		new ToggleableWriter(listWriter, false).write(10);

		assert listWriter.getList().isEmpty();
	}

	public void writerShouldBeToggleable() {
		ToggleableWriter writer = new ToggleableWriter(listWriter);

		for (int i = 1; i <= 10; i++) {
			writer.write(i);

			writer.setWrite(!writer.isWrite());
		}

		assert listWriter.getList().equals(Arrays.asList(1, 3, 5, 7, 9));
	}
}