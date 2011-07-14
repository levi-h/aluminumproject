/*
 * Copyright 2011 Levi Hoogenberg
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
package com.googlecode.aluminumproject.libraries.scripting.actions;

import com.googlecode.aluminumproject.utilities.Logger;
import com.googlecode.aluminumproject.writers.Writer;
import com.googlecode.aluminumproject.writers.WriterException;

import java.io.IOException;

/**
 * Adapts a {@link Writer writer} so that it can be used inside a script.
 *
 * @author levi_h
 */
class ScriptWriter extends java.io.Writer {
	private Writer writer;

	private final Logger logger;

	/**
	 * Creates a script writer.
	 *
	 * @param writer the underlying writer
	 */
	public ScriptWriter(Writer writer) {
		this.writer = writer;

		logger = Logger.get(getClass());
	}

	@Override
	public void write(char[] buffer, int offset, int length) throws IOException {
		String text = String.valueOf(buffer, offset, length);

		logger.debug("writing '", text, "'");

		try {
			writer.write(text);
		} catch (WriterException exception) {
			throw new IOException("can't write text", exception);
		}
	}

	@Override
	public void flush() throws IOException {
		logger.debug("flushing writer");

		try {
			writer.flush();
		} catch (WriterException exception) {
			throw new IOException("can't flush writer", exception);
		}
	}

	@Override
	public void close() {
		logger.warn("script writers don't support the close operation");
	}
}