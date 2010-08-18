/*
 * Copyright 2009-2010 Levi Hoogenberg
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
package com.googlecode.aluminumproject.templates;

import static com.googlecode.aluminumproject.configuration.DefaultConfiguration.CONFIGURATION_ELEMENT_PACKAGES;

import com.googlecode.aluminumproject.Logger;
import com.googlecode.aluminumproject.annotations.Ignored;
import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.configuration.ConfigurationElementFactory;
import com.googlecode.aluminumproject.configuration.ConfigurationException;
import com.googlecode.aluminumproject.configuration.ConfigurationParameters;
import com.googlecode.aluminumproject.expressions.ExpressionFactory;
import com.googlecode.aluminumproject.interceptors.ActionInterceptor;
import com.googlecode.aluminumproject.libraries.Library;
import com.googlecode.aluminumproject.libraries.LibraryException;
import com.googlecode.aluminumproject.libraries.LibraryInformation;
import com.googlecode.aluminumproject.libraries.actions.ActionContribution;
import com.googlecode.aluminumproject.libraries.actions.ActionContributionFactory;
import com.googlecode.aluminumproject.libraries.actions.ActionFactory;
import com.googlecode.aluminumproject.libraries.actions.ActionParameter;
import com.googlecode.aluminumproject.utilities.ConfigurationUtilities;
import com.googlecode.aluminumproject.utilities.ReflectionUtilities;
import com.googlecode.aluminumproject.utilities.UtilityException;
import com.googlecode.aluminumproject.utilities.finders.TypeFinder;
import com.googlecode.aluminumproject.utilities.finders.TypeFinder.TypeFilter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A {@link TemplateElementFactory template element factory} that creates default {@link TemplateElement template
 * elements}.
 * <p>
 * It's possible to configure a number of {@link ActionInterceptor action interceptors}. These will intercept all
 * actions (as opposed to action interceptors that are supplied by {@link ActionContribution action contributions},
 * which intercept a single action only). To add action interceptors, provide a configuration parameter named {@value
 * #ACTION_INTERCEPTOR_PACKAGES} or {@value
 * com.googlecode.aluminumproject.configuration.DefaultConfiguration#CONFIGURATION_ELEMENT_PACKAGES} that contains one
 * or more package names. All non-abstract action interceptors in these packages that are not annotated with {@link
 * Ignored &#64;Ignored} will be created and used.
 *
 * @author levi_h
 */
public class DefaultTemplateElementFactory implements TemplateElementFactory {
	private Configuration configuration;

	private List<ActionInterceptor> actionInterceptors;

	private final Logger logger;

	/**
	 * Creates a default template element factory.
	 */
	public DefaultTemplateElementFactory() {
		logger = Logger.get(getClass());
	}

	public void initialise(
			Configuration configuration, ConfigurationParameters parameters) throws ConfigurationException {
		this.configuration = configuration;

		createActionInterceptors(parameters);
	}

	private void createActionInterceptors(ConfigurationParameters parameters) throws ConfigurationException {
		actionInterceptors = new LinkedList<ActionInterceptor>();

		Set<String> actionInterceptorPackages = new HashSet<String>();

		Collections.addAll(actionInterceptorPackages, parameters.getValues(ACTION_INTERCEPTOR_PACKAGES));
		Collections.addAll(actionInterceptorPackages, parameters.getValues(CONFIGURATION_ELEMENT_PACKAGES));

		if (!actionInterceptorPackages.isEmpty()) {
			logger.debug("action interceptors will be looked for in ", actionInterceptorPackages);

			List<Class<?>> actionInterceptorClasses;

			try {
				actionInterceptorClasses = TypeFinder.find(new TypeFilter() {
					public boolean accepts(Class<?> type) {
						return ActionInterceptor.class.isAssignableFrom(type) && !ReflectionUtilities.isAbstract(type)
						&& !type.isAnnotationPresent(Ignored.class);
					}
				}, actionInterceptorPackages.toArray(new String[actionInterceptorPackages.size()]));
			} catch (UtilityException exception) {
				throw new ConfigurationException(exception, "can't find action interceptors");
			}

			ConfigurationElementFactory configurationElementFactory = configuration.getConfigurationElementFactory();

			for (Class<?> actionInterceptorClass: actionInterceptorClasses) {
				logger.debug("adding action interceptor of type ", actionInterceptorClass.getName());

				ActionInterceptor actionInterceptor =
					configurationElementFactory.instantiate(actionInterceptorClass.getName(), ActionInterceptor.class);

				actionInterceptors.add(actionInterceptor);
			}
		}
	}

