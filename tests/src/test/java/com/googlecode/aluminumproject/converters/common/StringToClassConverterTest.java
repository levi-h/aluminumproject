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

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("all")
@Test(groups = {"core", "fast"})
public class StringToClassConverterTest {
	private Converter<String> converter;

	@BeforeMethod
	public void createConverter() {
		converter = new StringToClassConverter();
	}

	public void classNamesShouldBeConvertible() {
		assert converter.convert("java.lang.String", Class.class) == String.class;
		assert converter.convert(getClass().getName(), Class.class) == getClass();
	}

	public void primitiveClassNamesShouldBeConvertible() {
		assert converter.convert("boolean", Class.class) == Boolean.TYPE;
		assert converter.convert("byte", Class.class) == Byte.TYPE;
		assert converter.convert("char", Class.class) == Character.TYPE;
		assert converter.convert("short", Class.class) == Short.TYPE;
		assert converter.convert("int", Class.class) == Integer.TYPE;
		assert converter.convert("long", Class.class) == Long.TYPE;
		assert converter.convert("float", Class.class) == Float.TYPE;
		assert converter.convert("double", Class.class) == Double.TYPE;
	}

	public void classNamesOfArraysShouldBeConvertible() {
		assert converter.convert("[I", Class.class) == int[].class;
		assert converter.convert("[[Z", Class.class) == boolean[][].class;
		assert converter.convert("[Ljava.lang.String;", Class.class) == String[].class;
		assert converter.convert("[[Ljava.lang.Object;", Class.class) == Object[][].class;
	}

	@Test(expectedExceptions = ConverterException.class)
	public void convertingNonexistentClassNameShouldCauseException() {
		converter.convert("java.lang.Stirng", Class.class);
	}
}