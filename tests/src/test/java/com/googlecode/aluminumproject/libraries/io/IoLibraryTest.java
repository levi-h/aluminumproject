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
package com.googlecode.aluminumproject.libraries.io;

import com.googlecode.aluminumproject.libraries.LibraryTest;
import com.googlecode.aluminumproject.libraries.io.functions.Directories;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("javadoc")
public abstract class IoLibraryTest extends LibraryTest {
	public IoLibraryTest() {
		super("templates/io", "xml");
	}

	protected File createTemporaryFile(int lineCount) throws IOException {
		File file = createTemporaryFile();

		BufferedWriter writer = new BufferedWriter(new FileWriter(file));

		try {
			for (int i = 1; i <= lineCount; i++) {
				writer.write(String.format("line %d\n", i));
			}
		} finally {
			writer.close();
		}

		return file;
	}

	protected File createTemporaryFile() throws IOException {
		File parentDirectory = Directories.temporaryDirectory();

		File temporaryFile = new File(parentDirectory, generateUniqueName(parentDirectory));
		temporaryFile.createNewFile();
		temporaryFile.deleteOnExit();

		return temporaryFile;
	}

	protected File createTemporaryDirectory() throws IOException {
		File parentDirectory = Directories.temporaryDirectory();

		File temporaryDirectory = new File(parentDirectory, generateUniqueName(parentDirectory));
		temporaryDirectory.mkdir();
		temporaryDirectory.deleteOnExit();

		return temporaryDirectory;
	}

	protected String generateUniqueName(File parentDirectory) {
		String name;
		int counter = 0;

		do {
			name = String.format("aluminum-%d", ++counter);
		} while (new File(parentDirectory, name).exists());

		return name;
	}

	protected List<String> readLines(File file) throws IOException {
		List<String> lines = new ArrayList<String>();

		BufferedReader reader = new BufferedReader(new FileReader(file));

		try {
			String line;

			while ((line = reader.readLine()) != null) {
				lines.add(line);
			}
		} finally {
			reader.close();
		}

		return lines;
	}
}