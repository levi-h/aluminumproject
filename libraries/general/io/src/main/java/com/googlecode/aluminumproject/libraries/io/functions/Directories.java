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
package com.googlecode.aluminumproject.libraries.io.functions;

import com.googlecode.aluminumproject.annotations.FunctionClass;
import com.googlecode.aluminumproject.annotations.Named;

import java.io.File;
import java.io.IOException;

@FunctionClass
@SuppressWarnings("javadoc")
public class Directories {
	private Directories() {}

	public static File currentDirectory() {
		return new File(System.getProperty("user.dir"));
	}

	public static File homeDirectory() {
		return new File(System.getProperty("user.home"));
	}

	public static File temporaryDirectory() {
		return new File(System.getProperty("java.io.tmpdir"));
	}

	public static File newDirectory(@Named("directory") File directory, @Named("name") String name)
			throws IllegalArgumentException, IOException {
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