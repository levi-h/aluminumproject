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

import java.io.BufferedOutputStream;
import java.io.OutputStream;

/**
 * Writes to an existing {@link OutputStream output stream}.
 */
public class OutputStreamWriter extends AbstractOutputStreamWriter {
	private OutputStream outputStream;

	/**
	 * Creates an output stream writer that does not buffer.
	 *
	 * @param outputStream the output stream to use
	 */
	public OutputStreamWriter(OutputStream outputStream) {
		this(outputStream, false);
	}

	/**
	 * Creates an output stream writer.
	 *
	 * @param outputStream the output stream to use
	 * @param buffer whether the output stream should be wrapped in a {@link BufferedOutputStream buffered output
	 *               stream}
	 */
	public OutputStreamWriter(OutputStream outputStream, boolean buffer) {
		super(buffer);

		this.outputStream = outputStream;
	}

	@Override
	protected OutputStream createOutputStream() {
		return outputStream;
	}
}