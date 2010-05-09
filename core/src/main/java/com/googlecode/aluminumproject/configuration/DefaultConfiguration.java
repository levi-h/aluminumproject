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

import static com.googlecode.aluminumproject.utilities.ReflectionUtilities.getPackageName;
import static com.googlecode.aluminumproject.utilities.ReflectionUtilities.instantiate;
import static com.googlecode.aluminumproject.utilities.ReflectionUtilities.isAbstract;

import com.googlecode.aluminumproject.Logger;
import com.googlecode.aluminumproject.annotations.Ignored;
import com.googlecode.aluminumproject.annotations.Named;
import com.googlecode.aluminumproject.cache.Cache;
import com.googlecode.aluminumproject.converters.ConverterRegistry;
import com.googlecode.aluminumproject.converters.DefaultConverterRegistry;
import com.googlecode.aluminumproject.expressions.ExpressionFactory;
import com.googlecode.aluminumproject.libraries.Library;
import com.googlecode.aluminumproject.parsers.Parser;
import com.googlecode.aluminumproject.resources.ClassPathTemplateFinderFactory;
import com.googlecode.aluminumproject.resources.MemoryTemplateStoreFinderFactory;
import com.googlecode.aluminumproject.resources.TemplateFinderFactory;
import com.googlecode.aluminumproject.resources.TemplateStoreFinderFactory;
import com.googlecode.aluminumproject.serialisers.Serialiser;
import com.googlecode.aluminumproject.templates.DefaultTemplateElementFactory;
import com.googlecode.aluminumproject.templates.TemplateElementFactory;
import com.googlecode.aluminumproject.utilities.Utilities;
import com.googlecode.aluminumproject.utilities.UtilityException;
import com.googlecode.aluminumproject.utilities.finders.TypeFinder;
import com.googlecode.aluminumproject.utilities.finders.TypeFinder.TypeFilter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A default configuration. It is created with a number of initialisation parameters, which determines which {@link
 * ConfigurationElement configuration elements} will be used.
 * <p>
 * The configuration elements will be created using the {@link DefaultConfigurationElementFactory default configuration
 * element factory}, although a different implementation can be used by providing a value for the {@value
 * #CONFIGURATION_ELEMENT_FACTORY_CLASS} parameter.
 * <p>
 * For the converter registry, the configuration will look for a parameter with the name {@value
 * #CONVERTER_REGISTRY_CLASS}. If the parameter exists, its value will be interpreted as the class name of the converter
 * registry. If the parameter is absent, the converter registry will default to a {@link DefaultConverterRegistry
 * default converter registry}.
 * <p>
 * The template element factory is configured in the same way - if a configuration parameter with {@value
 * #TEMPLATE_ELEMENT_FACTORY_CLASS} as name is present, its value will be used as template element factory class name.
 * If the parameter is not given, a {@link DefaultTemplateElementFactory default template element factory} will be used.
 * <p>
 * The default template finder factory and template store finder factory are a {@link ClassPathTemplateFinderFactory
 * class path template finder factory} and an {@link MemoryTemplateStoreFinderFactory in-memory template store finder
 * factory}, respectively; other implementations can be configured through the {@value #TEMPLATE_FINDER_FACTORY_CLASS}
 * and {@value #TEMPLATE_STORE_FINDER_FACTORY_CLASS} parameters.
 * <p>
 * For the cache, the configuration will look for a parameter with the name {@value #CACHE_CLASS}. If the parameter
 * exists, its value will be interpreted as the class name of the cache. If the parameter does not exist, no cache will
 * be used.
 * <p>
 * To find libraries, the configuration scans a number of packages. By default, a single package is used: {@code
 * com.googlecode.aluminumproject.libraries}. This list can be changed by supplying a configuration parameter named
 * {@value #LIBRARY_PACKAGES}. This configuration parameter should contain a comma-separated list of packages that
 * should be included when searching libraries.
 * <p>
 * Parsers and serialisers are found in a list of packages as well. The default packages that are being looked in are
 * {@code com.googlecode.aluminumproject.parsers} and {@code com.googlecode.aluminumproject.serialisers}, though
 * different sets of packages can be configured by using the {@value #PARSER_PACKAGES} and {@value #SERIALISER_PACKAGES}
 * parameters. The values should be a list of packages, separated by a comma, that should be scanned for parsers or
 * serialisers.
 * <p>
 * When a parser or serialiser is found, it is named after the last part of the package it's in: this means that a
 * parser named {@code com.googlecode.aluminumproject.parsers.simple.SimpleParser} or a serialiser named {@code
 * com.googlecode.aluminumproject.serialisers.simple.SimpleSerialiser} will be registered under the name {@code simple}.
 * This behaviour can be changed by annotating a parser or serialiser with {@link Named &#64;Named}.
 * <p>
 * Expression factories are found by looking into a list of packages. This list is taken from the configuration
 * parameter {@value #EXPRESSION_FACTORY_PACKAGES} (its value is expected to be a comma-separated package list). If the
 * configuration parameter is not specified, the package {@code com.googlecode.aluminumproject.expressions} will be
 * scanned.
 * <p>
 * Libraries, parsers, and expression factories are excluded from the configuration when they are annotated with {@link
 * Ignored &#64;Ignored}.
 *
 * @author levi_h
 */
public class DefaultConfiguration implements Configuration {
	private ConfigurationParameters parameters;

	private ConfigurationElementFactory configurationElementFactory;
	private ConverterRegistry converterRegistry;
	private TemplateElementFactory templateElementFactory;
	private TemplateFinderFactory templateFinderFactory;
	private TemplateStoreFinderFactory templateStoreFinderFactory;
	private Cache cache;
	private List<Library> libraries;
	private Map<String, Parser> parsers;
	private Map<String, Serialiser> serialisers;
	private List<ExpressionFactory> expressionFactories;

	private final Logger logger;

	/**
	 * Creates a default configuration without any parameters.
	 *
	 * @throws ConfigurationException when the configuration can't be created
	 */
	public DefaultConfiguration() throws ConfigurationException {
		this(new ConfigurationParameters());
	}

	/**
	 * Creates a default configuration.
	 *
	 * @param parameters the configuration parameters to use
	 * @throws ConfigurationException when the configuration can't be created
	 */
	public DefaultConfiguration(ConfigurationParameters parameters) throws ConfigurationException {
		this.parameters = parameters;

		logger = Logger.get(getClass());

		createConfigurationElementFactory();
		configurationElementFactory.initialise(this, parameters);

		createConverterRegistry();
		createTemplateElementFactory();
		createTemplateFinderFactory();
		createTemplateStoreFinderFactory();
		createCache();
		createLibraries();
		createParsers();
		createSerialisers();
		createExpressionFactories();

		initialise();
	}

	private void createConfigurationElementFactory() {
		String configurationElementFactoryClassName = parameters.getValue(
			CONFIGURATION_ELEMENT_FACTORY_CLASS, DefaultConfigurationElementFactory.class.getName());

		logger.debug("creating configuration element factory of type ", configurationElementFactoryClassName);

		try {
			configurationElementFactory =
				instantiate(configurationElementFactoryClassName, ConfigurationElementFactory.class);
		} catch (UtilityException exception) {
			throw new ConfigurationException(exception, "can't create configuration element factory");
		}
	}

	private void createConverterRegistry() {
		String converterRegistryClassName =
			parameters.getValue(CONVERTER_REGISTRY_CLASS, DefaultConverterRegistry.class.getName());

		logger.debug("creating converter registry of type ", converterRegistryClassName);

		converterRegistry =
			configurationElementFactory.instantiate(converterRegistryClassName, ConverterRegistry.class);
	}

	private void createTemplateElementFactory() {
		String templateElementFactoryClassName =
			parameters.getValue(TEMPLATE_ELEMENT_FACTORY_CLASS, DefaultTemplateElementFactory.class.getName());

		logger.debug("creating template element factory of type ", templateElementFactoryClassName);

		templateElementFactory =
			configurationElementFactory.instantiate(templateElementFactoryClassName, TemplateElementFactory.class);
	}

	private void createTemplateFinderFactory() {
		String templateFinderFactoryClassName =
			parameters.getValue(TEMPLATE_FINDER_FACTORY_CLASS, ClassPathTemplateFinderFactory.class.getName());

		logger.debug("creating template finder factory of type ", templateFinderFactoryClassName);

		templateFinderFactory =
			configurationElementFactory.instantiate(templateFinderFactoryClassName, TemplateFinderFactory.class);
	}

	private void createTemplateStoreFinderFactory() {
		String templateStoreFinderFactoryClassName =
			parameters.getValue(TEMPLATE_STORE_FINDER_FACTORY_CLASS, MemoryTemplateStoreFinderFactory.class.getName());

		logger.debug("creating template store finder factory of type ", templateStoreFinderFactoryClassName);

		templateStoreFinderFactory = configurationElementFactory.instantiate(
			templateStoreFinderFactoryClassName, TemplateStoreFinderFactory.class);
	}

	private void createCache() {
		String cacheClassName = parameters.getValue(CACHE_CLASS, null);

		if (cacheClassName == null) {
			logger.debug("not using a cache");
		} else {
			logger.debug("creating cache of type ", cacheClassName);

			cache = configurationElementFactory.instantiate(cacheClassName, Cache.class);
		}
	}

	private void createLibraries() throws ConfigurationException {
		String[] libraryPackages = parameters.getValues(LIBRARY_PACKAGES, getPackageName(Library.class));

		logger.debug("libraries will be looked for in ", libraryPackages);

		List<Class<?>> libraryClasses;

		try {
			libraryClasses = TypeFinder.find(new TypeFilter() {
				public boolean accepts(Class<?> type) {
					return Library.class.isAssignableFrom(type) && !isAbstract(type)
						&& !type.isAnnotationPresent(Ignored.class);
				}
			}, libraryPackages);
		} catch (UtilityException exception) {
			throw new ConfigurationException(exception, "can't find libraries");
		}

		libraries = new ArrayList<Library>();

		for (Class<?> libraryClass: libraryClasses) {
			logger.debug("adding library of type ", libraryClass.getName());

			libraries.add(configurationElementFactory.instantiate(libraryClass.getName(), Library.class));
		}
	}

	private void createParsers() throws ConfigurationException {
		String[] parserPackages = parameters.getValues(PARSER_PACKAGES, getPackageName(Parser.class));

		logger.debug("parsers will be looked for in ", parserPackages);

		List<Class<?>> parserClasses;

		try {
			parserClasses = TypeFinder.find(new TypeFilter() {
				public boolean accepts(Class<?> type) {
					return Parser.class.isAssignableFrom(type) && !isAbstract(type)
						&& !type.isAnnotationPresent(Ignored.class);
				}
			}, parserPackages);
		} catch (UtilityException exception) {
			throw new ConfigurationException(exception, "can't find parsers");
		}

		parsers = new HashMap<String, Parser>();

		for (Class<?> parserClass: parserClasses) {
			String name = getNameBasedOnAnnotationOrPackageName(parserClass);

			if (parsers.containsKey(name)) {
				throw new ConfigurationException("duplicate parser name: '", name, "' ",
					"(types: ", parsers.get(name).getClass().getName(), " and ", parserClass.getName(), ")");
			} else {
				logger.debug("registering parser of type ", parserClass.getName(), " under name '", name, "'");

				parsers.put(name, configurationElementFactory.instantiate(parserClass.getName(), Parser.class));
			}
		}
	}

	private void createSerialisers() throws ConfigurationException {
		String[] serialiserPackages = parameters.getValues(SERIALISER_PACKAGES, getPackageName(Serialiser.class));

		logger.debug("serialisers will be looked for in ", serialiserPackages);

		List<Class<?>> serialiserClasses;

		try {
			serialiserClasses = TypeFinder.find(new TypeFilter() {
				public boolean accepts(Class<?> type) {
					return Serialiser.class.isAssignableFrom(type) && !isAbstract(type)
						&& !type.isAnnotationPresent(Ignored.class);
				}
			}, serialiserPackages);
		} catch (UtilityException exception) {
			throw new ConfigurationException(exception, "can't find serialisers");
		}

		serialisers = new HashMap<String, Serialiser>();

		for (Class<?> serialiserClass: serialiserClasses) {
			String name = getNameBasedOnAnnotationOrPackageName(serialiserClass);

			if (serialisers.containsKey(name)) {
				throw new ConfigurationException("duplicate serialiser name: '", name, "' ",
					"(types: ", serialisers.get(name).getClass().getName(), " and ", serialiserClass.getName(), ")");
			} else {
				logger.debug("registering serialiser of type ", serialiserClass.getName(), " under name '", name, "'");

				serialisers.put(name,
					configurationElementFactory.instantiate(serialiserClass.getName(), Serialiser.class));
			}
		}
	}

	private String getNameBasedOnAnnotationOrPackageName(Class<?> type) {
		String name;

		if (type.isAnnotationPresent(Named.class)) {
			name = type.getAnnotation(Named.class).value();
		} else {
			String packageName = getPackageName(type);

			name = packageName.substring(packageName.lastIndexOf('.') + 1);
		}

		return name;
	}

	private void createExpressionFactories() throws ConfigurationException {
		String[] expressionFactoryPackages =
			parameters.getValues(EXPRESSION_FACTORY_PACKAGES, getPackageName(ExpressionFactory.class));

		List<Class<?>> expressionFactoryClasses;

		try {
			expressionFactoryClasses = Utilities.typed(TypeFinder.find(new TypeFilter() {
				public boolean accepts(Class<?> type) {
					return ExpressionFactory.class.isAssignableFrom(type) && !isAbstract(type)
						&& !type.isAnnotationPresent(Ignored.class);
				}
			}, expressionFactoryPackages));
		} catch (UtilityException exception) {
			throw new ConfigurationException(exception, "can't find expression factories");
		}

		expressionFactories = new ArrayList<ExpressionFactory>();

		for (Class<?> expressionFactoryClass: expressionFactoryClasses) {
			logger.debug("adding expression factory of type ", expressionFactoryClass.getName());

			expressionFactories.add(
				configurationElementFactory.instantiate(expressionFactoryClass.getName(), ExpressionFactory.class));
		}
	}

	private void initialise() throws ConfigurationException {
		converterRegistry.initialise(this, parameters);
		templateElementFactory.initialise(this, parameters);
		templateFinderFactory.initialise(this, parameters);
		templateStoreFinderFactory.initialise(this, parameters);

		if (cache != null) {
			cache.initialise(this, parameters);
		}

		for (Library library: libraries) {
			library.initialise(this, parameters);
		}

		for (Parser parser: parsers.values()) {
			parser.initialise(this, parameters);
		}

		for (Serialiser serialiser: serialisers.values()) {
			serialiser.initialise(this, parameters);
		}

		for (ExpressionFactory expressionFactory: expressionFactories) {
			expressionFactory.initialise(this, parameters);
		}
	}

	public ConfigurationElementFactory getConfigurationElementFactory() {
		return configurationElementFactory;
	}

	public ConverterRegistry getConverterRegistry() {
		return converterRegistry;
	}

	public TemplateElementFactory getTemplateElementFactory() {
		return templateElementFactory;
	}

	public TemplateFinderFactory getTemplateFinderFactory() {
		return templateFinderFactory;
	}

	public TemplateStoreFinderFactory getTemplateStoreFinderFactory() {
		return templateStoreFinderFactory;
	}

	public Cache getCache() {
		return cache;
	}

	public List<Library> getLibraries() {
		return Collections.unmodifiableList(libraries);
	}

	public Map<String, Parser> getParsers() {
		return Collections.unmodifiableMap(parsers);
	}

	public Map<String, Serialiser> getSerialisers() {
		return Collections.unmodifiableMap(serialisers);
	}

	public List<ExpressionFactory> getExpressionFactories() {
		return Collections.unmodifiableList(expressionFactories);
	}

	/**
	 * The name of the configuration parameter that contains the class name of the configuration element factory to use.
	 */
	public final static String CONFIGURATION_ELEMENT_FACTORY_CLASS =
		"configuration.default.configuration_element_factory.class";

	/** The name of the configuration parameter that contains the class name of the converter registry to use. */
	public final static String CONVERTER_REGISTRY_CLASS = "configuration.default.converter_registry.class";

	/** The name of the configuration parameter that contains the class name of the template element factory to use. */
	public final static String TEMPLATE_ELEMENT_FACTORY_CLASS = "configuration.default.template_element_factory.class";

	/** The name of the configuration parameter that contains the class name of the template finder factory to use. */
	public final static String TEMPLATE_FINDER_FACTORY_CLASS = "configuration.default.template_finder_factory.class";

	/**
	 * The name of the configuration parameter that contains the class name of the template store finder factory to use.
	 */
	public final static String TEMPLATE_STORE_FINDER_FACTORY_CLASS =
		"configuration.default.template_store_finder_factory.class";

	/** The name of the configuration parameter that contains the class name of the cache to use. */
	public final static String CACHE_CLASS = "configuration.default.cache.class";

	/**
	 * The name of the configuration parameter that holds the comma-separated list of packages that the configuration
	 * will scan for libraries.
	 */
	public final static String LIBRARY_PACKAGES = "configuration.default.library.packages";

	/**
	 * The name of the configuration parameter that contains a comma-separated list of packages in which should be
	 * looked for parsers.
	 */
	public final static String PARSER_PACKAGES = "configuration.default.parser.packages";

	/**
	 * The name of the configuration parameter that holds a comma-separated list of packages that may contain
	 * serialisers.
	 */
	public final static String SERIALISER_PACKAGES = "configuration.default.serialiser.packages";

	/**
	 * The name of the configuration parameter that has a comma-separated list of expression factory packages as its
	 * value.
	 */
	public final static String EXPRESSION_FACTORY_PACKAGES = "configuration.default.expression_factory.packages";
}