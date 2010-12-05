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

import java.io.OutputStream;

/**
 * Finds a place to store a resource.
 *
 * @author levi_h
 */
public interface ResourceStoreFinder {
	/**
	 * Finds an output stream for a resource with a certain name.
	 *
	 * @param name the name of the resource that should be stored
	 * @return an output stream where the resource can be stored into
	 * @throws UtilityException when no place can be found to store the resource
	 */
	OutputStream find(String name) throws UtilityException;
}