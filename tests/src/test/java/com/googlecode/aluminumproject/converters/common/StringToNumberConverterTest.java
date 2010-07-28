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

import java.math.BigDecimal;
import java.math.BigInteger;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("all")
@Test(groups = {"core", "fast"})
public class StringToNumberConverterTest {
	private Converter<String> converter;

	@BeforeMethod
	public void createConverter() {
		converter = new StringToNumberConverter();
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

	public void validStringsShouldBeConvertible() {
		Byte convertedByte = converter.convert("12", Byte.class);
		assert convertedByte != null;
		assert convertedByte.equals(Byte.valueOf((byte) 12));

		Short convertedShort = converter.convert("1234", Short.class);
		assert convertedShort != null;
		assert convertedShort.equals(Short.valueOf((short) 1234));

		Integer convertedInteger = converter.convert("123456", Integer.class);
		assert convertedInteger != null;
		assert convertedInteger.equals(Integer.valueOf(123456));

		Long convertedLong = converter.convert("12345678", Long.class);
		assert convertedLong != null;
		assert convertedLong.equals(Long.valueOf(12345678L));

		Float convertedFloat = converter.convert("12.34", Float.class);
		assert convertedFloat != null;
		assert convertedFloat.equals(Float.valueOf(12.34F));

		Double convertedDouble = converter.convert("1234.5678", Double.class);
		assert convertedDouble != null;
		assert convertedDouble.equals(Double.valueOf(1234.5678D));

		BigInteger convertedBigInteger = converter.convert("1234567890", BigInteger.class);
		assert convertedBigInteger != null;
		assert convertedBigInteger.equals(BigInteger.valueOf(1234567890L));

		BigDecimal convertedBigDecimal = converter.convert("12345.6789", BigDecimal.class);
		assert convertedBigDecimal != null;
		assert convertedBigDecimal.equals(BigDecimal.valueOf(12345.6789D));
	}

	@Test(expectedExceptions = ConverterException.class)
	public void tryingToConvertInvalidStringShouldCauseException() {
		converter.convert("one", Integer.class);
	}
}