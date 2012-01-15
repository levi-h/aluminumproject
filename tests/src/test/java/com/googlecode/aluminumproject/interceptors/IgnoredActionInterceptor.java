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
import com.googlecode.aluminumproject.annotations.Ignored;
import com.googlecode.aluminumproject.templates.ActionContext;
import com.googlecode.aluminumproject.templates.ActionPhase;

import java.util.EnumSet;
import java.util.Set;

/**
 * An action interceptor that should be ignored when all action interceptors in a package are added.
 */
@Ignored
public class IgnoredActionInterceptor implements ActionInterceptor {
	/**
	 * Creates an ignored action interceptor.
	 */
	public IgnoredActionInterceptor() {}

	public Set<ActionPhase> getPhases() {
		return EnumSet.allOf(ActionPhase.class);
	}

	public void intercept(ActionContext actionContext) throws AluminumException {
		actionContext.proceed();
	}
}