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
package com.googlecode.aluminumproject.parsers;

import com.googlecode.aluminumproject.configuration.ConfigurationElement;
import com.googlecode.aluminumproject.templates.Template;

/**
 * Parses a {@link Template template}. This interface does not prescribe where the template should come from, the only
 * requirement is that each template has a unique name.
 *
 * @author levi_h
 */
public interface Parser extends ConfigurationElement {
	/**
	 * Parses a template.
	 *
	 * @param name the name of the template
	 * @return the parsed template
	 * @throws ParseException when no template with the given name can be found or when the template can't be parsed
	 */
	Template parseTemplate(String name) throws ParseException;
}