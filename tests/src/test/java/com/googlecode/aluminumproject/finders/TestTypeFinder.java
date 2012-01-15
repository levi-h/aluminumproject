/*
 * Copyright 2012 Aluminum project
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

import com.googlecode.aluminumproject.configuration.Configuration;

import java.util.Collections;
import java.util.List;

/**
 * A type finder that can be used in tests.
 */
public class TestTypeFinder implements TypeFinder {
	private Configuration configuration;

	/**
	 * Creates a test type finder.
	 */
	public TestTypeFinder() {}

	public void initialise(Configuration configuration) {
		this.configuration = configuration;
	}

	public void disable() {
		configuration = null;
	}

	/**
	 * Returns the configuration that this type finder was initialised with.
	 *
	 * @return this type finder's configuration
	 */
	public Configuration getConfiguration() {
		return configuration;
	}

	public List<Class<?>> find(TypeFilter filter, String... packageNames) {
		return Collections.emptyList();
	}
}