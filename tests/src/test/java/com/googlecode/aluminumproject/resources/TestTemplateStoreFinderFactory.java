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

import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.utilities.resources.ResourceStoreFinder;

/**
 * A template store finder factory that can be used in tests.
 *
 * @author levi_h
 */
public class TestTemplateStoreFinderFactory implements TemplateStoreFinderFactory {
	private Configuration configuration;

	/**
	 * Creates a test template store finder factory.
	 */
	public TestTemplateStoreFinderFactory() {}

	public void initialise(Configuration configuration) {
		this.configuration = configuration;
	}

	public void disable() {
		configuration = null;
	}

	/**
	 * Returns the configuration that this template store finder factory was initialised with.
	 *
	 * @return this template store finder factory's configuration
	 */
	public Configuration getConfiguration() {
		return configuration;
	}

	public ResourceStoreFinder createTemplateStoreFinder() {
		return null;
	}
}