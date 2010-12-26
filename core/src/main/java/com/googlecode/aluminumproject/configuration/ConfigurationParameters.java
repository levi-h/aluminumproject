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
package com.googlecode.aluminumproject.configuration;

import com.googlecode.aluminumproject.utilities.Logger;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Initialisation parameters of a {@link Configuration configuration}.
 * <p>
 * Parameters can be {@link #addParameter(String, String) added} and {@link #getValue(String, String) retrieved} by
 * name. Please note that all parameter values are {@link String#trim() trimmed} and can't be empty or {@code null}.
 *
 * @author levi_h
 */
public class ConfigurationParameters {
	private Map<String, String> parameters;

	private final Logger logger;

	/**
	 * Creates empty configuration parameters.
	 */
	public ConfigurationParameters() {
		parameters = new HashMap<String, String>();

		logger = Logger.get(getClass());
	}

	/**
	 * Adds a parameter.
	 *
	 * @param name the name of the parameter
	 * @param value the value to set
	 * @throws ConfigurationException when {@code value} is {@code null} or empty
	 */
	public void addParameter(String name, String value) throws ConfigurationException {
		if (value == null) {
			throw new ConfigurationException("parameter '", name, "' should not be null");
		} else {
			value = value.trim();

			if (value.equals("")) {
				throw new ConfigurationException("parameter '", name, "' should not be empty");
			} else {
				logger.debug("adding configuration parameter '", name, "' with value '", value, "'");

				parameters.put(name, value);
			}
		}
	}

	/**
	 * Finds a parameter value.
	 *
	 * @param name the name of the parameter to retrieve the value of
	 * @param defaultValue the value to return if no configuration parameter with the given name was set
	 * @return the value of the parameter with the given name
	 */
	public String getValue(String name, String defaultValue) {
		return parameters.containsKey(name) ? parameters.get(name) : defaultValue;
	}

	/**
	 * Finds parameter values. Values are found by retrieving a parameter value and splitting it around commas.
	 *
	 * @param name the name of the parameter to retrieve the values of
	 * @param defaultValues the values to return if no configuration parameter with the given name was added
	 * @return the values of the parameter with the given name
	 */
	public String[] getValues(String name, String... defaultValues) {
		String[] values;

		if (parameters.containsKey(name)) {
			values = parameters.get(name).split("\\s*,\\s*");
		} else {
			values = defaultValues;
		}

		return values;
	}

	/**
	 * Finds a map of parameter values. Value maps are found by retrieving a parameter value and splitting it around
	 * commas and colons.
	 *
	 * @param name the name of the parameter to retrieve the value map of
	 * @param defaultValueMap the value map to return if no configuration parameter exists with the given name
	 * @return the value map of the parameter with the given name
	 * @throws ConfigurationException when the parameter value can't be parsed into a map
	 */
	public Map<String, String> getValueMap(
			String name, Map<String, String> defaultValueMap) throws ConfigurationException {
		Map<String, String> valueMap;

		if (parameters.containsKey(name)) {
			valueMap = new LinkedHashMap<String, String>();

			for (String entry: parameters.get(name).split("\\s*,\\s*")) {
				String[] keyAndValue = entry.split("\\s*:\\s*");

				if (keyAndValue.length == 2) {
					String key = keyAndValue[0];
					String value = keyAndValue[1];

					if (valueMap.containsKey(key)) {
						throw new ConfigurationException("duplicate key '", key, "' ",
							"in value map of parameter '", name, "'");
					} else {
						valueMap.put(key, value);
					}
				} else {
					throw new ConfigurationException("illegal entry (", entry, ") ",
						"for value map of parameter '", name, "'");
				}
			}
		} else {
			return defaultValueMap;
		}

		return valueMap;
	}
}