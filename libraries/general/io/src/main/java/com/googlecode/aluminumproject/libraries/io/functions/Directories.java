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
package com.googlecode.aluminumproject.libraries.io.functions;

import com.googlecode.aluminumproject.annotations.FunctionClass;

import java.io.File;
import java.io.IOException;

/**
 * Supplies all kinds of directories.
 *
 * @author levi_h
 */
@FunctionClass
public class Directories {
	private Directories() {}

	/**
	 * Returns the current working directory.
	 *
	 * @return the current directory
	 */
	public static File currentDirectory() {
		return new File(System.getProperty("user.dir"));
	}

	/**
	 * Returns the current user's home directory.
	 *
	 * @return the home directory
	 */
	public static File homeDirectory() {
		return new File(System.getProperty("user.home"));
	}

	/**
	 * Returns the directory that is used to store temporary files in.
	 *
	 * @return the temporary directory
	 */
	public static File temporaryDirectory() {
		return new File(System.getProperty("java.io.tmpdir"));
	}

	/**
	 * Creates a directory and returns it. Intermediate directories will be created when necessary.
	 *
	 * @param directory the directory in which to create the directory
	 * @param name the name of the new directory
	 * @return the new directory
	 * @throws IllegalArgumentException when a file with the given name already exists in the specified directory
	 * @throws IOException when the directory can't be created
	 */
	public static File newDirectory(File directory, String name) throws IllegalArgumentException, IOException {
		File newDirectory = new File(directory, name);

		if (newDirectory.exists()) {
			String message = String.format("directory '%s' already exists", newDirectory.getAbsolutePath());

			throw new IllegalArgumentException(message);
		} else if (!newDirectory.mkdirs()) {
			throw new IOException(String.format("can't create directory '%s'", newDirectory.getAbsolutePath()));
		}

		return newDirectory;
	}
}