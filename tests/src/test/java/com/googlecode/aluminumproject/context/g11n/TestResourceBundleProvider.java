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

import com.googlecode.aluminumproject.context.Context;

import java.util.ListResourceBundle;
import java.util.ResourceBundle;

/**
 * A resource bundle provider that can be used in tests.
 */
public class TestResourceBundleProvider implements ResourceBundleProvider {
	private ResourceBundle resourceBundle;

	/**
	 * Creates an empty test resource bundle provider.
	 */
	public TestResourceBundleProvider() {
		resourceBundle = new ListResourceBundle() {
			@Override
			protected Object[][] getContents() {
				return new Object[0][];
			}
		};
	}

	public ResourceBundle provide(Context context) {
		return resourceBundle;
	}
}