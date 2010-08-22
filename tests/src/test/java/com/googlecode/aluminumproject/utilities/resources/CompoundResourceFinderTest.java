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

import static com.googlecode.aluminumproject.utilities.resources.ResourceFinderTestUtilities.read;

import com.googlecode.aluminumproject.utilities.UtilityException;

import java.io.IOException;
import java.net.URL;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("all")
@Test(groups = {"utilities", "slow"})
public class CompoundResourceFinderTest {
	private ResourceFinder resourceFinder;

	@BeforeMethod
	public void createResourceFinder() {
		resourceFinder = new CompoundResourceFinder(
			new ClassPathResourceFinder(),
			new ClassPathResourceFinder("dummy")
		);
	}

	public void resourceFindableByFirstResourceFinderShouldBeFindable() throws IOException {
		URL url = resourceFinder.find("dummy.txt");
		assert url != null;
		assert read(url).endsWith("This file resides in the root of the class path.");
	}

	public void resourceFindableBySecondResourceFinderShouldBeFindable() throws IOException {
		URL url = resourceFinder.find("dummy-too.txt");
		assert url != null;
		assert read(url).endsWith("This file is located inside 'dummy' as well.");
	}

	@Test(expectedExceptions = UtilityException.class)
	public void resourceNotFindableByAnyResourceFinderShouldCauseException() {
		resourceFinder.find(".dummy");
	}
}