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

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.context.Context;

/**
 * Aggregates globalisation information providers for globalised library elements.
 * <p>
 * Normally, globalisation contexts will be made available by the {@link GlobalisationContextProvider globalisation
 * context provider}; in that case, the current globalisation context can be obtained using the {@link #from(Context)
 * designated method}.
 */
public class GlobalisationContext {
	private LocaleProvider localeProvider;
	private TimeZoneProvider timeZoneProvider;
	private ResourceBundleProvider resourceBundleProvider;
	private DateFormatProvider dateFormatProvider;
	private NumberFormatProvider numberFormatProvider;

	/**
	 * Creates a globalisation context.
	 *
	 * @param localeProvider the locale provider to use
	 * @param timeZoneProvider the time zone provider to use
	 * @param resourceBundleProvider the resource bundle provider to use
	 * @param dateFormatProvider the date format provider to use
	 * @param numberFormatProvider the number format provider to use
	 */
	public GlobalisationContext(LocaleProvider localeProvider, TimeZoneProvider timeZoneProvider,
			ResourceBundleProvider resourceBundleProvider, DateFormatProvider dateFormatProvider,
			NumberFormatProvider numberFormatProvider) {
		this.localeProvider = localeProvider;
		this.timeZoneProvider = timeZoneProvider;
		this.resourceBundleProvider = resourceBundleProvider;
		this.dateFormatProvider = dateFormatProvider;
		this.numberFormatProvider = numberFormatProvider;
	}

	/**
	 * Returns this globalisation context's locale provider.
	 *
	 * @return the locale provider of this globalisation context
	 */
	public LocaleProvider getLocaleProvider() {
		return localeProvider;
	}

	/**
	 * Replaces this globalisation context's locale provider.
	 *
	 * @param localeProvider the new locale provider of this globalisation context
	 */
	public void setLocaleProvider(LocaleProvider localeProvider) {
		this.localeProvider = localeProvider;
	}

	/**
	 * Returns this globalisation context's time zone provider.
	 *
	 * @return the time zone provider of this globalisation context
	 */
	public TimeZoneProvider getTimeZoneProvider() {
		return timeZoneProvider;
	}

	/**
	 * Replaces this globalisation context's time zone provider.
	 *
	 * @param timeZoneProvider the new time zone provider for this globalisation context
	 */
	public void setTimeZoneProvider(TimeZoneProvider timeZoneProvider) {
		this.timeZoneProvider = timeZoneProvider;
	}

	/**
	 * Returns this globalisation context's resource bundle provider.
	 *
	 * @return the resource bundle provider of this globalisation context
	 */
	public ResourceBundleProvider getResourceBundleProvider() {
		return resourceBundleProvider;
	}

	/**
	 * Replaces this globalisation context's resource bundle provider.
	 *
	 * @param resourceBundleProvider the new resource bundle provider for this globalisation context
	 */
	public void setResourceBundleProvider(ResourceBundleProvider resourceBundleProvider) {
		this.resourceBundleProvider = resourceBundleProvider;
	}

	/**
	 * Returns this globalisation context's date format provider.
	 *
	 * @return the date format provider of this globalisation context
	 */
	public DateFormatProvider getDateFormatProvider() {
		return dateFormatProvider;
	}

	/**
	 * Replaces this globalisation context's date format provider.
	 *
	 * @param dateFormatProvider the new date format provider for this globalisation context
	 */
	public void setDateFormatProvider(DateFormatProvider dateFormatProvider) {
		this.dateFormatProvider = dateFormatProvider;
	}

	/**
	 * Returns this globalisation context's number format provider.
	 *
	 * @return the number format provider of this globalisation context
	 */
	public NumberFormatProvider getNumberFormatProvider() {
		return numberFormatProvider;
	}

	/**
	 * Replaces this globalisation context's number format provider.
	 *
	 * @param numberFormatProvider the new number format provider for this globalisation context
	 */
	public void setNumberFormatProvider(NumberFormatProvider numberFormatProvider) {
		this.numberFormatProvider = numberFormatProvider;
	}

	/**
	 * Finds a globalisation context in a context.
	 *
	 * @param context the current context
	 * @return the current globalisation context
	 * @throws AluminumException when the given context does not contain a globalisation context
	 */
	public static GlobalisationContext from(Context context) throws AluminumException {
		return (GlobalisationContext) context.getImplicitObject(GLOBALISATION_CONTEXT);
	}

	/**
	 * The name that the {@link GlobalisationContextProvider globalisation context provider} uses for the globalisation
	 * context implicit object: {@value}.
	 */
	public final static String GLOBALISATION_CONTEXT =
		Context.RESERVED_IMPLICIT_OBJECT_NAME_PREFIX + ".library.g11n.globalisation_context";
}