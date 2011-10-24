/*
 * Copyright 2010 Levi Hoogenberg
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

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.googlecode.aluminumproject.configuration.ConfigurationParameters;
import com.googlecode.aluminumproject.configuration.TestConfiguration;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.DefaultContext;
import com.googlecode.aluminumproject.converters.ds.StringToMapConverter;
import com.googlecode.aluminumproject.utilities.GenericsUtilities;
import com.googlecode.aluminumproject.utilities.Injector;
import com.googlecode.aluminumproject.utilities.Utilities;

@SuppressWarnings("javadoc")
@Test(groups = {"libraries", "libraries-ds", "fast"})
public class StringToMapConverterTest {
	private Converter<String> converter;

	private Context context;

	@BeforeMethod
	public void createConverterAndContext() {
		TestConfiguration configuration = new TestConfiguration(new ConfigurationParameters());
		configuration.setConverterRegistry(new DefaultConverterRegistry());
		configuration.getConverterRegistry().initialise(configuration);

		converter = new StringToMapConverter();

		Injector injector = new Injector();
		injector.addValueProvider(new Injector.ClassBasedValueProvider(configuration));
		injector.inject(converter);

		context = new DefaultContext();
	}

	public void untypedMapShouldBeSupportedAsTargetType() {
		assert converter.supportsTargetType(Map.class);
	}

	public void typedMapShouldBeSupportedAsTargetType() {
		assert converter.supportsTargetType(GenericsUtilities.getType("java.util.Map<String, Number>", "java.lang"));
	}

	@Test(expectedExceptions = ConverterException.class)
	public void tryingToConvertToUnsupportedTypeShouldCauseException() {
		converter.convert("[a: 1]", List.class, context);
	}

	@Test(dependsOnMethods = "untypedMapShouldBeSupportedAsTargetType")
	public void untypedEmptyMapShouldBeConvertible() {
		Object convertedValue = converter.convert("[]", Map.class, context);
		assert convertedValue instanceof Map;
		assert convertedValue.equals(Collections.emptyMap());
	}

	@Test(dependsOnMethods = "untypedMapShouldBeSupportedAsTargetType")
	public void untypedMapShouldBeConvertible() {
		Object convertedValue = converter.convert("[a: 1, b: 2]", Map.class, context);
		assert convertedValue instanceof Map;

		Map<?, ?> map = (Map<?, ?>) convertedValue;
		assert map.size() == 2;
		assert map.containsKey("a");
		assert map.get("a").equals("1");
		assert map.containsKey("b");
		assert map.get("b").equals("2");
	}

	@Test(dependsOnMethods = "untypedMapShouldBeSupportedAsTargetType")
	public void typedMapShouldBeConvertible() {
		Type targetType = GenericsUtilities.getType("java.util.Map<String, Integer>", "java.lang");

		Object convertedValue = converter.convert("[a: 1, b: 2]", targetType, context);
		assert convertedValue instanceof Map;

		Map<String, Integer> map = Utilities.typed(convertedValue);
		assert map.size() == 2;
		assert map.containsKey("a");
		assert map.get("a") == 1;
		assert map.containsKey("b");
		assert map.get("b") == 2;
	}

	@Test(dependsOnMethods = "untypedMapShouldBeSupportedAsTargetType", expectedExceptions = ConverterException.class)
	public void tryingToConvertMapWithIllegalFormatShouldCauseException() {
		converter.convert("a: 1, b: 2", Map.class, context);
	}
}