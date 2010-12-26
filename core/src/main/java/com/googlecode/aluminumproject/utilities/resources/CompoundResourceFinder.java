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

import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * A resource finder that delegates to one or more other resource finders.
 * <p>
 * When a compound resource finder is asked to find a resource, it starts its first resource finder to find it. If no
 * resource could be found, the second resource finder is requested to find the resource. This process is repeated until
 * either the resource is found or the resource finder list is exhausted.
 *
 * @author levi_h
 */
public class CompoundResourceFinder implements ResourceFinder {
	private List<ResourceFinder> resourceFinders;

	private final Logger logger;

	/**
	 * Creates a compound resource finder.
	 *
	 * @param resourceFinders the resource finders to delegate to
	 */
	public CompoundResourceFinder(ResourceFinder... resourceFinders) {
		this.resourceFinders = Arrays.asList(resourceFinders);

		logger = Logger.get(getClass());
	}

	public URL find(String name) throws UtilityException {
		URL resource = null;

		Iterator<ResourceFinder> it = resourceFinders.iterator();

		while (it.hasNext() && (resource == null)) {
			ResourceFinder resourceFinder = it.next();

			logger.debug("trying ", resourceFinder);

			try {
				resource = resourceFinder.find(name);
			} catch (UtilityException exception) {}
		}

		if (resource == null) {
			throw new UtilityException("can't find resource with name '", name, "'");
		}

		return resource;
	}
}