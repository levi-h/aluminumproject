/*
 * Copyright 2010-2012 Aluminum project
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
package com.googlecode.aluminumproject.parsers.aluscript.instructions;

import com.googlecode.aluminumproject.parsers.aluscript.AluScriptContext;

import java.util.Map;

/**
 * An instruction that can be used in tests.
 */
public class TestInstruction implements Instruction {
	private boolean executed;

	/**
	 * Creates a test instruction.
	 */
	public TestInstruction() {}

	public String getName() {
		return "test";
	}

	/**
	 * Determines whether this instruction was executed or not.
	 *
	 * @return {@code true} if this instruction was executed, {@code false} otherwise
	 */
	public boolean isExecuted() {
		return executed;
	}

	public void execute(Map<String, String> parameters, AluScriptContext context) {
		executed = true;
	}
}