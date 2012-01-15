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
package com.googlecode.aluminumproject.utilities;

import org.slf4j.LoggerFactory;

/**
 * Wrapper around an <a href="http://www.slf4j.org/">SLF4J</a> logger. This offers the following advantages:
 * <ul>
 * <li>By using this class instead of the actual logger, switching between logging implementations becomes a breeze;
 * <li>All calls are wrapped inside an <code>is<i>Level</i>Enabled</code> call;
 * <li>All methods support variable argument lists (varargs).
 * </ul>
 * A consequence of the third advantage is that because varargs are required to be the last arguments of methods, any
 * exceptions must be the first argument of log methods.
 * <p>
 * The general usage of this class consists of obtaining a {@code Logger} with the {@link #get(Class) get method} and
 * invoking the logging methods as needed.
 */
public class Logger {
	private org.slf4j.Logger logger;

	private Logger(org.slf4j.Logger logger) {
		this.logger = logger;
	}

	/**
	 * Returns the underlying logger.
	 *
	 * @return the actual logger used
	 */
	org.slf4j.Logger getLogger() {
		return logger;
	}

	/**
	 * Logs a message at <i>trace</i> level if that level is enabled.
	 *
	 * @param messageParts the parts that, combined, form the message to log
	 */
	public void trace(Object... messageParts) {
		if (logger.isTraceEnabled()) {
			logger.trace(StringUtilities.join(messageParts));
		}
	}

	/**
	 * Logs a message at <i>trace</i> level if that level is enabled.
	 *
	 * @param exception the exception to log
	 * @param messageParts the parts that, combined, form the message to log
	 */
	public void trace(Throwable exception, Object... messageParts) {
		if (logger.isTraceEnabled()) {
			logger.trace(StringUtilities.join(messageParts), exception);
		}
	}

	/**
	 * Logs a message at <i>debug</i> level if that level is enabled.
	 *
	 * @param messageParts the parts that, combined, form the message to log
	 */
	public void debug(Object... messageParts) {
		if (logger.isDebugEnabled()) {
			logger.debug(StringUtilities.join(messageParts));
		}
	}

	/**
	 * Logs a message at <i>debug</i> level if that level is enabled.
	 *
	 * @param exception the exception to log
	 * @param messageParts the parts that, combined, form the message to log
	 */
	public void debug(Throwable exception, Object... messageParts) {
		if (logger.isDebugEnabled()) {
			logger.debug(StringUtilities.join(messageParts), exception);
		}
	}

	/**
	 * Logs a message at <i>info</i> level if that level is enabled.
	 *
	 * @param messageParts the parts that, combined, form the message to log
	 */
	public void info(Object... messageParts) {
		if (logger.isInfoEnabled()) {
			logger.info(StringUtilities.join(messageParts));
		}
	}

	/**
	 * Logs a message at <i>info</i> level if that level is enabled.
	 *
	 * @param exception the exception to log
	 * @param messageParts the parts that, combined, form the message to log
	 */
	public void info(Throwable exception, Object... messageParts) {
		if (logger.isInfoEnabled()) {
			logger.info(StringUtilities.join(messageParts), exception);
		}
	}

	/**
	 * Logs a message at <i>warn</i> level if that level is enabled.
	 *
	 * @param messageParts the parts that, combined, form the message to log
	 */
	public void warn(Object... messageParts) {
		if (logger.isWarnEnabled()) {
			logger.warn(StringUtilities.join(messageParts));
		}
	}

	/**
	 * Logs a message at <i>warn</i> level if that level is enabled.
	 *
	 * @param exception the exception to log
	 * @param messageParts the parts that, combined, form the message to log
	 */
	public void warn(Throwable exception, Object... messageParts) {
		if (logger.isWarnEnabled()) {
			logger.warn(StringUtilities.join(messageParts), exception);
		}
	}

	/**
	 * Logs a message at <i>error</i> level if that level is enabled.
	 *
	 * @param messageParts the parts that, combined, form the message to log
	 */
	public void error(Object... messageParts) {
		if (logger.isErrorEnabled()) {
			logger.error(StringUtilities.join(messageParts));
		}
	}

	/**
	 * Logs a message at <i>error</i> level if that level is enabled.
	 *
	 * @param exception the exception to log
	 * @param messageParts the parts that, combined, form the message to log
	 */
	public void error(Throwable exception, Object... messageParts) {
		if (logger.isErrorEnabled()) {
			logger.error(StringUtilities.join(messageParts), exception);
		}
	}

	/**
	 * Returns a logger for a certain class.
	 *
	 * @param forClass the class for which the logger should be retrieved
	 * @return a logger for the given class
	 */
	public static Logger get(Class<?> forClass) {
		return new Logger(LoggerFactory.getLogger(forClass));
	}
}