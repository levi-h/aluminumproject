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
 * Customises a {@link ConfigurationElement configuration element} after it has been created. Used by the {@link
 * DefaultConfigurationElementFactory default configuration element factory}.
 */
public interface ConfigurationElementCustomiser extends ConfigurationElement {
	/**
	 * Customises the new object.
	 *
	 * @param object the object to customise
	 * @throws AluminumException when something goes wrong when customising the object
	 */
	void customise(Object object) throws AluminumException;
}