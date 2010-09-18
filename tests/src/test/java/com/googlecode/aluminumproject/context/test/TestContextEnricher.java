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
package com.googlecode.aluminumproject.context.test;

import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.configuration.ConfigurationParameters;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.ContextEnricher;

/**
 * A context enricher that can be used in tests.
 *
 * @author levi_h
 */
public class TestContextEnricher implements ContextEnricher {
	private Configuration configuration;

	private boolean beforeTemplateInvoked;
	private boolean afterTemplateInvoked;

	/**
	 * Creates a test context enricher.
	 */
	public TestContextEnricher() {}

	public void initialise(Configuration configuration, ConfigurationParameters parameters) {
		this.configuration = configuration;
	}

	public void beforeTemplate(Context context) {
		beforeTemplateInvoked = true;
	}

	public void afterTemplate(Context context) {
		afterTemplateInvoked = true;
	}

	/**
	 * Returns the configuration that this context enricher was initialised with.
	 *
	 * @return this context enricher's configuration
	 */
	public Configuration getConfiguration() {
		return configuration;
	}

	/**
	 * Determines whether this context enricher's {@link #beforeTemplate(Context) beforeTemplate method} was invoked or
	 * not.
	 *
	 * @return {@code true} if {@code beforeTemplate} was invoked, {@code false} otherwise
	 */
	public boolean isBeforeTemplateInvoked() {
		return beforeTemplateInvoked;
	}

	/**
	 * Determines whether or not this context enricher's {@link #afterTemplate(Context) afterTemplate method} was
	 * invoked.
	 *
	 * @return {@code true} if {@code afterTemplate} was invoked, {@code false} otherwise
	 */
	public boolean isAfterTemplateInvoked() {
		return afterTemplateInvoked;
	}
}