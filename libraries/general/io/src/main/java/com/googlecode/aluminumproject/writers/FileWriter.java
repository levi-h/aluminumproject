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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Writes to a {@link File file}.
 *
 * @author levi_h
 */
public class FileWriter extends AbstractOutputStreamWriter {
	private File file;

	private boolean append;

	/**
	 * Creates a file writer.
	 *
	 * @param file the file to write to
	 * @param append {@code true} to append output to the file or {@code false} to replace it
	 */
	public FileWriter(File file, boolean append) {
		super(true);

		this.file = file;

		this.append = append;
	}

	@Override
	protected FileOutputStream createOutputStream() throws AluminumException {
		try {
			return new FileOutputStream(file, append);
		} catch (FileNotFoundException exception) {
			throw new AluminumException(exception, "can't create file output stream");
		}
	}
}