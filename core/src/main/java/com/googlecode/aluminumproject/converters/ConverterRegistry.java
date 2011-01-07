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
package com.googlecode.aluminumproject.converters;

import com.googlecode.aluminumproject.annotations.Injected;
import com.googlecode.aluminumproject.configuration.ConfigurationElement;
import com.googlecode.aluminumproject.context.Context;

import java.lang.reflect.Type;

/**
 * A registry for {@link Converter converters}.
 *
 * @author levi_h
 */
public interface ConverterRegistry extends ConfigurationElement {
	/**
	 * Registers a converter and injects any of its fields that are annotated with {@link Injected &#64;Injected} with
	 * appropriate values.
	 *
	 * @param converter the converter to register
	 * @throws ConverterException when the converter's fields can't be injected
	 */
	void registerConverter(Converter<?> converter) throws ConverterException;

	/**
	 * Finds a converter that can convert values from a certain source type to a particular target type.
	 *
	 * @param <S> the source type of the converter
	 * @param sourceType the type of the source values
	 * @param targetType the type of the converted values
	 * @return a converter that can convert values from the given source type into the given target type
	 * @throws ConverterException when no converter can be found for the given types
	 */
	<S> Converter<? super S> getConverter(Class<S> sourceType, Type targetType) throws ConverterException;

	/**
	 * Convenience method that converts a value using one of the registered converters. It is required to handle {@code
	 * null} values.
	 *
	 * @param <S> the source type
	 * @param value the value to convert (may be {@code null})
	 * @param targetType the type that the value should be converted into
	 * @param context the context in which the conversion takes place
	 * @return the converted value
	 * @throws ConverterException when no converter can be found for the source type or when something goes wrong while
	 *                            converting the value
	 *
	 */
	<S> Object convert(S value, Type targetType, Context context) throws ConverterException;
}