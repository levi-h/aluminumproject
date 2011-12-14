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
package com.googlecode.aluminumproject.converters.io;

import com.googlecode.aluminumproject.converters.Converter;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("all")
@Test(groups = {"libraries", "libraries-io", "fast"})
public class ByteArrayToStringConverterTest {
	private Converter<byte[]> converter;

	@BeforeMethod
	public void createConverter() {
		converter = new ByteArrayToStringConverter();
	}

	public void emptyByteArrayShouldBeConvertedToEmptyString() {
		Object convertedValue = converter.convert(new byte[0], String.class);
		assert convertedValue instanceof String;
		assert ((String) convertedValue).equals("");
	}

	public void byteArrayShouldBeConvertedToCorrespondingString() {
		Object convertedValue = converter.convert("text".getBytes(), String.class);
		assert convertedValue instanceof String;
		assert convertedValue.equals("text");
	}
}