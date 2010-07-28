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

import com.googlecode.aluminumproject.Logger;

/**
 * Convenience superclass for writers.
 * <p>
 * Subclasses only need to provide a {@link #write(Object) write} method. The {@link #clear() clear} method is not
 * supported, so writers that would like to offer buffer functionality should not call the super method from their clear
 * methods.
 * <p>
 * The {@link #close() close} method sets a {@code closed} flag. This flag is checked by the {@link #checkOpen()
 * checkOpen} method, which can be called from methods that depend on the writer not being closed.
 *
 * @author levi_h
 */
public abstract class AbstractWriter implements Writer {
	private boolean closed;

	/** The logger to use. */
	protected final Logger logger;

	/**
	 * Creates an abstract writer.
	 */
	protected AbstractWriter() {
		logger = Logger.get(getClass());
	}

	/**
	 * Checks whether this writer is open and throws an exception if it isn't. This method can be invoked from the
	 * {@link #write(Object) write}, {@link #clear() clear}, and {@link #flush() flush} methods to avoid operating on a
	 * closed writer.
	 *
	 * @throws WriterException when this writer is closed
	 */
	protected void checkOpen() throws WriterException {
		if (closed) {
			throw new WriterException("the writer is closed");
		}
	}

	public void clear() throws WriterException {
		throw new WriterException("clearing this writer is not supported");
	}

	public void flush() throws WriterException {
		checkOpen();
	}

	public void close() throws WriterException {
		checkOpen();

		flush();

		closed = true;
	}
}