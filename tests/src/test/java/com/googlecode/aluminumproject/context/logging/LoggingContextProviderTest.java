/*
 * Copyright 2013 Aluminum project
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
package com.googlecode.aluminumproject.context.logging;

import static com.googlecode.aluminumproject.context.logging.LoggingContext.LOGGING_CONTEXT;

import com.googlecode.aluminumproject.configuration.ConfigurationParameters;
import com.googlecode.aluminumproject.configuration.DefaultConfiguration;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.DefaultContext;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
@Test(groups = {"libraries", "libraries-logging", "fast"})
public class LoggingContextProviderTest {
	private Context context;

	private ConfigurationParameters configurationParameters;

	@BeforeMethod
	public void createContextAndConfigurationParameters() {
		context = new DefaultContext();

		configurationParameters = new ConfigurationParameters();
	}

	public void loggingContextShouldBeProvidedBeforeTemplate() {
		createLoggingContextProvider().beforeTemplate(context);

		assert context.getImplicitObjectNames().contains(LOGGING_CONTEXT);
	}

	public void loggingContextShouldBeInheritedWhenParentContextIsAvailable() {
		LoggingContextProvider loggingContextProvider = createLoggingContextProvider();

		loggingContextProvider.beforeTemplate(context);

		Context subcontext = context.createSubcontext();
		loggingContextProvider.beforeTemplate(subcontext);

		assert LoggingContext.from(context) == LoggingContext.from(subcontext);
	}

	public void loggerNamePrefixShouldDefaultToEmptyString() {
		createLoggingContextProvider().beforeTemplate(context);

		String loggerNamePrefix = LoggingContext.from(context).getLoggerNamePrefix();
		assert loggerNamePrefix != null;
		assert loggerNamePrefix.equals("");
	}

	public void loggerNamePrefixShouldBeConfigurable() {
		configurationParameters.addParameter(LoggingContextProvider.LOGGER_NAME_PREFIX, "test");

		createLoggingContextProvider().beforeTemplate(context);

		String loggerNamePrefix = LoggingContext.from(context).getLoggerNamePrefix();
		assert loggerNamePrefix != null;
		assert loggerNamePrefix.equals("test");
	}

	public void loggingContextShouldBeRemovedAfterTemplate() {
		LoggingContextProvider loggingContextProvider = createLoggingContextProvider();

		loggingContextProvider.beforeTemplate(context);
		loggingContextProvider.afterTemplate(context);

		assert !context.getImplicitObjectNames().contains(LOGGING_CONTEXT);
	}

	private LoggingContextProvider createLoggingContextProvider() {
		LoggingContextProvider loggingContextProvider = new LoggingContextProvider();
		loggingContextProvider.initialise(new DefaultConfiguration(configurationParameters));

		return loggingContextProvider;
	}
}