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

import java.lang.reflect.Type;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("all")
@Test(groups = {"core", "fast"})
public class StringToTypeConverterTest {
	private Converter<String> converter;

	@BeforeMethod
	public void createConverter() {
		converter = new StringToTypeConverter();
	}

	public void classNamesShouldBeConvertible() {
		assert converter.convert("java.lang.String", Type.class) == String.class;
		assert converter.convert(getClass().getName(), Type.class) == getClass();
	}

	public void primitiveClassNamesShouldBeConvertible() {
		assert converter.convert("boolean", Type.class) == Boolean.TYPE;
		assert converter.convert("byte", Type.class) == Byte.TYPE;
		assert converter.convert("char", Type.class) == Character.TYPE;
		assert converter.convert("short", Type.class) == Short.TYPE;
		assert converter.convert("int", Type.class) == Integer.TYPE;
		assert converter.convert("long", Type.class) == Long.TYPE;
		assert converter.convert("float", Type.class) == Float.TYPE;
		assert converter.convert("double", Type.class) == Double.TYPE;
	}

	public void classNamesOfArraysShouldBeConvertible() {
		assert converter.convert("[I", Type.class) == int[].class;
		assert converter.convert("[[Z", Type.class) == boolean[][].class;
		assert converter.convert("[Ljava.lang.String;", Type.class) == String[].class;
		assert converter.convert("[[Ljava.lang.Object;", Type.class) == Object[][].class;
	}

	@Test(expectedExceptions = ConverterException.class)
	public void convertingNonexistentClassNameShouldCauseException() {
		converter.convert("java.lang.Stirng", Type.class);
	}
}