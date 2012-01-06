/*
 * Copyright 2009-2012 Levi Hoogenberg
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

import static com.googlecode.aluminumproject.configuration.DefaultConfiguration.CONFIGURATION_ELEMENT_PACKAGES;
import static com.googlecode.aluminumproject.utilities.ReflectionUtilities.isAbstract;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.annotations.Ignored;
import com.googlecode.aluminumproject.finders.TypeFinder.TypeFilter;
import com.googlecode.aluminumproject.utilities.Logger;
import com.googlecode.aluminumproject.utilities.ReflectionUtilities;
import com.googlecode.aluminumproject.utilities.Utilities;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * The default {@link ConfigurationElementFactory configuration element factory} implementation.
 * <p>
 * To allow for easy customisation of new objects, the default configuration element factory contains a list of {@link
 * ConfigurationElementCustomiser configuration element customisers}. This list is empty by default, but when either the
 * {@value #CONFIGURATION_ELEMENT_CUSTOMISER_PACKAGES} parameter or the {@value
 * com.googlecode.aluminumproject.configuration.DefaultConfiguration#CONFIGURATION_ELEMENT_PACKAGES} parameter is
 * supplied, all customisers in the given packages that are not annotated with {@link Ignored &#64;Ignored} will be
 * applied to a new object after it's been created.
 *
 * @author levi_h
 */
public class DefaultConfigurationElementFactory implements ConfigurationElementFactory {
	private List<ConfigurationElementCustomiser> customisers;

	private final Logger logger;

	/**
	 * Creates a default configuration element factory.
	 */
	public DefaultConfigurationElementFactory() {
		customisers = new LinkedList<ConfigurationElementCustomiser>();

		logger = Logger.get(getClass());
	}

	public void initialise(Configuration configuration) throws AluminumException {
		Set<String> customiserPackageSet = new HashSet<String>();

		ConfigurationParameters parameters = configuration.getParameters();
		Collections.addAll(customiserPackageSet, parameters.getValues(CONFIGURATION_ELEMENT_CUSTOMISER_PACKAGES));
		Collections.addAll(customiserPackageSet, parameters.getValues(CONFIGURATION_ELEMENT_PACKAGES));

		if (customiserPackageSet.isEmpty()) {
			logger.debug("no configuration element customisers configured");
		} else {
			logger.debug("looking for configuration element customisers in ", customiserPackageSet);

			TypeFilter customiserClassFilter = new TypeFilter() {
				public boolean accepts(Class<?> type) {
					return ConfigurationElementCustomiser.class.isAssignableFrom(type) && !isAbstract(type)
						&& !type.isAnnotationPresent(Ignored.class);
				}
			};
			String[] customiserPackages = customiserPackageSet.toArray(new String[customiserPackageSet.size()]);

			List<Class<? extends ConfigurationElementCustomiser>> customiserClasses =
				Utilities.typed(configuration.getTypeFinder().find(customiserClassFilter, customiserPackages));

			for (Class<? extends ConfigurationElementCustomiser> customiserClass: customiserClasses) {
				logger.debug("adding configuration element customiser of type ", customiserClass.getName());

				customisers.add(ReflectionUtilities.instantiate(customiserClass));
			}
		}

		for (ConfigurationElementCustomiser customiser: customisers) {
			customiser.initialise(configuration);
		}
	}

	public void disable() {
		customisers.clear();
	}

	/**
	 * Returns all configured configuration element customisers.
	 *
	 * @return a list with all configuration element customisers.
	 */
	protected List<ConfigurationElementCustomiser> getConfigurationElementCustomisers() {
		return Collections.unmodifiableList(customisers);
	}

	public <T> T instantiate(String className, Class<T> type) throws AluminumException {
		logger.debug("instantiating ", className, " with type ", type.getName(), " from configuration element factory");

		T object = ReflectionUtilities.instantiate(className, type, Thread.currentThread().getContextClassLoader());

		for (ConfigurationElementCustomiser customiser: customisers) {
			logger.debug("applying configuration element customiser ", customiser);

			customiser.customise(object);
		}

		return object;
	}

	/**
	 * The name of the configuration parameter that contains a comma-separated list of packages in which will be looked
	 * for configuration element customisers.
	 */
	public final static String CONFIGURATION_ELEMENT_CUSTOMISER_PACKAGES =
		"configuration_element_factory.default.configuration_element_factory.packages";
}