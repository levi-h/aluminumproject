/*
 * Copyright 2011 Levi Hoogenberg
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
package com.googlecode.aluminumproject.converters.mail;

import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.DefaultContext;
import com.googlecode.aluminumproject.converters.Converter;
import com.googlecode.aluminumproject.converters.ConverterException;

import javax.mail.Address;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("all")
@Test(groups = {"libraries", "libraries-mail", "fast"})
public class StringToAddressConverterTest {
	private Converter<String> converter;

	private Context context;

	@BeforeMethod
	public void createConverterAndContext() {
		converter = new StringToAddressConverter();

		context = new DefaultContext();
	}

	public void simpleAddressShouldBeConvertible() {
		Address address = (Address) converter.convert("aluminum@aluminum", Address.class, context);
		assert address != null;

		String textualRepresentation = address.toString();
		assert textualRepresentation != null;
		assert textualRepresentation.equals("aluminum@aluminum");
	}

	public void addressWithPersonalNameShouldBeConvertible() {
		Address address =
			(Address) converter.convert("Aluminum <aluminum@aluminum>", Address.class, context);
		assert address != null;

		String textualRepresentation = address.toString();
		assert textualRepresentation != null;
		assert textualRepresentation.equals("Aluminum <aluminum@aluminum>");
	}

	@Test(expectedExceptions = ConverterException.class)
	public void tryingToConvertInvalidAddressShouldCauseException() {
		converter.convert("aluminum", Address.class, context);
	}
}