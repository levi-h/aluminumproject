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
package com.googlecode.aluminumproject.serialisers.test;

import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.configuration.ConfigurationParameters;
import com.googlecode.aluminumproject.serialisers.Serialiser;
import com.googlecode.aluminumproject.templates.Template;

/**
 * A serialiser that can be used in tests.
 *
 * @author levi_h
 */
public class TestSerialiser implements Serialiser {
	private Configuration configuration;

	/**
	 * Creates a test serialiser.
	 */
	public TestSerialiser() {}

	public void initialise(Configuration configuration, ConfigurationParameters parameters) {
		this.configuration = configuration;
	}

	/**
	 * Returns the configuration that this serialiser was initialised with.
	 *
	 * @return the configuration used
	 */
	public Configuration getConfiguration() {
		return configuration;
	}

	public void serialiseTemplate(Template template, String name) {}
}