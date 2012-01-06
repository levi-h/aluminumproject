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

import com.googlecode.aluminumproject.Aluminum;
import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.cache.Cache;
import com.googlecode.aluminumproject.context.ContextEnricher;
import com.googlecode.aluminumproject.converters.ConverterRegistry;
import com.googlecode.aluminumproject.expressions.ExpressionFactory;
import com.googlecode.aluminumproject.finders.TemplateFinder;
import com.googlecode.aluminumproject.finders.TemplateStoreFinder;
import com.googlecode.aluminumproject.libraries.Library;
import com.googlecode.aluminumproject.parsers.Parser;
import com.googlecode.aluminumproject.serialisers.Serialiser;
import com.googlecode.aluminumproject.templates.TemplateElementFactory;

import java.util.List;
import java.util.Map;

/**
 * Contains all dynamic parts of the {@link Aluminum template engine}.
 * <p>
 * A configuration consists of {@link ConfigurationElement configuration elements}. Upon creation, a configuration is
 * required to {@link ConfigurationElement#initialise(Configuration) initialise} the elements it consists of. Similarly,
 * when a configuration is {@link #close() closed}, it should {@link ConfigurationElement#disable() disable} its
 * elements.
 * <p>
 * Trying to use a configuration or its configuration elements after it has been closed could result in strange
 * behaviour.
 *
 * @author levi_h
 */
public interface Configuration {
	/**
	 * Returns the parameters that this configuration was created with.
	 *
	 * @return this configuration's parameters
	 * @throws AluminumException when this configuration has been closed
	 */
	ConfigurationParameters getParameters() throws AluminumException;

	/**
	 * Returns the configuration element factory.
	 *
	 * @return the configuration element factory to use
	 * @throws AluminumException when this configuration has been closed
	 */
	ConfigurationElementFactory getConfigurationElementFactory() throws AluminumException;

	/**
	 * Returns the converter registry.
	 *
	 * @return the converter registry to use
	 * @throws AluminumException when this configuration has been closed
	 */
	ConverterRegistry getConverterRegistry() throws AluminumException;

	/**
	 * Returns all libraries.
	 *
	 * @return a list with all configured libraries
	 * @throws AluminumException when this configuration has been closed
	 */
	List<Library> getLibraries() throws AluminumException;

	/**
	 * Returns the template element factory.
	 *
	 * @return the template element factory to use
	 * @throws AluminumException when this configuration has been closed
	 */
	TemplateElementFactory getTemplateElementFactory() throws AluminumException;

	/**
	 * Returns the template finder.
	 *
	 * @return the template finder to use
	 * @throws AluminumException when this configuration has been closed
	 */
	TemplateFinder getTemplateFinder() throws AluminumException;

	/**
	 * Returns all parsers that can be used to produce a template.
	 *
	 * @return a map with all of the configured parsers, keyed by their names
	 * @throws AluminumException when this configuration has been closed
	 */
	Map<String, Parser> getParsers() throws AluminumException;

	/**
	 * Returns the template store finder.
	 *
	 * @return the template store finder to use
	 * @throws AluminumException when this configuration has been closed
	 */
	TemplateStoreFinder getTemplateStoreFinder() throws AluminumException;

	/**
	 * Returns all serialisers that can be used to serialise a template.
	 *
	 * @return a map with all of the configured serialisers, with their names as keys
	 * @throws AluminumException when this configuration has been closed
	 */
	Map<String, Serialiser> getSerialisers() throws AluminumException;

 	/**
	 * Returns all context enrichers.
	 *
	 * @return a list with all of the configured context enrichers
	 * @throws AluminumException when this configuration has been closed
	 */
	List<ContextEnricher> getContextEnrichers() throws AluminumException;

	/**
	 * Returns all expression factories that will be used to recognise and create expressions.
	 *
	 * @return a list with all of the expression factories that can be used
	 * @throws AluminumException when this configuration has been closed
	 */
	List<ExpressionFactory> getExpressionFactories() throws AluminumException;

	/**
	 * Returns the cache. A template cache is optional.
	 *
	 * @return the cache to use
	 * @throws AluminumException when this configuration has been closed
	 */
	Cache getCache() throws AluminumException;

	/**
	 * Closes this configuration.
	 *
	 * @throws AluminumException when something goes wrong while closing this configuration or when it already has been
	 *                           closed
	 */
	void close() throws AluminumException;
}