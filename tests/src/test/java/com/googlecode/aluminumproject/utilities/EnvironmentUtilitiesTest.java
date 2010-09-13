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

import com.googlecode.aluminumproject.utilities.test.TestPropertySetContainer;

import org.testng.annotations.Test;

@SuppressWarnings("all")
@Test(groups = {"utilities", "slow"})
public class EnvironmentUtilitiesTest {
	public void versionShouldBeAvailable() {
		String version = EnvironmentUtilities.getVersion();
		assert version != null;
		assert version.equals("test");
	}

	public void propertySetContainerShouldBeAvailable() {
		assert EnvironmentUtilities.getPropertySetContainer() instanceof TestPropertySetContainer;
	}
}