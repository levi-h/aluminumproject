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
package com.googlecode.aluminumproject.context.twitter;

import com.googlecode.aluminumproject.Logger;
import com.googlecode.aluminumproject.context.ContextException;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains all Twitter accounts that can be used within actions. It is made available as an implicit object by the
 * {@link AccountProvider account provider} (under the name {@value AccountProvider#ACCOUNTS_IMPLICIT_OBJECT}).
 * <p>
 * The accounts object also keeps track of the {@link #getAccount() current} account. If a single account has been
 * added, that one will be current as well; otherwise, the current account has to be {@link #setAccount(String) set}.
 *
 * @author levi_h
 */
public class Accounts {
	private Map<String, Token> accounts;

	private String account;

	private final Logger logger;

	/**
	 * Creates an accounts object.
	 */
	Accounts() {
		accounts = new HashMap<String, Token>();

		logger = Logger.get(getClass());
	}

	/**
	 * Adds an account.
	 *
	 * @param account the name of the account to add
	 * @param token the token of the account to add
	 * @param secret the secret of the account to add
	 */
	void addAccount(String account, String token, String secret) {
		logger.debug("added account '", account, "'");

		accounts.put(account, new Token(token, secret));
	}

	/**
	 * Returns the current account.
	 *
	 * @return the name of the current account (may be {@code null})
	 */
	public String getAccount() {
		return ((account == null) && (accounts.size() == 1)) ? accounts.keySet().iterator().next() : account;
	}

	/**
	 * Changes the current account.
	 *
	 * @param account the name of the account that should be the current one
	 * @throws ContextException when no account with the given name has been added
	 */
	public void setAccount(String account) throws ContextException {
		if (account != null) {
			checkAccount(account);
		}

		this.account = account;
	}

	/**
	 * Determines an account's token.
	 *
	 * @param account the name of the account whose token should be returned
	 * @return the token of the account with the given name
	 * @throws ContextException when no account with the given name has been added
	 */
	public String getToken(String account) throws ContextException {
		return getAccount(account).token;
	}

	/**
	 * Determines an account's secret.
	 *
	 * @param account the name of the account whose secret should be returned
	 * @return the secret of the account with the given name
	 * @throws ContextException when no account with the given name has been added
	 */
	public String getSecret(String account) throws ContextException {
		return getAccount(account).secret;
	}

	private Token getAccount(String account) throws ContextException {
		checkAccount(account);

		return accounts.get(account);
	}

	private void checkAccount(String account) {
		if (!accounts.containsKey(account)) {
			throw new ContextException("no such account: '", account, "'");
		}
	}

	private static class Token {
		private String token;
		private String secret;

		public Token(String token, String secret) {
			this.token = token;
			this.secret = secret;
		}
	}
}