/*
 * Copyright 2010-2012 Levi Hoogenberg
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
package com.googlecode.aluminumproject.libraries.aludoc.functions;

import static com.googlecode.aluminumproject.libraries.aludoc.functions.Locations.relativeLocation;

import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
@Test(groups = {"libraries", "fast"})
public class LocationsTest {
	public void relativeLocationOfPageInSameDirectoryShouldBeDeterminable() {
		String relativeLocation = relativeLocation("a/source.html", "a/target.html");
		assert relativeLocation != null;
		assert relativeLocation.equals("target.html");
	}

	public void relativeLocationOfPageInParentDirectoryShouldBeDeterminable() {
		String relativeLocation = relativeLocation("a/b/source.html", "a/target.html");
		assert relativeLocation != null;
		assert relativeLocation.equals("../target.html");
	}

	public void relativeLocationOfPageInSubdirectoryShouldBeDeterminable() {
		String relativeLocation = relativeLocation("a/source.html", "a/b/target.html");
		assert relativeLocation != null;
		assert relativeLocation.equals("b/target.html");
	}

	public void relativeLocationOfPageInDifferentDirectoryShouldBeDeterminable() {
		String relativeLocation = relativeLocation("a/source.html", "b/target.html");
		assert relativeLocation != null;
		assert relativeLocation.equals("../b/target.html");
	}
}