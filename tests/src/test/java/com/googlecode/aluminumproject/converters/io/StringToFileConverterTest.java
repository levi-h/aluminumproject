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
import com.googlecode.aluminumproject.context.DefaultContext;
import com.googlecode.aluminumproject.converters.Converter;
import com.googlecode.aluminumproject.converters.ConverterException;

import java.io.File;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("all")
@Test(groups = {"libraries", "libraries-io", "slow"})
public class StringToFileConverterTest {
	private Converter<String> converter;

	private Context context;

	@BeforeMethod
	public void createConverterAndContext() {
		converter = new StringToFileConverter();

		context = new DefaultContext();
	}

	public void existingPathShouldResultInExistingFile() {
		Object convertedValue = converter.convert(System.getProperty("user.home"), File.class, context);
		assert convertedValue instanceof File;
		assert ((File) convertedValue).exists();
	}

	@Test(expectedExceptions = ConverterException.class)
	public void tryingToConvertUnknownPathShouldCauseException() {
		converter.convert("** nonexistent **", File.class, context);
	}
}