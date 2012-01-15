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
package com.googlecode.aluminumproject.parsers.aluscript.lines.instructions;

/**
 * A parameter of an {@link Instruction instruction}. It consists of a name (with an optional prefix) and a value.
 */
class InstructionParameter {
	private String namePrefix;
	private String name;
	private String value;

	/**
	 * Creates a parameter.
	 */
	public InstructionParameter() {}

	/**
	 * Returns the prefix of the name of this instruction parameter.
	 *
	 * @return this instruction parameter's name prefix
	 */
	public String getNamePrefix() {
		return namePrefix;
	}

	/**
	 * Sets this instruction parameter's name prefix.
	 *
	 * @param namePrefix the prefix of the name of this instruction parameter
	 */
	public void setNamePrefix(String namePrefix) {
		this.namePrefix = namePrefix;
	}

	/**
	 * Returns the name of this instruction parameter.
	 *
	 * @return this instruction parameter's name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets this instruction parameter's name.
	 *
	 * @param name the name of this instruction parameter
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the value of this instruction parameter.
	 *
	 * @return this instruction parameter's value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Sets this instruction parameter's value.
	 *
	 * @param value the value of this instruction parameter
	 */
	public void setValue(String value) {
		this.value = value;
	}
}