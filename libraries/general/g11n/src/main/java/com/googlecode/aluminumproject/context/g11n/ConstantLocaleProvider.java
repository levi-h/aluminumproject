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
package com.googlecode.aluminumproject.context.g11n;

import com.googlecode.aluminumproject.context.Context;

import java.util.Locale;

/**
 * Provides a constant locale, regardless of context.
 */
public class ConstantLocaleProvider implements LocaleProvider {
	private Locale locale;

	/**
	 * Creates a constant locale provider.
	 *
	 * @param locale the locale that should be provided
	 */
	public ConstantLocaleProvider(Locale locale) {
		this.locale = locale;
	}

	public Locale provide(Context context) {
		return locale;
	}
}