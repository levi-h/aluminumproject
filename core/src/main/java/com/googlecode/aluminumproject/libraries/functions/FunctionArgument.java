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
package com.googlecode.aluminumproject.libraries.functions;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.context.Context;

import java.lang.reflect.Type;

/**
 * The argument of a {@link Function function}.
 */
public interface FunctionArgument {
	/**
	 * Returns the value of this argument.
	 *
	 * @param type the required type for the argument value
	 * @param context the context in which the argument's function will run
	 * @return the value of this argument with the given type
	 * @throws AluminumException when the argument value can't be retrieved or converted to the desired type
	 */
	Object getValue(Type type, Context context) throws AluminumException;
}