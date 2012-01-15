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

import static com.googlecode.aluminumproject.context.mail.MailContext.MAIL_CONTEXT;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.configuration.ConfigurationElementFactory;
import com.googlecode.aluminumproject.configuration.ConfigurationParameters;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.ContextEnricher;
import com.googlecode.aluminumproject.utilities.Logger;

/**
 * Provides a {@link MailContext mail context} to contexts. Before each template, either the current mail context (the
 * one from the parent context) or a new mail context will be added as an implicit object; after the template has been
 * processed, it will be removed. Note that a mail context will only be provided when a {@link SessionProvider session
 * provider} is available.
 * <p>
 * The mail context's session provider is determined by looking at the {@value #SESSION_PROVIDER_CLASS} configuration
 * parameter. If it is available, a new instance of that class will be used as session provider. If it isn't, the
 * provider will default to a {@link PropertySetBasedSessionProvider property set-based session provider} if the name of
 * the property set is configured (using configuration property {@value #SESSION_PROPERTY_SET_NAME}).
 */
public class MailContextProvider implements ContextEnricher {
	private SessionProvider sessionProvider;

	private final Logger logger;

	/**
	 * Creates a mail context provider.
	 */
	public MailContextProvider() {
		logger = Logger.get(getClass());
	}

	public void initialise(Configuration configuration) throws AluminumException {
		createSessionProvider(configuration);
	}

	private void createSessionProvider(Configuration configuration) throws AluminumException {
		ConfigurationParameters parameters = configuration.getParameters();

		String sessionProviderClassName = parameters.getValue(SESSION_PROVIDER_CLASS, null);

		if (sessionProviderClassName == null) {
			String propertySetName = parameters.getValue(SESSION_PROPERTY_SET_NAME, null);

			if (propertySetName == null) {
				logger.debug("no session provider was configured, not providing mail contexts");
			} else {
				logger.debug("using property-set based session provider with name '", propertySetName, "'");

				sessionProvider = new PropertySetBasedSessionProvider(propertySetName);
			}
		} else {
			logger.debug("using session provider ", sessionProviderClassName);

			ConfigurationElementFactory configurationElementFactory = configuration.getConfigurationElementFactory();

			sessionProvider = configurationElementFactory.instantiate(sessionProviderClassName, SessionProvider.class);
		}
	}

	public void disable() {}

	public void beforeTemplate(Context context) throws AluminumException {
		Context parentContext = context.getParent();

		if ((parentContext != null) && parentContext.getImplicitObjectNames().contains(MAIL_CONTEXT)) {
			logger.debug("inheriting mail context from parent");

			context.addImplicitObject(MAIL_CONTEXT, MailContext.from(parentContext));
		} else if (sessionProvider != null) {
			logger.debug("enriching context with new mail context");

			context.addImplicitObject(MAIL_CONTEXT, new MailContext(sessionProvider));
		}
	}

	public void afterTemplate(Context context) throws AluminumException {
		if (sessionProvider != null) {
			context.removeImplicitObject(MAIL_CONTEXT);
		}
	}

	/**
	 * The name of the configuration parameter that holds the fully qualified class name of the session provider that
	 * should be used.
	 */
	public final static String SESSION_PROVIDER_CLASS = "library.mail.session_provider.class";

	/**
	 * The name of the configuration parameter that determines which property set will be used for the default,
	 * property-set based session provider.
	 */
	public final static String SESSION_PROPERTY_SET_NAME = "library.mail.session_provider.property_set.name";
}