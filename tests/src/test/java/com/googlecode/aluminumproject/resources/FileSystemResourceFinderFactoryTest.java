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

import static com.googlecode.aluminumproject.resources.FileSystemTemplateFinderFactory.TEMPLATE_DIRECTORIES;

import com.googlecode.aluminumproject.configuration.ConfigurationException;
import com.googlecode.aluminumproject.configuration.ConfigurationParameters;
import com.googlecode.aluminumproject.configuration.DefaultConfiguration;
import com.googlecode.aluminumproject.utilities.resources.CompoundResourceFinder;
import com.googlecode.aluminumproject.utilities.resources.FileSystemResourceFinder;

import org.testng.annotations.Test;

@SuppressWarnings("all")
@Test(groups = {"core", "fast"})
public class FileSystemResourceFinderFactoryTest {
	@Test(expectedExceptions = ConfigurationException.class)
	public void omittingTemplateDirectoriesShouldCauseException() {
		TemplateFinderFactory templateFinderFactory = new FileSystemTemplateFinderFactory();
		templateFinderFactory.initialise(new DefaultConfiguration(new ConfigurationParameters()));
	}

	public void supplyingSingleTemplateDirectoryShouldResultInFileSystemResourceFinder() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(TEMPLATE_DIRECTORIES, System.getProperty("java.io.tmpdir"));

		TemplateFinderFactory templateFinderFactory = new FileSystemTemplateFinderFactory();
		templateFinderFactory.initialise(new DefaultConfiguration(parameters));
		assert templateFinderFactory.createTemplateFinder() instanceof FileSystemResourceFinder;
	}

	public void supplyingMoreThanOneTemplateDirectoryShouldResultInCompoundResourceFinder() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(TEMPLATE_DIRECTORIES,
			String.format("%s, %s", System.getProperty("user.home"), System.getProperty("java.io.tmpdir")));

		TemplateFinderFactory templateFinderFactory = new FileSystemTemplateFinderFactory();
		templateFinderFactory.initialise(new DefaultConfiguration(parameters));
		assert templateFinderFactory.createTemplateFinder() instanceof CompoundResourceFinder;
	}
}