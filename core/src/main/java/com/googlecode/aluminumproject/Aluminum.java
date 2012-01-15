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
package com.googlecode.aluminumproject;

import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.templates.TemplateProcessor;
import com.googlecode.aluminumproject.writers.Writer;

/**
 * The template engine.
 * <p>
 * A template engine should be created once with a certain {@link Configuration configuration}. After that, the {@link
 * #processTemplate(String, String, Context, Writer) processTemplate method} can be used repeatedly. A template engine
 * that will no longer be used should be {@link #stop() stopped}.
 */
public class Aluminum {
	private Configuration configuration;

	private TemplateProcessor templateProcessor;

	/**
	 * Creates a template engine.
	 *
	 * @param configuration the configuration to use
	 */
	public Aluminum(Configuration configuration) {
		this.configuration = configuration;

		templateProcessor = new TemplateProcessor(configuration);
	}

	/**
	 * Processes a template.
	 *
	 * @param name the name of the template to process
	 * @param parser the name of the parser to use
	 * @param context the context to use
	 * @param writer the writer to use (it will be closed after the template has been processed)
	 * @throws AluminumException when the template can't be processed
	 */
	public void processTemplate(
			String name, String parser, Context context, Writer writer) throws AluminumException {
		try {
			try {
				templateProcessor.processTemplate(name, parser, context, writer);
			} catch (RuntimeException exception) {
				writer.clear();

				throw exception;
			} finally {
				writer.close();
			}
		} catch (AluminumException exception) {
			throw exception;
		} catch (RuntimeException exception) {
			throw new AluminumException(exception, "can't process template");
		}
	}

	/**
	 * Stops this template engine.
	 *
	 * @throws AluminumException when this template engine can't be stopped
	 */
	public void stop() throws AluminumException {
		configuration.close();
	}
}