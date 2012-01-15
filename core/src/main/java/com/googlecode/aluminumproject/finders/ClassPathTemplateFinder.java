/*
 * Copyright 2011-2012 Aluminum project
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
import com.googlecode.aluminumproject.configuration.Configuration;

import java.io.InputStream;

/**
 * Looks for templates in the class path.
 * <p>
 * By default, templates will be looked for in the root of the class path, but this template path can be configured
 * using the {@value #TEMPLATE_PATH} parameter. A template path is separated by slashes, but does not start with a
 * slash.
 */
public class ClassPathTemplateFinder extends AbstractTemplateFinder {
	private String templatePath;

	/**
	 * Creates a class path template finder.
	 */
	public ClassPathTemplateFinder() {}

	@Override
	public void initialise(Configuration configuration) throws AluminumException {
		super.initialise(configuration);

		templatePath = configuration.getParameters().getValue(TEMPLATE_PATH, null);

		if (templatePath != null) {
			logger.debug("using template path '", templatePath, "'");
		}
	}

	public InputStream find(String name) throws AluminumException {
		String fullName = (templatePath == null) ? name : String.format("%s/%s", templatePath, name);

		logger.debug("trying to find template '", fullName, "'");

		InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fullName);

		if (stream == null) {
			throw new AluminumException("can't find template '", fullName, "'");
		}

		logger.debug("found template '", fullName, "' in class path");

		return stream;
	}

	/** The name of the configuration parameter that contains the path in which templates will be looked for. */
	public final static String TEMPLATE_PATH = "template_finder.class_path.template_path";
}