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
 * Adds an attribute to an {@link DynamicElement element}. The library URL abbreviation will be used as namespace
 * prefix; the name of the action contribution will become the attribute name.
 *
 * @author levi_h
 */
public class DynamicAttribute implements ActionContribution {
	private @Injected ActionContributionDescriptor descriptor;

	/**
	 * Creates a dynamic attribute action contribution.
	 */
	public DynamicAttribute() {}

	public boolean canBeMadeTo(ActionFactory actionFactory) {
		return (actionFactory instanceof DefaultActionFactory) &&
			AbstractElement.class.isAssignableFrom((((DefaultActionFactory) actionFactory).getActionClass()));
	}

	public void make(Context context, Writer writer, ActionParameter parameter, ActionContributionOptions options)
			throws ActionException {
		String prefix = descriptor.getLibraryUrlAbbreviation();
		String name = descriptor.getName();
		String value = (String) descriptor.getParameter().getValue(String.class, context);

		options.addInterceptor(new AttributeAdder(prefix, name, value));
	}

	private class AttributeAdder implements ActionInterceptor {
		private String prefix;
		private String name;
		private String value;

		public AttributeAdder(String prefix, String name, String value) {
			this.prefix = prefix;
			this.name = name;
			this.value = value;
		}

		public Set<ActionPhase> getPhases() {
			return EnumSet.of(ActionPhase.CREATION);
		}

		public void intercept(ActionContext actionContext) throws InterceptionException {
			actionContext.proceed();

			((AbstractElement) actionContext.getAction()).addAttribute(prefix, name, value);
		}
	}
}