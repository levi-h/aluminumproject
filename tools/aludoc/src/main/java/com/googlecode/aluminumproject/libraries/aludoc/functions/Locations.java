/*
 * Copyright 2010 Levi Hoogenberg
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

/**
 * Provides functions related to locations of AluDoc pages and resources.
 *
 * @author levi_h
 */
@FunctionClass
public class Locations {
	private Locations() {}

	/**
	 * Returns the location of the home page.
	 *
	 * @return the location for the page that provides an overview of all other pages
	 */
	public static String homePage() {
		return "index.html";
	}

	/**
	 * Returns the location of a library page.
	 *
	 * @param library the library to determine the page location of
	 * @return the location for the page that documents the given library
	 */
	public static String libraryPage(Library library) {
		return String.format("libraries/%s/index.html", library.getInformation().getPreferredUrlAbbreviation());
	}

	/**
	 * Returns the location of an action page.
	 *
	 * @param actionFactory the factory of the action to determine the page location of
	 * @return the location for the page that documents the action that the given action factory creates
	 */
	public static String actionPage(ActionFactory actionFactory) {
		return String.format("libraries/%s/actions/%s.html",
			actionFactory.getLibrary().getInformation().getPreferredUrlAbbreviation(),
			actionFactory.getInformation().getName().replace(' ', '_'));
	}

	/**
	 * Returns the location of an action contribution page.
	 *
	 * @param actionContributionFactory the factory of the action contribution to determine the page location of
	 * @return the location for the page that documents the action contribution that the given action contribution
	 *         factory creates
	 */
	public static String actionContributionPage(ActionContributionFactory actionContributionFactory) {
		return String.format("libraries/%s/actions/contributions/%s.html",
			actionContributionFactory.getLibrary().getInformation().getPreferredUrlAbbreviation(),
			actionContributionFactory.getInformation().getName().replace(' ', '_'));
	}

	/**
	 * Returns the location of a function page.
	 *
	 * @param functionFactory the factory of the function to determine the page location of
	 * @return the location for the page that documents the function that the given function factory creates
	 */
	public static String functionPage(FunctionFactory functionFactory) {
		return String.format("libraries/%s/functions/%s.html",
			functionFactory.getLibrary().getInformation().getPreferredUrlAbbreviation(),
			functionFactory.getInformation().getName().replace(' ', '_'));
	}

	/**
	 * Returns the location of the style sheet.
	 *
	 * @return the location for the global style sheet
	 */
	public static String styleSheet() {
		return "resources/aludoc.css";
	}

	/**
	 * Determines the path to a location, relative to another location.
	 *
	 * @param sourceLocation the location to determine the path from
	 * @param targetLocation the location to determine the path to
	 * @return the specified target location, relative to the given source location
	 */
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