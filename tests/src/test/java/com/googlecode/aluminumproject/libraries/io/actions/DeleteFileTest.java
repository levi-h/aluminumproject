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
package com.googlecode.aluminumproject.libraries.io.actions;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.DefaultContext;
import com.googlecode.aluminumproject.libraries.io.IoLibraryTest;
import com.googlecode.aluminumproject.libraries.io.functions.Directories;
import com.googlecode.aluminumproject.libraries.io.functions.Files;

import java.io.File;
import java.io.IOException;

import org.testng.annotations.Test;

@SuppressWarnings("all")
@Test(groups = {"libraries", "libraries-io", "slow"})
public class DeleteFileTest extends IoLibraryTest {
	public void fileShouldBeDeletable() throws IOException {
		File temporaryDirectory = Directories.temporaryDirectory();

		File file = Files.newFile(temporaryDirectory, generateUniqueName(temporaryDirectory));

		Context context = new DefaultContext();
		context.setVariable("file", file);

		processTemplate("delete-file", context);

		assert !file.exists();
	}

	public void emptyDirectoryShouldBeDeletable() throws IOException {
		File temporaryDirectory = Directories.temporaryDirectory();

		File directory = Directories.newDirectory(temporaryDirectory, generateUniqueName(temporaryDirectory));

		Context context = new DefaultContext();
		context.setVariable("file", directory);

		processTemplate("delete-file", context);

		assert !directory.exists();
	}

	public void deletingDirectoryShouldDeleteFilesInDirectory() throws IOException {
		File temporaryDirectory = Directories.temporaryDirectory();

		File directory = Directories.newDirectory(temporaryDirectory, generateUniqueName(temporaryDirectory));
		File file = Files.newFile(directory, generateUniqueName(directory));

		Context context = new DefaultContext();
		context.setVariable("file", directory);

		processTemplate("delete-file", context);

		assert !directory.exists();
		assert !file.exists();
	}

	@Test(expectedExceptions = AluminumException.class)
	public void deletingNonexistentFileShouldCauseException() {
		File temporaryDirectory = Directories.temporaryDirectory();

		Context context = new DefaultContext();
		context.setVariable("file", new File(temporaryDirectory, generateUniqueName(temporaryDirectory)));

		processTemplate("delete-file", context);
	}
}