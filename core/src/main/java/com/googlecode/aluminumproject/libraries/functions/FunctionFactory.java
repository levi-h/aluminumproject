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
package com.googlecode.aluminumproject.libraries.functions;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.libraries.LibraryElement;

import java.util.List;

/**
 * Creates a {@link Function function}.
 *
 * @author levi_h
 */
public interface FunctionFactory extends LibraryElement {
	/**
	 * Returns information about the function.
	 *
	 * @return the information that describes the function
	 */
	FunctionInformation getInformation();

	/**
	 * Creates a function.
	 *
	 * @param arguments the arguments for the function
	 * @param context the context in which the function will run
	 * @return the new function
	 * @throws AluminumException when the function can't be created
	 */
	Function create(List<FunctionArgument> arguments, Context context) throws AluminumException;
}