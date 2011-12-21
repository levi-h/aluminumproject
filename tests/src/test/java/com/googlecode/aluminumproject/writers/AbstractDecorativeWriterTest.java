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

import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("all")
@Test(groups = {"core", "fast"})
public class AbstractDecorativeWriterTest {
	private ListWriter underlyingWriter;
	private DecorativeWriter writer;

	@BeforeMethod
	public void createWriters() {
		underlyingWriter = new ListWriter();
		writer = new AbstractDecorativeWriter(underlyingWriter) {
			public void write(Object object) throws AluminumException {
				checkOpen();

				getWriter().write(object);
			}
		};
	}

	public void underlyingWriterShouldBeObtainable() {
		assert writer.getWriter() == underlyingWriter;
	}

	@Test(dependsOnMethods = "underlyingWriterShouldBeObtainable")
	public void underlyingWriterOfClosedWriterShouldStillBeObtainable() {
		writer.close();

		assert writer.getWriter() == underlyingWriter;
	}

	public void underlyingWriterShouldBeReplaceable() {
		StringWriter newUnderlyingWriter = new StringWriter();

		writer.setWriter(newUnderlyingWriter);
		assert writer.getWriter() == newUnderlyingWriter;
	}

	public void replacingUnderlyingWriterShouldFlushWriter() {
		writer.write("*");

		List<?> list = underlyingWriter.getList();
		assert list != null;
		assert list.isEmpty();

		writer.setWriter(new StringWriter());

		list = underlyingWriter.getList();
		assert list != null;
		assert list.size() == 1;
		assert list.contains("*");
	}

	@Test(dependsOnMethods = "underlyingWriterShouldBeReplaceable", expectedExceptions = AluminumException.class)
	public void replacingUnderlyingWriterOfClosedWriterShouldCauseException() {
		writer.close();

		writer.setWriter(new StringWriter());
	}

	@Test(expectedExceptions = AluminumException.class)
	public void closingWriterShouldCloseUnderlyingWriter() {
		writer.close();

		underlyingWriter.close();
	}
}