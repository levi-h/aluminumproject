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
package com.googlecode.aluminumproject.libraries.functions;

import java.lang.reflect.Type;

/**
 * Provides information about a {@link FunctionArgument function argument}.
 */
public class FunctionArgumentInformation {
	private String name;
	private Type type;

	/**
	 * Creates function argument information.
	 *
	 * @param name the name of the argument (may be {@code null})
	 * @param type the type of the argument
	 */
	public FunctionArgumentInformation(String name, Type type) {
		this.name = name;
		this.type = type;
	}

	/**
	 * Returns the name of the argument. Argument names are optional, but if they are present, they should be unique
	 * across a {@link Function function}.
	 *
	 * @return the argument's name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the type of the argument.
	 *
	 * @return the argument type
	 */
	public Type getType() {
		return type;
	}
}