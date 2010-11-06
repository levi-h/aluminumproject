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
import com.googlecode.aluminumproject.libraries.g11n.model.NumberFormatType;

import java.text.NumberFormat;
import java.util.Locale;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("all")
@Test(groups = {"libraries", "libraries-g11n", "fast"})
public class LocaleBasedNumberFormatProviderTest {
	private NumberFormatProvider numberFormatProvider;

	private Context context;

	@BeforeMethod
	public void createNumberFormatProviderAndContext() {
		LocaleProvider localeProvider = new ConstantLocaleProvider(new Locale("de", "DE"));
		numberFormatProvider = new LocaleBasedNumberFormatProvider("#,###.##;#,###.##-");

		context = new DefaultContext();
		context.addImplicitObject(GlobalisationContext.GLOBALISATION_CONTEXT_IMPLICIT_OBJECT,
			new GlobalisationContext(localeProvider, null, null, numberFormatProvider));
	}

	public void numberTypeShouldResultInNumberProvider() {
		NumberFormat numberFormat = numberFormatProvider.provide(NumberFormatType.NUMBER, context);
		assert numberFormat != null;
		assert numberFormat.format(3.5).equals("3,5");
	}

	public void currencyTypeShouldResultInCurrencyProvider() {
		NumberFormat numberFormat = numberFormatProvider.provide(NumberFormatType.CURRENCY, context);
		assert numberFormat != null;
		assert numberFormat.format(1234.5).equals("1.234,50 \u20ac");
	}

	public void percentageTypeShouldResultInPercentageProvider() {
		NumberFormat numberFormat = numberFormatProvider.provide(NumberFormatType.PERCENTAGE, context);
		assert numberFormat != null;
		assert numberFormat.format(0.5).equals("50%"): numberFormat.format(5000L);
	}

	public void customTypeShouldResultInProviderWithCustomPattern() {
		NumberFormat numberFormat = numberFormatProvider.provide(NumberFormatType.CUSTOM, context);
		assert numberFormat != null;
		assert numberFormat.format(10L).equals("10");
		assert numberFormat.format(-9876.5).equals("9.876,5-");
	}
}