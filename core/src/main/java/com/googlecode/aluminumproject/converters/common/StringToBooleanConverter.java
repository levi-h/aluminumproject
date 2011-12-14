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

import com.googlecode.aluminumproject.converters.ClassBasedConverter;

/**
 * Converts {@link String strings} into {@link Boolean booleans} by using the {@link Boolean#parseBoolean(String)
 * parseBoolean method}.
 *
 * @author levi_h
 */
public class StringToBooleanConverter extends ClassBasedConverter<String, Boolean> {
	/**
	 * Constructs a string to boolean converter.
	 */
	public StringToBooleanConverter() {}

	@Override
	protected Boolean convert(String value) {
		return Boolean.parseBoolean(value);
	}
}