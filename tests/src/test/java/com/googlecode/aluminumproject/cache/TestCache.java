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

import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.configuration.ConfigurationParameters;
import com.googlecode.aluminumproject.templates.Template;

/**
 * A cache that can be used in tests.
 *
 * @author levi_h
 */
public class TestCache implements Cache {
	private Configuration configuration;

	/**
	 * Creates a test cache.
	 */
	public TestCache() {}

	public void initialise(Configuration configuration, ConfigurationParameters parameters) {
		this.configuration = configuration;
	}

	/**
	 * Returns the configuration that this cache was initialised with.
	 *
	 * @return this cache's configuration
	 */
	public Configuration getConfiguration() {
		return configuration;
	}

	public Template findTemplate(Key key) {
		return null;
	}

	public void storeTemplate(Key key, Template template) {}
}