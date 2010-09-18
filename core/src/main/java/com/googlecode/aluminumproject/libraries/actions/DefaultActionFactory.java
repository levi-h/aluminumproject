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
package com.googlecode.aluminumproject.libraries.actions;

import com.googlecode.aluminumproject.annotations.Ignored;
import com.googlecode.aluminumproject.annotations.Injected;
import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.configuration.ConfigurationElementFactory;
import com.googlecode.aluminumproject.configuration.ConfigurationException;
import com.googlecode.aluminumproject.configuration.ConfigurationParameters;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.libraries.AbstractLibraryElement;
import com.googlecode.aluminumproject.libraries.LibraryException;
import com.googlecode.aluminumproject.utilities.GenericsUtilities;
import com.googlecode.aluminumproject.utilities.ReflectionUtilities;
import com.googlecode.aluminumproject.utilities.StringUtilities;
import com.googlecode.aluminumproject.utilities.UtilityException;
import com.googlecode.aluminumproject.utilities.finders.MethodFinder;
import com.googlecode.aluminumproject.utilities.finders.MethodFinder.MethodFilter;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * An {@link ActionFactory action factory} that takes an {@link Action action} class and examines it to collect
 * information about it. By default, reflection will be used to compose information about the action and its parameters,
 * but this can be overridden by annotating the action class.
 * <p>
 * If an action class is annotated with {@link com.googlecode.aluminumproject.annotations.ActionInformation
 * &#64;ActionInformation}, the action name will be the value of the {@code name} attribute. Otherwise, the name that is
 * used is the name of the action class, {@link StringUtilities#humanise(String) humanised} and written in lower case
 * (so the action {@code CamelCase} would get the name <em>camel case</em>).
 * <p>
 * {@link ActionParameter Action parameters} are found by iterating through all of the action's setters that aren't
 * annotated with {@link Ignored &#64;Ignored}. Action parameters have the following properties:
 * <ul>
 * <li>A <i>name<i>: by default, this is the humanised name of the property, in lower case (so the method {@code
 *     setName(String name)} would result in a parameter with the name <em>name</em>);
 * <li>A <i>type</i>: this is the value of the parameter of the setter (so the method {@code setName(String name)} would
 *     cause the parameter to have the type {@code String});
 * <li>Whether it is <i>required</i> or not: all parameters are considered optional by default.
 * </ul>
 * The parameter's name, type, and whether it's required or not can be overridden by annotating its setter with {@link
 * com.googlecode.aluminumproject.annotations.ActionParameterInformation &#64;ActionParameterInformation}.
 * <p>
 * Actions may support dynamic parameters by implementing {@link DynamicallyParameterisable the dynamically
 * parameterisable interface}. Any parameters that are supplied to the action factory but whose names don't match any of
 * the found parameter names will be set as dynamic parameters.
 * <p>
 * Actions will be created using the configured {@link ConfigurationElementFactory configuration element factory}. After
 * an action has been created, all of its fields that are annotated with {@link Injected &#64;Injected} will be fed with
 * an appropriate value.
 *
 * @author levi_h
 */
public class DefaultActionFactory extends AbstractLibraryElement implements ActionFactory {
	private Class<? extends Action> actionClass;

	private Map<String, Method> parameterSetters;

	private ActionInformation information;

	/**
	 * Creates a default action factory.
	 *
	 * @param actionClass the class of the action to create
	 */
	public DefaultActionFactory(Class<? extends Action> actionClass) {
		this.actionClass = actionClass;
	}

	@Override
	public void initialise(
			Configuration configuration, ConfigurationParameters parameters) throws ConfigurationException {
		super.initialise(configuration, parameters);

		parameterSetters = new HashMap<String, Method>();

		information = new ActionInformation(getActionName(),
			getParameterInformation(), DynamicallyParameterisable.class.isAssignableFrom(actionClass));
	}

	private String getActionName() {
		String actionName = "";

		Class<com.googlecode.aluminumproject.annotations.ActionInformation> informationClass =
			com.googlecode.aluminumproject.annotations.ActionInformation.class;

		if (actionClass.isAnnotationPresent(informationClass)) {
			actionName = actionClass.getAnnotation(informationClass).name();
		}

		if (actionName.equals("")) {
			actionName = StringUtilities.humanise(actionClass.getSimpleName()).toLowerCase();
		}

		logger.debug("using action name '", actionName, "' for action class ", actionClass.getName());

		return actionName;
	}

	private List<ActionParameterInformation> getParameterInformation() throws ConfigurationException {
		List<ActionParameterInformation> parameterInformation = new LinkedList<ActionParameterInformation>();

		List<Method> parameterSetters;

		try {
			parameterSetters = MethodFinder.find(new MethodFilter() {
				public boolean accepts(Method method) {
					int modifiers = method.getModifiers();

					return Modifier.isPublic(modifiers) && !Modifier.isAbstract(modifiers)
						&& ReflectionUtilities.isSetter(method) && (method.getAnnotation(Ignored.class) == null);
				}
			}, actionClass);
		} catch (UtilityException exception) {
			throw new ConfigurationException(exception, "can't find parameter setters");
		}

		for (Method parameterSetter: parameterSetters) {
			Class<com.googlecode.aluminumproject.annotations.ActionParameterInformation> parameterInformationClass =
				com.googlecode.aluminumproject.annotations.ActionParameterInformation.class;

			com.googlecode.aluminumproject.annotations.ActionParameterInformation annotatedParameterInformation =
				parameterSetter.getAnnotation(parameterInformationClass);

			String parameterName;
			String parameterTypeName;
			Type parameterType;
			boolean required;

			if (annotatedParameterInformation == null) {
				parameterName = "";
				parameterTypeName = "";
				required = false;
			} else {
				parameterName = annotatedParameterInformation.name();
				parameterTypeName = annotatedParameterInformation.type();
				required = annotatedParameterInformation.required();
			}

			if (parameterName.equals("")) {
				String propertyName = ReflectionUtilities.getPropertyName(parameterSetter);

				parameterName = StringUtilities.humanise(propertyName).toLowerCase();
			}

			if (parameterTypeName.equals("")) {
				parameterType = parameterSetter.getGenericParameterTypes()[0];
			} else {
				try {
					parameterType = GenericsUtilities.getType(parameterTypeName, "java.lang", "java.util");
				} catch (UtilityException exception) {
					throw new ConfigurationException(exception, "can't get type of parameter '", parameterName, "'");
				}
			}

			logger.debug("found ", required ? "required" : "optional", " parameter;",
				" name: '", parameterName, "', type: ", parameterType);

			this.parameterSetters.put(parameterName, parameterSetter);

			parameterInformation.add(new ActionParameterInformation(parameterName, parameterType, required));
		}

		return parameterInformation;
	}

	public ActionInformation getInformation() {
		return information;
	}

	/**
	 * Returns the action class that will be instantiated.
	 *
	 * @return this default action factory's action class
	 */
	public Class<? extends Action> getActionClass() {
		return actionClass;
	}

	public Action create(Map<String, ActionParameter> parameters, Context context) throws ActionException {
		Action action;

		try {
			ConfigurationElementFactory configurationElementFactory =
				getConfiguration().getConfigurationElementFactory();

			action = configurationElementFactory.instantiate(actionClass.getName(), Action.class);
		} catch (ConfigurationException exception) {
			throw new ActionException(exception, "can't create action");
		}

		logger.debug("created action ", action);

		setParameters(action, new HashMap<String, ActionParameter>(parameters), context);
		logger.debug("set all parameters");

		try {
			injectFields(action);
		} catch (LibraryException exception) {
			throw new ActionException(exception, "can't inject action fields");
		}

		logger.debug("injected fields");

		return action;
	}

	private void setParameters(
			Action action, Map<String, ActionParameter> parameters, Context context) throws ActionException {
		for (ActionParameterInformation parameterInformation: information.getParameterInformation()) {
			String parameterName = parameterInformation.getName();

			if (parameters.containsKey(parameterName)) {
				ActionParameter parameter = parameters.remove(parameterName);
				Object parameterValue;

				Method parameterSetter = parameterSetters.get(parameterName);

				if (parameterSetter.getParameterTypes()[0] == ActionParameter.class) {
					parameterValue = parameter;
				} else {
					parameterValue = parameter.getValue(parameterInformation.getType(), context);
				}

				logger.debug("setting parameter '", parameterName, "' (value: ", parameterValue, ")");

				try {
					String propertyName = ReflectionUtilities.getPropertyName(parameterSetter);

					ReflectionUtilities.setProperty(action, Object.class, propertyName, parameterValue);
				} catch (UtilityException exception) {
					throw new ActionException(exception, "can't set parameter '", parameterName, "'",
						" (value: ", parameterValue, ") on action ", action);
				}
			} else if (parameterInformation.isRequired()) {
				throw new ActionException("missing required parameter '", parameterName, "'");
			} else {
				logger.debug("skipping optional parameter '", parameterName, "'");
			}
		}

		if (!parameters.isEmpty()) {
			if (getInformation().isDynamicallyParameterisable()) {
				for (Map.Entry<String, ActionParameter> parameter: parameters.entrySet()) {
					String parameterName = parameter.getKey();

					logger.debug("adding dynamic parameter '", parameterName, "'");

					((DynamicallyParameterisable) action).setParameter(parameterName, parameter.getValue());
				}
			} else {
				throw new ActionException("unknown parameters: ", parameters.keySet());
			}
		}
	}
}