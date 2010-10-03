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

import com.googlecode.aluminumproject.annotations.ActionParameterInformation;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.writers.Writer;

/**
 * An action that takes an {@link ActionParameter} as parameter.
 *
 * @author levi_h
 */
public class ParameterAction extends AbstractAction {
	/**
	 * Creates a parameter action.
	 */
	public ParameterAction() {}

	/**
	 * Sets the parameter that contains the description of this action.
	 *
	 * @param descriptionParameter the parameter containing the description to use
	 */
	@ActionParameterInformation(type = "String")
	public void setDescriptionParameter(ActionParameter descriptionParameter) {}

	public void execute(Context context, Writer writer) {}
}