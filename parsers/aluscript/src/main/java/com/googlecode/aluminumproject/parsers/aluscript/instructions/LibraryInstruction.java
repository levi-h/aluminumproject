/*
 * Copyright 2010-2012 Levi Hoogenberg
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

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.parsers.aluscript.AluScriptContext;

import java.util.Map;

/**
 * Adds one or more library URLs and their abbreviations to the {@link AluScriptContext context}, so that they can be
 * referenced by upcoming instructions at the same nesting level. The name of the instruction is {@value #NAME}.
 */
public class LibraryInstruction extends AbstractInstruction {
	/**
	 * Creates a library instruction.
	 */
	public LibraryInstruction() {
		super(NAME);
	}

	public void execute(Map<String, String> parameters, AluScriptContext context) throws AluminumException {
		if (parameters.isEmpty()) {
			throw new AluminumException("at least one library URL should be given");
		}

		for (String abbreviation: parameters.keySet()) {
			context.addLibraryUrlAbbreviation(abbreviation, parameters.get(abbreviation));
		}
	}

	/** The name of the library instruction: {@value}. */
	public final static String NAME = "library";
}