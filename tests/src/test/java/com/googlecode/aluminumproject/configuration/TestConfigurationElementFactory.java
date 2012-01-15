/*
 * Copyright 2009-2012 Aluminum project
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
package com.googlecode.aluminumproject.configuration;

import com.googlecode.aluminumproject.AluminumException;

/**
 * A configuration element factory that can be used in tests.
 */
public class TestConfigurationElementFactory implements ConfigurationElementFactory {
	private Configuration configuration;

	/**
	 * Creates a test configuration element factory.
	 */
	public TestConfigurationElementFactory() {}

	public void initialise(Configuration configuration) {
		this.configuration = configuration;
	}

	public void disable() {
		configuration = null;
	}

	/**
	 * Returns the configuration that this configuration element factory was initialised with.
	 *
	 * @return this configuration element factory's configuration
	 */
	public Configuration getConfiguration() {
		return configuration;
	}

	public <T> T instantiate(String className, Class<T> type) throws AluminumException {
		try {
			return type.cast(Thread.currentThread().getContextClassLoader().loadClass(className).newInstance());
		} catch (Exception exception) {
			throw new AluminumException(exception, "can't instantiate '", className, "'");
		}
	}
}