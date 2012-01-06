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
package com.googlecode.aluminumproject.finders;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.configuration.ConfigurationParameters;
import com.googlecode.aluminumproject.configuration.TestConfiguration;

import java.io.File;
import java.io.IOException;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
@Test(groups = {"core", "fast"})
public class FileSystemTemplateFinderTest extends TemplateFinderTest {
	private ClassLoader classLoader;

	@BeforeMethod
	public void createClassLoader() {
		classLoader = Thread.currentThread().getContextClassLoader();
	}

	@Test(expectedExceptions = AluminumException.class)
	public void omittingTemplateDirectoriesShouldCauseException() {
		TemplateFinder templateFinder = new FileSystemTemplateFinder();
		templateFinder.initialise(new TestConfiguration(new ConfigurationParameters()));
	}

	public void templatesShouldBeFindableInTemplateDirectory() throws IOException {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(FileSystemTemplateFinder.TEMPLATE_DIRECTORIES,
			new File(classLoader.getResource("templates").getPath()).getAbsolutePath());

		TemplateFinder templateFinder = new FileSystemTemplateFinder();
		templateFinder.initialise(new TestConfiguration(parameters));
		assertAvailable(templateFinder, "test.xml");
	}

	public void templatesShouldBeFindableInTemplateDirectories() throws IOException {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(FileSystemTemplateFinder.TEMPLATE_DIRECTORIES, String.format("%s, %s",
			new File(classLoader.getResource("templates").getPath()).getAbsolutePath(),
			new File(classLoader.getResource("templates/core").getPath()).getAbsolutePath()));

		TemplateFinder templateFinder = new FileSystemTemplateFinder();
		templateFinder.initialise(new TestConfiguration(parameters));
		assertAvailable(templateFinder, "if.xml");
	}

	@Test(
		dependsOnMethods = "templatesShouldBeFindableInTemplateDirectory",
		expectedExceptions = AluminumException.class
	)
	public void tryingToFindNonexistentTemplateShouldCauseException() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(FileSystemTemplateFinder.TEMPLATE_DIRECTORIES,
			new File(classLoader.getResource("templates").getPath()).getAbsolutePath());

		TemplateFinder templateFinder = new FileSystemTemplateFinder();
		templateFinder.initialise(new TestConfiguration(parameters));
		templateFinder.find("if.xml");
	}
}