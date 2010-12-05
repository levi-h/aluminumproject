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
import com.googlecode.aluminumproject.utilities.resources.FileSystemResourceStoreFinder;
import com.googlecode.aluminumproject.utilities.resources.ResourceStoreFinder;

import java.io.File;

/**
 * Creates resource store finders that look on the file system for places to store templates.
 * <p>
 * A file system template store finder factory accepts one configuration parameter, which is required: {@value
 * #DIRECTORY}, that specifies the directory in which the template store finder will look.
 *
 * @author levi_h
 */
public class FileSystemTemplateStoreFinderFactory implements TemplateStoreFinderFactory {
	private String directory;

	private final Logger logger;

	/**
	 * Creates a file system template store finder factory.
	 */
	public FileSystemTemplateStoreFinderFactory() {
		logger = Logger.get(getClass());
	}

	public void initialise(Configuration configuration) throws ConfigurationException {
		directory = configuration.getParameters().getValue(DIRECTORY, null);

		if (directory == null) {
			throw new ConfigurationException("please provide a directory");
		} else {
			logger.debug("using directory ", directory);
		}
	}

	public void disable() {}

	public ResourceStoreFinder createTemplateStoreFinder() {
		return new FileSystemResourceStoreFinder(new File(directory));
	}

	/** The name of the configuration property that contains the directory in which templates can be stored. */
	public final static String DIRECTORY = "template_store_finder.file_system.directory";
}