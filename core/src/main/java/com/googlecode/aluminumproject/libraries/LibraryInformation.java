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

/**
 * Information about a {@link Library}.
 * <p>
 * An important piece of information is the library URL, which, together with the library version, should be globally
 * unique. The preferred way of making sure that a library URL is unique is by choosing a host name that is owned by the
 * library creator.
 * <p>
 * Another library information aspect is whether or not the library supports dynamic library elements.
 *
 * @author levi_h
 */
public class LibraryInformation {
	private String url;
	private String version;

	private boolean dynamicActionsSupported;
	private boolean dynamicActionContributionsSupported;
	private boolean dynamicFunctionsSupported;

	/**
	 * Creates information for a library that does not support dynamic library elements.
	 *
	 * @param url the URL that uniquely identifies the library
	 * @param version the version of the library
	 */
	public LibraryInformation(String url, String version) {
		this(url, version, false, false, false);
	}

	/**
	 * Creates library information.
	 *
	 * @param url the URL that uniquely identifies the library
	 * @param version the version of the library
	 * @param dynamicActionsSupported whether or not the library supports dynamic actions
	 * @param dynamicActionContributionsSupported whether or not the library supports dynamic action contributions
	 * @param dynamicFunctionsSupported whether or not the library supports dynamic functions
	 */
	public LibraryInformation(String url, String version, boolean dynamicActionsSupported,
			boolean dynamicActionContributionsSupported, boolean dynamicFunctionsSupported) {
		this.url = url;
		this.version = version;

		this.dynamicActionsSupported = dynamicActionsSupported;
		this.dynamicActionContributionsSupported = dynamicActionContributionsSupported;
		this.dynamicFunctionsSupported = dynamicFunctionsSupported;
	}

	/**
	 * Returns the URL of the library.
	 *
	 * @return the library URL
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * Returns the version of the library.
	 *
	 * @return the library version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Returns a combination of the URL and the version of the library. As an example: if the library URL is {@code
	 * http://aluminumproject.googlecode.com/core} and the library has version 1.0, then the versioned library URL will
	 * be {@code http://aluminumproject.googlecode.com/core/1.0}.
	 *
	 * @return the library URL, with the library version appended to it
	 */
	public String getVersionedUrl() {
		return String.format("%s/%s", getUrl(), getVersion());
	}

	/**
	 * Returns whether this library supports dynamic actions or not.
	 *
	 * @return {@code true} if dynamic actions are supported, {@code false} otherwise
	 */
	public boolean supportsDynamicActions() {
		return dynamicActionsSupported;
	}

	/**
	 * Returns whether this library suppors dynamic action contributions or not.
	 *
	 * @return {@code true} if dynamic action contributions are supported, {@code false} otherwise
	 */
	public boolean supportsDynamicActionContributions() {
		return dynamicActionContributionsSupported;
	}

	/**
	 * Returns whether this library supports dynamic functions or not.
	 *
	 * @return {@code true} if dynamic functions are supported, {@code false} otherwise
	 */
	public boolean supportsDynamicFunctions() {
		return dynamicFunctionsSupported;
	}
}