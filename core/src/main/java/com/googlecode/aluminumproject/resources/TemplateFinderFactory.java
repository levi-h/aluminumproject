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
import com.googlecode.aluminumproject.parsers.Parser;
import com.googlecode.aluminumproject.utilities.resources.ResourceFinder;

/**
 * Creates {@link ResourceFinder resource finders} that are used by {@link Parser parsers} to find a template by name.
 *
 * @author levi_h
 */
public interface TemplateFinderFactory extends ConfigurationElement {
	/**
	 * Creates a template finder.
	 *
	 * @return the new template finder
	 * @throws ResourceException when the template finder can't be created
	 */
	ResourceFinder createTemplateFinder() throws ResourceException;
}