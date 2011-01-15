/*
 * Copyright 2010 Levi Hoogenberg
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

import com.googlecode.aluminumproject.annotations.Ignored;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * An {@link AbstractAction abstract action} that allows dynamic parameters, which it stores and {@link
 * #getDynamicParameters() offers} to its subclasses.
 *
 * @author levi_h
 */
public abstract class AbstractDynamicallyParameterisableAction
		extends AbstractAction implements DynamicallyParameterisable {
	private @Ignored Map<String, ActionParameter> dynamicParameters;

	/**
	 * Creates an abstract dynamically parameterisable action.
	 */
	protected AbstractDynamicallyParameterisableAction() {
		dynamicParameters = new LinkedHashMap<String, ActionParameter>();
	}

	/**
	 * Returns all of the dynamic parameters that were {@link #setParameter(String, ActionParameter) added} to this
	 * action.
	 *
	 * @return all of this action's dynamic parameters
	 */
	protected Map<String, ActionParameter> getDynamicParameters() {
		return Collections.unmodifiableMap(dynamicParameters);
	}

	public void setParameter(String name, ActionParameter parameter) {
		dynamicParameters.put(name, parameter);
	}
}