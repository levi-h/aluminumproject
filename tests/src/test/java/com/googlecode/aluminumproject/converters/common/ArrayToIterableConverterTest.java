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

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.converters.Converter;

import java.util.Iterator;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
@Test(groups = {"core", "fast"})
public class ArrayToIterableConverterTest {
	private Converter<Object> converter;

	@BeforeMethod
	public void createConverter() {
		converter = new ArrayToIterableConverter();
	}

	public void converterShouldBeAbleToHandleArrays() {
		assert converter.supportsSourceType(Object[].class);
	}

	public void converterShouldBeAbleToHandlePrimitiveArrays() {
		assert converter.supportsSourceType(byte[].class);
	}

	public void converterShouldNotBeAbleToHandleNonArrays() {
		assert !converter.supportsSourceType(Object.class);
	}

	public void arraysShouldBeConvertible() {
		Object iterable = converter.convert(new String[] {"a", "b"}, Iterable.class);
		assert iterable instanceof Iterable;

		Iterator<?> iterator = ((Iterable<?>) iterable).iterator();
		assert iterator != null;

		assert iterator.hasNext();

		Object firstElement = iterator.next();
		assert firstElement != null;
		assert firstElement.equals("a");

		assert iterator.hasNext();

		Object secondElement = iterator.next();
		assert secondElement != null;
		assert secondElement.equals("b");

		assert !iterator.hasNext();
	}

	public void primitiveArraysShouldBeConvertible() {
		Object convertedValue = converter.convert(new int[] {1, 0}, Iterable.class);
		assert convertedValue instanceof Iterable;

		Iterator<?> iterator = ((Iterable<?>) convertedValue).iterator();
		assert iterator != null;

		assert iterator.hasNext();

		Object firstElement = iterator.next();
		assert firstElement instanceof Integer;
		assert ((Integer) firstElement).intValue() == 1;

		assert iterator.hasNext();

		Object secondElement = iterator.next();
		assert secondElement instanceof Integer;
		assert ((Integer) secondElement).intValue() == 0;

		assert !iterator.hasNext();
	}

	@Test(expectedExceptions = AluminumException.class)
	public void tryingToConvertNonArrayShouldCauseException() {
		converter.convert("1, 2, 3", Iterable.class);
	}
}