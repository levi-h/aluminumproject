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

import com.googlecode.aluminumproject.utilities.Logger;
import com.googlecode.aluminumproject.utilities.UtilityException;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Finds files inside a directory.
 *
 * @author levi_h
 */
public class FileSystemResourceFinder implements ResourceFinder {
	private File directory;

	private final Logger logger;

	/**
	 * Creates a file system resource finder.
	 *
	 * @param directory the directory in which the files can be found
	 */
	public FileSystemResourceFinder(File directory) {
		this.directory = directory;

		logger = Logger.get(getClass());
	}

	public URL find(String name) throws UtilityException {
		logger.debug("trying to find resource '", name, "' in directory '", directory, "'");

		File file = new File(directory, name);

		if (file.exists()) {
			logger.debug("found resource '", name, "': ", file.getAbsolutePath());

			try {
				return file.toURI().toURL();
			} catch (MalformedURLException exception) {
				throw new UtilityException(exception, "can't convert file system resource '", name, "'to URL");
			}
		} else {
			throw new UtilityException("can't find resource '", name, "' in directory '", directory, "'");
		}
	}
}