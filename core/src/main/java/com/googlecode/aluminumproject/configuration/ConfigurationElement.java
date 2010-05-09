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

/**
 * A part of a {@link Configuration}.
 *
 * @author levi_h
 */
public interface ConfigurationElement {
	/**
	 * Initialises this configuration element. This method should be invoked by a {@link Configuration configuration}.
	 *
	 * @param configuration the configuration that this configuration element is part of
	 * @param parameters initialisation parameters for this configuration element
	 * @throws ConfigurationException when this configuration element can't be initialised
	 */
	void initialise(Configuration configuration, ConfigurationParameters parameters) throws ConfigurationException;
}