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
package com.googlecode.aluminumproject.libraries;

import com.googlecode.aluminumproject.annotations.Injected;
import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.configuration.ConfigurationParameters;
import com.googlecode.aluminumproject.configuration.TestConfiguration;
import com.googlecode.aluminumproject.utilities.Injector;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("all")
@Test(groups = {"core", "fast"})
public class AbstractLibraryElementTest {
	private AbstractLibraryElement libraryElement;

	@BeforeMethod
	public void createLibraryElement() {
		libraryElement = new DefaultLibraryElement();
		libraryElement.initialise(new TestConfiguration(new ConfigurationParameters()));
	}

	public void injectingFieldsShouldIncludeConfiguration() {
		InjectableWithInjectedConfiguration injectable = new InjectableWithInjectedConfiguration();

		libraryElement.injectFields(injectable);

		assert injectable.configuration != null;
	}

	private static class InjectableWithInjectedConfiguration {
		private @Injected Configuration configuration;
	}

	public void injectingFieldsShouldIncludeLibraryElement() {
		InjectableWithInjectedLibraryElement injectable = new InjectableWithInjectedLibraryElement();

		libraryElement.injectFields(injectable);

		assert injectable.libraryElement == libraryElement;
	}

	private static class InjectableWithInjectedLibraryElement {
		private @Injected LibraryElement libraryElement;
	}

	public void injectingFieldsShouldIncludeCustomValue() {
		InjectableWithBoolean injectable = new InjectableWithBoolean();

		libraryElement.injectFields(injectable);

		assert injectable.injected != null;
		assert injectable.injected.booleanValue();
	}

	private static class InjectableWithBoolean {
		private @Injected Boolean injected;
	}

	private static class DefaultLibraryElement extends AbstractLibraryElement {
		@Override
		protected void addValueProviders(Injector injector) {
			injector.addValueProvider(new Injector.ClassBasedValueProvider(true));
		}
	}
}