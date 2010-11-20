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

import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.writers.Writer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * An action that supports dynamic parameters.
 *
 * @author levi_h
 */
public class DynamicallyParameterisableAction extends AbstractAction implements DynamicallyParameterisable {
	private String regularParameter;

	private Map<String, ActionParameter> dynamicParameters;

	/**
	 * Creates a dynamically parameterisable action.
	 */
	public DynamicallyParameterisableAction() {
		dynamicParameters = new HashMap<String, ActionParameter>();
	}

	/**
	 * Returns the value of the regular parameter.
	 *
	 * @return this action's regular parameter's value
	 */
	public String getRegularParameter() {
		return regularParameter;
	}

	/**
	 * Sets a regular, non-dynamic parameter.
	 *
	 * @param regularParameter the value to use for the regular parameter
	 */
	public void setRegularParameter(String regularParameter) {
		this.regularParameter = regularParameter;
	}

	/**
	 * Returns all dynamic parameters that were {@link #setParameter(String, ActionParameter) set}.
	 *
	 * @return all of this action's dynamic parameters
	 */
	public Map<String, ActionParameter> getDynamicParameters() {
		return Collections.unmodifiableMap(dynamicParameters);
	}

	public void setParameter(String name, ActionParameter parameter) {
		dynamicParameters.put(name, parameter);
	}

	public void execute(Context context, Writer writer) {}
}