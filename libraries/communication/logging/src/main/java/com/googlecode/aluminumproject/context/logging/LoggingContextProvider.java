/*
 * Copyright 2013 Aluminum project
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
package com.googlecode.aluminumproject.context.logging;

import static com.googlecode.aluminumproject.context.logging.LoggingContext.LOGGING_CONTEXT;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.ContextEnricher;
import com.googlecode.aluminumproject.utilities.Logger;

/**
 * Provides a logging context to contexts by creating or inheriting one before each template and removing it afterwards.
 * <p>
 * The logging context can be configured using configuration parameters:
 * <ul>
 * <li>{@value #LOGGER_NAME_PREFIX} determines with which text the template name will be prefixed when obtaining a
 *     logger. It default to an empty string, which means the template name will become the logger name.
 * </ul>
 */
public class LoggingContextProvider implements ContextEnricher {
	private String templateNamePrefix;

	private final Logger logger;

	/**
	 * Creates a logging context provider.
	 */
	public LoggingContextProvider() {
		logger = Logger.get(getClass());
	}

	public void initialise(Configuration configuration) throws AluminumException {
		templateNamePrefix = configuration.getParameters().getValue(LOGGER_NAME_PREFIX, "");

		logger.debug("using logger name prefix '", templateNamePrefix, "'");
	}

	public void disable() {}

	public void beforeTemplate(Context context) throws AluminumException {
		Context parentContext = context.getParent();

		if ((parentContext != null) && parentContext.getImplicitObjectNames().contains(LOGGING_CONTEXT)) {
			logger.debug("inheriting logging context from parent");

			context.addImplicitObject(LOGGING_CONTEXT, LoggingContext.from(parentContext));
		} else {
			logger.debug("enriching context with new logging context");

			context.addImplicitObject(LOGGING_CONTEXT, new LoggingContext(templateNamePrefix));
		}
	}

	public void afterTemplate(Context context) throws AluminumException {
		context.removeImplicitObject(LOGGING_CONTEXT);
	}

	/** The name of the configuration parameter that controls the prefix that will be used for logger names. */
	public static String LOGGER_NAME_PREFIX = "library.logging.logger_name_prefix";
}