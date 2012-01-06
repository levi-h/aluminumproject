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
package com.googlecode.aluminumproject.libraries.text.functions;

import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
@Test(groups = {"libraries", "libraries-text", "fast"})
public class TextFunctionsTest {
	public void replacingNullShouldResultInNull() {
		assert TextFunctions.replace(null, "i", "a") == null;
	}

	public void replacingTextThatDoesNotContainTargetTextShouldResultInOriginalText() {
		String text = TextFunctions.replace("box", "i", "a");
		assert text != null;
		assert text.equals("box");
	}

	public void replacingTextShouldReplaceAllOccurrencesOfTargetTextWithReplacement() {
		String text = TextFunctions.replace("pitch", "i", "a");
		assert text != null;
		assert text.equals("patch");
	}

	public void humanisingNullShouldResultInNull() {
		assert TextFunctions.humanise(null) == null;
	}

	public void humanisingTextShouldResultInHumanisedText() {
		String text = TextFunctions.humanise("aHatTrick");
		assert text != null;
		assert text.equals("A hat trick"): text;
	}

	public void lowerCasingNullShouldResultInNull() {
		assert TextFunctions.lowerCase(null) == null;
	}

	public void turningTextIntoLowerCaseShouldResultInLowerCasedText() {
		String text = TextFunctions.lowerCase("BALL");
		assert text != null;
		assert text.equals("ball");
	}

	public void upperCasingNullShouldResultInNull() {
		assert TextFunctions.upperCase(null) == null;
	}

	public void turningTextIntoUpperCaseShouldResultInUpperCasedText() {
		String text = TextFunctions.upperCase("goal");
		assert text != null;
		assert text.equals("GOAL");
	}
}