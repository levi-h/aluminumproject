/*
 * Copyright 2010-2011 Levi Hoogenberg
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
package com.googlecode.aluminumproject.context.g11n;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.context.Context;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

/**
 * A date format provider that provides date formats that are based on the current {@link Locale locale}. All date
 * formats have GMT timezones.
 *
 * @author levi_h
 */
public class LocaleBasedDateFormatProvider implements DateFormatProvider {
	private String customPattern;

	/**
	 * Creates a locale-based date format provider.
	 *
	 * @param customPattern the pattern to use for the custom date format type
	 */
	public LocaleBasedDateFormatProvider(String customPattern) {
		this.customPattern = customPattern;
	}

	public DateFormat provide(DateFormatType type, Context context) throws AluminumException {
		DateFormat dateFormat;

		Locale locale = GlobalisationContext.from(context).getLocaleProvider().provide(context);

		if (type == DateFormatType.SHORT_DATE) {
			dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, locale);
		} else if (type == DateFormatType.SHORT_TIME) {
			dateFormat = DateFormat.getTimeInstance(DateFormat.SHORT, locale);
		} else if (type == DateFormatType.SHORT_DATE_AND_TIME) {
			dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, locale);
		} else if (type == DateFormatType.MEDIUM_DATE) {
			dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, locale);
		} else if (type == DateFormatType.MEDIUM_TIME) {
			dateFormat = DateFormat.getTimeInstance(DateFormat.MEDIUM, locale);
		} else if (type == DateFormatType.MEDIUM_DATE_AND_TIME) {
			dateFormat = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM, locale);
		} else if (type == DateFormatType.LONG_DATE) {
			dateFormat = DateFormat.getDateInstance(DateFormat.LONG, locale);
		} else if (type == DateFormatType.LONG_TIME) {
			dateFormat = DateFormat.getTimeInstance(DateFormat.LONG, locale);
		} else if (type == DateFormatType.LONG_DATE_AND_TIME) {
			dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		} else if (type == DateFormatType.CUSTOM) {
			dateFormat = new SimpleDateFormat(customPattern, locale);
		} else {
			throw new AluminumException("unexpected date format type: ", type);
		}

		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

		return dateFormat;
	}
}