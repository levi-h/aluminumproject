/*
 * Copyright 2013 Aluminum project
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
package com.googlecode.aluminumproject.libraries.fragments;

import com.googlecode.aluminumproject.finders.ClassPathTemplateFinder;
import com.googlecode.aluminumproject.libraries.LibraryInformation;
import com.googlecode.aluminumproject.libraries.LibraryTest;
import com.googlecode.aluminumproject.libraries.UseConfigurationParameter;

import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
@Test(groups = {"libraries", "libraries-fragments", "slow"})
@UseConfigurationParameter(name = ClassPathTemplateFinder.TEMPLATE_PATH, value = "templates/fragments")
public class FragmentLibraryTest extends LibraryTest {
	public static class DefaultFragmentLibrary extends FragmentLibrary {
		public LibraryInformation createInformation() {
			return new LibraryInformation("http://aluminumproject.googlecode.com/test/fragments/default", "tf", "test");
		}
	}

	public void defaultFragmentLibraryShouldUseCurrentParserAndNoFragmentPath() {
		assert processTemplate("include").equals("Hello!");
	}

	public void parametersShouldBeSupported() {
		assert processTemplate("include-with-parameters").equals("Hello, world!");
	}

	public static class XmlFragmentLibrary extends FragmentLibrary {
		public XmlFragmentLibrary() {
			super(null, "xml");
		}

		public LibraryInformation createInformation() {
			return new LibraryInformation("http://aluminumproject.googlecode.com/test/fragments/xml", "tfx", "test");
		}
	}

	public void parserShouldBeConfigurable() {
		assert processTemplate("include-with-parser").equals("Hello.");
	}

	public static class FrenchFragmentLibrary extends FragmentLibrary {
		public FrenchFragmentLibrary() {
			super("french", null);
		}

		public LibraryInformation createInformation() {
			return new LibraryInformation("http://aluminumproject.googlecode.com/test/fragments/french", "tff", "test");
		}
	}

	public void fragmentPathShouldBeConfigurable() {
		assert processTemplate("include-with-fragment-path").equals("Allô!");
	}
}
