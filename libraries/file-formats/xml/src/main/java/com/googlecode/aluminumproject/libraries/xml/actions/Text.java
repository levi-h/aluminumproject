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

import com.googlecode.aluminumproject.annotations.Typed;
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
import com.googlecode.aluminumproject.templates.ActionPhase;
import com.googlecode.aluminumproject.writers.Writer;

import java.util.EnumSet;
import java.util.Set;

/**
 * Sets the text of an {@link Element element}.
 *
 * @author levi_h
 */
@Typed("String")
public class Text implements ActionContribution {
	/**
	 * Creates a <em>text</em> action contribution.
	 */
	public Text() {}

	public boolean canBeMadeTo(ActionFactory actionFactory) {
		return (actionFactory instanceof DefaultActionFactory) &&
			AbstractElement.class.isAssignableFrom(((DefaultActionFactory) actionFactory).getActionClass());
	}

	public void make(Context context, Writer writer,
			ActionParameter parameter, ActionContributionOptions options) throws ActionException {
		String text = (String) parameter.getValue(String.class, context);

		options.addInterceptor(new TextSetter(text));
	}

	private class TextSetter implements ActionInterceptor {
		private String text;

		public TextSetter(String text) {
			this.text = text;
		}

		public Set<ActionPhase> getPhases() {
			return EnumSet.of(ActionPhase.CREATION);
		}

		public void intercept(ActionContext actionContext) throws InterceptionException {
			actionContext.proceed();

			try {
				((AbstractElement) actionContext.getAction()).setText(text);
			} catch (ActionException exception) {
				throw new InterceptionException(exception, "can't set text");
			}
		}
	}
}