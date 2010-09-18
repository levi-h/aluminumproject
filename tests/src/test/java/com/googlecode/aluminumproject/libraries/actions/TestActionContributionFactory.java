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
package com.googlecode.aluminumproject.libraries.actions;

import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.configuration.ConfigurationParameters;
import com.googlecode.aluminumproject.libraries.Library;

/**
 * Creates {@link TestActionContribution test action contributions}.
 *
 * @author levi_h
 */
public class TestActionContributionFactory implements ActionContributionFactory {
	private Configuration configuration;

	private ActionContributionInformation information;

	private Library library;

	/**
	 * Creates a test action contribution factory.
	 */
	public TestActionContributionFactory() {
		information = new ActionContributionInformation("test", Object.class);
	}

	public void initialise(Configuration configuration, ConfigurationParameters parameters) {
		this.configuration = configuration;
	}

	/**
	 * Returns the configuration that this action contribution factory was initialised with.
	 *
	 * @return the configuration used
	 */
	public Configuration getConfiguration() {
		return configuration;
	}

	public ActionContributionInformation getInformation() {
		return information;
	}

	public Library getLibrary() {
		return library;
	}

	public void setLibrary(Library library) {
		this.library = library;
	}

	public ActionContribution create() {
		return new TestActionContribution();
	}
}