	/**
	 * Returns the configuration that was injected into this template element factory.
	 *
	 * @return the configuration to use
	 */
	protected Configuration getConfiguration() {
		return configuration;
	}

	/**
	 * Returns all configured action interceptors.
	 *
	 * @return a list with all of the action interceptors that were found in the configured action interceptor packages
	 */
	protected List<ActionInterceptor> getActionInterceptors() {
		return actionInterceptors;
	}

	public ActionElement createActionElement(ActionDescriptor actionDescriptor,
			Map<String, ActionParameter> parameters, List<ActionContributionDescriptor> contributionDescriptors,
			Map<String, String> libraryUrlAbbreviations) throws TemplateException {
		String libraryUrlAbbreviation = actionDescriptor.getLibraryUrlAbbreviation();

		if (!libraryUrlAbbreviations.containsKey(libraryUrlAbbreviation)) {
			throw new TemplateException("unknown library URL abbreviation: '", libraryUrlAbbreviation, "'");
		}

		String libraryUrl = libraryUrlAbbreviations.get(libraryUrlAbbreviation);
		String name = actionDescriptor.getName();

		logger.debug("creating action element, library URL: '", libraryUrl, "', name: '", name, "', ",
			"parameters: ", parameters, ", contributions: ", contributionDescriptors);

		Library library = findLibrary(libraryUrl);
		logger.debug("found library for URL '", libraryUrl, "': ", library);

		ActionFactory actionFactory = findActionFactory(library, name);
		logger.debug("found action factory for action with name '", name, "': ", actionFactory);

		Map<ActionContributionFactory, ActionParameter> actionContributionFactories =
			createActionContributionFactories(contributionDescriptors, libraryUrlAbbreviations);
		logger.debug("created action contributions factories ",
			"for action with name '", name, "': ", actionContributionFactories);

		return createActionElement(actionFactory, parameters, actionContributionFactories,
			Collections.unmodifiableList(actionInterceptors), libraryUrlAbbreviations);
	}

	private Map<ActionContributionFactory, ActionParameter> createActionContributionFactories(
			List<ActionContributionDescriptor> contributionDescriptors,
			Map<String, String> libraryUrlAbbreviations) throws TemplateException {
		Map<ActionContributionFactory, ActionParameter> actionContributionFactories =
			new LinkedHashMap<ActionContributionFactory, ActionParameter>();

		for (ActionContributionDescriptor descriptor: contributionDescriptors) {
			String libraryUrlAbbreviation = descriptor.getLibraryUrlAbbreviation();

			if (!libraryUrlAbbreviations.containsKey(libraryUrlAbbreviation)) {
				throw new TemplateException("unknown library URL abbreviation: '", libraryUrlAbbreviation, "'");
			}

			Library library = findLibrary(libraryUrlAbbreviations.get(libraryUrlAbbreviation));
			ActionContributionFactory actionContributionFactory =
				findActionContributionFactory(library, descriptor.getName());

			actionContributionFactories.put(actionContributionFactory, descriptor.getParameter());
		}

		return actionContributionFactories;
	}

	private Library findLibrary(String libraryUrl) throws TemplateException {
		Library library = ConfigurationUtilities.findLibrary(configuration, libraryUrl);

		if (library == null) {
			throw new TemplateException("can't find library with URL '", libraryUrl, "'");
		} else {
			return library;
		}
	}

