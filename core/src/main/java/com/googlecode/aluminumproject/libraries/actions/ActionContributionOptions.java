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

import com.googlecode.aluminumproject.interceptors.ActionInterceptor;

/**
 * Ways in which an {@link ActionContribution action contribution} can contribute to an {@link Action action}.
 *
 * @author levi_h
 */
public interface ActionContributionOptions {
	/**
	 * Sets a parameter for the action.
	 *
	 * @param name the name of the parameter
	 * @param parameter the parameter that should be given to the action
	 */
	void setParameter(String name, ActionParameter parameter);

	/**
	 * Indicates that the action should not be executed.
	 */
	void skipAction();

	/**
	 * Adds an interceptor. It's not possible for the interceptor to intercept the contribution phase, since that's the
	 * current phase.
	 *
	 * @param interceptor the interceptor that should be used when creating or executing the action
	 */
	void addInterceptor(ActionInterceptor interceptor);
}