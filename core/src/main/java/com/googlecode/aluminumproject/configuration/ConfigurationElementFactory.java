/*
 * Copyright 2009-2011 Levi Hoogenberg
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
 * Creates configuration elements (except for the factory itself). Created configuration elements are encouraged to use
 * the configuration element factory when they need to instantiate classes as well. This allows users to customise new
 * objects from a central location.
 *
 * @author levi_h
 */
public interface ConfigurationElementFactory extends ConfigurationElement {
	/**
	 * Instantiates a class with a certain name.
	 *
	 * @param <T> the type of the new object
	 * @param className the name of the class to instantiate
	 * @param type the expected type of the new instance
	 * @return a new instance of the class with the given name, cast to the specified type
	 * @throws AluminumException when the class with the given name can't be instantiated or when it is not of the
	 *                           required type
	 */
	<T> T instantiate(String className, Class<T> type) throws AluminumException;
}