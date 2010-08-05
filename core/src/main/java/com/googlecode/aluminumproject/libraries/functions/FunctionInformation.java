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
package com.googlecode.aluminumproject.libraries.functions;

import com.googlecode.aluminumproject.libraries.Library;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

/**
 * Gives information on a {@link Function function}.
 *
 * @author levi_h
 */
public class FunctionInformation {
	private String name;
	private Type resultType;

	private List<FunctionArgumentInformation> argumentInformation;

	/**
	 * Creates function information.
	 *
	 * @param name the name of the function
	 * @param resultType the type of the function's results
	 * @param argumentInformation information about the arguments of the function
	 */
	public FunctionInformation(String name, Type resultType, List<FunctionArgumentInformation> argumentInformation) {
		this.name = name;
		this.resultType = resultType;

		this.argumentInformation = argumentInformation;
	}

	/**
	 * Returns the name of the function. It should be unique across a {@link Library library}.
	 *
	 * @return the function's name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the type of the function's results.
	 *
	 * @return the function's result type
	 */
	public Type getResultType() {
		return resultType;
	}

	/**
	 * Returns information about the arguments that the function needs.
	 *
	 * @return information that describes the arguments of the function
	 */
	public List<FunctionArgumentInformation> getArgumentInformation() {
		return Collections.unmodifiableList(argumentInformation);
	}
}