/*
 * Copyright 2009-2012 Aluminum project
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

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.annotations.Ignored;
import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.configuration.ConfigurationElementFactory;
import com.googlecode.aluminumproject.configuration.ConfigurationParameters;
import com.googlecode.aluminumproject.expressions.ExpressionFactory;
import com.googlecode.aluminumproject.finders.TypeFinder.TypeFilter;
import com.googlecode.aluminumproject.interceptors.ActionInterceptor;
import com.googlecode.aluminumproject.libraries.Library;
import com.googlecode.aluminumproject.libraries.LibraryInformation;
import com.googlecode.aluminumproject.libraries.actions.ActionContribution;
import com.googlecode.aluminumproject.libraries.actions.ActionContributionFactory;
import com.googlecode.aluminumproject.libraries.actions.ActionFactory;
import com.googlecode.aluminumproject.libraries.actions.ActionParameter;
import com.googlecode.aluminumproject.utilities.ConfigurationUtilities;
import com.googlecode.aluminumproject.utilities.Logger;
import com.googlecode.aluminumproject.utilities.ReflectionUtilities;

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
 */
public class DefaultTemplateElementFactory implements TemplateElementFactory {
	private Configuration configuration;

	private List<ActionInterceptor> actionInterceptors;

	private final Logger logger;

	/**
	 * Creates a default template element factory.
	 */
	public DefaultTemplateElementFactory() {
		actionInterceptors = new LinkedList<ActionInterceptor>();

		logger = Logger.get(getClass());
	}

	public void initialise(Configuration configuration) throws AluminumException {
		this.configuration = configuration;

		createActionInterceptors();
	}

