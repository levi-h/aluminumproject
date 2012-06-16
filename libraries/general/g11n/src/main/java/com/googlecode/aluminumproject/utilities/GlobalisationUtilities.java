/*
 * Copyright 2010-2012 Aluminum project
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
package com.googlecode.aluminumproject.utilities;

import com.googlecode.aluminumproject.AluminumException;

import java.util.Locale;
import java.util.TimeZone;

/**
 * Provides utility methods related to globalisation.
 */
public class GlobalisationUtilities {
	private GlobalisationUtilities() {}

	/**
	 * Converts a {@link String string} into a {@link Locale locale}. The following formats are supported:
	 * <ul>
	 * <li><i>language</i>;
	 * <li><i>language</i>_<i>country</i>;
	 * <li><i>language</i>_<i>country</i>_<i>variant</i>.
	 * </ul>
	 * This means that {@code "nl"}, {@code "nl_NL"}, and {@code "nl_NL_Amsterdam"} can all be converted into a locale.
	 *
	 * @param value the value to convert
	 * @return the converted locale
	 * @throws AluminumException when the value can't be converted into a locale
	 */
	public static Locale convertLocale(String value) throws AluminumException {
		Locale locale;

		String[] parts = value.split("_");

		switch (parts.length) {
			case 1:
				locale = new Locale(parts[0]);

				break;

			case 2:
				locale = new Locale(parts[0], parts[1]);

				break;

			case 3:
				locale = new Locale(parts[0], parts[1], parts[2]);

				break;

			default:
				throw new AluminumException("found more than three locale parts; ",
					"only the language, country and variant should be given");
		}

		return locale;
	}

	/**
	 * Converts a {@link String string} into a {@link TimeZone time zone} using the {@link TimeZone#getTimeZone(String)
	 * getTimeZone method}, with one exception: a time zone ID that is not recognised does not result in a default time
	 * zone, but in an {@link AluminumException exception}.
	 *
	 * @param value the value to convert
	 * @return the converted time zone
	 * @throws AluminumException when the value can't be converted into a time zone
	 */
	public static TimeZone convertTimeZone(String value) throws AluminumException {
		String id = null;
		boolean foundExactMatch = false;

		String[] availableIds = TimeZone.getAvailableIDs();
		int i = 0;

		while (!foundExactMatch && (i < availableIds.length)) {
			String availableId = availableIds[i];

			if (value.equals(availableId)) {
				id = availableId;

				foundExactMatch = true;
			} else {
				if (value.equalsIgnoreCase(availableId)) {
					id = availableId;
				}

				i++;
			}
		}

		if (id == null) {
			throw new AluminumException("can't find time zone with ID '", value, "'");
		} else {
			return TimeZone.getTimeZone(id);
		}
	}
}