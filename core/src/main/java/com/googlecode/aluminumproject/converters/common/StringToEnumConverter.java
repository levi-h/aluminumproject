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
package com.googlecode.aluminumproject.converters.common;

import com.googlecode.aluminumproject.converters.Converter;
import com.googlecode.aluminumproject.converters.ConverterException;

/**
 * Converts the name of an to {@link Enum enum} into an enum constant. All enum types are supported.
 *
 * @author levi_h
 */
public class StringToEnumConverter implements Converter<String> {
	/**
	 * Creates a string to enum converter.
	 */
	public StringToEnumConverter() {}

	public boolean supportsSourceType(Class<? extends String> sourceType) {
		return true;
	}

	public <T> boolean supportsTargetType(Class<T> targetType) {
		return targetType.isEnum();
	}

	public <T> T convert(String value, Class<T> targetType) throws ConverterException {
		try {
			@SuppressWarnings("unchecked")
			Enum<?> target = Enum.valueOf((Class<Enum>) targetType, value.toUpperCase());

			return targetType.cast(target);
		} catch (IllegalArgumentException exception) {
			throw new ConverterException(exception, "can't convert '", value, "' to type ", targetType.getName());
		}
	}
}