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
package com.googlecode.aluminumproject.utilities;

import com.googlecode.aluminumproject.annotations.Injected;

import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
@Test(groups = {"utilities", "fast"})
public class InjectorTest {
	public void injectorShouldNotInjectFieldsItHasNoValueProvidersFor() {
		Injectable injectable = new Injectable();

		new Injector().inject(injectable);

		assert injectable.value == null;
	}

	public void injectorShouldInjectFieldsItHasAValueProviderFor() {
		Injectable injectable = new Injectable();

		Injector injector = new Injector();
		injector.addValueProvider(new Injector.ClassBasedValueProvider("injected"));
		injector.inject(injectable);

		assert injectable.value != null;
		assert injectable.value.equals("injected");
	}

	private static class Injectable {
		private @Injected String value;
	}
}