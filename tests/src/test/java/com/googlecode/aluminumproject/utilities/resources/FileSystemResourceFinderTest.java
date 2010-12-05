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

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

@SuppressWarnings("all")
@Test(groups = {"utilities", "slow"})
public class FileSystemResourceFinderTest {
	private File tempDirectory;

	@BeforeTest
	public void createFile() throws IOException {
		tempDirectory = new File(System.getProperty("java.io.tmpdir"));

		File dummyFile = new File(tempDirectory, "dummy.txt");

		if (dummyFile.createNewFile()) {
			dummyFile.deleteOnExit();
		}
	}

	public void existingFileShouldBeFindable() {
		assert new FileSystemResourceFinder(tempDirectory).find("dummy.txt") != null;
	}

	@Test(expectedExceptions = UtilityException.class)
	public void nonexistingFileShouldCauseException() {
		StringBuilder nameBuilder = new StringBuilder("dummy");

		while (new File(tempDirectory, nameBuilder.toString()).exists()) {
			nameBuilder.insert(0, '.');
		}

		new FileSystemResourceFinder(tempDirectory).find(nameBuilder.toString());
	}
}