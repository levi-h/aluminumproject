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

import com.googlecode.aluminumproject.configuration.ConfigurationParameters;
import com.googlecode.aluminumproject.configuration.DefaultConfiguration;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.ContextException;
import com.googlecode.aluminumproject.context.DefaultContext;
import com.googlecode.aluminumproject.utilities.environment.EnvironmentUtilities;

import java.util.Properties;

import javax.mail.Session;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("all")
@Test(groups = {"libraries", "libraries-mail", "fast"})
public class MailContextProviderTest {
	private Context context;

	@BeforeClass
	public void createMailPropertySets() {
		EnvironmentUtilities.getPropertySetContainer().writePropertySet("mail", new Properties());

		Properties debugPropertySet = new Properties();
		debugPropertySet.setProperty("mail.debug", "true");
		EnvironmentUtilities.getPropertySetContainer().writePropertySet("mail-debug", debugPropertySet);
	}

	@BeforeMethod
	public void createContext() {
		context = new DefaultContext();
	}

	public void mailContextWithoutSessionProviderShouldNotBeProvided() {
		createMailContextProvider(new ConfigurationParameters()).beforeTemplate(context);

		assert !context.getImplicitObjectNames().contains(MailContext.MAIL_CONTEXT_IMPLICIT_OBJECT);
	}

	public void mailContextWithSessionProviderShouldBeProvided() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(MailContextProvider.SESSION_PROPERTY_SET_NAME, "mail");

		createMailContextProvider(parameters).beforeTemplate(context);

		assert context.getImplicitObjectNames().contains(MailContext.MAIL_CONTEXT_IMPLICIT_OBJECT);
	}

	public static class DefaultSessionProvider implements SessionProvider {
		public Session provide(Context context) throws ContextException {
			return Session.getDefaultInstance(new Properties());
		}
	}

	@Test(dependsOnMethods = "mailContextWithSessionProviderShouldBeProvided")
	public void sessionProviderShouldBeConfigurable() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(MailContextProvider.SESSION_PROVIDER_CLASS, DefaultSessionProvider.class.getName());

		createMailContextProvider(parameters).beforeTemplate(context);

		assert MailContext.from(context).getSessionProvider() instanceof DefaultSessionProvider;
	}

	@Test(dependsOnMethods = "mailContextWithSessionProviderShouldBeProvided")
	public void sessionProviderShouldDefaultToPropertySetBasedOne() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(MailContextProvider.SESSION_PROPERTY_SET_NAME, "mail");

		createMailContextProvider(parameters).beforeTemplate(context);

		assert MailContext.from(context).getSessionProvider() instanceof PropertySetBasedSessionProvider;
	}

	@Test(dependsOnMethods = "sessionProviderShouldDefaultToPropertySetBasedOne")
	public void propertySetOfDefaultSessionProviderShouldBeConfigurable() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(MailContextProvider.SESSION_PROPERTY_SET_NAME, "mail-debug");

		createMailContextProvider(parameters).beforeTemplate(context);

		assert MailContext.from(context).getSessionProvider().provide(context).getDebug();
	}

	@Test(dependsOnMethods = "mailContextWithSessionProviderShouldBeProvided")
	public void mailContextShouldBeInheritedFromParentContext() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(MailContextProvider.SESSION_PROPERTY_SET_NAME, "mail");

		MailContextProvider mailContextProvider = createMailContextProvider(parameters);

		mailContextProvider.beforeTemplate(context);

		Context subcontext = context.createSubcontext();
		mailContextProvider.beforeTemplate(subcontext);

		assert MailContext.from(context) == MailContext.from(subcontext);
	}

	private MailContextProvider createMailContextProvider(ConfigurationParameters parameters) {
		MailContextProvider mailContextProvider = new MailContextProvider();
		mailContextProvider.initialise(new DefaultConfiguration(parameters));

		return mailContextProvider;
	}
}