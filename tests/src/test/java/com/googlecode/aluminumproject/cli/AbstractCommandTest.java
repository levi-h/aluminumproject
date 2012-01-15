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
	 * @return information about the execution
	 */
	protected final Execution executeCommand(Command command, String... options) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ByteArrayOutputStream errorStream = new ByteArrayOutputStream();

		int status = command.execute(new PrintStream(outputStream), new PrintStream(errorStream), options);

		return new Execution(status, outputStream, errorStream);
	}

	/**
	 * Provides information about the execution of a command.
	 */
	protected class Execution {
		private int status;

		private String output;
		private String errors;

		private Execution(int status, ByteArrayOutputStream outputStream, ByteArrayOutputStream errorStream) {
			this.status = status;

			output = getContents(outputStream);
			errors = getContents(errorStream);
		}

		private String getContents(ByteArrayOutputStream stream) {
			StringBuilder contentsBuilder = new StringBuilder();

			for (String line: new String(stream.toByteArray()).split("\n")) {
				if (contentsBuilder.length() > 0) {
					contentsBuilder.append("\n");
				}

				contentsBuilder.append(line.trim());
			}

			return (contentsBuilder.length() == 0) ? null : contentsBuilder.toString();
		}

		/**
		 * Determines whether the command was executed successfully.
		 *
		 * @return {@code true} if the command completed successfully, {@code false} if it didn't
		 */
		public boolean wasSuccessful() {
			return status == 0;
		}

		/**
		 * Determines whether the command had any output.
		 *
		 * @return {@code true} if the output stream had any contents, {@code false} if it didn't
		 */
		public boolean hadOutput() {
			return output != null;
		}

		/**
		 * Returns the contents of the output stream.
		 *
		 * @return all of the command's output (possibly {@code null})
		 */
		public String getOutput() {
			return output;
		}

		/**
		 * Determines whether the command had any errors.
		 *
		 * @return {@code true} if the error stream contained anything, {@code false} if it didn't
		 */
		public boolean hadErrors() {
			return errors != null;
		}

		/**
		 * Returns the contents of the error stream.
		 *
		 * @return all of the command's errors (might be {@code null})
		 */
		public String getErrors() {
			return errors;
		}
	}
}