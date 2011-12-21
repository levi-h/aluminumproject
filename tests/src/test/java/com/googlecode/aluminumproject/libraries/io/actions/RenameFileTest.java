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

import java.io.File;
import java.io.IOException;

import org.testng.annotations.Test;

@SuppressWarnings("all")
@Test(groups = {"libraries", "libraries-io", "slow"})
public class RenameFileTest extends IoLibraryTest {
	public void fileShouldBeRenameable() throws IOException {
		File sourceFile = createTemporaryFile();

		File parentDirectory = sourceFile.getParentFile();
		File targetFile = new File(parentDirectory, generateUniqueName(parentDirectory));

		assert sourceFile.exists();
		assert !targetFile.exists();

		Context context = new DefaultContext();
		context.setVariable("source", sourceFile);
		context.setVariable("targetName", targetFile.getName());

		processTemplate("rename-file", context);

		assert !sourceFile.exists();
		assert targetFile.exists();
	}

	public void directoryShouldBeRenameable() throws IOException {
		File sourceDirectory = createTemporaryDirectory();

		File parentDirectory = sourceDirectory.getParentFile();
		File targetDirectory = new File(parentDirectory, generateUniqueName(parentDirectory));

		assert sourceDirectory.exists();
		assert !targetDirectory.exists();

		Context context = new DefaultContext();
		context.setVariable("source", sourceDirectory);
		context.setVariable("targetName", targetDirectory.getName());

		processTemplate("rename-file", context);

		assert !sourceDirectory.exists();
		assert targetDirectory.exists();
	}

	@Test(expectedExceptions = AluminumException.class)
	public void renamingNonexistentFileShouldCauseException() throws IOException {
		File parentDirectory = Directories.temporaryDirectory();
		File sourceFile = new File(parentDirectory, generateUniqueName(parentDirectory));

		assert !sourceFile.exists();

		Context context = new DefaultContext();
		context.setVariable("source", sourceFile);
		context.setVariable("targetName", String.format("%s'", sourceFile.getName()));

		processTemplate("rename-file", context);
	}

	@Test(expectedExceptions = AluminumException.class)
	public void movingFileShouldCauseException() throws IOException {
		File sourceFile = createTemporaryFile();

		Context context = new DefaultContext();
		context.setVariable("source", sourceFile);
		context.setVariable("targetName",
			String.format("%s/%s", generateUniqueName(sourceFile.getParentFile()), sourceFile.getName()));

		processTemplate("rename-file", context);
	}

	@Test(expectedExceptions = AluminumException.class)
	public void usingExistingTargetNameShouldCauseException() throws IOException {
		File sourceFile = createTemporaryFile();
		File targetFile = createTemporaryFile();

		Context context = new DefaultContext();
		context.setVariable("source", sourceFile);
		context.setVariable("targetName", targetFile.getName());

		processTemplate("rename-file", context);
	}
}