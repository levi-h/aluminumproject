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
package com.googlecode.aluminumproject.libraries.xml.actions;

import com.googlecode.aluminumproject.annotations.Injected;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.ContextException;
import com.googlecode.aluminumproject.interceptors.ActionInterceptor;
import com.googlecode.aluminumproject.interceptors.InterceptionException;
import com.googlecode.aluminumproject.libraries.actions.ActionContribution;
import com.googlecode.aluminumproject.libraries.actions.ActionContributionOptions;
import com.googlecode.aluminumproject.libraries.actions.ActionException;
import com.googlecode.aluminumproject.libraries.actions.ActionFactory;
import com.googlecode.aluminumproject.libraries.actions.ActionParameter;
import com.googlecode.aluminumproject.libraries.actions.DefaultActionFactory;
import com.googlecode.aluminumproject.templates.ActionContext;
import com.googlecode.aluminumproject.templates.ActionContributionDescriptor;
import com.googlecode.aluminumproject.templates.ActionPhase;
import com.googlecode.aluminumproject.writers.Writer;

import java.util.EnumSet;
import java.util.Set;

/**
 * Adds a namespace to an {@link AbstractElement element}.
 *
 * @author levi_h
 */
public class Namespace implements ActionContribution {
	private @Injected ActionContributionDescriptor descriptor;

	/**
	 * Creates a namespace action contribution.
	 */
	public Namespace() {}

	public boolean canBeMadeTo(ActionFactory actionFactory) {
		return (actionFactory instanceof DefaultActionFactory) &&
			AbstractElement.class.isAssignableFrom(((DefaultActionFactory) actionFactory).getActionClass());
	}

	public void make(Context context, Writer writer, ActionParameter parameter, ActionContributionOptions options)
			throws ActionException, ContextException {
		String prefix = descriptor.getLibraryUrlAbbreviation();
		String url = (String) descriptor.getParameter().getValue(String.class, context);

		options.addInterceptor(new NamespaceAdder(prefix, url));
	}

	private static class NamespaceAdder implements ActionInterceptor {
		private String prefix;
		private String url;

		public NamespaceAdder(String prefix, String url) {
			this.prefix = prefix;
			this.url = url;
		}

		public Set<ActionPhase> getPhases() {
			return EnumSet.of(ActionPhase.CREATION);
		}

		public void intercept(ActionContext actionContext) throws InterceptionException {
			actionContext.proceed();

			((AbstractElement) actionContext.getAction()).addNamespace(prefix, url);
		}
	}
}