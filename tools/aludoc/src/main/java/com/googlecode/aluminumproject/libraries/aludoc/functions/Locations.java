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

import com.googlecode.aluminumproject.annotations.FunctionClass;
import com.googlecode.aluminumproject.libraries.Library;
import com.googlecode.aluminumproject.libraries.actions.ActionContributionFactory;
import com.googlecode.aluminumproject.libraries.actions.ActionFactory;
import com.googlecode.aluminumproject.libraries.functions.FunctionFactory;

@FunctionClass
@SuppressWarnings("javadoc")
public class Locations {
	private Locations() {}

	public static String homePage() {
		return "index.html";
	}

	public static String libraryPage(Library library) {
		return String.format("libraries/%s/index.html", library.getInformation().getPreferredUrlAbbreviation());
	}

	public static String actionPage(ActionFactory actionFactory) {
		return String.format("libraries/%s/actions/%s.html",
			actionFactory.getLibrary().getInformation().getPreferredUrlAbbreviation(),
			actionFactory.getInformation().getName().replace(' ', '_'));
	}

	public static String actionContributionPage(ActionContributionFactory actionContributionFactory) {
		return String.format("libraries/%s/actions/contributions/%s.html",
			actionContributionFactory.getLibrary().getInformation().getPreferredUrlAbbreviation(),
			actionContributionFactory.getInformation().getName().replace(' ', '_'));
	}

	public static String functionPage(FunctionFactory functionFactory) {
		return String.format("libraries/%s/functions/%s.html",
			functionFactory.getLibrary().getInformation().getPreferredUrlAbbreviation(),
			functionFactory.getInformation().getName().replace(' ', '_'));
	}

	public static String styleSheet() {
		return "resources/aludoc.css";
	}

	public static String relativeLocation(String sourceLocation, String targetLocation) {
		String[] sourceComponents = sourceLocation.split("/");
		String[] targetComponents = targetLocation.split("/");

		int i = 0;

		while ((i + 1 < sourceComponents.length) && (i + 1 < targetComponents.length) &&
				sourceComponents[i].equals(targetComponents[i])) {
			i++;
		}

		StringBuilder locationBuilder = new StringBuilder();

		for (int j = 0; j < sourceComponents.length - (i + 1); j++) {
			locationBuilder.append("../");
		}

		for (int k = i; k < targetComponents.length; k++) {
			if (k > i) {
				locationBuilder.append('/');
			}

			locationBuilder.append(targetComponents[k]);
		}

		return locationBuilder.toString();
	}
}