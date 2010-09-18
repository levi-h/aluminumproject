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
package com.googlecode.aluminumproject.utilities.resources;

import java.io.IOException;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("all")
@Test(groups = {"utilities", "fast"})
public class MemoryResourceStoreFinderTest {
	private MemoryResourceStoreFinder resourceStoreFinder;

	@BeforeMethod
	public void createResourceStoreFinder() {
		resourceStoreFinder = new MemoryResourceStoreFinder();
	}

	public void newResourceShouldBeFindable() {
		assert resourceStoreFinder.find("new") != null;
	}

	@Test(dependsOnMethods = "storedResourceShouldBeRetrievable")
	public void existingResourceShouldBeFindable() throws IOException {
		resourceStoreFinder.find("joined").write("to".getBytes());
		resourceStoreFinder.find("joined").write("get".getBytes());
		resourceStoreFinder.find("joined").write("her".getBytes());

		byte[] resource = resourceStoreFinder.getResource("joined");
		assert resource != null;
		assert new String(resource).equals("together");
	}

	public void gettingNonexistentResourceShouldResultInNull() {
		assert resourceStoreFinder.getResource("nonexistent") == null;
	}

	@Test(dependsOnMethods = "newResourceShouldBeFindable")
	public void storedResourceShouldBeRetrievable() throws IOException {
		resourceStoreFinder.find("new").write("text".getBytes());

		byte[] resource = resourceStoreFinder.getResource("new");
		assert resource != null;
		assert new String(resource).equals("text");
	}

	public void removingNonexistentResourceShouldNotCauseException() {
		resourceStoreFinder.removeResource("nonexistent");
	}

	@Test(dependsOnMethods = {"gettingNonexistentResourceShouldResultInNull", "storedResourceShouldBeRetrievable"})
	public void resourceShouldBeRemovable() throws IOException {
		resourceStoreFinder.find("removed").write("text".getBytes());

		resourceStoreFinder.removeResource("removed");
		assert resourceStoreFinder.getResource("removed") == null;
	}
}