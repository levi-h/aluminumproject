/*
 * Copyright 2012 Levi Hoogenberg
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
import com.googlecode.aluminumproject.utilities.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/**
 * Type finder that locates types in directories and JAR files.
 */
public class DefaultTypeFinder implements TypeFinder {
	private final Logger logger;

	/**
	 * Creates a default type finder.
	 */
	public DefaultTypeFinder() {
		logger = Logger.get(getClass());
	}

	public void initialise(Configuration configuration) {}

	public void disable() {}

	public List<Class<?>> find(TypeFilter filter, String... packageNames) throws AluminumException {
		if ((packageNames == null) || (packageNames.length == 0)) {
			throw new IllegalArgumentException("please provide at least one package name");
		}

		List<Class<?>> types = new LinkedList<Class<?>>();

		for (String packageName: packageNames) {
			String packagePath = packageName.replace('.', '/');

			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

			try {
				for (URL url: Collections.list(classLoader.getResources(packagePath))) {
					String path = URLDecoder.decode(url.getPath(), "utf-8");
					boolean jar = Arrays.asList("jar", "zip").contains(url.getProtocol());

					if (jar) {
						path = path.substring(0, path.indexOf('!'));
					}

					if (path.startsWith("file:")) {
						path = path.substring(5);
					}

					File file = new File(path);

					if (jar) {
						findInJar(types, filter, classLoader, file, packagePath);
					} else {
						findInDirectory(types, filter, classLoader, file, packagePath);
					}
				}
			} catch (IOException exception) {
				throw new AluminumException(exception, "trying to find types in package ", packageName, " failed");
			}
		}

		logger.debug("matching types found in ", packageNames, ": ", types);

		return types;
	}

	private void findInJar(List<Class<?>> types,
			TypeFilter filter, ClassLoader classLoader, File jarFile, String packagePath) throws AluminumException {
		try {
			JarInputStream in = new JarInputStream(new FileInputStream(jarFile));

			try {
				JarEntry entry;

				while ((entry = in.getNextJarEntry()) != null) {
					String entryName = entry.getName();

					if (entryName.startsWith(packagePath)) {
						try {
							addMatchingType(types, filter, classLoader, entryName);
						} catch (ClassNotFoundException exception) {
							throw new AluminumException(exception, "can't find type ", entryName,
								" in JAR file ", jarFile.getAbsolutePath());
						}
					}
				}
			} finally {
				in.close();
			}
		} catch (IOException exception) {
			throw new AluminumException(exception,
				"trying to find types in JAR file ", jarFile.getAbsolutePath(), " failed");
		}
	}

	private void findInDirectory(List<Class<?>> types,
			TypeFilter filter, ClassLoader classLoader, File directory, String packagePath) throws AluminumException {
		File[] files = directory.listFiles();

		if (files != null) {
			for (File file: files) {
				if (file.isFile()) {
					String path = file.getAbsolutePath().replace(File.separatorChar, '/');

					try {
						addMatchingType(types, filter, classLoader, path.substring(path.indexOf(packagePath)));
					} catch (ClassNotFoundException exception) {
						throw new AluminumException(exception, "can't find type ", path,
							" in directory ", directory.getAbsolutePath());
					}
				} else if (file.isDirectory()) {
					findInDirectory(types, filter, classLoader, file, packagePath);
				}
			}
		}
	}

	private void addMatchingType(List<Class<?>> types,
			TypeFilter filter, ClassLoader classLoader, String typeName) throws ClassNotFoundException {
		if (typeName.endsWith(".class")) {
			Class<?> type = classLoader.loadClass(typeName.substring(0, typeName.lastIndexOf('.')).replace('/', '.'));

			if (filter.accepts(type)) {
				types.add(type);
			}
		}
	}
}