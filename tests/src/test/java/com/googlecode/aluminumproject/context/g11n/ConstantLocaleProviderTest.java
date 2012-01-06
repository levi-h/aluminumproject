/*
 * Copyright 2010-2012 Levi Hoogenberg
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

import com.googlecode.aluminumproject.context.DefaultContext;

import java.util.Locale;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
@Test(groups = {"libraries", "libraries-g11n", "fast"})
public class ConstantLocaleProviderTest {
	private LocaleProvider localeProvider;

	@BeforeMethod
	public void createLocaleProvider() {
		localeProvider = new ConstantLocaleProvider(Locale.ENGLISH);
	}

	public void localeProviderShouldProvideLocaleItWasConstructedWith() {
		Locale locale = localeProvider.provide(new DefaultContext());
		assert locale != null;
		assert locale.equals(Locale.ENGLISH);
	}
}