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

import java.text.DateFormat;

/**
 * Provides {@link DateFormat date formats} of various {@link DateFormatType types}.
 *
 * @author levi_h
 */
public interface DateFormatProvider {
	/**
	 * Provides a date format of a certain type.
	 *
	 * @param type the type of the date format to return
	 * @param context the current context
	 * @return a date format of the given type for the specified context
	 * @throws ContextException when the date format can't be provided
	 */
	DateFormat provide(DateFormatType type, Context context) throws ContextException;
}