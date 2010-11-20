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
package com.googlecode.aluminumproject.libraries.core;

import com.googlecode.aluminumproject.libraries.AbstractLibrary;
import com.googlecode.aluminumproject.libraries.LibraryInformation;
import com.googlecode.aluminumproject.utilities.ReflectionUtilities;
import com.googlecode.aluminumproject.utilities.environment.EnvironmentUtilities;

/**
 * Contains actions that can be used in almost any template.
 *
 * @author levi_h
 */
public class CoreLibrary extends AbstractLibrary {
	private LibraryInformation information;

	/**
	 * Creates a core library.
	 */
	public CoreLibrary() {
		super(ReflectionUtilities.getPackageName(CoreLibrary.class));

		String url = "http://aluminumproject.googlecode.com/core";
		String version = EnvironmentUtilities.getVersion();

		information = new LibraryInformation(url, version);
	}

	public LibraryInformation getInformation() {
		return information;
	}
}