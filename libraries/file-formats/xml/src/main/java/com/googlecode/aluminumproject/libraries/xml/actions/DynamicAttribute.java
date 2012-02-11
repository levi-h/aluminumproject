/*
 * Copyright 2010-2012 Aluminum project
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

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.annotations.Injected;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.interceptors.AbstractActionInterceptor;
import com.googlecode.aluminumproject.libraries.AbstractLibraryElementCreation;
import com.googlecode.aluminumproject.libraries.actions.ActionContribution;
import com.googlecode.aluminumproject.libraries.actions.ActionContributionFactory;
import com.googlecode.aluminumproject.libraries.actions.ActionContributionOptions;
import com.googlecode.aluminumproject.libraries.actions.ActionFactory;
import com.googlecode.aluminumproject.libraries.actions.ActionParameter;
import com.googlecode.aluminumproject.libraries.actions.ClassBasedActionFactory;
import com.googlecode.aluminumproject.templates.ActionContext;
import com.googlecode.aluminumproject.templates.ActionContributionDescriptor;
import com.googlecode.aluminumproject.templates.ActionPhase;
import com.googlecode.aluminumproject.writers.Writer;

@SuppressWarnings("javadoc")
public class DynamicAttribute
		extends AbstractLibraryElementCreation<ActionContributionFactory> implements ActionContribution {
	private @Injected ActionContributionDescriptor descriptor;

	public boolean canBeMadeTo(ActionFactory actionFactory) {
		return (actionFactory instanceof ClassBasedActionFactory) &&
			AbstractElement.class.isAssignableFrom((((ClassBasedActionFactory) actionFactory).getActionClass()));
	}

	public void make(Context context, Writer writer, ActionParameter parameter, ActionContributionOptions options)
			throws AluminumException {
		String prefix = descriptor.getLibraryUrlAbbreviation();
		String name = descriptor.getName();
		String value = (String) descriptor.getParameter().getValue(String.class, context);

		options.addInterceptor(new AttributeAdder(prefix, name, value));
	}

	private class AttributeAdder extends AbstractActionInterceptor {
		private String prefix;
		private String name;
		private String value;

		public AttributeAdder(String prefix, String name, String value) {
			super(ActionPhase.CREATION);

			this.prefix = prefix;
			this.name = name;
			this.value = value;
		}

		public void intercept(ActionContext actionContext) throws AluminumException {
			actionContext.proceed();

			((AbstractElement) actionContext.getAction()).addAttribute(prefix, name, value);
		}
	}
}