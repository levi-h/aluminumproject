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

import com.googlecode.aluminumproject.AluminumException;

/**
 * A writer that wraps another writer and writes {@link String strings} to it.
 * <p>
 * By default, a text writer does not flush write operations to the underlying writer. It's possible to do so by setting
 * the writer to <em>auto-flush</em> by using the {@link #TextWriter(Writer, boolean) appropriate constructor}.
 */
public class TextWriter extends AbstractWriter {
	private Writer writer;

	private StringBuilder buffer;
	private boolean autoFlush;

	/**
	 * Creates a text writer that does not flush automatically.
	 *
	 * @param writer the underlying writer
	 */
	public TextWriter(Writer writer) {
		this(writer, false);
	}

	/**
	 * Creates a text writer.
	 *
	 * @param writer the underlying writer
	 * @param autoFlush whether or not objects are written to the underlying writer after each write operation
	 */
	public TextWriter(Writer writer, boolean autoFlush) {
		this.writer = writer;

		buffer = new StringBuilder();
		this.autoFlush = autoFlush;
	}

	public void write(Object object) throws AluminumException {
		checkOpen();

		buffer.append(String.valueOf(object));

		if (autoFlush) {
			flush();
		}
	}

	@Override
	public void clear() throws AluminumException {
		checkOpen();

		buffer.delete(0, buffer.length());
	}

	@Override
	public void flush() throws AluminumException {
		super.flush();

		if (buffer.length() > 0) {
			writer.write(buffer.toString());
			writer.flush();

			clear();
		}
	}

	@Override
	public void close() throws AluminumException {
		super.close();

		writer.close();
	}
}