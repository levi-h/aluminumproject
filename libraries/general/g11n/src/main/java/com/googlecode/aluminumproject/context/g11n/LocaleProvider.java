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

import java.util.Locale;

/**
 * Provides a {@link Locale locale} for a template.
 *
 * @author levi_h
 */
public interface LocaleProvider {
	/**
	 * Determines the locale to use in a certain context.
	 *
	 * @param context the current context
	 * @return the locale to use
	 * @throws AluminumException when the locale can't be determined
	 */
	Locale provide(Context context) throws AluminumException;
}