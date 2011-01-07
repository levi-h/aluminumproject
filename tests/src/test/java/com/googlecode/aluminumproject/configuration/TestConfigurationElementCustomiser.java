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
package com.googlecode.aluminumproject.configuration;

import java.lang.reflect.Method;

/**
 * A configuration element customiser that can be used in tests. It checks whether objects have a public {@code
 * customise()} method and, if they have, invokes it.
 *
 * @author levi_h
 */
public class TestConfigurationElementCustomiser implements ConfigurationElementCustomiser {
	private Configuration configuration;

	/**
	 * Creates a test configuration element customiser.
	 */
	public TestConfigurationElementCustomiser() {}

	public void initialise(Configuration configuration) {
		this.configuration = configuration;
	}

	public void disable() {
		configuration = null;
	}

	/**
	 * Returns the configuration that this configuration element customiser was initialised with.
	 *
	 * @return this configuration element customiser's configuration
	 */
	public Configuration getConfiguration() {
		return configuration;
	}

	public void customise(Object object) throws ConfigurationException {
		for (Method method: object.getClass().getMethods()) {
			if (method.getName().equals("customise") && (method.getParameterTypes().length == 0)) {
				try {
					method.invoke(object);
				} catch (Exception exception) {
					throw new ConfigurationException(exception, "can't customise ", object);
				}
			}
		}
	}
}