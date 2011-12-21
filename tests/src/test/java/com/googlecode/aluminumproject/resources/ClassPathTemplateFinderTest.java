/*
 * Copyright 2011 Levi Hoogenberg
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

import org.testng.annotations.Test;

@SuppressWarnings("all")
@Test(groups = {"core", "fast"})
public class ClassPathTemplateFinderTest extends TemplateFinderTest {
	public void templatePathShouldDefaultToClassPathRoot() throws IOException {
		TemplateFinder templateFinder = new ClassPathTemplateFinder();
		templateFinder.initialise(new TestConfiguration(new ConfigurationParameters()));
		assertAvailable(templateFinder, "templates/test.xml");
	}

	@Test(dependsOnMethods = "templatePathShouldDefaultToClassPathRoot")
	public void templatePathShouldBeConfigurable() throws IOException {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(ClassPathTemplateFinder.TEMPLATE_PATH, "templates");

		TemplateFinder templateFinder = new ClassPathTemplateFinder();
		templateFinder.initialise(new TestConfiguration(parameters));
		assertAvailable(templateFinder, "test.xml");
	}

	@Test(dependsOnMethods = "templatePathShouldDefaultToClassPathRoot", expectedExceptions = AluminumException.class)
	public void tryingToFindNonexistentTemplateShouldCauseException() {
		TemplateFinder templateFinder = new ClassPathTemplateFinder();
		templateFinder.initialise(new TestConfiguration(new ConfigurationParameters()));
		templateFinder.find("test.xml");
	}
}