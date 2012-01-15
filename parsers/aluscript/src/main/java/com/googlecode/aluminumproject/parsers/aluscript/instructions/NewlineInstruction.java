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

/**
 * Adds a text element that contains a newline to the context. The name of the instruction is {@value #NAME}.
 */
public class NewlineInstruction extends TextInstruction {
	/**
	 * Creates a newline instruction.
	 */
	public NewlineInstruction() {
		super(NAME, "\n");
	}

	/** The name of the newline instruction: {@value}. */
	public final static String NAME = "newline";
}