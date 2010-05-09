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

import com.googlecode.aluminumproject.converters.ConverterException;
import com.googlecode.aluminumproject.converters.SimpleConverter;

import java.util.HashMap;
import java.util.Map;

/**
 * Converts class names into {@link Class classes}. It uses the {@link Thread#getContextClassLoader() context class
 * loader} to do so.
 *
 * @author levi_h
 */
public class StringToClassConverter extends SimpleConverter<String, Class<?>> {
	private Map<String, Class<?>> primitiveTypes;

	/**
	 * Creates a string to class converter.
	 */
	public StringToClassConverter() {
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
	protected Class<?> convert(String value) throws ConverterException {
		Class<?> convertedClass;

		if (primitiveTypes.containsKey(value)) {
			convertedClass = primitiveTypes.get(value);
		} else {
			try {
				convertedClass = Class.forName(value, false, Thread.currentThread().getContextClassLoader());
			} catch (ClassNotFoundException exception) {
				throw new ConverterException(exception, "can't convert class name to class");
			}
		}

		return convertedClass;
	}
}