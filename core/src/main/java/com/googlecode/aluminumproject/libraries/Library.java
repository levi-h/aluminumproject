/*
 * Copyright 2009-2010 Levi Hoogenberg
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
 * Function functions}.
 *
 * @author levi_h
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
	 * Returns all action contribution factories.
	 *
	 * @return a list with all registered action contribution factories
	 */
	List<ActionContributionFactory> getActionContributionFactories();

	/**
	 * Returns all function factories.
	 *
	 * @return a list with all registered function factories
	 */
	List<FunctionFactory> getFunctionFactories();
}