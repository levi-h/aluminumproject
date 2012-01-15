/*
 * Copyright 2011-2012 Aluminum project
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
import com.googlecode.aluminumproject.utilities.environment.EnvironmentUtilities;
import com.googlecode.aluminumproject.utilities.environment.PropertySetContainer;

import javax.mail.Session;

/**
 * A {@link SessionProvider session provider} that configures a {@link Session mail session} with properties that are
 * read from a property set.
 *
 * @see PropertySetContainer
 */
public class PropertySetBasedSessionProvider implements SessionProvider {
	private Session session;

	/**
	 * Creates a property set-based session provider.
	 *
	 * @param name the name of the property set to use
	 * @throws AluminumException when the session can't be created
	 */
	public PropertySetBasedSessionProvider(String name) throws AluminumException {
		session = Session.getInstance(EnvironmentUtilities.getPropertySetContainer().readPropertySet(name));
	}

	public Session provide(Context context) throws AluminumException {
		return session;
	}
}