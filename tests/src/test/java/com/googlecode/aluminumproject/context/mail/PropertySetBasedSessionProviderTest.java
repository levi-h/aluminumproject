/*
 * Copyright 2011 Levi Hoogenberg
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
package com.googlecode.aluminumproject.context.mail;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.context.DefaultContext;
import com.googlecode.aluminumproject.utilities.environment.EnvironmentUtilities;

import java.util.Properties;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("all")
@Test(groups = {"libraries", "libraries-mail", "fast"})
public class PropertySetBasedSessionProviderTest {
	@BeforeMethod
	public void addPropertySet() {
		EnvironmentUtilities.getPropertySetContainer().writePropertySet("mail", new Properties());
	}

	@AfterMethod
	public void removePropertySet() {
		EnvironmentUtilities.getPropertySetContainer().removePropertySet("mail");
	}

	public void sessionProviderShouldBeCreatableUsingExistingPropertySetName() {
		assert new PropertySetBasedSessionProvider("mail").provide(new DefaultContext()) != null;
	}

	@Test(expectedExceptions = AluminumException.class)
	public void creatingSessionProviderWithUnknownPropertySetNameShouldCauseException() {
		new PropertySetBasedSessionProvider("nonexistent");
	}
}