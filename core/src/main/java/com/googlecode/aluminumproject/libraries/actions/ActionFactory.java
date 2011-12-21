/*
 * Copyright 2009-2011 Levi Hoogenberg
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
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.libraries.LibraryElement;

import java.util.Map;

/**
 * Creates an {@link Action action}.
 *
 * @author levi_h
 */
public interface ActionFactory extends LibraryElement {
	/**
	 * Returns information about the action.
	 *
	 * @return the information that describes the action
	 */
	ActionInformation getInformation();

	/**
	 * Creates an action.
	 *
	 * @param parameters the parameters for the action
	 * @param context the context in which the action will execute
	 * @return the new action
	 * @throws AluminumException when the action can't be created
	 */
	Action create(Map<String, ActionParameter> parameters, Context context) throws AluminumException;
}