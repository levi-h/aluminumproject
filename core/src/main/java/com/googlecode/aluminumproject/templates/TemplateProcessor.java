/*
 * Copyright 2009-2012 Aluminum project
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
package com.googlecode.aluminumproject.templates;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.cache.Cache;
import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.ContextEnricher;
import com.googlecode.aluminumproject.parsers.Parser;
import com.googlecode.aluminumproject.utilities.Logger;
import com.googlecode.aluminumproject.writers.Writer;

import java.util.Map;

/**
 * Processes a single {@link Template template}.
 * <p>
 * All configured {@link ContextEnricher context enrichers} are given the opportunity to change the context before and
 * after the template is processed.
 */
public class TemplateProcessor {
	private Configuration configuration;

	private final Logger logger;

	/**
	 * Creates a template processor.
	 *
	 * @param configuration the configuration to use
	 */
	public TemplateProcessor(Configuration configuration) {
		this.configuration = configuration;

		logger = Logger.get(getClass());
	}

	/**
	 * Processes a template.
	 *
	 * @param name the name of the template to process
	 * @param parser the name of the parser to use
	 * @param context the context to use
	 * @param writer the writer to use
	 * @throws AluminumException when the template can't be processed
	 */
	public void processTemplate(String name, String parser, Context context, Writer writer) throws AluminumException {
		logger.debug("processing template '", name, "'");

		Template template = findTemplateInCache(name, parser);

		if (template == null) {
			template = parseTemplate(name, parser);

			storeTemplateInCache(name, parser, template);
		}

		TemplateInformation.from(context).setTemplate(template, name, parser);

		for (ContextEnricher contextEnricher: configuration.getContextEnrichers()) {
			contextEnricher.beforeTemplate(context);
		}

		for (TemplateElement templateElement: template.getChildren(null)) {
			templateElement.process(context, writer);
		}

		for (ContextEnricher contextEnricher: configuration.getContextEnrichers()) {
			contextEnricher.afterTemplate(context);
		}

		logger.debug("finished processing template '", name, "'");
	}

	private Template findTemplateInCache(String name, String parser) throws AluminumException {
		Template template;

		Cache cache = configuration.getCache();

		if (cache == null) {
			logger.debug("no cache configured, can't find template");

			template = null;
		} else {
			logger.debug("finding template in cache");

			template = cache.findTemplate(new Cache.Key(name, parser));
		}

		return template;
	}

	private void storeTemplateInCache(String name, String parser, Template template) throws AluminumException {
		Cache cache = configuration.getCache();

		if (cache == null) {
			logger.debug("no cache configured, can't store template");
		} else {
			logger.debug("storing template in cache");

			cache.storeTemplate(new Cache.Key(name, parser), template);
		}
	}

	private Template parseTemplate(String name, String parser) throws AluminumException {
		logger.debug("parsing template '", name, "' with parser '", parser, "'");

		Map<String, Parser> parsers = configuration.getParsers();

		if (parsers.containsKey(parser)) {
			return parsers.get(parser).parseTemplate(name);
		} else {
			throw new AluminumException("unknown parser: ", parser);
		}
	}
}