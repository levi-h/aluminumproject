/*
 * Copyright 2009-2012 Aluminum project
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

import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
@Test(groups = {"utilities", "fast"})
public class StringUtilitiesTest {
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void joiningNullShouldThrowException() {
		StringUtilities.join((Object[]) null);
	}

	public void joiningSinglePartShouldResultInSelfsamePart() {
		String joinResult = StringUtilities.join(4);
		assert joinResult != null;
		assert joinResult.equals("4");
	}

	public void joiningMoreThanOnePartShouldWork() {
		String joinResult = StringUtilities.join(null, " and ", Void.class.getSimpleName().toLowerCase());
		assert joinResult != null;
		assert joinResult.equals("null and void");
	}

	@Test(dependsOnMethods = "joiningMoreThanOnePartShouldWork")
	public void joiningArraysShouldWork() {
		String joinResult = StringUtilities.join(new Object[] {1, new int[] {2, 3}, 4}, ", ",
			"can I have a little more?");
		assert joinResult != null;
		assert joinResult.equals("[1, [2, 3], 4], can I have a little more?");
	}

	public void emptyStringShouldRemainEmptyWhenHumanised() {
		String humaniseResult = StringUtilities.humanise("");
		assert humaniseResult != null;
		assert humaniseResult.equals("");
	}

	public void firstWordOfHumanisedTextShouldBeCapitalised() {
		String humaniseResult = StringUtilities.humanise("word");
		assert humaniseResult != null;
		assert humaniseResult.equals("Word");
	}

	@Test(dependsOnMethods = "firstWordOfHumanisedTextShouldBeCapitalised")
	public void wordsShouldBeSeparatedWithSpacesInHumanisedText() {
		String humaniseResult;

		humaniseResult = StringUtilities.humanise("camelCase");
		assert humaniseResult != null;
		assert humaniseResult.equals("Camel case");

		humaniseResult = StringUtilities.humanise("trappedInABox");
		assert humaniseResult != null;
		assert humaniseResult.equals("Trapped in a box");
	}

	public void wordsInUpperCaseShouldBeLeftAloneInHumanisedText() {
		String humaniseResult;

		humaniseResult = StringUtilities.humanise("BBC");
		assert humaniseResult != null;
		assert humaniseResult.equals("BBC");

		humaniseResult = StringUtilities.humanise("aUFO");
		assert humaniseResult != null;
		assert humaniseResult.equals("A UFO");

		humaniseResult = StringUtilities.humanise("URLDecoder");
		assert humaniseResult != null;
		assert humaniseResult.equals("URL decoder");
	}
}