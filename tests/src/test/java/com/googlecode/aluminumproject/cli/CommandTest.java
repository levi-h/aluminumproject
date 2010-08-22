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

import com.googlecode.aluminumproject.cli.commands.test.TestCommand;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("all")
@Test(groups = {"cli", "fast"})
public class CommandTest extends AbstractCommandTest {
	private TestCommand command;

	@BeforeMethod
	public void createCommand() {
		command = new TestCommand();
	}

	public void debugStatementsShouldNotBePrintedByDefault() {
		String[] output = executeCommand(command);
		assert output[0].equals("");
		assert output[1].equals("");
	}

	public void supplyingDebugOptionShouldCauseDebugStatementsToBePrinted() {
		String[] output = executeCommand(command, "--debug");
		assert output[0].equals("DEBUG: executing test command");
		assert output[1].equals("");
	}

	public void supplyingHelpOptionShouldDisplayHelpMessage() {
		String[] output = executeCommand(command, "--help");
		assert output[0].equals(getHelpMessage(false, false, false));
		assert output[1].equals("");
	}

	@Test(dependsOnMethods = "supplyingHelpOptionShouldDisplayHelpMessage")
	public void supplyingShortenedHelpOptionShouldDisplayHelpMessage() {
		String[] output = executeCommand(command, "-h");
		assert output[0].equals(getHelpMessage(false, false, false));
		assert output[1].equals("");
	}

	@Test(dependsOnMethods = "supplyingHelpOptionShouldDisplayHelpMessage")
	public void helpMessageShouldIncludeName() {
		command.getHelpInformation().put("name", "test");

		String[] output = executeCommand(command, "-h");
		assert output[0].equals(getHelpMessage(true, false, false));
		assert output[1].equals("");
	}

	@Test(dependsOnMethods = "supplyingHelpOptionShouldDisplayHelpMessage")
	public void helpMessageShouldIncludeDescription() {
		command.getHelpInformation().put("description", "A test command.");

		String[] output = executeCommand(command, "-h");
		assert output[0].equals(getHelpMessage(false, true, false));
		assert output[1].equals("");
	}

	@Test(dependsOnMethods = "supplyingHelpOptionShouldDisplayHelpMessage")
	public void helpMessageShouldIncludeUsage() {
		command.getHelpInformation().put("usage", "Usage: test [options]");

		String[] output = executeCommand(command, "-h");
		assert output[0].equals(getHelpMessage(false, false, true));
		assert output[1].equals("");
	}

	@Test(dependsOnMethods = {
		"helpMessageShouldIncludeName",
		"helpMessageShouldIncludeDescription",
		"helpMessageShouldIncludeUsage"
	})
	public void helpInformationShouldBeDisplayedInCorrectOrder() {
		command.getHelpInformation().put("name", "test");
		command.getHelpInformation().put("description", "A test command.");
		command.getHelpInformation().put("usage", "Usage: test [options]");

		String[] output = executeCommand(command, "-h");
		assert output[0].equals(getHelpMessage(true, true, true));
		assert output[1].equals("");
	}

	private String getHelpMessage(boolean includeName, boolean includeDescription, boolean includeUsage) {
		StringBuilder helpMessageBuilder = new StringBuilder();

		if (includeName) {
			helpMessageBuilder.append("test\n");
			helpMessageBuilder.append("\n");
		}

		if (includeDescription) {
			helpMessageBuilder.append("A test command.\n");
			helpMessageBuilder.append("\n");
		}

		if (includeUsage) {
			helpMessageBuilder.append("Usage: test [options]\n");
			helpMessageBuilder.append("\n");
		}

		helpMessageBuilder.append(
			"The following options are accepted:\n" +
			"\n" +
			"Option                                  Description\n" +
			"------                                  -----------\n" +
			"--debug                                 Prints debug statements.\n" +
			"-h, --help                              Displays this help message.\n" +
			"--print-stack-traces                    Prints the stack traces of exceptions."
		);

		return helpMessageBuilder.toString();
	}

	public void stackTracesShouldBeHiddenByDefault() {
		command.setArgumentRequired(true);

		String[] output = executeCommand(command);
		assert output[0].equals("");
		assert output[1].equals("No arguments were supplied.");
	}

	public void supplyingStackTraceOptionShouldPrintStackTraces() {
		command.setArgumentRequired(true);

		String[] output = executeCommand(command, "--print-stack-traces");
		assert output[0].equals("");
		assert output[1].startsWith("No arguments were supplied.");
		assert output[1].contains("IllegalStateException");
	}
}