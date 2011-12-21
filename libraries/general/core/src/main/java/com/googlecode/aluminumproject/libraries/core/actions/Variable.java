/*
 * Copyright 2010-2011 Levi Hoogenberg
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
package com.googlecode.aluminumproject.libraries.core.actions;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.annotations.Named;
import com.googlecode.aluminumproject.annotations.Typed;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.interceptors.AbstractActionInterceptor;
import com.googlecode.aluminumproject.libraries.actions.Action;
import com.googlecode.aluminumproject.libraries.actions.ActionContribution;
import com.googlecode.aluminumproject.libraries.actions.ActionContributionOptions;
import com.googlecode.aluminumproject.libraries.actions.ActionFactory;
import com.googlecode.aluminumproject.libraries.actions.ActionParameter;
import com.googlecode.aluminumproject.templates.ActionContext;
import com.googlecode.aluminumproject.templates.ActionPhase;
import com.googlecode.aluminumproject.utilities.Utilities;
import com.googlecode.aluminumproject.writers.DecorativeWriter;
import com.googlecode.aluminumproject.writers.ListWriter;
import com.googlecode.aluminumproject.writers.Writer;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

/**
 * Contains two action contributions that can be used to capture the output of an action and store it in a context
 * variable. When the contributed action does not have any output, no variable will be set. If it outputs more than one
 * value, the type of the context variable will be a {@link List list}.
 *
 * @author levi_h
 */
public class Variable {
	private Variable() {}

	/**
	 * Sets the name of the {@link Variable variable} that will contain the output of the action that it contributes to.
	 *
	 * @author levi_h
	 */
	@Named("variable name")
	@Typed("String")
	public static class Name implements ActionContribution {
		/**
		 * Creates a <em>variable name</em> action contribution.
		 */
		public Name() {}

		public boolean canBeMadeTo(ActionFactory actionFactory) {
			return true;
		}

		public void make(Context context, Writer writer, ActionParameter parameter, ActionContributionOptions options)
				throws AluminumException {
			options.addInterceptor(new NameInterceptor((String) parameter.getValue(String.class, context)));
		}
	}

	private static class NameInterceptor extends AbstractActionInterceptor {
		private String name;

		public NameInterceptor(String name) throws AluminumException {
			super(ActionPhase.EXECUTION);

			this.name = name;
		}

		public void intercept(ActionContext actionContext) throws AluminumException {
			ListWriter writer = new ListWriter(true);

			proceed(actionContext, writer);

			List<?> list = writer.getList();

			if (list.isEmpty()) {
				logger.debug("not setting variable '", name, "', the action did not write anything");
			} else {
				String scope = getScopes(actionContext.getContext()).remove(actionContext.getAction());

				Object value = (list.size() == 1) ? list.get(0) : list;

				if (scope == null) {
					logger.debug("setting context variable '", name, "' with value ", value, " in innermost scope");

					actionContext.getContext().setVariable(name, value);
				} else {
					logger.debug("setting context variable '", name, "' with value ", value, " in scope '", scope, "'");

					actionContext.getContext().setVariable(scope, name, value);
				}
			}
		}

		private void proceed(ActionContext actionContext, Writer writer) throws AluminumException {
			Writer originalWriter = actionContext.getWriter();

			DecorativeWriter decorativeWriter = getInnermostDecorativeWriter(originalWriter);
			Writer underlyingWriter;

			if (decorativeWriter == null) {
				underlyingWriter = null;

				actionContext.setWriter(writer);
			} else {
				underlyingWriter = decorativeWriter.getWriter();

				decorativeWriter.setWriter(writer);
			}

			actionContext.proceed();

			if (decorativeWriter == null) {
				actionContext.setWriter(originalWriter);
			} else {
				decorativeWriter.setWriter(underlyingWriter);
			}
		}

		private DecorativeWriter getInnermostDecorativeWriter(Writer writer) {
			DecorativeWriter decorativeWriter = null;

			while (writer instanceof DecorativeWriter) {
				decorativeWriter = (DecorativeWriter) writer;

				writer = decorativeWriter.getWriter();
			}

			return decorativeWriter;
		}
	}

	/**
	 * Sets the scope of the {@link Variable variable} that will contain the output of the action that it contributes
	 * to. If no scope is given, the variable will be set in the innermost scope.
	 *
	 * @author levi_h
	 */
	@Named("variable scope")
	@Typed("String")
	public static class Scope implements ActionContribution {
		/**
		 * Creates a <em>variable scope</em> action contribution.
		 */
		public Scope() {}

		public boolean canBeMadeTo(ActionFactory actionFactory) {
			return true;
		}

		public void make(Context context, Writer writer, ActionParameter parameter, ActionContributionOptions options)
				throws AluminumException {
			options.addInterceptor(new ScopeInterceptor((String) parameter.getValue(String.class, context)));
		}
	}

	private static class ScopeInterceptor extends AbstractActionInterceptor {
		private String scope;

		public ScopeInterceptor(String scope) throws AluminumException {
			super(ActionPhase.EXECUTION);

			this.scope = scope;
		}

		public void intercept(ActionContext actionContext) throws AluminumException {
			Map<Action, String> scopes = getScopes(actionContext.getContext());
			Action action = actionContext.getAction();

			scopes.put(action, scope);

			actionContext.proceed();

			if (scopes.containsKey(action)) {
				throw new AluminumException("the 'variable scope' action contribution",
					" can't be used without the 'variable name' one");
			}
		}
	}

	private static Map<Action, String> getScopes(Context context) throws AluminumException {
		if (!context.getImplicitObjectNames().contains(SCOPES)) {
			context.addImplicitObject(SCOPES, new IdentityHashMap<Action, String>());
		}

		return Utilities.typed(context.getImplicitObject(SCOPES));
	}

	private final static String SCOPES =
		Context.RESERVED_IMPLICIT_OBJECT_NAME_PREFIX + ".libraries.core.variable.scopes";
}