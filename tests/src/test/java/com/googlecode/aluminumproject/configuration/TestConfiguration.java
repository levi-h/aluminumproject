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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A configuration that can be used in tests. Its main purpose is to initialise configuration elements that are tested
 * in isolation with.
 *
 * @author levi_h
 */
public class TestConfiguration implements Configuration {
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
	private List<ContextEnricher> contextEnrichers;
	private List<ExpressionFactory> expressionFactories;

	/**
	 * Creates a test configuration.
	 *
	 * @param parameters the parameters to use for the configuration element factory
	 */
	public TestConfiguration(ConfigurationParameters parameters) {
		this.parameters = parameters;

		configurationElementFactory = new DefaultConfigurationElementFactory();
		configurationElementFactory.initialise(this);

		libraries = new ArrayList<Library>();
		parsers = new HashMap<String, Parser>();
		serialisers = new HashMap<String, Serialiser>();
		contextEnrichers = new ArrayList<ContextEnricher>();
		expressionFactories = new ArrayList<ExpressionFactory>();
	}

	public ConfigurationParameters getParameters() {
		return parameters;
	}

	public ConfigurationElementFactory getConfigurationElementFactory() {
		return configurationElementFactory;
	}

	public ConverterRegistry getConverterRegistry() {
		return converterRegistry;
	}

	/**
	 * Sets the converter registry.
	 *
	 * @param converterRegistry the converter registry to use
	 */
	public void setConverterRegistry(ConverterRegistry converterRegistry) {
		this.converterRegistry = converterRegistry;
	}

	public TemplateElementFactory getTemplateElementFactory() {
		return templateElementFactory;
	}

	/**
	 * Sets the template element factory.
	 *
	 * @param templateElementFactory the template element factory to use
	 */
	public void setTemplateElementFactory(TemplateElementFactory templateElementFactory) {
		this.templateElementFactory = templateElementFactory;
	}

	public TemplateFinderFactory getTemplateFinderFactory() {
		return templateFinderFactory;
	}

	/**
	 * Sets the template finder factory.
	 *
	 * @param templateFinderFactory the template finder factory to use
	 */
	public void setTemplateFinderFactory(TemplateFinderFactory templateFinderFactory) {
		this.templateFinderFactory = templateFinderFactory;
	}

	public TemplateStoreFinderFactory getTemplateStoreFinderFactory() {
		return templateStoreFinderFactory;
	}

	/**
	 * Sets the template store finder factory.
	 *
	 * @param templateStoreFinderFactory the template store finder factory to use
	 */
	public void setTemplateStoreFinderFactory(TemplateStoreFinderFactory templateStoreFinderFactory) {
		this.templateStoreFinderFactory = templateStoreFinderFactory;
	}

	public Cache getCache() {
		return cache;
	}

	/**
	 * Sets the cache.
	 *
	 * @param cache the cache to use
	 */
	public void setCache(Cache cache) {
		this.cache = cache;
	}

	public List<Library> getLibraries() {
		return Collections.unmodifiableList(libraries);
	}

	/**
	 * Adds a library to the list of libraries.
	 *
	 * @param library the library to add
	 */
	public void addLibrary(Library library) {
		libraries.add(library);
	}

	public Map<String, Parser> getParsers() {
		return Collections.unmodifiableMap(parsers);
	}

	/**
	 * Adds or replaces a parser.
	 *
	 * @param name the name to register the parser under
	 * @param parser the parser to add
	 */
	public void addParser(String name, Parser parser) {
		parsers.put(name, parser);
	}

	public Map<String, Serialiser> getSerialisers() {
		return Collections.unmodifiableMap(serialisers);
	}

	/**
	 * Adds or replaces a serialiser.
	 *
	 * @param name the name to register the serialiser under
	 * @param serialiser the serialiser to add
	 */
	public void addSerialiser(String name, Serialiser serialiser) {
		serialisers.put(name, serialiser);
	}

	public List<ContextEnricher> getContextEnrichers() {
		return Collections.unmodifiableList(contextEnrichers);
	}

	/**
	 * Adds a context enricher to the list of context enrichers.
	 *
	 * @param contextEnricher the context enricher to add
	 */
	public void addContextEnricher(ContextEnricher contextEnricher) {
		contextEnrichers.add(contextEnricher);
	}

	public List<ExpressionFactory> getExpressionFactories() {
		return Collections.unmodifiableList(expressionFactories);
	}

	/**
	 * Adds an expression factory to the list of expression factories.
	 *
	 * @param expressionFactory the expression factory to add
	 */
	public void addExpressionFactory(ExpressionFactory expressionFactory) {
		expressionFactories.add(expressionFactory);
	}

	public void close() {}
}