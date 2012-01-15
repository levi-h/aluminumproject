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

import com.googlecode.aluminumproject.libraries.io.IoLibraryTest;

import java.io.File;
import java.io.IOException;

import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
@Test(groups = {"libraries", "libraries-io", "fast"})
public class FilesTest extends IoLibraryTest {
	public void nameOfFileShouldResultInLastPartOfPath() {
		String name = Files.name(new File("/tmp/test.txt"));
		assert name != null;
		assert name.equals("test.txt");
	}

	public void nameOfFileWithEmptyNameShouldResultInEmptyString() {
		String name = Files.name(new File(""));
		assert name != null;
		assert name.equals("");
	}

	public void nameWithoutExtensionShouldBeDeterminable() {
		String extension = Files.nameWithoutExtension(new File("test.txt"));
		assert extension != null;
		assert extension.equals("test");
	}

	public void nameWithoutExtensionOfFileWithoutExtensionShouldEqualName() {
		String extension = Files.nameWithoutExtension(new File("test"));
		assert extension != null;
		assert extension.equals("test");
	}

	public void extensionShouldBeDeterminable() {
		String extension = Files.extension(new File("test.txt"));
		assert extension != null;
		assert extension.equals("txt");
	}

	public void extensionOfFileWithoutExtensionShouldResultInEmptyString() {
		String extension = Files.extension(new File("test"));
		assert extension != null;
		assert extension.equals("");
	}

	public void sizeOfFileShouldBeDeterminable() throws IOException {
		assert Files.size(createTemporaryFile(5)) == 35;
	}

	public void contentsOfFileShouldBeDeterminable() throws IOException {
		byte[] contents = Files.contents(createTemporaryFile(3));
		assert contents != null;

		String[] lines = new String(contents).split("\n");
		assert lines != null;
		assert lines.length == 3;

		assert lines[0] != null;
		assert lines[0].equals("line 1");

		assert lines[1] != null;
		assert lines[1].equals("line 2");

		assert lines[2] != null;
		assert lines[2].equals("line 3");
	}

	public void typeOfFileShouldBeDeterminable() throws IOException {
		File file = createTemporaryFile();
		assert Files.isFile(file);
		assert !Files.isDirectory(file);

		File directory = createTemporaryDirectory();
		assert !Files.isFile(directory);
		assert Files.isDirectory(directory);
	}

	public void nonexistentNewFileShouldBeCreatable() throws IOException {
		File directory = Directories.temporaryDirectory();

		File file = Files.newFile(directory, generateUniqueName(directory));
		assert file != null;
		assert file.exists();
		assert file.isFile();

		file.deleteOnExit();
	}

	public void creatingNewFileShouldCreateNonexistentIntermediateDirectories() throws IOException {
		File temporaryDirectory = Directories.temporaryDirectory();
		File directory = new File(temporaryDirectory, generateUniqueName(temporaryDirectory));

		assert !directory.exists();

		Files.newFile(directory, "intermediate/aluminum");

		assert directory.exists();
		assert new File(directory, "intermediate").exists();
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void creatingExistingFileShouldCauseException() throws IOException {
		File file = createTemporaryFile();

		Files.newFile(file.getParentFile(), file.getName());
	}
}