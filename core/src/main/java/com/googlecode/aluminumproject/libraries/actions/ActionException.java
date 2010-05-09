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
package com.googlecode.aluminumproject.libraries.actions;

import com.googlecode.aluminumproject.AluminumException;

/**
 * Thrown by {@link Action actions} when their execution fails.
 *
 * @author levi_h
 */
public class ActionException extends AluminumException {
	/**
	 * Creates an action exception.
	 *
	 * @param messageParts the parts that form the exception message
	 */
	public ActionException(Object... messageParts) {
		super(messageParts);
	}

	/**
	 * Creates an action exception.
	 *
	 * @param cause the cause of the exception
	 * @param messageParts the parts that form the exception message
	 */
	public ActionException(Throwable cause, Object... messageParts) {
		super(cause, messageParts);
	}

	private final static long serialVersionUID = 20090308L;
}