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
package com.googlecode.aluminumproject.libraries.io.actions;

import static com.googlecode.aluminumproject.libraries.io.functions.Directories.temporaryDirectory;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.DefaultContext;
import com.googlecode.aluminumproject.libraries.io.IoLibraryTest;

import java.io.File;
import java.io.IOException;

import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
@Test(groups = {"libraries", "libraries-io", "slow"})
public class MoveFileTest extends IoLibraryTest {
	public void fileShouldBeMovable() throws IOException {
		File sourceFile = createTemporaryFile();
		File targetDirectory = createTemporaryDirectory();
		File targetFile = new File(targetDirectory, sourceFile.getName());

		assert !targetFile.exists();

		Context context = new DefaultContext();
		context.setVariable("source", sourceFile);
		context.setVariable("targetDirectory", targetDirectory);

		processTemplate("move-file", context);

		assert targetFile.exists();
	}

	public void directoryShouldBeMovable() throws IOException {
		File sourceDirectory = createTemporaryFile();
		File parentOfTargetDirectory = createTemporaryDirectory();
		File targetDirectory = new File(parentOfTargetDirectory, sourceDirectory.getName());

		assert !targetDirectory.exists();

		Context context = new DefaultContext();
		context.setVariable("source", sourceDirectory);
		context.setVariable("targetDirectory", parentOfTargetDirectory);

		processTemplate("move-file", context);

		assert targetDirectory.exists();
	}

	@Test(expectedExceptions = AluminumException.class)
	public void movingNonexistentFileShouldCauseException() throws IOException {
		File sourceFile = new File(temporaryDirectory(), generateUniqueName(temporaryDirectory()));

		assert !sourceFile.exists();

		Context context = new DefaultContext();
		context.setVariable("source", sourceFile);
		context.setVariable("targetDirectory", createTemporaryDirectory());

		processTemplate("move-file", context);
	}

	@Test(expectedExceptions = AluminumException.class)
	public void movingFileToDirectoryThatContainsFileWithSameNameShouldCauseException() throws IOException {
		File sourceFile = createTemporaryFile();
		File targetDirectory = createTemporaryDirectory();

		File existingFile = new File(targetDirectory, sourceFile.getName());
		existingFile.createNewFile();
		existingFile.deleteOnExit();

		Context context = new DefaultContext();
		context.setVariable("source", sourceFile);
		context.setVariable("targetDirectory", targetDirectory);

		processTemplate("move-file", context);
	}

	@Test(expectedExceptions = AluminumException.class)
	public void movingFileToNonexistentDirectoryShouldCauseException() throws IOException {
		File sourceFile = createTemporaryFile();
		File targetDirectory = new File(temporaryDirectory(), generateUniqueName(temporaryDirectory()));

		assert !targetDirectory.exists();

		Context context = new DefaultContext();
		context.setVariable("source", sourceFile);
		context.setVariable("targetDirectory", targetDirectory);

		processTemplate("move-file", context);
	}
}