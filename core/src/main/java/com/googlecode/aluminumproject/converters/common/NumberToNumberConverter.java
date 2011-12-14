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

import com.googlecode.aluminumproject.annotations.Ignored;
import com.googlecode.aluminumproject.converters.ClassBasedConverter;
import com.googlecode.aluminumproject.converters.Converter;
import com.googlecode.aluminumproject.converters.ConverterException;
import com.googlecode.aluminumproject.utilities.ReflectionUtilities;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Converts from one kind of {@link Number number} to another. Both widening and narrowing conversions are supported.
 *
 * @author levi_h
 */
public class NumberToNumberConverter implements Converter<Number> {
	private Map<Class<?>, Converter<Number>> converters;

	/**
	 * Creates a number to number converter.
	 */
	public NumberToNumberConverter() {
		converters = new HashMap<Class<?>, Converter<Number>>();
		converters.put(Byte.TYPE, new NumberToByteConverter());
		converters.put(Short.TYPE, new NumberToShortConverter());
		converters.put(Integer.TYPE, new NumberToIntegerConverter());
		converters.put(Long.TYPE, new NumberToLongConverter());
		converters.put(Float.TYPE, new NumberToFloatConverter());
		converters.put(Double.TYPE, new NumberToDoubleConverter());

		for (Class<?> type: new HashSet<Class<?>>(converters.keySet())) {
			converters.put(ReflectionUtilities.wrapPrimitiveType(type), converters.get(type));
		}

		converters.put(BigInteger.class, new NumberToBigIntegerConverter());
		converters.put(BigDecimal.class, new NumberToBigDecimalConverter());
	}

	public boolean supportsSourceType(Class<? extends Number> sourceType) {
		return true;
	}

	public boolean supportsTargetType(Type targetType) {
		return converters.containsKey(targetType);
	}

	public Object convert(Number value, Type targetType) throws ConverterException {
		if (!supportsTargetType(targetType)) {
			throw new ConverterException(targetType, " is not a supported target type");
		}

		return converters.get(targetType).convert(value, targetType);
	}

	@Ignored
	private static class NumberToByteConverter extends ClassBasedConverter<Number, Byte> {
		@Override
		protected Byte convert(Number value) {
			return Byte.valueOf(value.byteValue());
		}
	}

	@Ignored
	private static class NumberToShortConverter extends ClassBasedConverter<Number, Short> {
		@Override
		protected Short convert(Number value) {
			return Short.valueOf(value.shortValue());
		}
	}

	@Ignored
	private static class NumberToIntegerConverter extends ClassBasedConverter<Number, Integer> {
		@Override
		protected Integer convert(Number value) {
			return Integer.valueOf(value.intValue());
		}
	}

	@Ignored
	private static class NumberToLongConverter extends ClassBasedConverter<Number, Long> {
		@Override
		protected Long convert(Number value) {
			return Long.valueOf(value.longValue());
		}
	}

	@Ignored
	private static class NumberToFloatConverter extends ClassBasedConverter<Number, Float> {
		@Override
		protected Float convert(Number value) {
			return Float.valueOf(value.floatValue());
		}
	}

	@Ignored
	private static class NumberToDoubleConverter extends ClassBasedConverter<Number, Double> {
		@Override
		protected Double convert(Number value) {
			return Double.valueOf(value.doubleValue());
		}
	}

	@Ignored
	private static class NumberToBigIntegerConverter extends ClassBasedConverter<Number, BigInteger> {
		@Override
		protected BigInteger convert(Number value) {
			return BigInteger.valueOf(value.longValue());
		}
	}

	@Ignored
	private static class NumberToBigDecimalConverter extends ClassBasedConverter<Number, BigDecimal> {
		@Override
		protected BigDecimal convert(Number value) {
			return BigDecimal.valueOf(value.doubleValue());
		}
	}
}