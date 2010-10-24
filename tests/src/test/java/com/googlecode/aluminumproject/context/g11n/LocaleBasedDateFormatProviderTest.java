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
package com.googlecode.aluminumproject.context.g11n;

import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.DefaultContext;
import com.googlecode.aluminumproject.libraries.g11n.model.DateFormatType;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("all")
@Test(groups = {"libraries", "libraries-g11n", "fast"})
public class LocaleBasedDateFormatProviderTest {
	private DateFormatProvider dateFormatProvider;

	private Context context;

	private Date date;

	@BeforeMethod
	public void createContextAndDate() {
		ConstantLocaleProvider localeProvider = new ConstantLocaleProvider(new Locale("es"));
		dateFormatProvider = new LocaleBasedDateFormatProvider("yyyyMMdd HHmm");

		GlobalisationContext globalisationContext =
			new GlobalisationContext(localeProvider, null, dateFormatProvider);

		context = new DefaultContext();
		context.addImplicitObject(GlobalisationContext.GLOBALISATION_CONTEXT_IMPLICIT_OBJECT, globalisationContext);

		date = new Date(2713500000L);
	}

	public void shortDateTypeShouldResultInShortDateProvider() {
		DateFormat dateFormat = dateFormatProvider.provide(DateFormatType.SHORT_DATE, context);
		assert dateFormat != null;
		assert dateFormat.format(date).equals("1/02/70");
	}

	public void shortTimeTypeShouldResultInShortTimeProvider() {
		DateFormat dateFormat = dateFormatProvider.provide(DateFormatType.SHORT_TIME, context);
		assert dateFormat != null;
		assert dateFormat.format(date).equals("9:45");
	}

	public void shortDateAndTimeTypeShouldResultInShortDateAndTimeProvider() {
		DateFormat dateFormat = dateFormatProvider.provide(DateFormatType.SHORT_DATE_AND_TIME, context);
		assert dateFormat != null;
		assert dateFormat.format(date).equals("1/02/70 9:45");
	}

	public void mediumDateTypeShouldResultInMediumDateProvider() {
		DateFormat dateFormat = dateFormatProvider.provide(DateFormatType.MEDIUM_DATE, context);
		assert dateFormat != null;
		assert dateFormat.format(date).equals("01-feb-1970");
	}

	public void mediumTimeTypeShouldResultInMediumTimeProvider() {
		DateFormat dateFormat = dateFormatProvider.provide(DateFormatType.MEDIUM_TIME, context);
		assert dateFormat != null;
		assert dateFormat.format(date).equals("9:45:00");
	}

	public void mediumDateAndTimeTypeShouldResultInMediumDateAndTimeProvider() {
		DateFormat dateFormat = dateFormatProvider.provide(DateFormatType.MEDIUM_DATE_AND_TIME, context);
		assert dateFormat != null;
		assert dateFormat.format(date).equals("01-feb-1970 9:45:00");
	}

	public void longDateTypeShouldResultInLongDateProvider() {
		DateFormat dateFormat = dateFormatProvider.provide(DateFormatType.LONG_DATE, context);
		assert dateFormat != null;
		assert dateFormat.format(date).equals("1 de febrero de 1970");
	}

	public void longTimeTypeShouldResultInLongTimeProvider() {
		DateFormat dateFormat = dateFormatProvider.provide(DateFormatType.LONG_TIME, context);
		assert dateFormat != null;
		assert dateFormat.format(date).equals("9:45:00 GMT");
	}

	public void longDateAndTimeTypeShouldResultInLongDateAndTimeProvider() {
		DateFormat dateFormat = dateFormatProvider.provide(DateFormatType.LONG_DATE_AND_TIME, context);
		assert dateFormat != null;
		assert dateFormat.format(date).equals("1 de febrero de 1970 9:45:00 GMT");
	}

	public void customTypeShouldResultInDateProviderWithCustomPattern() {
		DateFormat dateFormat = dateFormatProvider.provide(DateFormatType.CUSTOM, context);
		assert dateFormat != null;
		assert dateFormat.format(date).equals("19700201 0945");
	}
}