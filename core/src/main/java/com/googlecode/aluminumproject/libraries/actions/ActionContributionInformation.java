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

import com.googlecode.aluminumproject.libraries.Library;

/**
 * Provides information about an {@link ActionContribution action contribution}.
 *
 * @author levi_h
 */
public class ActionContributionInformation {
	private String name;

	/**
	 * Creates action contribution information.
	 *
	 * @param name the name of the action contribution
	 */
	public ActionContributionInformation(String name) {
		this.name = name;
	}

	/**
	 * Returns the name of the action contribution. It should be unique for its {@link Library library}.
	 *
	 * @return the action contribution's unique name
	 */
	public String getName() {
		return name;
	}
}