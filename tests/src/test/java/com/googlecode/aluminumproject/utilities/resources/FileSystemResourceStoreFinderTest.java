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
package com.googlecode.aluminumproject.utilities.resources;

import com.googlecode.aluminumproject.utilities.UtilityException;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("all")
@Test(groups = {"utilities", "slow"})
public class FileSystemResourceStoreFinderTest {
	private File tempDirectory;

	@BeforeMethod
	public void locateTempDirectory() {
		tempDirectory = new File(System.getProperty("java.io.tmpdir"));
	}

	public void locationForNonexistingFileShouldBeFindable() throws IOException {
		String name = generateName(true);

		try {
			OutputStream out = new FileSystemResourceStoreFinder(tempDirectory).find(name);
			assert out != null;

			out.close();
		} finally {
			File file = new File(name);

			if (file.exists()) {
				file.deleteOnExit();
			}
		}
	}

	@Test(expectedExceptions = UtilityException.class)
	public void findingLocationForResourceWithDirectoryNameShouldCauseException() {
		String name = generateName(false);

		File directory = new File(tempDirectory, name);
		assert directory.mkdir();
		directory.deleteOnExit();

		new FileSystemResourceStoreFinder(tempDirectory).find(name);
	}

	private String generateName(boolean includeExtension) {
		String name;

		int i = 0;

		do {
			name = String.format("%s-%d%s", getClass().getSimpleName(), i++, includeExtension ? ".tmp" : "");
		} while (new File(name).exists());

		return name;
	}
}