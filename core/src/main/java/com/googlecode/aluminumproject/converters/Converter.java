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

/**
 * Able to convert values of a source type to one or more target types.
 * <p>
 * Converters should support neither {@code null} source objects nor primitive source or target types and are encouraged
 * to be case-insensitive when converting strings.
 *
 * @author levi_h
 * @param <S> the type of the source values
 */
public interface Converter<S> {
	/**
	 * Checks whether this converter supports converting values from a certain source type. Normally, classes express
	 * this by implementing {@link Converter} with the supported type (so most implementations would return {@code
	 * true} from this method), but there could be specialised cases in which not all subtypes of that type are
	 * supported by the converter.
	 *
	 * @param sourceType the source type to check
	 * @return {@code true} if this converter is able to convert values from the given source type, {@code false}
	 *         otherwise
	 */
	boolean supportsSourceType(Class<? extends S> sourceType);

	/**
	 * Checks whether this converter supports converting values into a certain target type.
	 *
	 * @param <T> the target type
	 * @param targetType the target type to check
	 * @return {@code true} if this converter is able to convert source values into the given target type, {@code false}
	 *         otherwise
	 */
	<T> boolean supportsTargetType(Class<T> targetType);

	/**
	 * Converts a value of the source type into a supported target type.
	 *
	 * @param <T> the target type
	 * @param value the value to convert
	 * @param targetType the type to convert the value into
	 * @return the converted value
	 * @throws ConverterException when the given target type is not supported or when something goes wrong while
	 *                            converting the value
	 */
	<T> T convert(S value, Class<T> targetType) throws ConverterException;
}