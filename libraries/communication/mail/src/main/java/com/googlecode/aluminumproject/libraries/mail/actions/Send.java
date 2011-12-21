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

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.annotations.Ignored;
import com.googlecode.aluminumproject.annotations.Required;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.mail.MailContext;
import com.googlecode.aluminumproject.libraries.actions.AbstractAction;
import com.googlecode.aluminumproject.writers.Writer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.mail.Address;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

/**
 * Sends an e-mail.
 *
 * @author levi_h
 */
public class Send extends AbstractAction {
	private Address from;
	private @Required String subject;

	private @Ignored Map<RecipientType, List<Address>> recipients;

	/**
	 * Creates a <em>send</em> action.
	 */
	public Send() {
		recipients = new HashMap<RecipientType, List<Address>>();
	}

	/**
	 * Adds a recipient for the e-mail.
	 *
	 * @param type the type of the new recipient
	 * @param recipient the recipient to add
	 */
	void addRecipient(RecipientType type, Address recipient) {
		if (!recipients.containsKey(type)) {
			recipients.put(type, new LinkedList<Address>());
		}

		recipients.get(type).add(recipient);
	}

	public void execute(Context context, Writer writer) throws AluminumException {
		Session session = MailContext.from(context).getSessionProvider().provide(context);

		sendMessage(createMessage(context, writer, session), session);
	}

	private MimeMessage createMessage(
			Context context, Writer writer, Session session) throws AluminumException {
		String content = getBodyText(context, writer);

		if (!recipients.containsKey(RecipientType.TO)) {
			throw new AluminumException("at least one 'to' address is needed");
		}

		MimeMessage message = new MimeMessage(session);

		try {
			if (from == null) {
				message.setFrom();
			} else {
				message.setFrom(from);
			}

			for (RecipientType type: recipients.keySet()) {
				for (Address address: recipients.get(type)) {
					message.addRecipient(type, address);
				}
			}

			message.setSubject(subject);
			message.setContent(content, "text/plain");
		} catch (MessagingException exception) {
			throw new AluminumException(exception, "can't create message");
		}

		return message;
	}

	private void sendMessage(MimeMessage message, Session session) throws AluminumException {
		try {
			Transport transport = session.getTransport();

			try {
				transport.connect();

				transport.sendMessage(message, message.getAllRecipients());
			} finally {
				if (transport.isConnected()) {
					transport.close();
				}
			}
		} catch (MessagingException exception) {
			throw new AluminumException(exception, "can't send message");
		}
	}
}