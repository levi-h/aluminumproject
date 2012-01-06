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
import com.googlecode.aluminumproject.configuration.Configuration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Finds locations for templates on the file system.
 * <p>
 * A file system template store finder accepts one configuration parameter, which is required: {@value
 * #TEMPLATE_DIRECTORY}. It specifies the directory in which templates will be stored.
 *
 * @author levi_h
 */
public class FileSystemTemplateStoreFinder extends AbstractTemplateStoreFinder {
	private String directory;

	/**
	 * Creates a file system template store finder.
	 */
	public FileSystemTemplateStoreFinder() {}

	@Override
	public void initialise(Configuration configuration) throws AluminumException {
		super.initialise(configuration);

		directory = configuration.getParameters().getValue(TEMPLATE_DIRECTORY, null);

		if (directory == null) {
			throw new AluminumException("please provide a directory");
		} else {
			logger.debug("using directory ", directory);
		}
	}

	public OutputStream find(String name) throws AluminumException {
		logger.debug("trying to find location to store template '", name, "' in directory '", directory, "'");

		try {
			return new FileOutputStream(new File(directory, name));
		} catch (FileNotFoundException exception) {
			throw new AluminumException(exception, "can't find place in directory '", directory, "'",
				" to store template '", name, "'");
		}
	}

	/** The name of the configuration property that contains the directory in which templates can be stored. */
	public final static String TEMPLATE_DIRECTORY = "template_store_finder.file_system.template_directory";
}