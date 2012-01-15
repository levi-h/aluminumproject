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

import java.util.ResourceBundle;

/**
 * Provides a {@link ResourceBundle resource bundle} for a template.
 */
public interface ResourceBundleProvider {
	/**
	 * Determines the resource bundle to use in a particular context.
	 *
	 * @param context the current context
	 * @return a suitable resource bundle for the given context
	 * @throws AluminumException if no resource bundle can be worked out
	 */
	ResourceBundle provide(Context context) throws AluminumException;
}