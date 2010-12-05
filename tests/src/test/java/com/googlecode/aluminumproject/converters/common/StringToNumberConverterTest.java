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

import java.math.BigDecimal;
import java.math.BigInteger;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("all")
@Test(groups = {"core", "fast"})
public class StringToNumberConverterTest {
	private Converter<String> converter;

	private Context context;

	@BeforeMethod
	public void createConverter() {
		converter = new StringToNumberConverter();

		context = new DefaultContext();
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
		Object convertedValue;

		convertedValue = converter.convert("12", Byte.class, context);
		assert convertedValue instanceof Byte;
		assert convertedValue.equals(Byte.valueOf((byte) 12));

		convertedValue = converter.convert("1234", Short.class, context);
		assert convertedValue instanceof Short;
		assert convertedValue.equals(Short.valueOf((short) 1234));

		convertedValue = converter.convert("123456", Integer.class, context);
		assert convertedValue instanceof Integer;
		assert convertedValue.equals(Integer.valueOf(123456));

		convertedValue = converter.convert("12345678", Long.class, context);
		assert convertedValue instanceof Long;
		assert convertedValue.equals(Long.valueOf(12345678L));

		convertedValue = converter.convert("12.34", Float.class, context);
		assert convertedValue instanceof Float;
		assert convertedValue.equals(Float.valueOf(12.34F));

		convertedValue = converter.convert("1234.5678", Double.class, context);
		assert convertedValue instanceof Double;
		assert convertedValue.equals(Double.valueOf(1234.5678D));

		convertedValue = converter.convert("1234567890", BigInteger.class, context);
		assert convertedValue instanceof BigInteger;
		assert convertedValue.equals(BigInteger.valueOf(1234567890L));

		convertedValue = converter.convert("12345.6789", BigDecimal.class, context);
		assert convertedValue instanceof BigDecimal;
		assert convertedValue.equals(BigDecimal.valueOf(12345.6789D));
	}

	@Test(expectedExceptions = ConverterException.class)
	public void tryingToConvertInvalidStringShouldCauseException() {
		converter.convert("one", Integer.class, context);
	}

	public void mostSuitableTypeShouldBeSelectedForConversionsToBaseType() {
		assert converter.convert("-5", Number.class, context) instanceof Integer;
		assert converter.convert("5", Number.class, context) instanceof Integer;
		assert converter.convert("-5000000000", Number.class, context) instanceof Long;
		assert converter.convert("5000000000", Number.class, context) instanceof Long;
		assert converter.convert("-50000000000000000000", Number.class, context) instanceof BigInteger;
		assert converter.convert("50000000000000000000", Number.class, context) instanceof BigInteger;
		assert converter.convert("-0.5", Number.class, context) instanceof Double;
		assert converter.convert("0.5", Number.class, context) instanceof Double;
		assert converter.convert("-50000000000.00000000005", Number.class, context) instanceof BigDecimal;
		assert converter.convert("50000000000.00000000005", Number.class, context) instanceof BigDecimal;
	}
}