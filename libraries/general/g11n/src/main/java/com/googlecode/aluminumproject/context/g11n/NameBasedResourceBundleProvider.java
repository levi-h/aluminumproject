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

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.context.Context;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * A {@link ResourceBundleProvider resource bundle provider} that delegates to the {@link
 * ResourceBundle#getBundle(String, Locale) factory method} that accepts a base name and a {@link Locale locale}. The
 * base name that will be used needs to be supplied when creating a name-based resource bundle provider; the locale is
 * obtained by asking the current {@link LocaleProvider locale provider}.
 */
public class NameBasedResourceBundleProvider implements ResourceBundleProvider {
	private String baseName;

	/**
	 * Creates a name-based resource bundle provider.
	 *
	 * @param baseName the base name of the provided resource bundles
	 */
	public NameBasedResourceBundleProvider(String baseName) {
		this.baseName = baseName;
	}

	public ResourceBundle provide(Context context) throws AluminumException {
		Locale locale = GlobalisationContext.from(context).getLocaleProvider().provide(context);

		try {
			return ResourceBundle.getBundle(baseName, locale);
		} catch (MissingResourceException exception) {
			throw new AluminumException(exception, "can't provide resource bundle");
		}
	}
}