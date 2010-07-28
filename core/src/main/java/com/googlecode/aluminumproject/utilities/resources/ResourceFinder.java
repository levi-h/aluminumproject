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
package com.googlecode.aluminumproject.utilities.resources;

import com.googlecode.aluminumproject.utilities.UtilityException;

import java.net.URL;

/**
 * Finds resources based on their names. How resources are found varies between implementations.
 *
 * @author levi_h
 */
public interface ResourceFinder {
	/**
	 * Finds the URL of a resource with a certain name.
	 *
	 * @param name the name of the resource to find
	 * @return a URL that leads to the resource
	 * @throws UtilityException when the resource can't be found
	 */
	URL find(String name) throws UtilityException;
}