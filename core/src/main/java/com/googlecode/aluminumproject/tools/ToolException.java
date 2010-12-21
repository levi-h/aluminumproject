/*
 * Copyright 2010 Levi Hoogenberg
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
package com.googlecode.aluminumproject.tools;

import com.googlecode.aluminumproject.AluminumException;

/**
 * Thrown by tools when something goes wrong while using them. Tools may, depending on their size, either subclass
 * {@code ToolException} or use it directly.
 *
 * @author levi_h
 */
public class ToolException extends AluminumException {
	/**
	 * Creates a tool exception.
	 *
	 * @param messageParts the parts of the exception message
	 */
	public ToolException(Object... messageParts) {
		super(messageParts);
	}

	/**
	 * Creates a tool exception.
	 *
	 * @param cause the cause of the exception
	 * @param messageParts the parts of the exception message
	 */
	public ToolException(Throwable cause, Object... messageParts) {
		super(cause, messageParts);
	}

	private final static long serialVersionUID = 20101221L;
}