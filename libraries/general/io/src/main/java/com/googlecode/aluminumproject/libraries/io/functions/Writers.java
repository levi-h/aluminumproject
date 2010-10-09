/*
 * Copyright 2010 Levi Hoogenberg
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
package com.googlecode.aluminumproject.libraries.io.functions;

import com.googlecode.aluminumproject.annotations.FunctionClass;
import com.googlecode.aluminumproject.writers.FileWriter;
import com.googlecode.aluminumproject.writers.Writer;

import java.io.File;

/**
 * Provides I/O related writers.
 *
 * @author levi_h
 */
@FunctionClass
public class Writers {
	private Writers() {}

	/**
	 * Creates a {@link FileWriter file writer} that will overwrite the contents of its target file.
	 *
	 * @param target the file to write to
	 * @return the new file writer
	 */
	public static Writer fileWriter(File target) {
		return new FileWriter(target, false);
	}

	/**
	 * Creates a {@link FileWriter file writer} that will append to the contents of its target file.
	 *
	 * @param target the file to write to
	 * @return the new file writer
	 */
	public static Writer appendingFileWriter(File target) {
		return new FileWriter(target, true);
	}
}