/*
 * Copyright 2009-2012 Levi Hoogenberg
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
import com.googlecode.aluminumproject.configuration.TestConfiguration;
import com.googlecode.aluminumproject.interceptors.ActionInterceptor;
import com.googlecode.aluminumproject.interceptors.TestActionInterceptor;
import com.googlecode.aluminumproject.utilities.ReflectionUtilities;

import java.util.Iterator;
import java.util.List;

import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
@Test(groups = {"core", "fast"})
public class DefaultTemplateElementFactoryTest {
	public void configuredActionInterceptorsShouldBeAdded() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(DefaultTemplateElementFactory.ACTION_INTERCEPTOR_PACKAGES,
			ReflectionUtilities.getPackageName(TestActionInterceptor.class));

		DefaultTemplateElementFactory templateElementFactory = new DefaultTemplateElementFactory();
		templateElementFactory.initialise(new TestConfiguration(parameters));

		assert findActionInterceptorOfType(templateElementFactory, TestActionInterceptor.class) != null;
	}

	public void actionInterceptorsThatAreConfiguredAsConfigurationElementsShouldBeAdded() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(DefaultConfiguration.CONFIGURATION_ELEMENT_PACKAGES,
			ReflectionUtilities.getPackageName(TestActionInterceptor.class));

		DefaultTemplateElementFactory templateElementFactory = new DefaultTemplateElementFactory();
		templateElementFactory.initialise(new TestConfiguration(parameters));

		assert findActionInterceptorOfType(templateElementFactory, TestActionInterceptor.class) != null;
	}

	private <T extends ActionInterceptor> T findActionInterceptorOfType(
			DefaultTemplateElementFactory templateElementFactory, Class<T> actionInterceptorType) {
		List<ActionInterceptor> actionInterceptors = templateElementFactory.getActionInterceptors();
		assert actionInterceptors != null;

		Iterator<ActionInterceptor> it = actionInterceptors.iterator();
		T actionInterceptor = null;

		while (it.hasNext() && (actionInterceptor == null)) {
			ActionInterceptor actionInterceptorInList = it.next();

			if (actionInterceptorType.isInstance(actionInterceptorInList)) {
				actionInterceptor = actionInterceptorType.cast(actionInterceptorInList);
			}
		}

		return actionInterceptor;
	}
}