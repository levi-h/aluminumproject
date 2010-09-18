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
package com.googlecode.aluminumproject.libraries.functions;

import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.configuration.ConfigurationParameters;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.libraries.Library;

import java.util.Collections;
import java.util.List;

/**
 * Creates {@link TestFunction test functions}.
 *
 * @author levi_h
 */
public class TestFunctionFactory implements FunctionFactory {
	private Configuration configuration;

	private FunctionInformation information;

	private Library library;

	/**
	 * Creates a test function factory.
	 */
	public TestFunctionFactory() {
		information = new FunctionInformation("test", Void.TYPE, Collections.<FunctionArgumentInformation>emptyList());
	}

	public void initialise(Configuration configuration, ConfigurationParameters parameters) {
		this.configuration = configuration;
	}

	/**
	 * Returns the configuration that this function factory was initialised with.
	 *
	 * @return the configuration used
	 */
	public Configuration getConfiguration() {
		return configuration;
	}

	public FunctionInformation getInformation() {
		return information;
	}

	public Library getLibrary() {
		return library;
	}

	public void setLibrary(Library library) {
		this.library = library;
	}

	public Function create(List<FunctionArgument> arguments, Context context) throws FunctionException {
		return new TestFunction();
	}
}