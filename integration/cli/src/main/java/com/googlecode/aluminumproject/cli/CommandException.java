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
package com.googlecode.aluminumproject.cli;

import com.googlecode.aluminumproject.AluminumException;

/**
 * Thrown by {@link Command commands} when they can't be executed or by {@link OptionEffect option effects when they
 * can't be applied}.
 *
 * @author levi_h
 */
public class CommandException extends AluminumException {
	/**
	 * Creates a command exception.
	 *
	 * @param messageParts the parts of the exception message
	 */
	public CommandException(Object... messageParts) {
		super(messageParts);
	}

	/**
	 * Creates a command exception.
	 *
	 * @param cause the cause of the exception
	 * @param messageParts the parts of the exception message
	 */
	public CommandException(Throwable cause, Object... messageParts) {
		super(cause, messageParts);
	}

	private static final long serialVersionUID = 20090719L;
}