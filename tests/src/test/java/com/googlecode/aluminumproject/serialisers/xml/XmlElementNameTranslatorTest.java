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
package com.googlecode.aluminumproject.serialisers.xml;

import com.googlecode.aluminumproject.serialisers.ElementNameTranslator;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
@Test(groups = {"serialisers", "serialisers-xml", "fast"})
public class XmlElementNameTranslatorTest {
	private ElementNameTranslator translator;

	@BeforeMethod
	public void createTranslator() {
		translator = new XmlElementNameTranslator();
	}

	public void spacesInActionNameShouldBeReplacedWithHyphens() {
		String translatedActionName = translator.translateActionName("play music");
		assert translatedActionName != null;
		assert translatedActionName.equals("play-music");
	}

	public void spacesInActionParameterNameShouldBeReplacedWithHyphens() {
		String translatedActionParameterName = translator.translateActionParameterName("by band");
		assert translatedActionParameterName != null;
		assert translatedActionParameterName.equals("by-band");
	}

	public void spacesInActionContributionNameShouldBeReplacedWithHyphens() {
		String translatedActionContributionName = translator.translateActionContributionName("on hours");
		assert translatedActionContributionName != null;
		assert translatedActionContributionName.equals("on-hours");
	}
}