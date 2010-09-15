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

import com.googlecode.aluminumproject.annotations.ActionParameterInformation;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.ContextException;
import com.googlecode.aluminumproject.context.twitter.AccountProvider;
import com.googlecode.aluminumproject.context.twitter.Accounts;
import com.googlecode.aluminumproject.libraries.actions.AbstractAction;
import com.googlecode.aluminumproject.libraries.actions.ActionException;
import com.googlecode.aluminumproject.utilities.TwitterUtilities;
import com.googlecode.aluminumproject.utilities.UtilityException;
import com.googlecode.aluminumproject.utilities.environment.Response;
import com.googlecode.aluminumproject.writers.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * Updates a user's status.
 *
 * @author levi_h
 */
public class UpdateStatus extends AbstractAction {
	private String status;

	/**
	 * Creates an <em>update status</em> action.
	 */
	public UpdateStatus() {}

	/**
	 * Sets the text of the status update.
	 *
	 * @param status the status text to use
	 */
	@ActionParameterInformation(required = true)
	public void setStatus(String status) {
		this.status = status;
	}

	public void execute(Context context, Writer writer) throws ActionException, ContextException {
		Accounts accounts = (Accounts) context.getImplicitObject(AccountProvider.ACCOUNTS_IMPLICIT_OBJECT);

		String account = accounts.getAccount();

		if (account == null) {
			throw new ActionException("no account was set");
		}

		logger.debug("updating status of ", account);

		String method = "POST";
		String url = "http://api.twitter.com/1/statuses/update.xml";

		String token = accounts.getToken(account);
		String secret = accounts.getSecret(account);

		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("status", status);

		Response response;

		try {
			response = TwitterUtilities.sendRequest(method, url, token, secret, parameters);
		} catch (UtilityException exception) {
			throw new ActionException(exception, "can't make API call");
		}

		if (response.getStatusCode() != 200) {
			throw new ActionException("API call failed: ", new String(response.getBody()));
		}
	}
}