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

import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.configuration.ConfigurationParameters;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.converters.Converter;
import com.googlecode.aluminumproject.converters.ConverterRegistry;

import java.lang.reflect.Type;

/**
 * A converter registry that can be used in tests.
 *
 * @author levi_h
 */
public class TestConverterRegistry implements ConverterRegistry {
	private Configuration configuration;

	/**
	 * Creates a test converter registry.
	 */
	public TestConverterRegistry() {}

	public void initialise(Configuration configuration, ConfigurationParameters parameters) {
		this.configuration = configuration;
	}

	/**
	 * Returns the configuration that this converter registry was initialised with.
	 *
	 * @return this converter registry's configuration
	 */
	public Configuration getConfiguration() {
		return configuration;
	}

	public void registerConverter(Converter<?> converter) {}

	public <S> Converter<? super S> getConverter(Class<S> sourceType, Type targetType) {
		return null;
	}

	public <S> Object convert(S value, Type targetType, Context context) {
		return null;
	}
}