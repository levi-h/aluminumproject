/*
 * Copyright 2010-2012 Levi Hoogenberg
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
package com.googlecode.aluminumproject.utilities.environment;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.utilities.Logger;
import com.googlecode.aluminumproject.utilities.ReflectionUtilities;

import java.io.IOException;
import java.util.Properties;

/**
 * Provides utilities related to the environment in which Aluminum runs.
 */
public class EnvironmentUtilities {
	private EnvironmentUtilities() {}

	/**
	 * Returns the current version of Aluminum.
	 *
	 * @return the current Aluminum version in the form <em>major.minor</em>
	 */
	public static String getVersion() {
		return version;
	}

	/**
	 * Returns a property set container with property sets that reflect the current environment.
	 *
	 * @return the property set container for the current environment
	 */
	public static PropertySetContainer getPropertySetContainer() {
		return propertySetContainer;
	}

	/**
	 * Returns an environment-specific web client.
	 *
	 * @return the web client for the current environment
	 */
	public static WebClient getWebClient() {
		return webClient;
	}

	private static String version;
	private static PropertySetContainer propertySetContainer;
	private static WebClient webClient;

	static {
		Logger logger = Logger.get(EnvironmentUtilities.class);

		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

		Properties environment = new Properties();

		try {
			logger.debug("loading aluminum.properties");

			environment.load(classLoader.getResourceAsStream("aluminum.properties"));
		} catch (IOException exception) {
			logger.warn(exception, "can't read aluminum.properties");

			throw new ExceptionInInitializerError(exception);
		}

		version = environment.getProperty("version");

		try {
			String propertySetContainerClassName = environment.getProperty("property_set_container");

			propertySetContainer =
				ReflectionUtilities.instantiate(propertySetContainerClassName, PropertySetContainer.class, classLoader);
		} catch (AluminumException exception) {
			logger.warn(exception, "can't create property set container");

			throw new ExceptionInInitializerError(exception);
		}

		try {
			String webClientClassName = environment.getProperty("web_client");

			webClient = ReflectionUtilities.instantiate(webClientClassName, WebClient.class, classLoader);
		} catch (AluminumException exception) {
			logger.warn(exception, "can't create web client");

			throw new ExceptionInInitializerError(exception);
		}
	}
}