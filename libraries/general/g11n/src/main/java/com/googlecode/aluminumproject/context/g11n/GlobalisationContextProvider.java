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
package com.googlecode.aluminumproject.context.g11n;

import com.googlecode.aluminumproject.Logger;
import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.configuration.ConfigurationElementFactory;
import com.googlecode.aluminumproject.configuration.ConfigurationException;
import com.googlecode.aluminumproject.configuration.ConfigurationParameters;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.ContextEnricher;
import com.googlecode.aluminumproject.context.ContextException;
import com.googlecode.aluminumproject.utilities.GlobalisationUtilities;
import com.googlecode.aluminumproject.utilities.UtilityException;

import java.util.Locale;

/**
 * Enriches the context with a {@link GlobalisationContext globalisation context}. Before each template, a globalisation
 * context is created and made available as an implicit object (with the name {@value
 * GlobalisationContext#GLOBALISATION_CONTEXT_IMPLICIT_OBJECT}). After the template has been processed, the implicit
 * object is removed again.
 * <p>
 * The type of the locale provider that the globalisation context is created with can be configured with the {@value
 * #LOCALE_PROVIDER_CLASS} parameter. By default, a {@link ConstantLocaleProvider constant locale provider} will be
 * used with either the value of the configuration parameter {@value #LOCALE} or the {@link Locale#getDefault() default
 * locale}.
 *
 * @author levi_h
 */
public class GlobalisationContextProvider implements ContextEnricher {
	private LocaleProvider localeProvider;

	private final Logger logger;

	/**
	 * Creates a globalisation context provider.
	 */
	public GlobalisationContextProvider() {
		logger = Logger.get(getClass());
	}

	public void initialise(
			Configuration configuration, ConfigurationParameters parameters) throws ConfigurationException {
		createLocaleProvider(configuration, parameters);
	}

	private void createLocaleProvider(
			Configuration configuration, ConfigurationParameters parameters) throws ConfigurationException {
		String localeProviderClassName = parameters.getValue(LOCALE_PROVIDER_CLASS, null);

		if (localeProviderClassName == null) {
			logger.debug("no locale provider configured, using constant locale provider");

			Locale locale;

			String configuredLocale = parameters.getValue(LOCALE, null);

			if (configuredLocale == null) {
				logger.debug("no locale configured, using default locale");

				locale = Locale.getDefault();
			} else {
				logger.debug("using configured locale '", configuredLocale, "'");

				try {
					locale = GlobalisationUtilities.convertLocale(configuredLocale);
				} catch (UtilityException exception) {
					throw new ConfigurationException(exception, "can't use configured locale");
				}
			}

			localeProvider = new ConstantLocaleProvider(locale);
		} else {
			logger.debug("using configured locale provider of type ", localeProviderClassName);

			ConfigurationElementFactory configurationElementFactory = configuration.getConfigurationElementFactory();

			localeProvider = configurationElementFactory.instantiate(localeProviderClassName, LocaleProvider.class);
		}
	}

	public void beforeTemplate(Context context) throws ContextException {
		context.addImplicitObject(GlobalisationContext.GLOBALISATION_CONTEXT_IMPLICIT_OBJECT,
			new GlobalisationContext(localeProvider));
	}

	public void afterTemplate(Context context) throws ContextException {
		context.removeImplicitObject(GlobalisationContext.GLOBALISATION_CONTEXT_IMPLICIT_OBJECT);
	}

	/**
	 * The name of the configuration parameter that contains the class name of the locale provider that should be used.
	 */
	public final static String LOCALE_PROVIDER_CLASS = "library.g11n.locale_provider.class";

	/**
	 * The name of the configuration parameter that holds the locale that will be given to the constant locale provider
	 * when no locale provider is configured.
	 */
	public final static String LOCALE = "library.g11n.locale";
}