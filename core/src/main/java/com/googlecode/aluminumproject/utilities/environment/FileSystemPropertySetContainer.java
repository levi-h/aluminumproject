/*
 * Copyright 2010-2012 Aluminum project
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
package com.googlecode.aluminumproject.utilities.environment;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.utilities.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * A property set container that uses the file system to both read and write property sets. Each property set is stored
 * in a properties file that carries its name; the location of the property sets is {@code ~/.aluminum}.
 */
public class FileSystemPropertySetContainer implements PropertySetContainer {
	private File location;

	private final Logger logger;

	/**
	 * Creates a file system property set container.
	 */
	public FileSystemPropertySetContainer() {
		this(new File(System.getProperty("user.home"), ".aluminum"));
	}

	/**
	 * Creates a file system property set container at a custom location.
	 *
	 * @param location the location of the property set files
	 */
	FileSystemPropertySetContainer(File location) {
		this.location = location;

		logger = Logger.get(getClass());
	}

	public boolean containsPropertySet(String name) {
		return getPropertyFile(name).exists();
	}

	public Properties readPropertySet(String name) throws AluminumException {
		Properties propertySet = new Properties();

		try {
			InputStream in = new FileInputStream(getPropertyFile(name));

			try {
				logger.debug("reading property set '", name, "'");

				propertySet.load(in);
			} finally {
				in.close();
			}
		} catch (IOException exception) {
			throw new AluminumException(exception, "can't read property set '", name, "'");
		}

		return propertySet;
	}

	public void writePropertySet(String name, Properties propertySet) throws AluminumException {
		if (!location.exists() && !location.mkdirs()) {
			throw new AluminumException("can't create property set location ", location.getAbsolutePath());
		}

		try {
			OutputStream out = new FileOutputStream(getPropertyFile(name));

			try {
				logger.debug("writing property set '", name, "'");

				propertySet.store(out, null);
			} finally {
				out.close();
			}
		} catch (IOException exception) {
			throw new AluminumException(exception, "can't write property set '", name, "'");
		}
	}

	public void removePropertySet(String name) throws AluminumException {
		File propertyFile = getPropertyFile(name);

		if (!propertyFile.exists()) {
			throw new AluminumException("can't find property set '", name, "' to remove");
		} else if (!propertyFile.delete()) {
			throw new AluminumException("can't remove property set '", name, "'");
		}
	}

	private File getPropertyFile(String name) {
		return new File(location, String.format("%s.properties", name));
	}
}