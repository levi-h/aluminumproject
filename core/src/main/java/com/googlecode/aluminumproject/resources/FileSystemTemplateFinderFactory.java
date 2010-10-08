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

import com.googlecode.aluminumproject.Logger;
import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.configuration.ConfigurationException;
import com.googlecode.aluminumproject.utilities.resources.CompoundResourceFinder;
import com.googlecode.aluminumproject.utilities.resources.FileSystemResourceFinder;
import com.googlecode.aluminumproject.utilities.resources.ResourceFinder;

import java.io.File;

/**
 * Creates resource finders that locate templates on the file system.
 * <p>
 * The directories that contain the templates can be configured using the {@value #TEMPLATE_DIRECTORIES} parameter. At
 * least one directory should be provided.
 *
 * @author levi_h
 */
public class FileSystemTemplateFinderFactory implements TemplateFinderFactory {
	private String[] templateDirectories;

	private final Logger logger;

	/**
	 * Creates a file system template finder factory.
	 */
	public FileSystemTemplateFinderFactory() {
		logger = Logger.get(getClass());
	}

	public void initialise(Configuration configuration) throws ConfigurationException {
		templateDirectories = configuration.getParameters().getValues(TEMPLATE_DIRECTORIES);

		if (templateDirectories.length == 0) {
			throw new ConfigurationException("please provide one or more template directories");
		} else {
			logger.debug("using template directories ", templateDirectories);
		}
	}

	public ResourceFinder createTemplateFinder() throws ResourceException {
		FileSystemResourceFinder[] resourceFinders = new FileSystemResourceFinder[templateDirectories.length];

		for (int i = 0; i < templateDirectories.length; i++) {
			resourceFinders[i] = new FileSystemResourceFinder(new File(templateDirectories[i]));
		}

		return (resourceFinders.length == 1) ? resourceFinders[0] : new CompoundResourceFinder(resourceFinders);
	}

	/**
	 * The name of the configuration parameter that holds a comma-separated list of directories that can contain
	 * templates.
	 */
	public final static String TEMPLATE_DIRECTORIES = "template_finder_factory.file_system.template_directories";
}