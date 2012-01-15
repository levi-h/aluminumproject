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
package com.googlecode.aluminumproject.tools.aludoc;

import com.googlecode.aluminumproject.configuration.DefaultConfiguration;
import com.googlecode.aluminumproject.libraries.Library;
import com.googlecode.aluminumproject.libraries.TestLibrary;
import com.googlecode.aluminumproject.libraries.actions.ActionContributionFactory;
import com.googlecode.aluminumproject.libraries.actions.ActionContributionInformation;
import com.googlecode.aluminumproject.libraries.actions.ActionFactory;
import com.googlecode.aluminumproject.libraries.actions.ActionInformation;
import com.googlecode.aluminumproject.libraries.actions.ActionParameterInformation;
import com.googlecode.aluminumproject.libraries.aludoc.AluDocLibrary;
import com.googlecode.aluminumproject.libraries.functions.FunctionArgumentInformation;
import com.googlecode.aluminumproject.libraries.functions.FunctionFactory;
import com.googlecode.aluminumproject.libraries.functions.FunctionInformation;
import com.googlecode.aluminumproject.utilities.StringUtilities;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
@Test(groups = {"tools", "tools-aludoc", "slow"})
public class TranslationsTest {
	private List<Library> libraries;

	private List<Locale> locales;

	@BeforeClass
	public void createLibrariesAndLocales() {
		libraries = new LinkedList<Library>();

		for (Library library: new DefaultConfiguration().getLibraries()) {
			if (!Arrays.asList(AluDocLibrary.URL, TestLibrary.URL).contains(library.getInformation().getUrl())) {
				libraries.add(library);
			}
		}

		locales = Arrays.asList(Locale.ENGLISH);
	}

	public void translationsShouldBeAvailableForLibraries() {
		for (Library library: libraries) {
			assertTranslationAvailable(library, "name");
			assertTranslationAvailable(library, "description");
		}
	}

	public void translationsShouldBeAvailableForActions() {
		for (Library library: libraries) {
			for (ActionFactory actionFactory: library.getActionFactories()) {
				ActionInformation actionInformation = actionFactory.getInformation();

				String actionKeyPart = asKeyPart(actionInformation.getName(), false);

				assertTranslationAvailable(library, String.format("%s.description", actionKeyPart));

				for (ActionParameterInformation parameterInformation: actionInformation.getParameterInformation()) {
					String parameterKeyPart = asKeyPart(parameterInformation.getName(), false);

					assertTranslationAvailable(library,
						String.format("%s.%s.description", actionKeyPart, parameterKeyPart));
				}
			}
		}
	}

	public void translationsShouldBeAvailableForActionContributions() {
		for (Library library: libraries) {
			for (ActionContributionFactory contributionFactory: library.getActionContributionFactories()) {
				ActionContributionInformation contributionInformation = contributionFactory.getInformation();

				String contributionKeyPart = asKeyPart(contributionInformation.getName(), false);

				assertTranslationAvailable(library, String.format("%s.description", contributionKeyPart));

				String parameterName = contributionInformation.getParameterNameWhenAction();

				if (parameterName != null) {
					assertTranslationAvailable(library,
						String.format("%s.contribution_description", contributionKeyPart));
					assertTranslationAvailable(library,
						String.format("%s.%s.description", contributionKeyPart, asKeyPart(parameterName, false)));
				}
			}
		}
	}

	public void translationsShouldBeAvailableForFunctions() {
		for (Library library: libraries) {
			for (FunctionFactory functionFactory: library.getFunctionFactories()) {
				FunctionInformation functionInformation = functionFactory.getInformation();

				String functionKeyPart = asKeyPart(functionInformation.getName(), true);

				assertTranslationAvailable(library, String.format("%s.description", functionKeyPart));

				for (FunctionArgumentInformation argumentInformation: functionInformation.getArgumentInformation()) {
					String argumentKeyPart = asKeyPart(argumentInformation.getName(), true);

					assertTranslationAvailable(library,
						String.format("%s.%s.description", functionKeyPart, argumentKeyPart));
				}
			}
		}
	}

	private String asKeyPart(String name, boolean humanise) {
		return (humanise ? StringUtilities.humanise(name).toLowerCase() : name).replace(' ', '_');
	}

	private void assertTranslationAvailable(Library library, String key) {
		for (Locale locale: locales) {
			ResourceBundle libraryMessageBundle =
				getMessageBundle(library.getInformation().getPreferredUrlAbbreviation(), locale);
			ResourceBundle commonMessageBundle = getMessageBundle("common", locale);

			String errorMessage = StringUtilities.join("library '", library.getInformation().getUrl(), "'",
				" does not contain a translation for key '", key, "' (locale: '", locale, "')");

			assert libraryMessageBundle.containsKey(key) || commonMessageBundle.containsKey(key): errorMessage;
		}
	}

	private ResourceBundle getMessageBundle(String name, Locale locale) {
		return ResourceBundle.getBundle(String.format("aludoc/%s", name), locale);
	}
}