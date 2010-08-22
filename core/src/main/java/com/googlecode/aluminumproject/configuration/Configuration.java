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

import com.googlecode.aluminumproject.Aluminum;
import com.googlecode.aluminumproject.cache.Cache;
import com.googlecode.aluminumproject.context.ContextEnricher;
import com.googlecode.aluminumproject.converters.ConverterRegistry;
import com.googlecode.aluminumproject.expressions.ExpressionFactory;
import com.googlecode.aluminumproject.libraries.Library;
import com.googlecode.aluminumproject.parsers.Parser;
import com.googlecode.aluminumproject.resources.TemplateFinderFactory;
import com.googlecode.aluminumproject.resources.TemplateStoreFinderFactory;
import com.googlecode.aluminumproject.serialisers.Serialiser;
import com.googlecode.aluminumproject.templates.TemplateElementFactory;

import java.util.List;
import java.util.Map;

/**
 * Contains all dynamic parts of the {@link Aluminum template engine}.
 * <p>
 * A configuration consists of {@link ConfigurationElement configuration elements}. Upon creation, a configuration is
 * required to {@link ConfigurationElement#initialise(Configuration, ConfigurationParameters) initialise} the elements
 * it consists of.
 *
 * @author levi_h
 */
public interface Configuration {
	/**
	 * Returns the configuration element factory.
	 *
	 * @return the configuration element factory to use
	 */
	ConfigurationElementFactory getConfigurationElementFactory();

	/**
	 * Returns the converter registry.
	 *
	 * @return the converter registry to use
	 */
	ConverterRegistry getConverterRegistry();

	/**
	 * Returns all libraries.
	 *
	 * @return a list with all configured libraries
	 */
	List<Library> getLibraries();

	/**
	 * Returns the template element factory.
	 *
	 * @return the template element factory to use
	 */
	TemplateElementFactory getTemplateElementFactory();

	/**
	 * Returns the template finder factory.
	 *
	 * @return the template finder factory to use
	 */
	TemplateFinderFactory getTemplateFinderFactory();

	/**
	 * Returns all parsers that can be used to produce a template.
	 *
	 * @return a map with all of the configured parsers, keyed by their names
	 */
	Map<String, Parser> getParsers();

	/**
	 * Returns the template store finder factory.
	 *
	 * @return the template store finder factory to use
	 */
	TemplateStoreFinderFactory getTemplateStoreFinderFactory();

	/**
	 * Returns all serialisers that can be used to serialise a template.
	 *
	 * @return a map with all of the configured serialisers, with their names as keys
	 */
	Map<String, Serialiser> getSerialisers();

 	/**
	 * Returns all context enrichers.
	 *
	 * @return a list with all of the configured context enrichers
	 */
	List<ContextEnricher> getContextEnrichers();

	/**
	 * Returns all expression factories that will be used to recognise and create expressions.
	 *
	 * @return a list with all of the expression factories that can be used
	 */
	List<ExpressionFactory> getExpressionFactories();

	/**
	 * Returns the cache. A template cache is optional.
	 *
	 * @return the cache to use
	 */
	Cache getCache();
}