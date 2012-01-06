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
package com.googlecode.aluminumproject.cli;

import com.googlecode.aluminumproject.cli.commands.TestCommand;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
@Test(groups = {"integration", "integration-cli", "fast"})
public class CommandTest extends AbstractCommandTest {
	private TestCommand command;

	@BeforeMethod
	public void createCommand() {
		command = new TestCommand();
	}

	public void debugStatementsShouldNotBePrintedByDefault() {
		Execution execution = executeCommand(command);
		assert execution.wasSuccessful();
		assert !execution.hadOutput();
		assert !execution.hadErrors();
	}

	public void supplyingDebugOptionShouldCauseDebugStatementsToBePrinted() {
		Execution execution = executeCommand(command, "--debug");
		assert execution.wasSuccessful();
		assert execution.hadOutput();
		assert execution.getOutput().equals("DEBUG: executing test command");
		assert !execution.hadErrors();
	}

	public void supplyingHelpOptionShouldDisplayHelpMessage() {
		Execution execution = executeCommand(command, "--help");
		assert execution.wasSuccessful();
		assert execution.hadOutput();
		assert execution.getOutput().equals(getHelpMessage(false, false, false));
		assert !execution.hadErrors();
	}

	@Test(dependsOnMethods = "supplyingHelpOptionShouldDisplayHelpMessage")
	public void supplyingShortenedHelpOptionShouldDisplayHelpMessage() {
		Execution execution = executeCommand(command, "-h");
		assert execution.wasSuccessful();
		assert execution.hadOutput();
		assert execution.getOutput().equals(getHelpMessage(false, false, false));
		assert !execution.hadErrors();
	}

	@Test(dependsOnMethods = "supplyingHelpOptionShouldDisplayHelpMessage")
	public void helpMessageShouldIncludeName() {
		command.getHelpInformation().put("name", "test");

		Execution execution = executeCommand(command, "-h");
		assert execution.wasSuccessful();
		assert execution.hadOutput();
		assert execution.getOutput().equals(getHelpMessage(true, false, false));
		assert !execution.hadErrors();
	}

	@Test(dependsOnMethods = "supplyingHelpOptionShouldDisplayHelpMessage")
	public void helpMessageShouldIncludeDescription() {
		command.getHelpInformation().put("description", "A test command.");

		Execution execution = executeCommand(command, "-h");
		assert execution.wasSuccessful();
		assert execution.hadOutput();
		assert execution.getOutput().equals(getHelpMessage(false, true, false));
		assert !execution.hadErrors();
	}

	@Test(dependsOnMethods = "supplyingHelpOptionShouldDisplayHelpMessage")
	public void helpMessageShouldIncludeUsage() {
		command.getHelpInformation().put("usage", "Usage: test [options]");

		Execution execution = executeCommand(command, "-h");
		assert execution.wasSuccessful();
		assert execution.hadOutput();
		assert execution.getOutput().equals(getHelpMessage(false, false, true));
		assert !execution.hadErrors();
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

		Execution execution = executeCommand(command, "-h");
		assert execution.wasSuccessful();
		assert execution.hadOutput();
		assert execution.getOutput().equals(getHelpMessage(true, true, true));
		assert !execution.hadErrors();
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

		Execution execution = executeCommand(command);
		assert !execution.wasSuccessful();
		assert !execution.hadOutput();
		assert execution.hadErrors();
		assert execution.getErrors().equals("no arguments were supplied");
	}

	public void supplyingStackTraceOptionShouldPrintStackTraces() {
		command.setArgumentRequired(true);

		Execution execution = executeCommand(command, "--print-stack-traces");
		assert !execution.wasSuccessful();
		assert !execution.hadOutput();
		assert execution.hadErrors();
		assert execution.getErrors().startsWith("no arguments were supplied");
		assert execution.getErrors().contains("IllegalStateException");
	}

	public void supplyingUnrecognisedOptionShouldResultInErrorMessage() {
		Execution execution = executeCommand(command, "--nonexistent");
		assert !execution.wasSuccessful();
		assert !execution.hadOutput();
		assert execution.hadErrors();
		assert execution.getErrors().equals("'nonexistent' is not a recognized option");
	}
}