/*
 * Copyright 2009-2011 Levi Hoogenberg
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
package com.googlecode.aluminumproject.cli.commands;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.cli.Command;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A command that can be used inside tests.
 *
 * @author levi_h
 */
public class TestCommand extends Command {
	private Map<String, String> helpInformation;

	private boolean argumentRequired;

	/**
	 * Creates a test command.
	 */
	public TestCommand() {
		helpInformation = new HashMap<String, String>();
	}

	/**
	 * Sets whether arguments are required or not. If they are and no arguments are supplied, then the {@link
	 * #execute(PrintStream, PrintStream, List) execute method} will throw an exception.
	 *
	 * @param argumentRequired {@code true} if this command needs an argument, {@code false} otherwise
	 */
	public void setArgumentRequired(boolean argumentRequired) {
		this.argumentRequired = argumentRequired;
	}

	@Override
	public Map<String, String> getHelpInformation() {
		return helpInformation;
	}

	@Override
	protected void execute(
			PrintStream outputStream, PrintStream errorStream, List<String> arguments) throws AluminumException {
		logger.debug("executing test command");

		if (argumentRequired && arguments.isEmpty()) {
			throw new AluminumException(new IllegalStateException(), "no arguments were supplied");
		}
	}
}