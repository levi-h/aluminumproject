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
 * A writer that builds a {@link String string}. Only strings can be written; they are all concatenated into a single
 * string, which can be retrieved using the {@link #getString() getString method}.
 * <p>
 * Note that string writers do not support the {@link #clear() clear} method. If a form of buffering is required, other
 * writers (such as the {@link TextWriter text writer}) can be used to wrap a string writer in.
 */
public class StringWriter extends AbstractWriter {
	private StringBuilder builder;

	/**
	 * Creates a string writer. It contains an empty string.
	 */
	public StringWriter() {
		builder = new StringBuilder();
	}

	/**
	 * Returns a concatenation of all strings that were {@link #write(Object) written} to this writer.
	 *
	 * @return the built string
	 */
	public String getString() {
		return builder.toString();
	}

	public void write(Object object) throws AluminumException {
		checkOpen();

		if (object instanceof String) {
			builder.append((String) object);
		} else {
			throw new AluminumException("string writers only accept strings");
		}
	}
}