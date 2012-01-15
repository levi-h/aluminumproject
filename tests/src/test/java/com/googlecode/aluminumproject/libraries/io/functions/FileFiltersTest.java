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

import static com.googlecode.aluminumproject.libraries.io.functions.FileFilters.directories;
import static com.googlecode.aluminumproject.libraries.io.functions.FileFilters.files;
import static com.googlecode.aluminumproject.libraries.io.functions.FileFilters.filesNamedLike;
import static com.googlecode.aluminumproject.libraries.io.functions.FileFilters.filesWithExtension;

import com.googlecode.aluminumproject.libraries.io.IoLibraryTest;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
@Test(groups = {"libraries", "libraries-io", "slow"})
public class FileFiltersTest extends IoLibraryTest {
	public void fileFilterShouldAcceptFiles() throws IOException {
		FileFilter fileFilter = files();
		assert fileFilter != null;
		assert fileFilter.accept(createTemporaryFile());
	}

	public void fileFilterShouldNotAcceptDirectories() throws IOException {
		FileFilter fileFilter = files();
		assert fileFilter != null;
		assert !fileFilter.accept(createTemporaryDirectory());
	}

	public void directoryFilterShouldAcceptDirectories() throws IOException {
		FileFilter directoryFilter = directories();
		assert directoryFilter != null;
		assert directoryFilter.accept(createTemporaryDirectory());
	}

	public void directoryFilterShouldNotAcceptFiles() throws IOException {
		FileFilter directoryFilter = directories();
		assert directoryFilter != null;
		assert !directoryFilter.accept(createTemporaryFile());
	}

	public void nameFilterShouldAcceptFilesWhoseNamesMatchPattern() {
		FileFilter nameFilter = filesNamedLike(".*\\.jpg");
		assert nameFilter != null;
		assert nameFilter.accept(new File("image.jpg"));
	}

	public void nameFilterShouldNotAcceptFilesWhoseNamesDoNotMatchPattern() {
		FileFilter nameFilter = filesNamedLike(".*\\.jpg");
		assert nameFilter != null;
		assert !nameFilter.accept(new File("image.gif"));
	}

	public void extensionFilterShouldAcceptFilesWithDesiredExtensions() {
		FileFilter extensionFilter = filesWithExtension("jpg");
		assert extensionFilter != null;
		assert extensionFilter.accept(new File("image.jpg"));
	}

	@Test(dependsOnMethods = "extensionFilterShouldAcceptFilesWithDesiredExtensions")
	public void extensionFilterShouldBeCaseInsensitive() {
		assert filesWithExtension("jpg").accept(new File("image.JPG"));
	}

	public void extensionFilterShouldNotAcceptFilesWithOtherExtensions() {
		FileFilter extensionFilter = filesWithExtension("jpg");
		assert extensionFilter != null;
		assert !extensionFilter.accept(new File("image.gif"));
	}
}