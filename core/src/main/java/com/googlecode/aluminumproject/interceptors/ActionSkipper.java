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

import com.googlecode.aluminumproject.libraries.actions.Action;
import com.googlecode.aluminumproject.templates.ActionContext;
import com.googlecode.aluminumproject.templates.ActionPhase;

/**
 * Skips both the {@link ActionPhase#CREATION creation} and {@link ActionPhase#EXECUTION execution} of an intercepted
 * {@link Action action}.
 */
public class ActionSkipper extends AbstractActionInterceptor {
	/**
	 * Creates an action skipper.
	 */
	public ActionSkipper() {
		super(ActionPhase.CREATION, ActionPhase.EXECUTION);
	}

	public void intercept(ActionContext actionContext) {}
}