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

import java.text.NumberFormat;

/**
 * Provides {@link NumberFormat number formats} of different types.
 *
 * @author levi_h
 */
public interface NumberFormatProvider {
	/**
	 * Provides a number format of a certain type.
	 *
	 * @param type the desired number format type
	 * @param context the current context
	 * @return a number format of the given type
	 * @throws ContextException when the number format can't be provided
	 */
	NumberFormat provide(NumberFormatType type, Context context) throws ContextException;
}