/*
 * Copyright 2011 Levi Hoogenberg
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
package com.googlecode.aluminumproject.libraries.mail.actions;

import com.googlecode.aluminumproject.annotations.Required;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.libraries.actions.AbstractAction;
import com.googlecode.aluminumproject.libraries.actions.ActionException;
import com.googlecode.aluminumproject.writers.Writer;

import javax.mail.Address;
import javax.mail.Message.RecipientType;

/**
 * Adds a recipient to an e-mail.
 *
 * @author levi_h
 */
public class AddRecipient extends AbstractAction {
	private RecipientType type;
	private @Required Address address;

	/**
	 * Creates an <em>add recipient</em> action.
	 */
	public AddRecipient() {
		type = RecipientType.TO;
	}

	public void execute(Context context, Writer writer) throws ActionException {
		Send send = findAncestorOfType(Send.class);

		if (send == null) {
			throw new ActionException("'add recipient' actions can only be used inside 'send' actions");
		}

		send.addRecipient(type, address);
	}
}