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
import com.googlecode.aluminumproject.context.ContextException;

/**
 * Aggregates globalisation information providers for globalised library elements.
 * <p>
 * Normally, globalisation contexts will be made available by the {@link GlobalisationContextProvider globalisation
 * context provider}; in that case, the current globalisation context can be obtained using the {@link #from(Context)
 * designated method}.
 *
 * @author levi_h
 */
public class GlobalisationContext {
	private LocaleProvider localeProvider;

	/**
	 * Creates a globalisation context.
	 *
	 * @param localeProvider the locale provider to use
	 */
	public GlobalisationContext(LocaleProvider localeProvider) {
		this.localeProvider = localeProvider;
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
	 * Finds a globalisation context in a context.
	 *
	 * @param context the current context
	 * @return the current globalisation context
	 * @throws ContextException when the given context does not contain a globalisation context
	 */
	public static GlobalisationContext from(Context context) throws ContextException {
		return (GlobalisationContext) context.getImplicitObject(GLOBALISATION_CONTEXT_IMPLICIT_OBJECT);
	}

	/**
	 * The name that the {@link GlobalisationContextProvider globalisation context provider} uses for the globalisation
	 * context implicit object: {@value}.
	 */
	public final static String GLOBALISATION_CONTEXT_IMPLICIT_OBJECT =
		Context.RESERVED_IMPLICIT_OBJECT_NAME_PREFIX + ".library.g11n.globalisation_context";
}