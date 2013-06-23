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
package com.googlecode.aluminumproject.libraries.logging;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.libraries.AbstractLibrary;
import com.googlecode.aluminumproject.libraries.LibraryInformation;
import com.googlecode.aluminumproject.utilities.ReflectionUtilities;
import com.googlecode.aluminumproject.utilities.environment.EnvironmentUtilities;

@SuppressWarnings("javadoc")
public class LoggingLibrary extends AbstractLibrary {
	private LibraryInformation information;

	public LoggingLibrary() {
		super(ReflectionUtilities.getPackageName(LoggingLibrary.class));
	}

	@Override
	public void initialise(Configuration configuration) throws AluminumException {
		super.initialise(configuration);

		information = new LibraryInformation(URL, "l", EnvironmentUtilities.getVersion());
	}

	public LibraryInformation getInformation() {
		return information;
	}

	public final static String URL = "http://aluminumproject.googlecode.com/logging";
}