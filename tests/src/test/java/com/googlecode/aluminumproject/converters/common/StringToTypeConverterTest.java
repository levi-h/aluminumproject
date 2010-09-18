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
import com.googlecode.aluminumproject.context.DefaultContext;
import com.googlecode.aluminumproject.converters.Converter;
import com.googlecode.aluminumproject.converters.ConverterException;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("all")
@Test(groups = {"core", "fast"})
public class StringToTypeConverterTest {
	private Converter<String> converter;

	private Context context;

	@BeforeMethod
	public void createConverterAndContext() {
		converter = new StringToTypeConverter();

		context = new DefaultContext();
	}

	public void classNamesShouldBeConvertible() {
		assert converter.convert("String", Type.class, context) == String.class;
		assert converter.convert(getClass().getName(), Type.class, context) == getClass();
	}

	public void primitiveClassNamesShouldBeConvertible() {
		assert converter.convert("boolean", Type.class, context) == Boolean.TYPE;
		assert converter.convert("byte", Type.class, context) == Byte.TYPE;
		assert converter.convert("char", Type.class, context) == Character.TYPE;
		assert converter.convert("short", Type.class, context) == Short.TYPE;
		assert converter.convert("int", Type.class, context) == Integer.TYPE;
		assert converter.convert("long", Type.class, context) == Long.TYPE;
		assert converter.convert("float", Type.class, context) == Float.TYPE;
		assert converter.convert("double", Type.class, context) == Double.TYPE;
	}

	public void parameterisedClassNamesShouldBeConvertible() {
		Object type = converter.convert("Iterable<String>", Type.class, context);
		assert type instanceof ParameterizedType;

		ParameterizedType parameterisedType = (ParameterizedType) type;
		assert parameterisedType.getRawType() == Iterable.class;

		Type[] actualTypeArguments = parameterisedType.getActualTypeArguments();
		assert actualTypeArguments.length == 1;
		assert actualTypeArguments[0] == String.class;
	}

	@Test(expectedExceptions = ConverterException.class)
	public void convertingNonexistentClassNameShouldCauseException() {
		converter.convert("java.lang.Stirng", Type.class, context);
	}
}