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
package com.googlecode.aluminumproject.converters;

import com.googlecode.aluminumproject.AluminumException;

/**
 * Thrown by {@link ConverterRegistry converter registries} when no converter can be found or by {@link Converter
 * converters} in case of a conversion failure.
 *
 * @author levi_h
 */
public class ConverterException extends AluminumException {
	/**
	 * Creates a converter exception.
	 *
	 * @param messageParts the parts of the exception message
	 */
	public ConverterException(Object... messageParts) {
		super(messageParts);
	}

	/**
	 * Creates a converter exception.
	 *
	 * @param cause the cause of the exception
	 * @param messageParts the parts of the exception message
	 */
	public ConverterException(Throwable cause, Object... messageParts) {
		super(cause, messageParts);
	}

	private static final long serialVersionUID = 20090325L;
}