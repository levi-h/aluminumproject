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
package com.googlecode.aluminumproject.libraries.io.functions;

import com.googlecode.aluminumproject.libraries.io.IoLibraryTest;

import java.io.File;
import java.io.IOException;

import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
@Test(groups = {"libraries", "libraries-io", "fast"})
public class DirectoriesTest extends IoLibraryTest {
	public void obtainingCurrentDirectoryShouldResultInDirectory() {
		File currentDirectory = Directories.currentDirectory();
		assert currentDirectory != null;
		assert currentDirectory.isDirectory();
	}

	public void obtainingHomeDirectoryShouldResultInDirectory() throws IOException {
		File homeDirectory = Directories.homeDirectory();
		assert homeDirectory != null;
		assert homeDirectory.isDirectory();
	}

	public void obtainingTemporaryDirectoryShouldResultInDirectory() throws IOException {
		File temporaryDirectory = Directories.temporaryDirectory();
		assert temporaryDirectory != null;
		assert temporaryDirectory.isDirectory();
	}

	public void nonexistentNewDirectoryShouldBeCreatable() throws IOException {
		File directory = Directories.temporaryDirectory();

		File newDirectory = Directories.newDirectory(directory, generateUniqueName(directory));
		assert newDirectory != null;
		assert newDirectory.exists();
		assert newDirectory.isDirectory();

		newDirectory.deleteOnExit();
	}

	@Test(dependsOnMethods = "nonexistentNewDirectoryShouldBeCreatable")
	public void intermediateDirectoriesShouldBeCreatedWhenCreatingNewDirectory() throws IOException {
		File directory = Directories.temporaryDirectory();

		String intermediateDirectoryName = generateUniqueName(directory);

		File newDirectory = Directories.newDirectory(directory, String.format("%s/new", intermediateDirectoryName));

		File intermediateDirectory = new File(directory, intermediateDirectoryName);
		assert intermediateDirectory.exists();
		assert intermediateDirectory.isDirectory();

		newDirectory.deleteOnExit();
		intermediateDirectory.deleteOnExit();
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void creatingExistingDirectoryShouldCauseException() throws IOException {
		File directory = createTemporaryDirectory();

		Directories.newDirectory(directory.getParentFile(), directory.getName());
	}
}