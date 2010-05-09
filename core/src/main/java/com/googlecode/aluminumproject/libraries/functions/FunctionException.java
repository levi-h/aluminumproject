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

import com.googlecode.aluminumproject.AluminumException;

/**
 * Thrown by {@link FunctionFactory function factories} when a function can't be created and by {@link Function
 * functions} when they can't be called.
 *
 * @author levi_h
 */
public class FunctionException extends AluminumException {
	/**
	 * Creates a function exception.
	 *
	 * @param messageParts the parts that form the exception message
	 */
	public FunctionException(Object... messageParts) {
		super(messageParts);
	}

	/**
	 * Creates a function exception.
	 *
	 * @param cause the cause of the exception
	 * @param messageParts the parts that form the exception message
	 */
	public FunctionException(Throwable cause, Object... messageParts) {
		super(cause, messageParts);
	}

	private static final long serialVersionUID = 20090524L;
}