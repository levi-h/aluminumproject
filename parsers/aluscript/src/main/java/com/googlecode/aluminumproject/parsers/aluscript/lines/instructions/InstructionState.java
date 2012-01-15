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
package com.googlecode.aluminumproject.parsers.aluscript.lines.instructions;

/**
 * A possible state of an {@link InstructionTokenProcessor instruction token processor}.
 */
enum InstructionState {
	/** The instruction token processor is ready to create an instruction. */
	READY,

	/** The instruction token processor has created an instruction and expects an instruction name or its prefix. */
	INSTRUCTION_NAME_OR_PREFIX,

	/** The instruction token processor has received an instruction name prefix and now expects its name. */
	INSTRUCTION_NAME,

	/** The instruction token processor has received an instruction name and expects a parameter name or its prefix. */
	PARAMETER_NAME_OR_PREFIX,

	/** The instruction token processor has received a parameter name prefix and expects its name. */
	PARAMETER_NAME,

	/** The instruction token processor has received a parameter name and expects a value. */
	PARAMETER_VALUE,

	/** The instruction token processor has received all parameters with their values and no longer expects any text. */
	AFTER_PARAMETERS,

	/** The instruction token processor has completed an instruction. */
	DONE;
}