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
package com.googlecode.aluminumproject.utilities.finders;

import com.googlecode.aluminumproject.utilities.ReflectionUtilities;
import com.googlecode.aluminumproject.utilities.finders.TypeFinder.TypeFilter;

import java.util.List;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
@Test(groups = {"utilities", "fast"})
public class TypeFinderTest {
	private TypeFilter acceptFilter;
	private TypeFilter denyFilter;

	private String testClassPackageName;
	private String testAnnotationPackageName;

	@BeforeTest
	public void setUpFiltersAndPackageNames() {
		acceptFilter = new TypeFilter() {
			public boolean accepts(Class<?> type) {
				return true;
			}
		};
		denyFilter = new TypeFilter() {
			public boolean accepts(Class<?> type) {
				return false;
			}
		};

		testClassPackageName = ReflectionUtilities.getPackageName(getClass());
		testAnnotationPackageName = ReflectionUtilities.getPackageName(Test.class);
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void notSupplyingPackagesShouldCauseException() {
		TypeFinder.find(acceptFilter);
	}

	public void typesInDirectoryShouldBeFound() {
		List<Class<?>> types = TypeFinder.find(acceptFilter, testClassPackageName);
		assert types != null;
		assert types.contains(getClass());
	}

	public void typesInJarShouldBeFound() {
		List<Class<?>> types = TypeFinder.find(acceptFilter, testAnnotationPackageName);
		assert types != null;
		assert types.contains(Test.class);
	}

	@Test(dependsOnMethods = {"typesInDirectoryShouldBeFound", "typesInJarShouldBeFound"})
	public void searchingInMoreThanOnePackageShouldWork() {
		List<Class<?>> types = TypeFinder.find(acceptFilter, testClassPackageName, testAnnotationPackageName);
		assert types != null;
		assert types.contains(getClass());
		assert types.contains(Test.class);
	}

	@Test(dependsOnMethods = "searchingInMoreThanOnePackageShouldWork")
	public void filterShouldBeApplied() {
		List<Class<?>> types = TypeFinder.find(denyFilter, testClassPackageName, testAnnotationPackageName);
		assert types != null;
		assert types.isEmpty();
	}
}