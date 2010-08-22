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
package com.googlecode.aluminumproject.resources;

import com.googlecode.aluminumproject.configuration.ConfigurationElement;
import com.googlecode.aluminumproject.serialisers.Serialiser;
import com.googlecode.aluminumproject.utilities.resources.ResourceStoreFinder;

/**
 * Creates {@link ResourceStoreFinder resource store finders} that are used by {@link Serialiser serialisers} to find a
 * location to store a template.
 *
 * @author levi_h
 */
public interface TemplateStoreFinderFactory extends ConfigurationElement {
	/**
	 * Creates a template store finder.
	 *
	 * @return the new template store finder
	 * @throws ResourceException when the template store finder can't be created
	 */
	ResourceStoreFinder createTemplateStoreFinder() throws ResourceException;
}