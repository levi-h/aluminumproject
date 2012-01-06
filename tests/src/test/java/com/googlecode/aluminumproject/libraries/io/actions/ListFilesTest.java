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

import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.DefaultContext;
import com.googlecode.aluminumproject.libraries.io.IoLibraryTest;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
@Test(groups = {"libraries", "libraries-io", "slow"})
public class ListFilesTest extends IoLibraryTest {
	private File firstTemporaryFile;
	private File secondTemporaryFile;

	@BeforeClass
	public void createTemporaryFiles() throws IOException {
		firstTemporaryFile = createTemporaryFile();
		secondTemporaryFile = createTemporaryFile();
	}

	public void notSupplyingFilterShouldListAllFiles() {
		Context context = new DefaultContext();

		processTemplate("list-files", context);

		Object files = context.getVariable(Context.TEMPLATE_SCOPE, "files");
		assert files instanceof List;

		List<?> fileList = (List<?>) files;
		assert fileList.size() >= 2;
		assert fileList.contains(firstTemporaryFile);
		assert fileList.contains(secondTemporaryFile);
	}

	@Test(dependsOnMethods = "notSupplyingFilterShouldListAllFiles")
	public void onlyFilteredFilesShouldBeListed() {
		Context context = new DefaultContext();
		context.setVariable("pattern", Pattern.quote(firstTemporaryFile.getName()));

		processTemplate("list-files-with-filter", context);

		List<?> fileList = (List<?>) context.getVariable(Context.TEMPLATE_SCOPE, "files");
		assert fileList.contains(firstTemporaryFile);
		assert !fileList.contains(secondTemporaryFile);
	}
}