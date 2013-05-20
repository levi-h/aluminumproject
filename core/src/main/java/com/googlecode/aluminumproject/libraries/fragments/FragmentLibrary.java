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

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.libraries.Library;
import com.googlecode.aluminumproject.libraries.LibraryInformation;
import com.googlecode.aluminumproject.libraries.actions.ActionContributionFactory;
import com.googlecode.aluminumproject.libraries.actions.ActionFactory;
import com.googlecode.aluminumproject.libraries.functions.FunctionFactory;

import java.util.Collections;
import java.util.List;

/**
 * Provides support for fragments (named includes).
 *
 * @see FragmentAction
 */
public abstract class FragmentLibrary implements Library {
	private String fragmentPath;
	private String parser;

	private Configuration configuration;

	private LibraryInformation information;

	/**
	 * Creates a fragment library that will locate fragments in the default location and will use the current parser.
	 */
	protected FragmentLibrary() {}

	/**
	 * Creates a fragment library.
	 *
	 * @param fragmentPath the path in which the fragments are located ({@code null} to use the default path)
	 * @param parser the name of the parser to use for the included fragments ({@code null} to use the same parser as
	 *               the including template)
	 */
	protected FragmentLibrary(String fragmentPath, String parser) {
		this.fragmentPath = fragmentPath;
		this.parser = parser;
	}

	public void initialise(Configuration configuration) {
		this.configuration = configuration;

		LibraryInformation information = createInformation();

		this.information = new LibraryInformation(information.getUrl(), information.getPreferredUrlAbbreviation(),
			information.getVersion(), true, false, false);
	}

	/**
	 * Returns information about this library.
	 *
	 * @return the URL, preferred URL abbreviation, and version of this library
	 */
	public abstract LibraryInformation createInformation();

	public LibraryInformation getInformation() {
		return information;
	}

	public List<ActionFactory> getActionFactories() {
		return Collections.emptyList();
	}

	public ActionFactory getDynamicActionFactory(String name) {
		String qualifiedName = (fragmentPath == null) ? name : String.format("%s/%s", fragmentPath, name);

		FragmentAction.Factory actionFactory = new FragmentAction.Factory(qualifiedName, parser);
		actionFactory.initialise(configuration);
		actionFactory.setLibrary(this);

		return actionFactory;
	}

	public List<ActionContributionFactory> getActionContributionFactories() {
		return Collections.emptyList();
	}

	public ActionContributionFactory getDynamicActionContributionFactory(String name) throws AluminumException {
		throw new AluminumException("fragment libraries don't support dynamic action contribution factories");
	}

	public List<FunctionFactory> getFunctionFactories() {
		return Collections.emptyList();
	}

	public FunctionFactory getDynamicFunctionFactory(String name) throws AluminumException {
		throw new AluminumException("fragment libraries don't support dynamic functions");
	}

	public void disable() {}
}