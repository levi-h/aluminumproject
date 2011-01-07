/*
 * Copyright 2010 Levi Hoogenberg
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
package com.googlecode.aluminumproject.context;

import com.googlecode.aluminumproject.configuration.ConfigurationElement;

/**
 * Enriches the context when a template is being processed.
 * <p>
 * An example of where a context enricher might be useful is when a library would like to ensure that the context
 * contains a certain implicit object, so that all of its actions and functions can use it.
 *
 * @author levi_h
 */
public interface ContextEnricher extends ConfigurationElement {
	/**
	 * Invoked before a template is being processed.
	 *
	 * @param context the context in which the template will be processed
	 * @throws ContextException when something goes wrong while working with the context
	 */
	void beforeTemplate(Context context) throws ContextException;

	/**
	 * Invoked after a template is being processed.
	 *
	 * @param context the context in which the template was processed
	 * @throws ContextException when something goes wrong while working with the context
	 */
	void afterTemplate(Context context) throws ContextException;
}