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
package com.googlecode.aluminumproject.libraries.logging.actions;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.logging.LoggingContext;
import com.googlecode.aluminumproject.libraries.actions.AbstractAction;
import com.googlecode.aluminumproject.templates.TemplateInformation;
import com.googlecode.aluminumproject.writers.Writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("javadoc")
abstract class AbstractLogAction extends AbstractAction {
	private String message;

	public void execute(Context context, Writer writer) throws AluminumException {
		if (message == null) {
			message = getBodyText(context, writer);
		}

		String loggerNamePrefix = LoggingContext.from(context).getLoggerNamePrefix();
		String templateName = TemplateInformation.from(context).getTemplate().getName();

		String loggerName =
			String.format("%s%s%s", loggerNamePrefix, loggerNamePrefix.isEmpty() ? "" : ".", templateName);

		getLevel().log(LoggerFactory.getLogger(loggerName), message);
	}

	protected abstract Level getLevel();

	public static enum Level {
		TRACE {
			protected void log(Logger logger, String message) {
				if (logger.isTraceEnabled()) {
					logger.trace(message);
				}
			}
		},
		DEBUG {
			protected void log(Logger logger, String message) {
				if (logger.isDebugEnabled()) {
					logger.debug(message);
				}
			}
		},
		INFO {
			protected void log(Logger logger, String message) {
				if (logger.isInfoEnabled()) {
					logger.info(message);
				}
			}
		},
		WARN {
			protected void log(Logger logger, String message) {
				if (logger.isWarnEnabled()) {
					logger.warn(message);
				}
			}
		},
		ERROR {
			protected void log(Logger logger, String message) {
				if (logger.isErrorEnabled()) {
					logger.error(message);
				}
			}
		};

		protected abstract void log(Logger logger, String message);
	}
}
