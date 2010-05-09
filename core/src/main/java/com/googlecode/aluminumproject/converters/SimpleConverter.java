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

import com.googlecode.aluminumproject.utilities.GenericsUtilities;
import com.googlecode.aluminumproject.utilities.ReflectionUtilities;
import com.googlecode.aluminumproject.utilities.Utilities;

/**
 * Abstract superclass for all converters that have both a single source type and a single target type.
 *
 * @param <S> the type of the values to convert
 * @param <T> the type of the converted values
 * @author levi_h
 */
public abstract class SimpleConverter<S, T> implements Converter<S> {
	private Class<T> targetType;

	/**
	 * Creates a simple converter.
	 */
	protected SimpleConverter() {
		targetType = Utilities.typed(GenericsUtilities.getTypeArgument(getClass(), SimpleConverter.class, 1));
	}

	public boolean supportsSourceType(Class<? extends S> sourceType) {
		return true;
	}

	public final <U> boolean supportsTargetType(Class<U> targetType) {
		return ReflectionUtilities.wrapPrimitiveType(targetType).isAssignableFrom(this.targetType);
	}

	public final <U> U convert(S value, Class<U> targetType) throws ConverterException {
		if (supportsTargetType(targetType)) {
			// not using targetType.cast here, since that fails when targetType is a primitive type

			return Utilities.<U>typed(convert(value));
		} else {
			throw new ConverterException("expected ", this.targetType.getName()," as target type, ",
				"not ", targetType.getName());
		}
	}

	/**
	 * Template method that will be invoked by the {@link #convert(Object, Class) convert method}.
	 *
	 * @param value the value to convert
	 * @return the converted value
	 * @throws ConverterException when the value can't be converted to the target type
	 */
	protected abstract T convert(S value) throws ConverterException;
}