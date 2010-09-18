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
package com.googlecode.aluminumproject.converters.io;

import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.converters.ClassBasedConverter;
import com.googlecode.aluminumproject.converters.ConverterException;

import java.io.File;

/**
 * Converts strings to files by using the {@link File#File(String) File constructor that accepts a path}.
 *
 * @author levi_h
 */
public class StringToFileConverter extends ClassBasedConverter<String, File> {
	/**
	 * Creates a string to file converter.
	 */
	public StringToFileConverter() {}

	@Override
	protected File convert(String value, Context context) throws ConverterException {
		File file = new File(value);

		if (!file.exists()) {
			throw new ConverterException("file '", value, "' does not exist");
		}

		return file;
	}
}