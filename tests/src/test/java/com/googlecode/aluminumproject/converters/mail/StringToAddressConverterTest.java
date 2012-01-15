/*
 * Copyright 2011-2012 Aluminum project
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

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.converters.Converter;

import javax.mail.Address;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
@Test(groups = {"libraries", "libraries-mail", "fast"})
public class StringToAddressConverterTest {
	private Converter<String> converter;

	@BeforeMethod
	public void createConverter() {
		converter = new StringToAddressConverter();
	}

	public void simpleAddressShouldBeConvertible() {
		Address address = (Address) converter.convert("aluminum@aluminum", Address.class);
		assert address != null;

		String textualRepresentation = address.toString();
		assert textualRepresentation != null;
		assert textualRepresentation.equals("aluminum@aluminum");
	}

	public void addressWithPersonalNameShouldBeConvertible() {
		Address address =
			(Address) converter.convert("Aluminum <aluminum@aluminum>", Address.class);
		assert address != null;

		String textualRepresentation = address.toString();
		assert textualRepresentation != null;
		assert textualRepresentation.equals("Aluminum <aluminum@aluminum>");
	}

	@Test(expectedExceptions = AluminumException.class)
	public void tryingToConvertInvalidAddressShouldCauseException() {
		converter.convert("aluminum", Address.class);
	}
}