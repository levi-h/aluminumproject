/*
 * Copyright 2011 Levi Hoogenberg
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
package com.googlecode.aluminumproject.libraries.scripting;

import com.googlecode.aluminumproject.libraries.AbstractLibrary;
import com.googlecode.aluminumproject.libraries.LibraryInformation;
import com.googlecode.aluminumproject.utilities.ReflectionUtilities;
import com.googlecode.aluminumproject.utilities.environment.EnvironmentUtilities;

/**
 * Allows template authors to include scripts in their templates.
 *
 * @author levi_h
 */
public class ScriptingLibrary extends AbstractLibrary {
	private LibraryInformation information;

	/**
	 * Creates a scripting library.
	 */
	public ScriptingLibrary() {
		super(ReflectionUtilities.getPackageName(ScriptingLibrary.class));

		information = new LibraryInformation(URL, "s", EnvironmentUtilities.getVersion());
	}

	public LibraryInformation getInformation() {
		return information;
	}

	/** The URL of the scripting library: {@value}. */
	public final static String URL = "http://aluminumproject.googlecode.com/scripting";
}