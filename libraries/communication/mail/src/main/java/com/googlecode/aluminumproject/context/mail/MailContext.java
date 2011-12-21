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
package com.googlecode.aluminumproject.context.mail;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.context.Context;

/**
 * Contains mail-related information for mail library elements.
 * <p>
 * With a default configuration, a mail context is made available as an implicit object (by the {@link
 * MailContextProvider mail context provider}) and can be obtained by using a {@link #from(Context) convenience method}.
 *
 * @author levi_h
 */
public class MailContext {
	private SessionProvider sessionProvider;

	/**
	 * Creates a mail context.
	 *
	 * @param sessionProvider the session provider to use
	 */
	public MailContext(SessionProvider sessionProvider) {
		this.sessionProvider = sessionProvider;
	}

	/**
	 * Returns this mail context's session provider.
	 *
	 * @return the session provider in this mail context
	 */
	public SessionProvider getSessionProvider() {
		return sessionProvider;
	}

	/**
	 * Sets this mail context's session provider.
	 *
	 * @param sessionProvider the session provider to use
	 */
	public void setSessionProvider(SessionProvider sessionProvider) {
		this.sessionProvider = sessionProvider;
	}

	/**
	 * Extracts the mail context implicit object from a context.
	 *
	 * @param context the context to find the mail context in
	 * @return the mail context in the context
	 * @throws AluminumException when the context does not contain a mail context implicit object
	 */
	public static MailContext from(Context context) throws AluminumException {
		return (MailContext) context.getImplicitObject(MAIL_CONTEXT);
	}

	/**
	 * The name of the implicit object that is created by the {@link MailContextProvider mail context provider}:
	 * {@value}.
	 */
	public final static String MAIL_CONTEXT =
		Context.RESERVED_IMPLICIT_OBJECT_NAME_PREFIX + ".library.mail.mail_context";
}