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
package com.googlecode.aluminumproject.libraries;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.configuration.ConfigurationElement;
import com.googlecode.aluminumproject.libraries.actions.Action;
import com.googlecode.aluminumproject.libraries.actions.ActionContribution;
import com.googlecode.aluminumproject.libraries.actions.ActionContributionFactory;
import com.googlecode.aluminumproject.libraries.actions.ActionFactory;
import com.googlecode.aluminumproject.libraries.functions.Function;
import com.googlecode.aluminumproject.libraries.functions.FunctionFactory;

import java.util.List;

/**
 * A library.
 * <p>
 * A library consists of a set of {@link Action actions}, {@link ActionContribution action contributions}, and {@link
 * Function functions}. Libraries may support dynamic actions (the names of which are not known until runtime); whether
 * or not this is the case can be found by inspecting their {@link #getInformation() library information}.
 */
public interface Library extends ConfigurationElement {
	/**
	 * Returns information about this library.
	 *
	 * @return the information that describes this library
	 */
	LibraryInformation getInformation();

	/**
	 * Returns all action factories.
	 *
	 * @return a list with all registered action factories
	 */
	List<ActionFactory> getActionFactories();

	/**
	 * Tries to obtain an action factory for a dynamic action.
	 *
	 * @param name the name of the dynamic action
	 * @return an action factory that creates dynamic actions with the given name
	 * @throws AluminumException when this library does not support dynamic actions or when the action factory can't be
	 *                           obtained
	 */
	ActionFactory getDynamicActionFactory(String name) throws AluminumException;

	/**
	 * Returns all action contribution factories.
	 *
	 * @return a list with all registered action contribution factories
	 */
	List<ActionContributionFactory> getActionContributionFactories();

	/**
	 * Tries to obtain an action contribution factory for a dynamic action contribution.
	 *
	 * @param name the name of the dynamic action contribution
	 * @return an action contribution factory that creates dynamic action contributions with the given name
	 * @throws AluminumException when this library does not support dynamic action contributions or when the action
	 *                           contribution factory can't be obtained
	 */
	ActionContributionFactory getDynamicActionContributionFactory(String name) throws AluminumException;

	/**
	 * Returns all function factories.
	 *
	 * @return a list with all registered function factories
	 */
	List<FunctionFactory> getFunctionFactories();

	/**
	 * Tries to obtain a function factory for a dynamic function.
	 *
	 * @param name the name of the dynamic function
	 * @return a function factory that creates dynamic functions with the given name
	 * @throws AluminumException when this library does not support dynamic functions or when the function factory can't
	 *                           be obtained
	 */
	FunctionFactory getDynamicFunctionFactory(String name) throws AluminumException;
}