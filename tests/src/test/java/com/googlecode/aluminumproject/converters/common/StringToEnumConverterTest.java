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

import com.googlecode.aluminumproject.converters.Converter;
import com.googlecode.aluminumproject.converters.ConverterException;

import java.lang.annotation.ElementType;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("all")
@Test(groups = {"core", "fast"})
public class StringToEnumConverterTest {
	private Converter<String> converter;

	@BeforeMethod
	public void createConverter() {
		converter = new StringToEnumConverter();
	}

	public void converterShouldSupportEnumTargetTypes() {
		assert converter.supportsTargetType(ElementType.class);
		assert converter.supportsTargetType(Thread.State.class);
	}

	public void converterShouldNotSupportNonEnumTargetTypes() {
		assert !converter.supportsTargetType(Enum.class);
		assert !converter.supportsTargetType(String.class);
	}

	public void literalNamesShouldBeConverted() {
		assert converter.convert("FIELD", ElementType.class) == ElementType.FIELD;
		assert converter.convert("NEW", Thread.State.class) == Thread.State.NEW;
	}

	@Test(dependsOnMethods = "literalNamesShouldBeConverted")
	public void conversionShouldBeCaseInsensitive() {
		assert converter.convert("field", ElementType.class) == ElementType.FIELD;
		assert converter.convert("new", Thread.State.class) == Thread.State.NEW;
	}

	@Test(dependsOnMethods = "conversionShouldBeCaseInsensitive")
	public void underscoresShouldBeReplaceableWithSpaces() {
		assert converter.convert("annotation type", ElementType.class) == ElementType.ANNOTATION_TYPE;
	}

	@Test(dependsOnMethods = "conversionShouldBeCaseInsensitive")
	public void conversionShouldPreferExactMatches() {
		assert converter.convert("A", Letter.class) == Letter.A;
		assert converter.convert("b", Letter.class) == Letter.b;
	}

	private static enum Letter {
		A, a, B, b;
	}

	@Test(expectedExceptions = ConverterException.class)
	public void convertingNonexistingNameShouldCauseException() {
		converter.convert("INTERFACE", ElementType.class);
	}

	@Test(expectedExceptions = ConverterException.class)
	public void supplyingUnsupportedTargetTypeShouldCauseException() {
		converter.convert("NAME", Enum.class);
	}
}