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

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Writes to an {@link OutputStream output stream}. When an object is being written, one of the following things
 * happens:
 * <ul>
 * <li>When the object is a byte array, it is written to the output stream as-is;
 * <li>In any other case, the object is being {@link String#valueOf(Object) converted} to a string and the bytes in that
 *     string are written to the output stream.
 * </ul>
 * Subclasses have to implement a single method: one that {@link #createOutputStream() creates} an output stream.
 * <p>
 * An output stream writer optionally implements buffering; in that case, a {@link BufferedOutputStream buffered output
 * stream} is used to write objects to. Regardless of whether an abstract output stream writer buffers or not, {@link
 * #clear() clearing} it is not supported.
 *
 * @author levi_h
 */
public abstract class AbstractOutputStreamWriter extends AbstractWriter {
	private OutputStream outputStream;

	private boolean buffer;

	/**
	 * Creates an abstract output stream writer that does not buffer.
	 */
	public AbstractOutputStreamWriter() {
		this(false);
	}

	/**
	 * Creates an abstract output stream writer.
	 *
	 * @param buffer whether a buffered output stream should be used
	 */
	protected AbstractOutputStreamWriter(boolean buffer) {
		this.buffer = buffer;
	}

	private OutputStream getOutputStream() {
		if (outputStream == null) {
			logger.debug("creating output stream");

			outputStream = createOutputStream();

			if (buffer) {
				outputStream = new BufferedOutputStream(outputStream);
			}
		}

		return outputStream;
	}

	/**
	 * Creates the output stream that should be used. This method is invoked when the output stream is first needed.
	 *
	 * @return the output stream that should be written to
	 * @throws WriterException when the output stream can't be created
	 */
	protected abstract OutputStream createOutputStream() throws WriterException;

	public void write(Object object) throws WriterException {
		checkOpen();

		byte[] bytes;

		if (object instanceof byte[]) {
			bytes = (byte[]) object;
		} else {
			bytes = String.valueOf(object).getBytes();
		}

		try {
			getOutputStream().write(bytes);
		} catch (IOException exception) {
			throw new WriterException(exception, "can't write object ", object);
		}
	}

	@Override
	public void flush() throws WriterException {
		checkOpen();

		try {
			getOutputStream().flush();
		} catch (IOException exception) {
			throw new WriterException(exception, "can't flush output stream");
		}
	}

	@Override
	public void close() throws WriterException {
		super.close();

		try {
			getOutputStream().close();
		} catch (IOException exception) {
			throw new WriterException(exception, "can't close output stream");
		}
	}
}