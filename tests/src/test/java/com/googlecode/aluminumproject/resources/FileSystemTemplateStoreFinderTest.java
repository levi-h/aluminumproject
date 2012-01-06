/*
 * Copyright 2011-2012 Levi Hoogenberg
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
package com.googlecode.aluminumproject.resources;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.configuration.ConfigurationParameters;
import com.googlecode.aluminumproject.configuration.TestConfiguration;

import java.io.IOException;
import java.io.OutputStream;

import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
@Test(groups = {"core", "fast"})
public class FileSystemTemplateStoreFinderTest {
	@Test(expectedExceptions = AluminumException.class)
	public void omittingTemplateDirectoryShouldCauseException() {
		TemplateStoreFinder templateStoreFinder = new FileSystemTemplateStoreFinder();
		templateStoreFinder.initialise(new TestConfiguration(new ConfigurationParameters()));
	}

	public void locationForTemplateShouldBeFindableInTemplateDirectory() throws IOException {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(FileSystemTemplateStoreFinder.TEMPLATE_DIRECTORY, System.getProperty("java.io.tmpdir"));

		TemplateStoreFinder templateStoreFinder = new FileSystemTemplateStoreFinder();
		templateStoreFinder.initialise(new TestConfiguration(parameters));

		OutputStream stream = templateStoreFinder.find("template");
		assert stream != null;
		stream.close();
	}

	@Test(expectedExceptions = AluminumException.class)
	public void tryingToFindUnavailableLocationForTemplateShouldCauseException() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(FileSystemTemplateStoreFinder.TEMPLATE_DIRECTORY,
			Thread.currentThread().getContextClassLoader().getResource("templates").getPath());

		TemplateStoreFinder templateStoreFinder = new FileSystemTemplateStoreFinder();
		templateStoreFinder.initialise(new TestConfiguration(parameters));

		templateStoreFinder.find("core");
	}
}