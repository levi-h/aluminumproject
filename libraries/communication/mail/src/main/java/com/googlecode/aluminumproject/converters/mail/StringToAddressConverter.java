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

import com.googlecode.aluminumproject.converters.ClassBasedConverter;
import com.googlecode.aluminumproject.converters.ConverterException;

import javax.mail.Address;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

/**
 * Converts strings to {@link Address addresses} by constructing new {@link InternetAddress internet addresses}.
 *
 * @author levi_h
 */
public class StringToAddressConverter extends ClassBasedConverter<String, Address> {
	/**
	 * Creates a string to address converter.
	 */
	public StringToAddressConverter() {}

	@Override
	protected Address convert(String value) throws ConverterException {
		try {
			return new InternetAddress(value, true);
		} catch (AddressException exception) {
			throw new ConverterException(exception, "can't create internet address");
		}
	}
}