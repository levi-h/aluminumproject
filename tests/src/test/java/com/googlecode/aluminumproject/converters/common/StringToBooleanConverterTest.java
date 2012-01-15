/*
 * Copyright 2009-2012 Aluminum project
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

@SuppressWarnings("javadoc")
@Test(groups = {"core", "fast"})
public class StringToBooleanConverterTest {
	private Converter<String> converter;

	@BeforeMethod
	public void createConverter() {
		converter = new StringToBooleanConverter();
	}

	public void literalTrueShouldResultInTrue() {
		Object convertedValue = converter.convert("true", Boolean.TYPE);
		assert convertedValue instanceof Boolean;
		assert ((Boolean) convertedValue).booleanValue();
	}

	public void literalFalseShouldResultInFalse() {
		Object convertedValue = converter.convert("false", Boolean.TYPE);
		assert convertedValue instanceof Boolean;
		assert !((Boolean) convertedValue).booleanValue();
	}

	public void nonLiteralShouldResultInFalse() {
		Object convertedValue = converter.convert("yes", Boolean.TYPE);
		assert convertedValue instanceof Boolean;
		assert !((Boolean) convertedValue).booleanValue();
	}

	@Test(dependsOnMethods = {"literalTrueShouldResultInTrue", "literalFalseShouldResultInFalse"})
	public void conversionShouldBeCaseInsensitive() {
		Object convertedValue;

		convertedValue = converter.convert("TRUE", Boolean.TYPE);
		assert convertedValue instanceof Boolean;
		assert ((Boolean) convertedValue).booleanValue();

		convertedValue = converter.convert("False", Boolean.TYPE);
		assert convertedValue instanceof Boolean;
		assert !((Boolean) convertedValue).booleanValue();
	}
}