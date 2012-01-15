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

import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.utilities.Logger;

/**
 * Makes it easier to implement the {@link TemplateStoreFinder template store finder interface}.
 */
public abstract class AbstractTemplateStoreFinder implements TemplateStoreFinder {
	/** The logger to use. */
	protected final Logger logger;

	/**
	 * Creates an abstract template store finder.
	 */
	public AbstractTemplateStoreFinder() {
		logger = Logger.get(getClass());
	}

	public void initialise(Configuration configuration) {}

	public void disable() {}
}