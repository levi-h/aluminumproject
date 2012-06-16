/*
 * Copyright 2012 Aluminum project
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
package com.googlecode.aluminumproject.converters.g11n;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.converters.ClassBasedConverter;
import com.googlecode.aluminumproject.utilities.GlobalisationUtilities;

import java.util.TimeZone;

/**
 * Converts strings to time zones using the {@link GlobalisationUtilities#convertTimeZone(String) utility method}.
 */
public class StringToTimeZoneConverter extends ClassBasedConverter<String, TimeZone> {
	/**
	 * Creates a string to time zone converter.
	 */
	public StringToTimeZoneConverter() {}

	@Override
	protected TimeZone convert(String value) throws AluminumException {
		return GlobalisationUtilities.convertTimeZone(value);
	}
}