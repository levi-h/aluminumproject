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

import com.googlecode.aluminumproject.converters.Converter;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("all")
@Test(groups = {"core", "fast"})
public class ObjectToStringConverterTest {
	private Converter<Object> converter;

	@BeforeMethod
	public void createConverter() {
		converter = new ObjectToStringConverter();
	}

	public void objectsShouldBeConvertible() {
		Object convertedValue;

		convertedValue = converter.convert(true, String.class);
		assert convertedValue instanceof String;
		assert convertedValue.equals("true");

		convertedValue = converter.convert(3, String.class);
		assert convertedValue instanceof String;
		assert convertedValue.equals("3");
	}
}