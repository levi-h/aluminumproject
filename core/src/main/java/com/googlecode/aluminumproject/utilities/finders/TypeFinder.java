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
package com.googlecode.aluminumproject.utilities.finders;

import com.googlecode.aluminumproject.utilities.UtilityException;

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
 * Finds types in the class path, based on a filter. Types can reside either in a directory or in a JAR file. To find
 * types, supply a {@link TypeFilter filter} and one or more package names to the {@link #find(TypeFilter, String...)
 * find method}.
 *
 * @author levi_h
 */
public class TypeFinder {
	private TypeFinder() {}

	/**
	 * The filter used by the {@link TypeFinder#find(TypeFilter, String...) find method}.
	 *
	 * @author levi_h
	 */
	public static interface TypeFilter {
		/**
		 * Determines whether a certain type is accepted by this filter.
		 *
		 * @param type the type to possibly include
		 * @return {@code true} if this filter matches the given type, {@code false} otherwise
		 */
		boolean accepts(Class<?> type);
	}

	/**
	 * Finds types matching a certain filter in a number of packages.
	 *
	 * @param filter the filter to apply
	 * @param packageNames the names of the packages to include in the search (subpackages will be included)
	 * @return a list with all types in the given packages that match the given filter (may be empty)
	 * @throws IllegalArgumentException when no package names are supplied
	 * @throws UtilityException when something goes wrong while trying to find matching types
	 */
	public static List<Class<?>> find(TypeFilter filter, String... packageNames) throws UtilityException {
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
				throw new UtilityException(exception, "trying to find types in package ", packageName, " failed");
			}
		}

		return types;
	}

	private static void findInJar(List<Class<?>> types, TypeFilter filter, ClassLoader classLoader,
			File jarFile, String packagePath) throws UtilityException {
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
							throw new UtilityException(exception, "can't find type ", entryName,
								" in JAR file ", jarFile.getAbsolutePath());
						}
					}
				}
			} finally {
				in.close();
			}
		} catch (IOException exception) {
			throw new UtilityException(exception,
				"trying to find types in JAR file ", jarFile.getAbsolutePath(), " failed");
		}
	}

	private static void findInDirectory(List<Class<?>> types, TypeFilter filter, ClassLoader classLoader,
			File directory, String packagePath) throws UtilityException {
		File[] files = directory.listFiles();

		if (files != null) {
			for (File file: files) {
				if (file.isFile()) {
					String path = file.getAbsolutePath().replace(File.separatorChar, '/');

					try {
						addMatchingType(types, filter, classLoader, path.substring(path.indexOf(packagePath)));
					} catch (ClassNotFoundException exception) {
						throw new UtilityException(exception, "can't find type ", path,
							" in directory ", directory.getAbsolutePath());
					}
				} else if (file.isDirectory()) {
					findInDirectory(types, filter, classLoader, file, packagePath);
				}
			}
		}
	}

	private static void addMatchingType(List<Class<?>> types, TypeFilter filter, ClassLoader classLoader,
			String typeName) throws ClassNotFoundException {
		if (typeName.endsWith(".class")) {
			Class<?> type = classLoader.loadClass(typeName.substring(0, typeName.lastIndexOf('.')).replace('/', '.'));

			if (filter.accepts(type)) {
				types.add(type);
			}
		}
	}
}