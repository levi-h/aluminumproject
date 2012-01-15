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

import com.googlecode.aluminumproject.AluminumException;

/**
 * Writes objects to its underlying writer or ignores them, depending on its state.
 */
public class ToggleableWriter extends AbstractDecorativeWriter {
	private boolean write;

	/**
	 * Creates a toggleable writer that writes objects to its underlying writer.
	 *
	 * @param writer the underlying writer
	 */
	public ToggleableWriter(Writer writer) {
		this(writer, true);
	}

	/**
	 * Creates a toggleable writer.
	 *
	 * @param writer the underlying writer
	 * @param write whether or not objects should be written to the given writer
	 */
	public ToggleableWriter(Writer writer, boolean write) {
		super(writer);

		setWrite(write);
	}

	/**
	 * Determines whether this writer is toggled on or off.
	 *
	 * @return whether objects are written to the underlying writer or not
	 */
	public boolean isWrite() {
		return write;
	}

	/**
	 * Toggles this writer on or off.
	 *
	 * @param write {@code true} to write objects to the underlying writer, {@code false} to ignore any written objects
	 */
	public void setWrite(boolean write) {
		this.write = write;
	}

	public void write(Object object) throws AluminumException {
		if (write) {
			getWriter().write(object);
		} else {
			logger.debug("ignoring ", object);
		}
	}
}