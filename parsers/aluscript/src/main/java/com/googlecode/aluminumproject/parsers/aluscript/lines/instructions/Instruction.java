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
package com.googlecode.aluminumproject.parsers.aluscript.lines.instructions;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * An instruction as built by an {@link InstructionTokenProcessor instruction token processor}. It contains a (possibly
 * prefixed) name and a set of parameters, one of which can be under edit.
 *
 * @author levi_h
 */
class Instruction {
	private String namePrefix;
	private String name;

	private List<InstructionParameter> parameters;

	private InstructionParameter parameter;

	/**
	 * Creates an instruction.
	 */
	public Instruction() {
		parameters = new LinkedList<InstructionParameter>();
	}

	/**
	 * Returns the prefix of the name of this instruction.
	 *
	 * @return this instruction's name prefix
	 */
	public String getNamePrefix() {
		return namePrefix;
	}

	/**
	 * Sets this instruction's name prefix.
	 *
	 * @param namePrefix the prefix of the name of this instruction
	 */
	public void setNamePrefix(String namePrefix) {
		this.namePrefix = namePrefix;
	}

	/**
	 * Returns the name of this instruction.
	 *
	 * @return this instruction's name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets this instruction's name.
	 *
	 * @param name the name of this instruction
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Creates a parameter so that it can be edited.
	 */
	public void createParameter() {
		parameter = new InstructionParameter();
	}

	/**
	 * Returns the instruction parameter that's under edit. It may be {@code null}; in that case, a parameter can be
	 * {@link #createParameter() created}.
	 *
	 * @return this instruction's currently edited parameter
	 */
	public InstructionParameter getParameter() {
		return parameter;
	}

	/**
	 * Adds the currently edited parameter to the list that contains all of this instruction's parameters. After this
	 * call, the parameter is no longer under edit.
	 */
	public void addParameter() {
		parameters.add(parameter);

		parameter = null;
	}

	/**
	 * Returns a list that contains all of the parameters of this instruction.
	 *
	 * @return all of this instruction's parameters
	 */
	public List<InstructionParameter> getParameters() {
		return Collections.unmodifiableList(parameters);
	}
}