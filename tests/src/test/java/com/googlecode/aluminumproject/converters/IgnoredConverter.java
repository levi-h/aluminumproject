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

import com.googlecode.aluminumproject.annotations.Ignored;
import com.googlecode.aluminumproject.context.Context;

/**
 * A converter that will be ignored when adding registering all converters in a package.
 *
 * @author levi_h
 */
@Ignored
public class IgnoredConverter extends ClassBasedConverter<String, String> {
	/**
	 * Creates an ignored converter.
	 */
	public IgnoredConverter() {}

	@Override
	protected String convert(String value, Context context) {
		return new StringBuilder(value).reverse().toString();
	}
}