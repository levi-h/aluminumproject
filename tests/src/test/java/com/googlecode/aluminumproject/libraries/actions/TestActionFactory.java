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
package com.googlecode.aluminumproject.libraries.actions;

import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.libraries.Library;

import java.util.Collections;
import java.util.Map;

/**
 * Creates {@link TestAction test actions}.
 */
public class TestActionFactory implements ActionFactory {
	private Configuration configuration;

	private ActionInformation information;

	private Library library;

	/**
	 * Creates a test action factory.
	 */
	public TestActionFactory() {
		information = new ActionInformation("test", Collections.<ActionParameterInformation>emptyList(), false);
	}

	public void initialise(Configuration configuration) {
		this.configuration = configuration;
	}

	public void disable() {
		configuration = null;
	}

	/**
	 * Returns the configuration that this action factory was initialised with.
	 *
	 * @return the configuration used
	 */
	public Configuration getConfiguration() {
		return configuration;
	}

	public ActionInformation getInformation() {
		return information;
	}

	public Library getLibrary() {
		return library;
	}

	public void setLibrary(Library library) {
		this.library = library;
	}

	public Action create(Map<String, ActionParameter> parameters, Context context) {
		return new TestAction();
	}
}