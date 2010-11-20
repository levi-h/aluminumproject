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

import com.googlecode.aluminumproject.Logger;
import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.utilities.resources.ClassPathResourceFinder;
import com.googlecode.aluminumproject.utilities.resources.ResourceFinder;

/**
 * Creates resource finders that look for templates in the class path.
 * <p>
 * By default, templates will be looked for in the root of the class path, but this template path can be configured
 * using the {@value #TEMPLATE_PATH} parameter. A template path is separated by slashes, but does not start with a
 * slash.
 *
 * @author levi_h
 */
public class ClassPathTemplateFinderFactory implements TemplateFinderFactory {
	private String templatePath;

	private final Logger logger;

	/**
	 * Creates a class path template finder factory.
	 */
	public ClassPathTemplateFinderFactory() {
		logger = Logger.get(getClass());
	}

	public void initialise(Configuration configuration) {
		templatePath = configuration.getParameters().getValue(TEMPLATE_PATH, null);

		if (templatePath != null) {
			logger.debug("using template path '", templatePath, "'");
		}
	}

	public ResourceFinder createTemplateFinder() throws ResourceException {
		return new ClassPathResourceFinder(templatePath);
	}

	/** The name of the configuration parameter that contains the path in which templates will be looked for. */
	public final static String TEMPLATE_PATH = "template_finder_factory.class_path.template_path";
}