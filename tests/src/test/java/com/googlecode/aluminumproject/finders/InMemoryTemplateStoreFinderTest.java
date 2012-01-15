/*
 * Copyright 2011-2012 Aluminum project
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

import java.io.IOException;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
@Test(groups = {"core", "fast"})
public class InMemoryTemplateStoreFinderTest {
	private InMemoryTemplateStoreFinder templateStoreFinder;

	@BeforeMethod
	public void createTemplateStoreFinder() {
		templateStoreFinder = new InMemoryTemplateStoreFinder();
		templateStoreFinder.initialise(new TestConfiguration(new ConfigurationParameters()));
	}

	public void newTemplateShouldBeFindable() {
		assert templateStoreFinder.find("new") != null;
	}

	@Test(dependsOnMethods = "storedTemplateShouldBeRetrievable")
	public void existingTemplateShouldBeFindable() throws IOException {
		templateStoreFinder.find("joined").write("to".getBytes());
		templateStoreFinder.find("joined").write("get".getBytes());
		templateStoreFinder.find("joined").write("her".getBytes());

		byte[] template = templateStoreFinder.get("joined");
		assert template != null;
		assert new String(template).equals("together");
	}

	public void gettingNonexistentTemplateShouldResultInNull() {
		assert templateStoreFinder.get("nonexistent") == null;
	}

	@Test(dependsOnMethods = "newTemplateShouldBeFindable")
	public void storedTemplateShouldBeRetrievable() throws IOException {
		templateStoreFinder.find("new").write("text".getBytes());

		byte[] template = templateStoreFinder.get("new");
		assert template != null;
		assert new String(template).equals("text");
	}

	public void removingNonexistentTemplateShouldNotCauseException() {
		templateStoreFinder.remove("nonexistent");
	}

	@Test(dependsOnMethods = {"gettingNonexistentTemplateShouldResultInNull", "storedTemplateShouldBeRetrievable"})
	public void templateShouldBeRemovable() throws IOException {
		templateStoreFinder.find("removed").write("text".getBytes());

		templateStoreFinder.remove("removed");
		assert templateStoreFinder.get("removed") == null;
	}
}