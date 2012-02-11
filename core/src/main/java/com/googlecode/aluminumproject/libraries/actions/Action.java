/*
 * Copyright 2009-2012 Aluminum project
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
import com.googlecode.aluminumproject.libraries.LibraryElementCreation;
import com.googlecode.aluminumproject.writers.Writer;

/**
 * The base building block of a template.
 */
public interface Action extends LibraryElementCreation<ActionFactory> {
	/**
	 * Returns the parent of this action.
	 *
	 * @return this action's parent or {@code null} if this action does not have a parent
	 */
	Action getParent();

	/**
	 * Sets the action that contains this action.
	 *
	 * @param parent the parent of this action
	 */
	void setParent(Action parent);

	/**
	 * Sets the body of this action.
	 *
	 * @param body the action body
	 */
	void setBody(ActionBody body);

	/**
	 * Executes this action.
	 *
	 * @param context the context to execute in
	 * @param writer the writer to use
	 * @throws AluminumException when this action can't be executed
	 */
	void execute(Context context, Writer writer) throws AluminumException;
}