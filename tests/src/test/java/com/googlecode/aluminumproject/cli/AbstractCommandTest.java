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
package com.googlecode.aluminumproject.cli;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;

/**
 * Abstract superclass of command tests.
 *
 * @author levi_h
 */
public abstract class AbstractCommandTest {
	/**
	 * Creates an abstract command test.
	 */
	protected AbstractCommandTest() {}

	/**
	 * Resets the logger context, since {@link Command the tested commands} tamper with it.
	 *
	 * @throws JoranException when the logger context can't be reset
	 */
	@AfterClass
	public void resetLoggerContext() throws JoranException {
		LoggerContext loggerFactory = (LoggerContext) LoggerFactory.getILoggerFactory();

		loggerFactory.stop();

		JoranConfigurator configurator = new JoranConfigurator();
		configurator.setContext(loggerFactory);
		configurator.doConfigure(Thread.currentThread().getContextClassLoader().getResource("logback.xml"));

		loggerFactory.start();
	}

	/**
	 * Executes a command with certain options.
	 *
	 * @param command the command to execute
	 * @param options the options to execute the command with
	 * @return an array of length two containing the contents of the output stream and the error stream, respectively
	 */
	protected final String[] executeCommand(Command command, String... options) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ByteArrayOutputStream errorStream = new ByteArrayOutputStream();

		command.execute(new PrintStream(outputStream), new PrintStream(errorStream), options);

		return new String[] {
			getContents(outputStream),
			getContents(errorStream)
		};
	}

	private String getContents(ByteArrayOutputStream out) {
		StringBuilder contentsBuilder = new StringBuilder();

		for (String line: new String(out.toByteArray()).split("\n")) {
			if (contentsBuilder.length() > 0) {
				contentsBuilder.append("\n");
			}

			contentsBuilder.append(line.trim());
		}

		return contentsBuilder.toString();
	}
}