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

import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.converters.Converter;
import com.googlecode.aluminumproject.converters.ConverterException;

import java.lang.reflect.Type;

/**
 * Converts the name of an to {@link Enum enum} into an enum constant. All enum types are supported.
 * <p>
 * The conversion is case-insensitive. Using spaces instead of underscores is supported.
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

	public boolean supportsTargetType(Type targetType) {
		return (targetType instanceof Class) && ((Class<?>) targetType).isEnum();
	}

	public Object convert(String value, Type targetType, Context context) throws ConverterException {
		if (!supportsTargetType(targetType)) {
			throw new ConverterException("expected enum target type, not ", targetType);
		}

		Object match = null;
		Object exactMatch = null;

		@SuppressWarnings("unchecked")
		Class<Enum<?>> targetClass = (Class<Enum<?>>) targetType;

		for (Enum<?> enumConstant: targetClass.getEnumConstants()) {
			String name = enumConstant.name();

			if (name.equals(value)) {
				exactMatch = enumConstant;
			} else if ((match == null) && name.equalsIgnoreCase(value.replace(' ', '_'))) {
				match = enumConstant;
			}
		}

		Object convertedValue;

		if (exactMatch != null) {
			convertedValue = exactMatch;
		} else if (match != null) {
			convertedValue = match;
		} else {
			throw new ConverterException("enum ", targetClass.getName(), " does not contain a constant",
				" named '", value, "'");
		}

		return convertedValue;
	}
}