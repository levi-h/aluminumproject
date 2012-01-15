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

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.utilities.ReflectionUtilities;

import joptsimple.OptionSet;
import joptsimple.OptionSpec;

/**
 * An option effect that will set a property on a command.
 *
 * @param <T> the type of the property that will be set
 */
public class PropertyEffect<T> implements OptionEffect<Object> {
	private Command command;
	private Class<T> propertyType;
	private String propertyName;
	private T value;

	/**
	 * Creates a property effect.
	 *
	 * @param command the command to set the property on
	 * @param propertyType the type of the property
	 * @param propertyName the name of the property
	 * @param value the value to set
	 */
	public PropertyEffect(Command command, Class<T> propertyType, String propertyName, T value) {
		this.command = command;
		this.propertyType = propertyType;
		this.propertyName = propertyName;
		this.value = value;
	}

	public void apply(OptionSet options, OptionSpec<Object> option) throws AluminumException {
		ReflectionUtilities.setProperty(command, propertyType, propertyName, value);
	}
}