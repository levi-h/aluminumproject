/*
 * Copyright 2010-2011 Levi Hoogenberg
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
package com.googlecode.aluminumproject.utilities.environment;

import com.googlecode.aluminumproject.AluminumException;

import java.io.File;
import java.util.Properties;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("all")
@Test(groups = {"utilities", "slow"})
public class FileSystemPropertySetContainerTest {
	private PropertySetContainer container;
	private PropertySetContainer emptyContainer;

	@BeforeMethod
	public void createContainers() {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();

		container = new FileSystemPropertySetContainer(new File(loader.getResource("property-sets").getFile()));

		File temporaryDirectory = new File(System.getProperty("java.io.tmpdir"));

		File emptyDirectory;
		int i = 0;

		do {
			emptyDirectory = new File(temporaryDirectory, String.format("property-sets-%d", ++i));
		} while (emptyDirectory.exists());

		emptyContainer = new FileSystemPropertySetContainer(emptyDirectory);
	}

	public void containerShouldContainExistingPropertySets() {
		assert container.containsPropertySet("prices");
	}

	public void containerShouldNotContainNonexistentPropertySets() {
		assert !container.containsPropertySet("unknown");
		assert !emptyContainer.containsPropertySet("new");
	}

	@Test(dependsOnMethods = "containerShouldContainExistingPropertySets")
	public void existingPropertySetsShouldBeReadable() {
		Properties prices = container.readPropertySet("prices");

		String price = prices.getProperty("radio");
		assert price != null;
		assert price.equals("50");
	}

	@Test(
		dependsOnMethods = "containerShouldNotContainNonexistentPropertySets",
		expectedExceptions = AluminumException.class
	)
	public void tryingToReadNonexistentPropertySetShouldCauseException() {
		container.readPropertySet("unknown");
	}

	@Test(dependsOnMethods = "containerShouldNotContainNonexistentPropertySets")
	public void writingNewPropertySetShouldMakeItAvailable() {
		emptyContainer.writePropertySet("new", new Properties());

		assert emptyContainer.containsPropertySet("new");
	}

	@Test(dependsOnMethods = "writingNewPropertySetShouldMakeItAvailable")
	public void writtenPropertySetShouldBeReadable() {
		Properties ages = new Properties();
		ages.setProperty("levi", "28"); // good old days

		emptyContainer.writePropertySet("ages", ages);

		ages = emptyContainer.readPropertySet("ages");

		String age = ages.getProperty("levi");
		assert age != null;
		assert age.equals("28");
	}

	@Test(dependsOnMethods = "writingNewPropertySetShouldMakeItAvailable")
	public void writtenPropertySetShouldBeRemovable() {
		Properties romanNumerals = new Properties();
		romanNumerals.setProperty("1", "I");
		romanNumerals.setProperty("2", "II");
		romanNumerals.setProperty("3", "III");
		romanNumerals.setProperty("4", "IV");
		romanNumerals.setProperty("5", "V");

		emptyContainer.writePropertySet("roman-numerals", romanNumerals);

		emptyContainer.removePropertySet("roman-numerals");
		assert !emptyContainer.containsPropertySet("roman-numerals");
	}

	@Test(dependsOnMethods = "writingNewPropertySetShouldMakeItAvailable", expectedExceptions = AluminumException.class)
	public void tryingToRemoveNonexistentPropertySetShouldCauseException() {
		emptyContainer.removePropertySet("unknown");
	}
}