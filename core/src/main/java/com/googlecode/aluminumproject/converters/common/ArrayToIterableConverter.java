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
import com.googlecode.aluminumproject.converters.Converter;
import com.googlecode.aluminumproject.converters.ConverterException;

import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Iterator;

/**
 * Converts arrays (of any type, including primitives) to {@link Iterable iterables}.
 *
 * @author levi_h
 */
public class ArrayToIterableConverter implements Converter<Object> {
	/**
	 * Creates an array to iterable converter.
	 */
	public ArrayToIterableConverter() {}

	public boolean supportsSourceType(Class<? extends Object> sourceType) {
		return sourceType.isArray();
	}

	public boolean supportsTargetType(Type targetType) {
		Class<?> targetClass = null;

		if (targetType instanceof Class) {
			targetClass = (Class<?>) targetType;
		} else if (targetType instanceof ParameterizedType) {
			targetClass = (Class<?>) ((ParameterizedType) targetType).getRawType();
		}

		return targetClass == Iterable.class;
	}

	public Object convert(Object value, Type targetType, Context context) throws ConverterException {
		if (!value.getClass().isArray()) {
			throw new ConverterException("can't convert ", value, ", since it is not an array");
		} else if (!supportsTargetType(targetType)) {
			throw new ConverterException("target type ", targetType, " is not supported");
		}

		return new ArrayIterable(value);
	}

	private static class ArrayIterable implements Iterable<Object> {
		private Object array;

		public ArrayIterable(Object array) {
			this.array = array;
		}

		public Iterator<Object> iterator() {
			return new ArrayIterator(array);
		}
	}

	private static class ArrayIterator implements Iterator<Object> {
		private Object array;

		private int index;
		private int length;

		public ArrayIterator(Object array) {
			this.array = array;

			index = 0;
			length = Array.getLength(array);
		}

		public boolean hasNext() {
			return index < length;
		}

		public Object next() {
			return Array.get(array, index++);
		}

		public void remove() {
			throw new UnsupportedOperationException("can't remove array elements");
		}
	}
}