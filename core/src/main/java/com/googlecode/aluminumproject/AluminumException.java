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
package com.googlecode.aluminumproject;

import com.googlecode.aluminumproject.utilities.StringUtilities;

/**
 * Thrown by the template engine when an unexpected condition arises.
 */
public class AluminumException extends RuntimeException {
	private String origin;

	/**
	 * Creates an Aluminum exception.
	 *
	 * @param messageParts the parts of the exception message
	 */
	public AluminumException(Object... messageParts) {
		super(StringUtilities.join(messageParts));
	}

	/**
	 * Creates an Aluminum exception.
	 *
	 * @param cause the cause of the exception
	 * @param messageParts the parts of the exception message.
	 */
	public AluminumException(Throwable cause, Object... messageParts) {
		super(StringUtilities.join(messageParts), cause);
	}

	/**
	 * Returns the origin of this exception.
	 *
	 * @return this exception's origin (may be {@code null})
	 */
	public String getOrigin() {
		return origin;
	}

	/**
	 * Sets the origin of this exception.
	 *
	 * @param origin the exception origin
	 */
	public void setOrigin(String origin) {
		this.origin = origin;
	}

	@Override
	public String getMessage() {
		String message = super.getMessage();

		if (origin != null) {
			message = String.format("%s (%s)", message, origin);
		}

		return message;
	}

	private final static long serialVersionUID = 20120120L;
}