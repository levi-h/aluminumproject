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
package com.googlecode.aluminumproject.libraries.twitter.actions;

import com.googlecode.aluminumproject.annotations.ActionContributionInformation;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.ContextException;
import com.googlecode.aluminumproject.context.twitter.AccountProvider;
import com.googlecode.aluminumproject.context.twitter.Accounts;
import com.googlecode.aluminumproject.interceptors.ActionInterceptor;
import com.googlecode.aluminumproject.interceptors.InterceptionException;
import com.googlecode.aluminumproject.libraries.actions.ActionContribution;
import com.googlecode.aluminumproject.libraries.actions.ActionContributionOptions;
import com.googlecode.aluminumproject.libraries.actions.ActionException;
import com.googlecode.aluminumproject.libraries.actions.ActionFactory;
import com.googlecode.aluminumproject.libraries.actions.ActionParameter;
import com.googlecode.aluminumproject.templates.ActionContext;
import com.googlecode.aluminumproject.templates.ActionPhase;
import com.googlecode.aluminumproject.writers.Writer;

import java.util.EnumSet;
import java.util.Set;

/**
 * Sets the account with which a Twitter action should be executed.
 *
 * @author levi_h
 */
@ActionContributionInformation(parameterType = "String")
public class WithAccount implements ActionContribution {
	/**
	 * Creates a <em>with account</em> action contribution.
	 */
	public WithAccount() {}

	public boolean canBeMadeTo(ActionFactory actionFactory) {
		return true;
	}

	public void make(Context context, Writer writer,
			ActionParameter parameter, ActionContributionOptions options) throws ActionException {
		String account = (String) parameter.getValue(String.class, context);

		options.addInterceptor(new AccountSetter(account));
	}

	private static class AccountSetter implements ActionInterceptor {
		private String account;

		public AccountSetter(String account) {
			this.account = account;
		}

		public Set<ActionPhase> getPhases() {
			return EnumSet.of(ActionPhase.EXECUTION);
		}

		public void intercept(ActionContext actionContext) throws InterceptionException {
			try {
				Accounts accounts =
					(Accounts) actionContext.getContext().getImplicitObject(AccountProvider.ACCOUNTS_IMPLICIT_OBJECT);

				String originalAccount = accounts.getAccount();

				try {
					accounts.setAccount(account);

					actionContext.proceed();
				} finally {
					accounts.setAccount(originalAccount);
				}
			} catch (ContextException exception) {
				throw new InterceptionException(exception, "can't set account '", account, "'");
			}
		}
	}
}