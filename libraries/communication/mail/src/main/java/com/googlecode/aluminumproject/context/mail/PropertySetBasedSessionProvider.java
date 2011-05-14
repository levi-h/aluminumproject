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

import com.googlecode.aluminumproject.configuration.ConfigurationException;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.ContextException;
import com.googlecode.aluminumproject.utilities.UtilityException;
import com.googlecode.aluminumproject.utilities.environment.EnvironmentUtilities;
import com.googlecode.aluminumproject.utilities.environment.PropertySetContainer;

import javax.mail.Session;

/**
 * A {@link SessionProvider session provider} that configures a {@link Session mail session} with properties that are
 * read from a property set.
 *
 * @author levi_h
 * @see PropertySetContainer
 */
public class PropertySetBasedSessionProvider implements SessionProvider {
	private Session session;

	/**
	 * Creates a property set-based session provider.
	 *
	 * @param name the name of the property set to use
	 * @throws ConfigurationException when the session can't be created
	 */
	public PropertySetBasedSessionProvider(String name) throws ConfigurationException {
		PropertySetContainer propertySetContainer = EnvironmentUtilities.getPropertySetContainer();

		try {
			session = Session.getInstance(propertySetContainer.readPropertySet(name));
		} catch (UtilityException exception) {
			throw new ConfigurationException(exception, "can't create mail session '", name, "'");
		}
	}

	public Session provide(Context context) throws ContextException {
		return session;
	}
}