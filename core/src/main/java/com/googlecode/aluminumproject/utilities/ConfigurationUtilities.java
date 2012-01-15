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
package com.googlecode.aluminumproject.utilities;

import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.configuration.ConfigurationElement;
import com.googlecode.aluminumproject.libraries.Library;
import com.googlecode.aluminumproject.libraries.LibraryInformation;

import java.util.Iterator;

/**
 * Contains utility methods that help when working with {@link Configuration configurations} and {@link
 * ConfigurationElement configuration elements}.
 */
public class ConfigurationUtilities {
	private ConfigurationUtilities() {}

	/**
	 * Finds a library in a configuration by its URL or returns {@code null} if the configuration does not contain a
	 * library with that URL. Both regular and the versioned library URLs are recognised.
	 *
	 * @param configuration the configuration to find the library in
	 * @param url the (possibly versioned) URL of the library to find
	 * @return the library with the given URL or {@code null} if the given configuration does not contain a library with
	 *         the requested URL
	 */
	public static Library findLibrary(Configuration configuration, String url) {
		Iterator<Library> itLibraries = configuration.getLibraries().iterator();

		Library library = null;

		while ((library == null) && itLibraries.hasNext()) {
			library = itLibraries.next();

			LibraryInformation libraryInformation = library.getInformation();

			if (!libraryInformation.getUrl().equals(url) && !libraryInformation.getVersionedUrl().equals(url)) {
				library = null;
			}
		}

		return library;
	}
}