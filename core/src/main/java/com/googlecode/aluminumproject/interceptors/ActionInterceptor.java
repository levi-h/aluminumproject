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
package com.googlecode.aluminumproject.interceptors;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.libraries.actions.Action;
import com.googlecode.aluminumproject.templates.ActionContext;
import com.googlecode.aluminumproject.templates.ActionPhase;

import java.util.Set;

/**
 * Intercepts one or more of the {@link ActionPhase phases} that cause an {@link Action action} to be executed.
 */
public interface ActionInterceptor {
	/**
	 * Returns the set of phases that this action interceptor intercepts.
	 *
	 * @return all phases that are intercepted by this action interceptor
	 */
	Set<ActionPhase> getPhases();

	/**
	 * Intercepts an action phase.
	 *
	 * @param actionContext the context of the action
	 * @throws AluminumException when the phase can't be intercepted
	 */
	void intercept(ActionContext actionContext) throws AluminumException;
}