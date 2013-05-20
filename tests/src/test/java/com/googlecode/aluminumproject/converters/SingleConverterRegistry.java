/*
 * Copyright 2013 Aluminum project
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
package com.googlecode.aluminumproject.converters;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.utilities.Utilities;

import java.lang.reflect.Type;

/**
 * A converter registry with a single converter.
 *
 * @param <T> the source type of the converter
 */
public class SingleConverterRegistry<T> implements ConverterRegistry {
	private Class<T> sourceType;

	private Converter<T> converter;

	/**
	 * Creates a single converter registry.
	 *
	 * @param sourceType the source type that the converter supports
	 * @param converter the converter to use
	 */
	public SingleConverterRegistry(Class<T> sourceType, Converter<T> converter) {
		this.sourceType = sourceType;

		this.converter = converter;
	}

	public void initialise(Configuration configuration) {}

	public void registerConverter(Converter<?> converter) {
		throw new AluminumException("single converter registry supports only one converter");
	}

	public <S> Converter<? super S> getConverter(Class<S> sourceType, Type targetType) throws AluminumException {
		if (sourceType == this.sourceType) {
			return Utilities.typed(converter);
		} else {
			throw new AluminumException("single converter does not support source type ", sourceType.getName());
		}
	}

	public <S> Object convert(S value, Type targetType) throws AluminumException {
		return converter.convert(sourceType.cast(value), targetType);
	}

	public void disable() {}
}