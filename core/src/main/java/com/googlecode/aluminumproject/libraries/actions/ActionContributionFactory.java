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

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.libraries.LibraryElement;

/**
 * Creates an {@link ActionContribution action contribution}.
 */
public interface ActionContributionFactory extends LibraryElement {
	/**
	 * Returns information about the action contribution.
	 *
	 * @return the information that describes the action contribution
	 */
	ActionContributionInformation getInformation();

	/**
	 * Creates the action contribution.
	 *
	 * @return the new action contribution
	 * @throws AluminumException when the action contribution can't be created
	 */
	ActionContribution create() throws AluminumException;
}