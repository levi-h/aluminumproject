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

import com.googlecode.aluminumproject.Logger;
import com.googlecode.aluminumproject.utilities.UtilityException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Finds resource store locations in a directory. The name of the resource will become the file name.
 *
 * @author levi_h
 */
public class FileSystemResourceStoreFinder implements ResourceStoreFinder {
	private File directory;

	private final Logger logger;

	/**
	 * Creates a file system resource store finder.
	 *
	 * @param directory the directory in which the files will be created
	 */
	public FileSystemResourceStoreFinder(File directory) {
		this.directory = directory;

		logger = Logger.get(getClass());
	}

	public OutputStream find(String name) throws UtilityException {
		logger.debug("trying to find location to store resource '", name, "' in directory '", directory, "'");

		try {
			return new FileOutputStream(new File(directory, name));
		} catch (FileNotFoundException exception) {
			throw new UtilityException(exception, "can't find place to store resource '", name, "'",
				" in directory '", directory, "'");
		}
	}
}