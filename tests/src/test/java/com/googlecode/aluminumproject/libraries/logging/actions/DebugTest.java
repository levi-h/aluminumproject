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

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;

import com.googlecode.aluminumproject.libraries.logging.LoggingLibraryTest;

import java.util.List;

import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
@Test(groups = {"libraries", "libraries-logging", "slow"})
public class DebugTest extends LoggingLibraryTest {
	public void messageShouldBeLogged() {
		processTemplate("debug");

		List<ILoggingEvent> loggingEvents = getLoggingEvents("debug");
		assert loggingEvents.size() == 1;

		ILoggingEvent loggingEvent = loggingEvents.get(0);
		assert loggingEvent != null;
		assert loggingEvent.getLevel() == Level.DEBUG;
		assert loggingEvent.getMessage().equals("debug message");
	}

	public void bodyMessageShouldBeLogged() {
		processTemplate("debug-with-body");

		List<ILoggingEvent> loggingEvents = getLoggingEvents("debug-with-body");
		assert loggingEvents.size() == 1;

		ILoggingEvent loggingEvent = loggingEvents.get(0);
		assert loggingEvent != null;
		assert loggingEvent.getLevel() == Level.DEBUG;
		assert loggingEvent.getMessage().equals("body message");
	}
}