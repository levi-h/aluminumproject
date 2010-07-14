/*
 * Copyright 2009-2010 Levi Hoogenberg
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
package com.googlecode.aluminumproject.templates;

import com.googlecode.aluminumproject.configuration.ConfigurationParameters;
import com.googlecode.aluminumproject.configuration.DefaultConfiguration;
import com.googlecode.aluminumproject.configuration.test.TestConfiguration;
import com.googlecode.aluminumproject.interceptors.ActionInterceptor;
import com.googlecode.aluminumproject.interceptors.test.TestActionInterceptor;
import com.googlecode.aluminumproject.libraries.test.TestLibrary;
import com.googlecode.aluminumproject.utilities.ReflectionUtilities;

import java.util.List;

import org.testng.annotations.Test;

@SuppressWarnings("all")
@Test(groups = {"core", "fast"})
public class DefaultTemplateElementFactoryTest {
	public void configuredActionInterceptorsShouldBeAdded() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(DefaultTemplateElementFactory.ACTION_INTERCEPTOR_PACKAGES,
			ReflectionUtilities.getPackageName(TestActionInterceptor.class));

		DefaultTemplateElementFactory templateElementFactory = new DefaultTemplateElementFactory();
		templateElementFactory.initialise(new TestConfiguration(parameters), parameters);

		List<ActionInterceptor> actionInterceptors = templateElementFactory.getActionInterceptors();
		assert actionInterceptors != null;
		assert actionInterceptors.size() == 1;

		ActionInterceptor actionInterceptor = actionInterceptors.get(0);
		assert actionInterceptor != null;
		assert actionInterceptor instanceof TestActionInterceptor;
	}

	public void actionInterceptorsThatAreConfiguredAsConfigurationElementsShouldBeAdded() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(DefaultConfiguration.CONFIGURATION_ELEMENT_PACKAGES,
			ReflectionUtilities.getPackageName(TestActionInterceptor.class));

		DefaultTemplateElementFactory templateElementFactory = new DefaultTemplateElementFactory();
		templateElementFactory.initialise(new TestConfiguration(parameters), parameters);

		List<ActionInterceptor> actionInterceptors = templateElementFactory.getActionInterceptors();
		assert actionInterceptors != null;
		assert actionInterceptors.size() == 1;

		ActionInterceptor actionInterceptor = actionInterceptors.get(0);
		assert actionInterceptor != null;
		assert actionInterceptor instanceof TestActionInterceptor;
	}

	public void libraryShouldBeFindableByUrl() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		TestConfiguration configuration = new TestConfiguration(parameters);

		TestLibrary testLibrary = new TestLibrary();
		testLibrary.initialise(configuration, parameters);
		configuration.addLibrary(testLibrary);

		DefaultTemplateElementFactory templateElementFactory = new DefaultTemplateElementFactory();
		templateElementFactory.initialise(configuration, parameters);

		assert templateElementFactory.findLibrary("http://aluminumproject.googlecode.com/test") == testLibrary;
	}

	public void libraryShouldBeFindableByVersionedUrl() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		TestConfiguration configuration = new TestConfiguration(parameters);

		TestLibrary testLibrary = new TestLibrary();
		testLibrary.initialise(configuration, parameters);
		configuration.addLibrary(testLibrary);

		DefaultTemplateElementFactory templateElementFactory = new DefaultTemplateElementFactory();
		templateElementFactory.initialise(configuration, parameters);

		assert templateElementFactory.findLibrary("http://aluminumproject.googlecode.com/test/test") == testLibrary;
	}
}