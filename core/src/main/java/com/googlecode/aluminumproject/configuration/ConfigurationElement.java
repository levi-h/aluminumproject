/*
 * Copyright 2009-2012 Levi Hoogenberg
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
 * A part of a {@link Configuration configuration}.
 * <p>
 * The methods of configuration elements are not meant to be called by objects other than the configurations that
 * contain them.
 */
public interface ConfigurationElement {
	/**
	 * Initialises this configuration element.
	 *
	 * @param configuration the configuration that this configuration element is part of
	 * @throws AluminumException when this configuration element can't be initialised
	 */
	void initialise(Configuration configuration) throws AluminumException;

	/**
	 * Disables this configuration element.
	 *
	 * @throws AluminumException when this configuration element can't be disabled
	 */
	void disable() throws AluminumException;
}