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

import com.googlecode.aluminumproject.annotations.Ignored;
import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.libraries.actions.ActionContributionFactory;
import com.googlecode.aluminumproject.libraries.actions.ActionFactory;
import com.googlecode.aluminumproject.libraries.functions.FunctionFactory;

import java.util.List;

/**
 * A library that should be ignored when adding all libraries in a package.
 *
 * @author levi_h
 */
@Ignored
public class IgnoredLibrary implements Library {
	/**
	 * Creates an ignored library.
	 */
	public IgnoredLibrary() {}

	public void initialise(Configuration configuration) {}

	public void disable() {}

	public LibraryInformation getInformation() {
		return null;
	}

	public List<ActionFactory> getActionFactories() {
		return null;
	}

	public ActionFactory getDynamicActionFactory(String name) {
		return null;
	}

	public List<ActionContributionFactory> getActionContributionFactories() {
		return null;
	}

	public ActionContributionFactory getDynamicActionContributionFactory(String name) {
		return null;
	}

	public List<FunctionFactory> getFunctionFactories() {
		return null;
	}

	public FunctionFactory getDynamicFunctionFactory(String name) {
		return null;
	}
}