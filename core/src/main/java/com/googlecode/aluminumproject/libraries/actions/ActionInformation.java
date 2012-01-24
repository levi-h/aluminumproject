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

import com.googlecode.aluminumproject.libraries.Library;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

/**
 * Provides information about an {@link Action}.
 */
public class ActionInformation {
	private String name;

	private List<ActionParameterInformation> parameterInformation;
	private boolean dynamicallyParameterisable;

	private Type resultTypeWhenFunction;

	/**
	 * Creates action information.
	 *
	 * @param name the name of the action
	 * @param parameterInformation information about the parameters of the action
	 * @param dynamicallyParameterisable whether the action supports dynamic parameters or not
	 * @param resultTypeWhenFunction the result type of the action when it is used as if it were a function (may be
	 *                               {@code null} to indicate that the action can not be used as a function)
	 */
	public ActionInformation(String name, List<ActionParameterInformation> parameterInformation,
			boolean dynamicallyParameterisable, Type resultTypeWhenFunction) {
		this.name = name;

		this.parameterInformation = parameterInformation;
		this.dynamicallyParameterisable = dynamicallyParameterisable;

		this.resultTypeWhenFunction = resultTypeWhenFunction;
	}

	/**
	 * Returns the name of the action. It should be unique across a {@link Library library}.
	 *
	 * @return the action's name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns information about the parameters that the action accepts.
	 *
	 * @return information that describes the parameters of the action
	 */
	public List<ActionParameterInformation> getParameterInformation() {
		return Collections.unmodifiableList(parameterInformation);
	}

	/**
	 * Returns whether the action supports dynamic parameters or not.
	 *
	 * @return {@code true} if the action supports dynamic parameters or {@code false} if it doesn't
	 */
	public boolean isDynamicallyParameterisable() {
		return dynamicallyParameterisable;
	}

	/**
	 * Returns the action's result type when it is used as a function.
	 *
	 * @return the result type of the action when it is used as if it were a function (possibly {@code null})
	 */
	public Type getResultTypeWhenFunction() {
		return resultTypeWhenFunction;
	}
}