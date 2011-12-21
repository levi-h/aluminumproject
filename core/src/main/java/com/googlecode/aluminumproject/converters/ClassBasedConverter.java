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
package com.googlecode.aluminumproject.converters;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.utilities.GenericsUtilities;
import com.googlecode.aluminumproject.utilities.ReflectionUtilities;
import com.googlecode.aluminumproject.utilities.Utilities;

import java.lang.reflect.Type;

/**
 * Abstract superclass for all converters that are able to convert from a single source type into a single target type,
 * both of which are {@link Class classes}.
 *
 * @param <S> the type of the values to convert
 * @param <T> the type of the converted values
 * @author levi_h
 */
public abstract class ClassBasedConverter<S, T> implements Converter<S> {
	private Class<T> targetType;

	/**
	 * Creates a class-based converter.
	 */
	protected ClassBasedConverter() {
		targetType = Utilities.typed(GenericsUtilities.getTypeArgument(getClass(), ClassBasedConverter.class, 1));
	}

	public boolean supportsSourceType(Class<? extends S> sourceType) {
		return true;
	}

	public final boolean supportsTargetType(Type targetType) {
		return (targetType instanceof Class) &&
			ReflectionUtilities.wrapPrimitiveType((Class<?>) targetType).isAssignableFrom(this.targetType);
	}

	public Object convert(S value, Type targetType) throws AluminumException {
		if (supportsTargetType(targetType)) {
			return convert(value);
		} else {
			throw new AluminumException("expected ", this.targetType.getName()," as target type, not ", targetType);
		}
	}

	/**
	 * Template method that will be invoked by the {@link #convert(Object, Type) convert method}.
	 *
	 * @param value the value to convert
	 * @return the converted value
	 * @throws AluminumException when the value can't be converted to the target type
	 */
	protected abstract T convert(S value) throws AluminumException;
}