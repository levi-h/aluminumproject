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

import java.net.URL;

/**
 * Finds resources that are available in the class path.
 *
 * @author levi_h
 */
public class ClassPathResourceFinder implements ResourceFinder {
	private String pathPrefix;

	private final Logger logger;

	/**
	 * Creates a class path resource finder that expects resources in the root of the class path.
	 */
	public ClassPathResourceFinder() {
		this(null);
	}

	/**
	 * Creates a class path resource finder.
	 *
	 * @param pathPrefix the (slash-separated) path in which the resources are expected to be found
	 */
	public ClassPathResourceFinder(String pathPrefix) {
		this.pathPrefix = pathPrefix;

		logger = Logger.get(getClass());
	}

	public URL find(String name) throws UtilityException {
		String fullName = (pathPrefix == null) ? name : String.format("%s/%s", pathPrefix, name);

		logger.debug("trying to find resource '", fullName, "'");

		URL url = Thread.currentThread().getContextClassLoader().getResource(fullName);

		if (url == null) {
			throw new UtilityException("can't find resource '", fullName, "'");
		}

		logger.debug("found resource '", fullName, "': ", url);

		return url;
	}
}