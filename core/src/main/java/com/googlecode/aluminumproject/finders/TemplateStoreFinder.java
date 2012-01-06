/*
 * Copyright 2011-2012 Levi Hoogenberg
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
package com.googlecode.aluminumproject.finders;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.configuration.ConfigurationElement;
import com.googlecode.aluminumproject.serialisers.Serialiser;

import java.io.OutputStream;

/**
 * Used by a {@link Serialiser serialiser} to find a location to store a template.
 *
 * @author levi_h
 */
public interface TemplateStoreFinder extends ConfigurationElement {
	/**
	 * Finds an output stream for a template with a certain name.
	 *
	 * @param name the name of the template to find an output stream for
	 * @return an output stream where the template can be written to
	 * @throws AluminumException when no output stream can be found for the template
	 */
	OutputStream find(String name) throws AluminumException;
}