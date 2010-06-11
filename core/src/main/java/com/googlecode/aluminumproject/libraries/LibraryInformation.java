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
 * An important piece of information is the library URL, which should be globally unique. The preferred way of making
 * sure that a library URL is unique is by choosing a host name that is owned by the library creator.
 * <p>
 * Another library information aspect is whether or not the library supports dynamic actions.
 *
 * @author levi_h
 */
public class LibraryInformation {
	private String url;
	private String displayName;

	private boolean dynamicActionsSupported;

	/**
	 * Creates information for a library that does not support dynamic actions.
	 *
	 * @param url the URL that uniquely identifies the library
	 * @param displayName the name that should be used when referring to the library
	 */
	public LibraryInformation(String url, String displayName) {
		this(url, displayName, false);
	}

	/**
	 * Creates library information.
	 *
	 * @param url the URL that uniquely identifies the library
	 * @param displayName the name that should be used when referring to the library
	 * @param dynamicActionsSupported whether or not the library supports dynamic actions
	 */
	public LibraryInformation(String url, String displayName, boolean dynamicActionsSupported) {
		this.url = url;
		this.displayName = displayName;

		this.dynamicActionsSupported = dynamicActionsSupported;
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
	 * Returns the display name of the library.
	 *
	 * @return the library's display name
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Returns whether this library supports dynamic actions or not.
	 *
	 * @return {@code true} if dynamic actions are supported, {@code false} otherwise
	 */
	public boolean supportsDynamicActions() {
		return dynamicActionsSupported;
	}
}