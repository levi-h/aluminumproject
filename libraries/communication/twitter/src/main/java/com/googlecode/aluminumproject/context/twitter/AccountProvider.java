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
import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.configuration.ConfigurationException;
import com.googlecode.aluminumproject.configuration.ConfigurationParameters;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.ContextEnricher;
import com.googlecode.aluminumproject.context.ContextException;
import com.googlecode.aluminumproject.utilities.TwitterUtilities;
import com.googlecode.aluminumproject.utilities.UtilityException;
import com.googlecode.aluminumproject.utilities.environment.EnvironmentUtilities;
import com.googlecode.aluminumproject.utilities.environment.PropertySetContainer;
import com.googlecode.aluminumproject.utilities.environment.Response;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Provides an {@link Accounts accounts implicit object}, so that templates can use the Twitter API.
 * <p>
 * Accounts can be configured in the following ways:
 * <ul>
 * <li>By configuring a property set named {@value #ACCOUNTS_PROPERTY_SET}, with account names as keys and passwords as
 *     values. Upon initialisation, the account provider converts these accounts to access tokens; after that, the
 *     property set is no longer needed.
 * </ul>
 *
 * @author levi_h
 */
public class AccountProvider implements ContextEnricher {
	private Accounts accounts;

	private final Logger logger;

	/**
	 * Creates an account provider.
	 */
	public AccountProvider() {
		logger = Logger.get(getClass());
	}

	public void initialise(
			Configuration configuration, ConfigurationParameters parameters) throws ConfigurationException {
		accounts = new Accounts();

		try {
			PropertySetContainer propertySetContainer = EnvironmentUtilities.getPropertySetContainer();

			Properties tokens = propertySetContainer.containsPropertySet(TOKENS_PROPERTY_SET)
				? propertySetContainer.readPropertySet(TOKENS_PROPERTY_SET) : new Properties();

			if (propertySetContainer.containsPropertySet(ACCOUNTS_PROPERTY_SET)) {
				Properties accounts = propertySetContainer.readPropertySet(ACCOUNTS_PROPERTY_SET);

				if (convertAccounts(accounts, tokens)) {
					propertySetContainer.writePropertySet(TOKENS_PROPERTY_SET, tokens);
				}
			}

			if (!tokens.isEmpty()) {
				addAccounts(tokens);
			}
		} catch (UtilityException exception) {
			throw new ConfigurationException(exception, "can't initialise account provider");
		}
	}

	private boolean convertAccounts(Properties accounts, Properties tokens) throws UtilityException {
		Set<String> existingAccounts = new HashSet<String>();

		if (!tokens.isEmpty()) {
			Collections.addAll(existingAccounts, tokens.getProperty("accounts").split(" "));
		}

		int existingAccountCount = existingAccounts.size();

		for (String account: accounts.stringPropertyNames()) {
			if (existingAccounts.add(account)) {
				logger.debug("obtaining access token for account '", account, "'");

				String url = "https://api.twitter.com/oauth/access_token";

				Map<String, String> parameters = new HashMap<String, String>();
				parameters.put("x_auth_username", account);
				parameters.put("x_auth_password", accounts.getProperty(account));
				parameters.put("x_auth_mode", "client_auth");

				Response response = TwitterUtilities.sendRequest("POST", url, "", "", parameters);

				if (response.getStatusCode() == 200) {
					Map<String, String> tokenInformation = parseAccessToken(new String(response.getBody()));

					tokens.setProperty("accounts", tokens.containsKey("accounts")
						? String.format("%s %s", tokens.getProperty("accounts"), account) : account);

					tokens.setProperty(String.format("%s.token", account), tokenInformation.get("oauth_token"));
					tokens.setProperty(String.format("%s.secret", account), tokenInformation.get("oauth_token_secret"));
				} else {
					throw new UtilityException("can't obtain access token for account '", account, "'");
				}
			}
		}

		return existingAccounts.size() > existingAccountCount;
	}

	private Map<String, String> parseAccessToken(String accessToken) throws UtilityException {
		Map<String, String> elements = new HashMap<String, String>();

		for (String element: accessToken.split("&")) {
			String[] parts = element.split("=");

			try {
				elements.put(parts[0], URLDecoder.decode(parts[1], "utf-8"));
			} catch (UnsupportedEncodingException exception) {
				throw new UtilityException(exception, "can't decode '", parts[1], "'");
			}
		}

		return elements;
	}

	private void addAccounts(Properties tokens) {
		for (String account: tokens.getProperty("accounts").split(" ")) {
			String token = tokens.getProperty(String.format("%s.token", account));
			String secret = tokens.getProperty(String.format("%s.secret", account));

			accounts.addAccount(account, token, secret);
		}
	}

	public void beforeTemplate(Context context) throws ContextException {
		context.addImplicitObject(ACCOUNTS_IMPLICIT_OBJECT, accounts);
	}

	public void afterTemplate(Context context) throws ContextException {
		context.removeImplicitObject(ACCOUNTS_IMPLICIT_OBJECT);
	}

	/** The name of the property set that contains all convertible accounts: {@value}. */
	public final static String ACCOUNTS_PROPERTY_SET = "library.twitter.accounts";

	/** The name of the property set that contains all converted accounts: {@value}. */
	private final static String TOKENS_PROPERTY_SET = "library.twitter.tokens";

	/** The name of the implicit object that contains all available accounts: {@value}. */
	public final static String ACCOUNTS_IMPLICIT_OBJECT =
		Context.RESERVED_IMPLICIT_OBJECT_NAME_PREFIX + ".library.twitter.accounts";
}