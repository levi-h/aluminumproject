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

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.context.Context;

/**
 * The context in which logging library elements operate.
 * <p>
 * Without any custom configuration, the logging context will be made available by a {@link LoggingContextProvider
 * logging context provider} and can be retrieved using the {@link #from(Context) from method}.
 */
public class LoggingContext {
	private String loggerNamePrefix;

	/**
	 * Creates a logging context.
	 *
	 * @param loggerNamePrefix the prefix to use for logger names (may be empty)
	 */
	public LoggingContext(String loggerNamePrefix) {
		this.loggerNamePrefix = loggerNamePrefix;
	}

	/**
	 * Returns the configured logger name prefix.
	 *
	 * @return the prefix of the logger name that will be used
	 */
	public String getLoggerNamePrefix() {
		return loggerNamePrefix;
	}

	/**
	 * Sets the name that template names will be prefixed with when logging a message.
	 *
	 * @param loggerNamePrefix the logger name prefix to use (may be empty)
	 */
	public void setLoggerNamePrefix(String loggerNamePrefix) {
		this.loggerNamePrefix = loggerNamePrefix;
	}

	/**
	 * Finds the logging context from a context.
	 *
	 * @param context the context to extract the logging context from
	 * @return the context's implicit object that represents the logging context
	 * @throws AluminumException when the context does not contain a logging context
	 */
	public static LoggingContext from(Context context) throws AluminumException {
		return (LoggingContext) context.getImplicitObject(LOGGING_CONTEXT);
	}

	/**
	 * The name of the implicit object that the {@link LoggingContextProvider logging context provider} will create:
	 * {@value}.
	 */
	public final static String LOGGING_CONTEXT =
		Context.RESERVED_IMPLICIT_OBJECT_NAME_PREFIX + ".library.logging.logging_context";
}