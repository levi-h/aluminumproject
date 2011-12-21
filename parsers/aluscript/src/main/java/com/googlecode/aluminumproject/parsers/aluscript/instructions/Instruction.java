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
package com.googlecode.aluminumproject.parsers.aluscript.instructions;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.parsers.aluscript.AluScriptContext;
import com.googlecode.aluminumproject.parsers.aluscript.AluScriptParser;

import java.util.Map;

/**
 * An instruction for the {@link AluScriptParser AluScript parser}. It allows a number of parameters and modifies the
 * {@link AluScriptContext context}.
 *
 * @author levi_h
 */
public interface Instruction {
	/**
	 * Returns the name of this instruction.
	 *
	 * @return this instruction's name
	 */
	String getName();

	/**
	 * Executes this instruction in a certain context.
	 *
	 * @param parameters the parameters of this instruction (may be empty, but not {@code null})
	 * @param context the context to use
	 * @throws AluminumException when something goes wrong while executing this instruction
	 */
	void execute(Map<String, String> parameters, AluScriptContext context) throws AluminumException;
}