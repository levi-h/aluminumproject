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
package com.googlecode.aluminumproject.configuration;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.utilities.ReflectionUtilities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
@Test(groups = {"core", "fast"})
public class DefaultConfigurationElementFactoryTest {
	private DefaultConfigurationElementFactory configurationElementFactory;

	@BeforeMethod
	public void createConfigurationElementFactory() {
		configurationElementFactory = new DefaultConfigurationElementFactory();
		configurationElementFactory.initialise(new TestConfiguration(new ConfigurationParameters()));
	}

	public void classShouldBeInstantiable() {
		assert configurationElementFactory.instantiate("java.util.HashMap", Map.class) instanceof HashMap;
	}

	@Test(expectedExceptions = AluminumException.class)
	public void instantiatingUninstantiableTypeShouldCauseException() {
		configurationElementFactory.instantiate("java.util.Map", Map.class);
	}

	@Test(dependsOnMethods = "classShouldBeInstantiable", expectedExceptions = AluminumException.class)
	public void supplyingIncompatibleTypeShouldCauseException() {
		configurationElementFactory.instantiate("java.util.HashMap", SortedMap.class);
	}

	public void noConfigurationElementCustomisersShouldBeConfiguredByDefault() {
		List<ConfigurationElementCustomiser> customisers =
			configurationElementFactory.getConfigurationElementCustomisers();
		assert customisers != null;
		assert customisers.isEmpty();
	}

	public void allConfigurationElementCustomisersInPackageShouldBeAdded() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(DefaultConfigurationElementFactory.CONFIGURATION_ELEMENT_CUSTOMISER_PACKAGES,
			ReflectionUtilities.getPackageName(TestConfigurationElementCustomiser.class));

		DefaultConfigurationElementFactory configurationElementFactory = new DefaultConfigurationElementFactory();
		configurationElementFactory.initialise(new TestConfiguration(parameters));

		List<ConfigurationElementCustomiser> customisers =
			configurationElementFactory.getConfigurationElementCustomisers();
		assert customisers != null;
		assert customisers.size() == 1;
		assert customisers.get(0) instanceof TestConfigurationElementCustomiser;
	}

	public void allConfigurationElementCustomisersInConfigurationElementPackageShouldBeAdded() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(DefaultConfiguration.CONFIGURATION_ELEMENT_PACKAGES,
			ReflectionUtilities.getPackageName(TestConfigurationElementCustomiser.class));

		DefaultConfigurationElementFactory configurationElementFactory = new DefaultConfigurationElementFactory();
		configurationElementFactory.initialise(new TestConfiguration(parameters));

		List<ConfigurationElementCustomiser> customisers =
			configurationElementFactory.getConfigurationElementCustomisers();
		assert customisers != null;
		assert customisers.size() == 1;
		assert customisers.get(0) instanceof TestConfigurationElementCustomiser;
	}

	@Test(dependsOnMethods = "allConfigurationElementCustomisersInPackageShouldBeAdded")
	public void configurationElementFactoryShouldInitialiseConfigurationElementCustomisers() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(DefaultConfigurationElementFactory.CONFIGURATION_ELEMENT_CUSTOMISER_PACKAGES,
			ReflectionUtilities.getPackageName(TestConfigurationElementCustomiser.class));

		TestConfiguration configuration = new TestConfiguration(parameters);

		DefaultConfigurationElementFactory configurationElementFactory = new DefaultConfigurationElementFactory();
		configurationElementFactory.initialise(configuration);

		List<ConfigurationElementCustomiser> configurationElementCustomisers =
			configurationElementFactory.getConfigurationElementCustomisers();
		TestConfigurationElementCustomiser configurationElementCustomiser =
			(TestConfigurationElementCustomiser) configurationElementCustomisers.get(0);
		assert configurationElementCustomiser.getConfiguration() == configuration;
	}

	public static class Customisable {
		private boolean customised;

		public boolean isCustomised() {
			return customised;
		}

		public void customise() {
			customised = true;
		}
	}

	@Test(dependsOnMethods = {"classShouldBeInstantiable", "allConfigurationElementCustomisersInPackageShouldBeAdded"})
	public void configurationElementCustomisersShouldBeAppliedToNewObjects() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(DefaultConfigurationElementFactory.CONFIGURATION_ELEMENT_CUSTOMISER_PACKAGES,
			ReflectionUtilities.getPackageName(TestConfigurationElementCustomiser.class));

		DefaultConfigurationElementFactory configurationElementFactory = new DefaultConfigurationElementFactory();
		configurationElementFactory.initialise(new TestConfiguration(parameters));

		assert configurationElementFactory.instantiate(Customisable.class.getName(), Customisable.class).isCustomised();
	}
}