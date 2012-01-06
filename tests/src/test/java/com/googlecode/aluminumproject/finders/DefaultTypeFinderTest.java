/*
 * Copyright 2012 Levi Hoogenberg
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
package com.googlecode.aluminumproject.finders;

import com.googlecode.aluminumproject.configuration.ConfigurationParameters;
import com.googlecode.aluminumproject.configuration.TestConfiguration;
import com.googlecode.aluminumproject.finders.TypeFinder.TypeFilter;
import com.googlecode.aluminumproject.utilities.ReflectionUtilities;

import java.util.List;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
@Test(groups = {"core", "slow"})
public class DefaultTypeFinderTest {
	private TypeFinder typeFinder;

	private String directoryPackageName;
	private String jarPackageName;

	@BeforeMethod
	public void createTypeFinder() {
		typeFinder = new DefaultTypeFinder();
		typeFinder.initialise(new TestConfiguration(new ConfigurationParameters()));

		directoryPackageName = ReflectionUtilities.getPackageName(getClass());
		jarPackageName = ReflectionUtilities.getPackageName(Test.class);
	}

	@AfterMethod
	public void disableTypeFinder() {
		typeFinder.disable();
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void notSupplyingPackagesShouldCauseException() {
		typeFinder.find(new AcceptFilter());
	}

	public void typesInDirectoryShouldBeFound() {
		List<Class<?>> types = typeFinder.find(new AcceptFilter(), directoryPackageName);
		assert types != null;
		assert types.contains(getClass());
	}

	public void typesInJarShouldBeFound() {
		List<Class<?>> types = typeFinder.find(new AcceptFilter(), jarPackageName);
		assert types != null;
		assert types.contains(Test.class);
	}

	@Test(dependsOnMethods = {"typesInDirectoryShouldBeFound", "typesInJarShouldBeFound"})
	public void searchingInMoreThanOnePackageShouldWork() {
		List<Class<?>> types = typeFinder.find(new AcceptFilter(), directoryPackageName, jarPackageName);
		assert types != null;
		assert types.contains(getClass());
		assert types.contains(Test.class);
	}

	@Test(dependsOnMethods = "searchingInMoreThanOnePackageShouldWork")
	public void filterShouldBeApplied() {
		List<Class<?>> types = typeFinder.find(new DenyFilter(), directoryPackageName, jarPackageName);
		assert types != null;
		assert types.isEmpty();
	}

	private class AcceptFilter implements TypeFilter {
		public boolean accepts(Class<?> type) {
			return true;
		}
	}

	private class DenyFilter implements TypeFilter {
		public boolean accepts(Class<?> type) {
			return false;
		}
	}
}