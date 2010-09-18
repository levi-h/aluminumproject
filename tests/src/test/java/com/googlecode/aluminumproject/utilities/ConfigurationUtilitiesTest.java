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
package com.googlecode.aluminumproject.utilities;

import static com.googlecode.aluminumproject.utilities.ConfigurationUtilities.findLibrary;

import com.googlecode.aluminumproject.annotations.Injected;
import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.configuration.ConfigurationParameters;
import com.googlecode.aluminumproject.configuration.test.TestConfiguration;
import com.googlecode.aluminumproject.libraries.test.TestLibrary;

import java.lang.reflect.Field;
import java.util.Locale;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("all")
@Test(groups = {"utilities", "fast"})
public class ConfigurationUtilitiesTest {
	private Configuration configuration;

	@BeforeMethod
	public void createConfiguration() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		TestConfiguration configuration = new TestConfiguration(parameters);

		TestLibrary testLibrary = new TestLibrary();
		testLibrary.initialise(configuration, parameters);
		configuration.addLibrary(testLibrary);

		this.configuration = configuration;
	}

	public void libraryShouldBeFindableByUrl() {
		assert findLibrary(configuration, "http://aluminumproject.googlecode.com/test") instanceof TestLibrary;
	}

	public void libraryShouldBeFindableByVersionedUrl() {
		assert findLibrary(configuration, "http://aluminumproject.googlecode.com/test/test") instanceof TestLibrary;
	}

	public void tryingToFindUnknownLibraryShouldResultInNull() {
		assert findLibrary(configuration, "http://aluminumproject.googlecode.com/nonexistent") == null;
	}
}