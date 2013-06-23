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
package com.googlecode.aluminumproject.libraries.logging;

import static org.slf4j.Logger.ROOT_LOGGER_NAME;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.read.ListAppender;

import com.googlecode.aluminumproject.libraries.LibraryTest;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

@SuppressWarnings("javadoc")
public class LoggingLibraryTest extends LibraryTest {
	private Appender<ILoggingEvent> originalAppender;
	private Level originalLevel;

	private ListAppender<ILoggingEvent> listAppender;

	public LoggingLibraryTest() {
		super("templates/logging", "aluscript");
	}

	@BeforeMethod
	public void configureTestLogging() {
		LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

		Logger rootLogger = context.getLogger(ROOT_LOGGER_NAME);

		originalAppender = rootLogger.getAppender("console");

		rootLogger.detachAppender(originalAppender);

		listAppender = new ListAppender<ILoggingEvent>();
		listAppender.setContext(context);
		listAppender.start();

		rootLogger.addAppender(listAppender);

		originalLevel = rootLogger.getLevel();

		rootLogger.setLevel(Level.ALL);
	}

	@AfterMethod
	public void revertLoggingConfiguration() {
		Logger rootLogger = ((LoggerContext) LoggerFactory.getILoggerFactory()).getLogger(ROOT_LOGGER_NAME);

		listAppender.stop();

		rootLogger.detachAppender(listAppender);

		if (originalAppender != null) {
			rootLogger.addAppender(originalAppender);
		}

		rootLogger.setLevel(originalLevel);
	}

	protected List<ILoggingEvent> getLoggingEvents(String loggerName) {
		List<ILoggingEvent> loggingEvents = new LinkedList<ILoggingEvent>();

		for (ILoggingEvent loggingEvent: listAppender.list) {
			if (loggingEvent.getLoggerName().equals(loggerName)) {
				loggingEvents.add(loggingEvent);
			}
		}

		return loggingEvents;
	}

}