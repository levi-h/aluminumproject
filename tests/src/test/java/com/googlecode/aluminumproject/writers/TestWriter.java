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


/**
 * A writer that can be used in tests.
 *
 * @author levi_h
 */
public class TestWriter extends AbstractWriter {
	private boolean flushed;

	/**
	 * Creates a test writer.
	 */
	public TestWriter() {}

	public void write(Object object) throws AluminumException {
		checkOpen();
	}

	@Override
	public void flush() throws AluminumException {
		super.flush();

		flushed = true;
	}

	/**
	 * Determines whether this writer has been flushed or not.
	 *
	 * @return {@code true} if this writer is flushed, {@code false} otherwise
	 */
	public boolean isFlushed() {
		return flushed;
	}
}