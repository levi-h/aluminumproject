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
import com.googlecode.aluminumproject.utilities.resources.MemoryResourceStoreFinder;
import com.googlecode.aluminumproject.utilities.resources.ResourceStoreFinder;

/**
 * A template store finder factory that creates an in-memory resource store finder, which is returned for each call to
 * the {@link #createTemplateStoreFinder() createTemplateStoreFinder method}.
 *
 * @author levi_h
 */
public class MemoryTemplateStoreFinderFactory implements TemplateStoreFinderFactory {
	private ResourceStoreFinder templateStoreFinder;

	private final Logger logger;

	/**
	 * Creates an in-memory template store finder factory.
	 */
	public MemoryTemplateStoreFinderFactory() {
		logger = Logger.get(getClass());
	}

	public void initialise(Configuration configuration) {
		logger.debug("creating in-memory template store finder");

		templateStoreFinder = new MemoryResourceStoreFinder();
	}

	public void disable() {}

	public ResourceStoreFinder createTemplateStoreFinder() {
		return templateStoreFinder;
	}
}