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

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.converters.Converter;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("all")
@Test(groups = {"core", "fast"})
public class NumberToNumberConverterTest {
	private Converter<Number> converter;

	@BeforeMethod
	public void createConverter() {
		converter = new NumberToNumberConverter();
	}

	public void converterShouldSupportPrimitiveTypes() {
		assert converter.supportsTargetType(Byte.TYPE);
		assert converter.supportsTargetType(Short.TYPE);
		assert converter.supportsTargetType(Integer.TYPE);
		assert converter.supportsTargetType(Long.TYPE);
		assert converter.supportsTargetType(Float.TYPE);
		assert converter.supportsTargetType(Double.TYPE);
	}

	public void converterShouldSupportNonPrimitiveTypes() {
		assert converter.supportsTargetType(Byte.class);
		assert converter.supportsTargetType(Short.class);
		assert converter.supportsTargetType(Integer.class);
		assert converter.supportsTargetType(Long.class);
		assert converter.supportsTargetType(Float.class);
		assert converter.supportsTargetType(Double.class);
		assert converter.supportsTargetType(BigInteger.class);
		assert converter.supportsTargetType(BigDecimal.class);
	}

	public void wideningConversionsShouldBeSupported() {
		Object convertedValue;

		convertedValue = converter.convert(3, Byte.class);
		assert convertedValue instanceof Byte;
		assert convertedValue.equals(Byte.valueOf((byte) 3));

		convertedValue = converter.convert(21, Short.class);
		assert convertedValue instanceof Short;
		assert convertedValue.equals(Short.valueOf((short) 21));

		convertedValue = converter.convert(987L, Integer.class);
		assert convertedValue instanceof Integer;
		assert convertedValue.equals(Integer.valueOf(987));

		convertedValue = converter.convert(10, Long.class);
		assert convertedValue instanceof Long;
		assert convertedValue.equals(Long.valueOf(10L));

		convertedValue = converter.convert(0.8, Float.class);
		assert convertedValue instanceof Float;
		assert convertedValue.equals(Float.valueOf(0.8F));

		convertedValue = converter.convert(1.5F, Double.class);
		assert convertedValue instanceof Double;
		assert convertedValue.equals(Double.valueOf(1.5));

		convertedValue = converter.convert(12345, BigInteger.class);
		assert convertedValue instanceof BigInteger;
		assert convertedValue.equals(BigInteger.valueOf(12345L));

		convertedValue = converter.convert(4.5F, BigDecimal.class);
		assert convertedValue instanceof BigDecimal;
		assert convertedValue.equals(BigDecimal.valueOf(4.5));
	}

	public void narrowingConversionsShouldBeSupported() {
		Object convertedValue = converter.convert(128, Byte.class);
		assert convertedValue instanceof Byte;
		assert convertedValue.equals(Byte.valueOf(Byte.MIN_VALUE));
	}

	@Test(expectedExceptions = AluminumException.class)
	public void tryingToConvertIntoUnsupportedTargetTypeShouldCauseException() {
		converter.convert(10, String.class);
	}
}