	/**
	 * Finds an action factory for an action with a certain name. If no action factory can be found for the given action
	 * name, the library will be asked to provide a dynamic action factory.
	 *
	 * @param library the library to search in
	 * @param name the name of the action to find an action factory for
	 * @return the action factory for the action with the given name or a dynamic action factory
	 * @throws TemplateException when no action factory for an action with the given name can be found in the library
	 */
	protected ActionFactory findActionFactory(Library library, String name) throws TemplateException {
		ActionFactory actionFactory = null;

		Iterator<ActionFactory> actionFactories = library.getActionFactories().iterator();

		while (actionFactories.hasNext() && (actionFactory == null)) {
			actionFactory = actionFactories.next();

			if (!actionFactory.getInformation().getName().equals(name)) {
				actionFactory = null;
			}
		}

		if (actionFactory == null) {
			LibraryInformation libraryInformation = library.getInformation();

			if (libraryInformation.supportsDynamicActions()) {
				try {
					actionFactory = library.getDynamicActionFactory(name);
				} catch (LibraryException exception) {
					throw new TemplateException("can't get dynamic action factory for action '", name, "'");
				}
			} else {
				throw new TemplateException("can't find action factory for action with name '", name, "'",
					" in library with URL '", libraryInformation.getUrl(), "' and dynamic actions are not supported");
			}
		}

		return actionFactory;
	}

	/**
	 * Finds an action contribution factory for an action contribution with a certain name.
	 *
	 * @param library the library to search in
	 * @param name the name of the action contribution to find an action contribution factory for
	 * @return the action contribution factory for the action contribution with the given name
	 * @throws TemplateException when no action contribution factory for an action contribution with the given name can
	 *                           be found in the library
	 */
	protected ActionContributionFactory findActionContributionFactory(
			Library library, String name) throws TemplateException {
		ActionContributionFactory actionContributionFactory = null;

		Iterator<ActionContributionFactory> actionContributionFactoryIterator =
			library.getActionContributionFactories().iterator();

		while (actionContributionFactoryIterator.hasNext() && (actionContributionFactory == null)) {
			actionContributionFactory = actionContributionFactoryIterator.next();

			if (!actionContributionFactory.getInformation().getName().equals(name)) {
				actionContributionFactory = null;
			}
		}

		if (actionContributionFactory == null) {
			LibraryInformation libraryInformation = library.getInformation();

			if (libraryInformation.supportsDynamicActionContributions()) {
				try {
					actionContributionFactory = library.getDynamicActionContributionFactory(name);
				} catch (LibraryException exception) {
					throw new TemplateException(exception, "can't get dynamic action contribution factory",
						" for action contribution '", name, "'");
				}
			} else {
				throw new TemplateException("can't find action contribution factory for action contribution",
					" with name '", name, "' in library with URL '", library.getInformation().getUrl(), "'",
					" and dynamic action contributions are not supported");
			}
		}

		return actionContributionFactory;
	}

	/**
	 * Creates an action element, given an action factory, a parameter map, a contribution factory map, a list of action
	 * interceptors and a map of library URL abbreviations.
	 *
	 * @param actionFactory the action factory to use
	 * @param parameters the action parameters to use
	 * @param actionContributionFactories the action contributions to use
	 * @param actionInterceptors the action interceptors to use
	 * @param libraryUrlAbbreviations the library URL abbreviations to use
	 * @return the new action element
	 */
	protected ActionElement createActionElement(
			ActionFactory actionFactory, Map<String, ActionParameter> parameters,
			Map<ActionContributionFactory, ActionParameter> actionContributionFactories,
			List<ActionInterceptor> actionInterceptors, Map<String, String> libraryUrlAbbreviations) {
		logger.debug("creating action element for action factory ", actionFactory, ", parameters ", parameters, ", ",
			"action interceptors ", actionInterceptors, ", and contribution factories ", actionContributionFactories);

		return new DefaultActionElement(configuration, actionFactory, parameters, actionContributionFactories,
			actionInterceptors, libraryUrlAbbreviations);
	}

	public TextElement createTextElement(String text, Map<String, String> libraryUrlAbbreviations) {
		logger.debug("creating text element for text '", text, "'");

		return new DefaultTextElement(text, libraryUrlAbbreviations);
	}

	public ExpressionElement createExpressionElement(ExpressionFactory expressionFactory, String text,
			Map<String, String> libraryUrlAbbreviations) {
		logger.debug("creating expression element for text '", text, "'");

		return new DefaultExpressionElement(expressionFactory, text, libraryUrlAbbreviations);
	}

	/**
	 * The name of the configuration parameter that holds a comma-separated list of packages in which will be looked for
	 * action interceptors.
	 */
	public final static String ACTION_INTERCEPTOR_PACKAGES =
		"template_element_factory.default.action_interceptor.packages";
}