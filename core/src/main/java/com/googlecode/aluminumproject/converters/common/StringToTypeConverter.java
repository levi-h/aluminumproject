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

import com.googlecode.aluminumproject.converters.ClassBasedConverter;
import com.googlecode.aluminumproject.converters.ConverterException;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Converts {@link String strings} into {@link Type types}. The following types are supported:
 * <ul>
 * <li>{@link Class Classes} (using the {@link Thread#getContextClassLoader() context class loader}).
 * </ul>
 *
 * @author levi_h
 */
public class StringToTypeConverter extends ClassBasedConverter<String, Type> {
	private Map<String, Class<?>> primitiveTypes;

	/**
	 * Creates a string to type converter.
	 */
	public StringToTypeConverter() {
		primitiveTypes = new HashMap<String, Class<?>>();
		primitiveTypes.put("boolean", Boolean.TYPE);
		primitiveTypes.put("byte", Byte.TYPE);
		primitiveTypes.put("char", Character.TYPE);
		primitiveTypes.put("short", Short.TYPE);
		primitiveTypes.put("int", Integer.TYPE);
		primitiveTypes.put("long", Long.TYPE);
		primitiveTypes.put("float", Float.TYPE);
		primitiveTypes.put("double", Double.TYPE);
	}

	@Override
	protected Type convert(String value) throws ConverterException {
		Type convertedType = convertIntoClass(value);

		if (convertedType == null) {
			throw new ConverterException("can't convert '", value, "' into a type");
		}

		return convertedType;
	}

	private Class<?> convertIntoClass(String value) {
		Class<?> convertedClass;

		if (primitiveTypes.containsKey(value)) {
			convertedClass = primitiveTypes.get(value);
		} else {
			try {
				convertedClass = Class.forName(value, false, Thread.currentThread().getContextClassLoader());
			} catch (ClassNotFoundException exception) {
				convertedClass = null;
			}
		}

		return convertedClass;
	}
}