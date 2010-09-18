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
package com.googlecode.aluminumproject.resources;

import com.googlecode.aluminumproject.configuration.ConfigurationParameters;
import com.googlecode.aluminumproject.configuration.TestConfiguration;
import com.googlecode.aluminumproject.utilities.resources.ClassPathResourceFinder;

import org.testng.annotations.Test;

@SuppressWarnings("all")
@Test(groups = {"core", "fast"})
public class ClassPathTemplateFinderFactoryTest {
	public void factoryShouldCreateClassPathResourceFinders() {
		ConfigurationParameters parameters = new ConfigurationParameters();

		TemplateFinderFactory templateFinderFactory = new ClassPathTemplateFinderFactory();
		templateFinderFactory.initialise(new TestConfiguration(parameters), parameters);
		assert templateFinderFactory.createTemplateFinder() instanceof ClassPathResourceFinder;
	}

	@Test(dependsOnMethods = "factoryShouldCreateClassPathResourceFinders")
	public void templatePathShouldDefaultToClassPathRoot() {
		ConfigurationParameters parameters = new ConfigurationParameters();

		TemplateFinderFactory templateFinderFactory = new ClassPathTemplateFinderFactory();
		templateFinderFactory.initialise(new TestConfiguration(parameters), parameters);
		assert templateFinderFactory.createTemplateFinder().find("templates/xml/test.xml") != null;
	}

	@Test(dependsOnMethods = "templatePathShouldDefaultToClassPathRoot")
	public void templatePathShouldChangeClassPathPrefix() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(ClassPathTemplateFinderFactory.TEMPLATE_PATH, "templates/xml");

		TemplateFinderFactory templateFinderFactory = new ClassPathTemplateFinderFactory();
		templateFinderFactory.initialise(new TestConfiguration(parameters), parameters);
		assert templateFinderFactory.createTemplateFinder().find("test.xml") != null;
	}
}