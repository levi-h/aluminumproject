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
package com.googlecode.aluminumproject.converters.test;

import com.googlecode.aluminumproject.annotations.Injected;
import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.converters.ClassBasedConverter;
import com.googlecode.aluminumproject.converters.ConverterRegistry;

/**
 * A converter that can be used in tests.
 *
 * @author levi_h
 */
public class TestConverter extends ClassBasedConverter<Float, CharSequence> {
	private @Injected Configuration configuration;

	/**
	 * Creates a test converter.
	 */
	public TestConverter() {}

	/**
	 * Returns the current configuration, which is injected by a {@link ConverterRegistry converter registry}.
	 *
	 * @return the injected configuration
	 */
	public Configuration getConfiguration() {
		return configuration;
	}

	@Override
	protected CharSequence convert(Float value) {
		return value.toString();
	}
}