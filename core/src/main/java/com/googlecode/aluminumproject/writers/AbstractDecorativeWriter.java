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
 * Convenience superclass for decorative writers. It implements the {@link #getWriter() getWriter} and {@link
 * #setWriter(Writer) setWriter} methods and overrides the {@link #flush() flush} and {@link #close() close} methods to
 * propagate these operations to the underlying writer.
 */
public abstract class AbstractDecorativeWriter extends AbstractWriter implements DecorativeWriter {
	private Writer writer;

	/**
	 * Creates an abstract decorative writer.
	 *
	 * @param writer the underlying writer
	 */
	protected AbstractDecorativeWriter(Writer writer) {
		this.writer = writer;
	}

	public Writer getWriter() {
		return writer;
	}

	public void setWriter(Writer writer) throws AluminumException {
		flush();

		this.writer = writer;
	}

	@Override
	public void flush() throws AluminumException {
		super.flush();

		writer.flush();
	}

	@Override
	public void close() throws AluminumException {
		super.close();

		writer.close();
	}
}