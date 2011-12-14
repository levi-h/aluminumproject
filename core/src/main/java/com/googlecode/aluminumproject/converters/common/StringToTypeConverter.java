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
package com.googlecode.aluminumproject.converters.common;

import com.googlecode.aluminumproject.converters.ClassBasedConverter;
import com.googlecode.aluminumproject.converters.ConverterException;
import com.googlecode.aluminumproject.utilities.GenericsUtilities;
import com.googlecode.aluminumproject.utilities.UtilityException;

import java.lang.reflect.Type;

/**
 * Converts {@link String strings} into {@link Type types} using a {@link GenericsUtilities#getType(String, String...)
 * utility method} (with {@code java.lang} and {@code java.util} as default packages).
 *
 * @author levi_h
 */
public class StringToTypeConverter extends ClassBasedConverter<String, Type> {
	/**
	 * Creates a string to type converter.
	 */
	public StringToTypeConverter() {}

	@Override
	protected Type convert(String value) throws ConverterException {
		try {
			return GenericsUtilities.getType(value, "java.lang", "java.util");
		} catch (UtilityException exception) {
			throw new ConverterException(exception, "can't convert '", value, "' into a type");
		}
	}
}