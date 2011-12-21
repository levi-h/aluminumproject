/*
 * Copyright 2011 Levi Hoogenberg
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

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.configuration.ConfigurationElement;
import com.googlecode.aluminumproject.parsers.Parser;

import java.io.InputStream;

/**
 * Used by a {@link Parser parser} to find a template by name.
 *
 * @author levi_h
 */
public interface TemplateFinder extends ConfigurationElement {
	/**
	 * Finds an input stream for a template with a certain name.
	 *
	 * @param name the name of the template to find an input stream for
	 * @return an input stream where the template can be read from
	 * @throws AluminumException when no input stream can be found for the template
	 */
	InputStream find(String name) throws AluminumException;
}