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
package com.googlecode.aluminumproject.cache;

import com.googlecode.aluminumproject.Logger;
import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.templates.Template;

import java.util.HashMap;
import java.util.Map;

/**
 * An in-memory {@link Cache cache} implementation. It does not have any configuration parameters.
 *
 * @author levi_h
 */
public class MemoryCache implements Cache {
	private Map<Key, Template> templates;

	private final Logger logger;

	/**
	 * Creates a memory cache.
	 */
	public MemoryCache() {
		templates = new HashMap<Key, Template>();

		logger = Logger.get(getClass());
	}

	public void initialise(Configuration configuration) {}

	public void storeTemplate(Key key, Template template) {
		logger.debug("storing ", template, " under ", key);

		templates.put(key, template);
	}

	public Template findTemplate(Key key) {
		Template template = templates.get(key);

		logger.debug("template with ", key, ": ", template);

		return template;
	}
}