	private void createActionInterceptors() throws AluminumException {
		Set<String> actionInterceptorPackages = new HashSet<String>();

		ConfigurationParameters parameters = configuration.getParameters();
		Collections.addAll(actionInterceptorPackages, parameters.getValues(ACTION_INTERCEPTOR_PACKAGES));
		Collections.addAll(actionInterceptorPackages, parameters.getValues(CONFIGURATION_ELEMENT_PACKAGES));

		if (!actionInterceptorPackages.isEmpty()) {
			logger.debug("action interceptors will be looked for in ", actionInterceptorPackages);

			List<Class<?>> actionInterceptorClasses = configuration.getTypeFinder().find(new TypeFilter() {
				public boolean accepts(Class<?> type) {
					return ActionInterceptor.class.isAssignableFrom(type) && !ReflectionUtilities.isAbstract(type)
						&& !type.isAnnotationPresent(Ignored.class);
				}
			}, actionInterceptorPackages.toArray(new String[actionInterceptorPackages.size()]));

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

	public void disable() {
		actionInterceptors.clear();
	}

	public ActionElement createActionElement(ActionDescriptor actionDescriptor,
			Map<String, ActionParameter> parameters, List<ActionContributionDescriptor> contributionDescriptors,
			Map<String, String> libraryUrlAbbreviations, int lineNumber) throws AluminumException {
		String libraryUrlAbbreviation = actionDescriptor.getLibraryUrlAbbreviation();

		if (!libraryUrlAbbreviations.containsKey(libraryUrlAbbreviation)) {
			throw new AluminumException("unknown library URL abbreviation: '", libraryUrlAbbreviation, "'");
		}

		String libraryUrl = libraryUrlAbbreviations.get(libraryUrlAbbreviation);
		String name = actionDescriptor.getName();

		logger.debug("creating action element, library URL: '", libraryUrl, "', name: '", name, "', ",
			"parameters: ", parameters, ", contributions: ", contributionDescriptors);

		Library library = findLibrary(libraryUrl);
		logger.debug("found library for URL '", libraryUrl, "': ", library);

		ActionFactory actionFactory = findActionFactory(library, name);
		logger.debug("found action factory for action with name '", name, "': ", actionFactory);

		Map<ActionContributionDescriptor, ActionContributionFactory> actionContributionFactories =
			createActionContributionFactories(contributionDescriptors, libraryUrlAbbreviations);
		logger.debug("created action contributions factories ",
			"for action with name '", name, "': ", actionContributionFactories);

		return createActionElement(actionDescriptor, actionFactory, parameters, actionContributionFactories,
			Collections.unmodifiableList(actionInterceptors), libraryUrlAbbreviations, lineNumber);
	}

	private Map<ActionContributionDescriptor, ActionContributionFactory> createActionContributionFactories(
			List<ActionContributionDescriptor> contributionDescriptors,
			Map<String, String> libraryUrlAbbreviations) throws AluminumException {
		Map<ActionContributionDescriptor, ActionContributionFactory> actionContributionFactories =
			new LinkedHashMap<ActionContributionDescriptor, ActionContributionFactory>();

		for (ActionContributionDescriptor descriptor: contributionDescriptors) {
			String libraryUrlAbbreviation = descriptor.getLibraryUrlAbbreviation();

			if (!libraryUrlAbbreviations.containsKey(libraryUrlAbbreviation)) {
				throw new AluminumException("unknown library URL abbreviation: '", libraryUrlAbbreviation, "'");
			}

			Library library = findLibrary(libraryUrlAbbreviations.get(libraryUrlAbbreviation));
			ActionContributionFactory actionContributionFactory =
				findActionContributionFactory(library, descriptor.getName());

			actionContributionFactories.put(descriptor, actionContributionFactory);
		}

		return actionContributionFactories;
	}

	private Library findLibrary(String libraryUrl) throws AluminumException {
		Library library = ConfigurationUtilities.findLibrary(configuration, libraryUrl);

		if (library == null) {
			throw new AluminumException("can't find library with URL '", libraryUrl, "'");
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
	 * @throws AluminumException when no action factory for an action with the given name can be found in the library
	 */
	protected ActionFactory findActionFactory(Library library, String name) throws AluminumException {
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

			if (libraryInformation.isSupportingDynamicActions()) {
				actionFactory = library.getDynamicActionFactory(name);
			} else {
				throw new AluminumException("can't find action factory for action with name '", name, "'",
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
	 * @throws AluminumException when no action contribution factory for an action contribution with the given name can
	 *                           be found in the library
	 */
	protected ActionContributionFactory findActionContributionFactory(
			Library library, String name) throws AluminumException {
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

			if (libraryInformation.isSupportingDynamicActionContributions()) {
				actionContributionFactory = library.getDynamicActionContributionFactory(name);
			} else {
				throw new AluminumException("can't find action contribution factory for action contribution",
					" with name '", name, "' in library with URL '", library.getInformation().getUrl(), "'",
					" and dynamic action contributions are not supported");
			}
		}

		return actionContributionFactory;
	}

	/**
	 * Creates an action element.
	 *
	 * @param actionDescriptor a descriptor of the action
	 * @param actionFactory the action factory to use
	 * @param parameters the action parameters to use
	 * @param actionContributionFactories the action contributions to use
	 * @param actionInterceptors the action interceptors to use
	 * @param libraryUrlAbbreviations the library URL abbreviations to use
	 * @param lineNumber the line number of the action element
	 * @return the new action element
	 */
	protected ActionElement createActionElement(
			ActionDescriptor actionDescriptor, ActionFactory actionFactory, Map<String, ActionParameter> parameters,
			Map<ActionContributionDescriptor, ActionContributionFactory> actionContributionFactories,
			List<ActionInterceptor> actionInterceptors, Map<String, String> libraryUrlAbbreviations, int lineNumber) {
		logger.debug("creating action element for action factory ", actionFactory, ", parameters ", parameters, ", ",
			"action interceptors ", actionInterceptors, ", and contribution factories ", actionContributionFactories);

		return new DefaultActionElement(configuration, actionDescriptor, actionFactory, parameters,
			actionContributionFactories, actionInterceptors, libraryUrlAbbreviations, lineNumber);
	}

	public TextElement createTextElement(String text, Map<String, String> libraryUrlAbbreviations, int lineNumber) {
		logger.debug("creating text element for text '", text, "'");

		return new DefaultTextElement(text, libraryUrlAbbreviations, lineNumber);
	}

	public ExpressionElement createExpressionElement(ExpressionFactory expressionFactory, String text,
			Map<String, String> libraryUrlAbbreviations, int lineNumber) {
		logger.debug("creating expression element for text '", text, "'");

		return new DefaultExpressionElement(expressionFactory, text, libraryUrlAbbreviations, lineNumber);
	}

	/**
	 * The name of the configuration parameter that holds a comma-separated list of packages in which will be looked for
	 * action interceptors.
	 */
	public final static String ACTION_INTERCEPTOR_PACKAGES =
		"template_element_factory.default.action_interceptor.packages";
}