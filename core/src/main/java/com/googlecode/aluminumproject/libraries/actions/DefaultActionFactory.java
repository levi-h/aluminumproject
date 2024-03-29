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
package com.googlecode.aluminumproject.libraries.actions;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.annotations.Ignored;
import com.googlecode.aluminumproject.annotations.Injected;
import com.googlecode.aluminumproject.annotations.Named;
import com.googlecode.aluminumproject.annotations.Required;
import com.googlecode.aluminumproject.annotations.Typed;
import com.googlecode.aluminumproject.annotations.UsableAsFunction;
import com.googlecode.aluminumproject.annotations.ValidInside;
import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.configuration.ConfigurationElementFactory;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.libraries.AbstractLibraryElement;
import com.googlecode.aluminumproject.templates.TemplateInformation;
import com.googlecode.aluminumproject.utilities.GenericsUtilities;
import com.googlecode.aluminumproject.utilities.ReflectionUtilities;
import com.googlecode.aluminumproject.utilities.StringUtilities;
import com.googlecode.aluminumproject.utilities.finders.FieldFinder;
import com.googlecode.aluminumproject.utilities.finders.FieldFinder.FieldFilter;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * An {@link ActionFactory action factory} that takes an {@link Action action} class and examines it to collect
 * information about it. By default, reflection will be used to compose information about the action and its parameters,
 * but this can be overridden by annotating the action class.
 * <p>
 * If an action class is annotated with {@link Named &#64;Named}, its value will be used to name the action. Otherwise,
 * the name that is used is the name of the action class, {@link StringUtilities#humanise(String) humanised} and written
 * in lower case (so the action {@code CamelCase} would get the name <em>camel case</em>).
 * <p>
 * {@link ActionParameter Action parameters} are found by iterating through all of the action's non-final, non-static
 * fields that aren't annotated with {@link Ignored &#64;Ignored} or {@link Injected &#64;Injected}. Action parameters
 * have the following properties:
 * <ul>
 * <li>A <i>name<i>: by default, this is the humanised name of the field, in lower case (so the field {@code fullName}
 *     would result in a parameter with the name <em>full name</em>), though it may be overridden by placing a {@link
 *     Named &#64;Named} annotation on the field;
 * <li>A <i>type</i>: this is the same as the field type, unless the field is annotated with {@link Typed &#64;Typed};
 * <li>Whether it is <i>required</i> or not: all parameters that are not marked {@link Required &#64;Required} are
 *     considered optional.
 * </ul>
 * Actions may support dynamic parameters by implementing {@link DynamicallyParameterisable the dynamically
 * parameterisable interface}. Any parameters that are supplied to the action factory but whose names don't match any of
 * the found parameter names will be set as dynamic parameters.
 * <p>
 * Actions that are annotated with {@link UsableAsFunction &#64;UsableAsFunction} can be used as if they were functions.
 * The parameters of these actions that are available as function arguments are supplied as an annotation attribute;
 * this list will at the least include all required parameters.
 * <p>
 * Actions will be created using the configured {@link ConfigurationElementFactory configuration element factory}. After
 * an action has been created, all of its fields that are annotated with {@link Injected &#64;Injected} will be fed with
 * an appropriate value.
 */
public class DefaultActionFactory extends AbstractLibraryElement implements ClassBasedActionFactory {
	private Class<? extends Action> actionClass;

	private Map<String, Field> parameterFields;

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
	public void initialise(Configuration configuration) throws AluminumException {
		super.initialise(configuration);

		parameterFields = new HashMap<String, Field>();

		boolean dynamicallyParameterisable = DynamicallyParameterisable.class.isAssignableFrom(actionClass);

		UsableAsFunction usableAsFunction = actionClass.getAnnotation(UsableAsFunction.class);

		Type resultTypeWhenFunction;
		List<String> functionArgumentParameters;

		if (usableAsFunction == null) {
			resultTypeWhenFunction = null;
			functionArgumentParameters = null;
		} else {
			resultTypeWhenFunction = GenericsUtilities.getType(usableAsFunction.resultType(), "java.lang", "java.util");
			functionArgumentParameters = Arrays.asList(usableAsFunction.argumentParameters());
		}

		information = new ActionInformation(getActionName(),
			getParameterInformation(functionArgumentParameters), dynamicallyParameterisable, resultTypeWhenFunction);
	}

	private String getActionName() {
		String actionName;

		if (actionClass.isAnnotationPresent(Named.class)) {
			actionName = actionClass.getAnnotation(Named.class).value();
		} else {
			actionName = StringUtilities.humanise(actionClass.getSimpleName()).toLowerCase();
		}

		logger.debug("using action name '", actionName, "' for action class ", actionClass.getName());

		return actionName;
	}

	private List<ActionParameterInformation> getParameterInformation(List<String> functionArguments)
			throws AluminumException {
		List<ActionParameterInformation> parameterInformation = new LinkedList<ActionParameterInformation>();

		Set<String> unknownFunctionArguments = new HashSet<String>();

		if (functionArguments != null) {
			unknownFunctionArguments.addAll(functionArguments);
		}

		List<Field> parameterFields = FieldFinder.find(new FieldFilter() {
			public boolean accepts(Field field) {
				int modifiers = field.getModifiers();

				return !Modifier.isStatic(modifiers) && !Modifier.isFinal(modifiers)
					&& (field.getAnnotation(Ignored.class) == null) && (field.getAnnotation(Injected.class) == null);
			}
		}, actionClass);

		for (Field parameterField: parameterFields) {
			String parameterName;

			if (parameterField.isAnnotationPresent(Named.class)) {
				parameterName = parameterField.getAnnotation(Named.class).value();
			} else {
				parameterName = StringUtilities.humanise(parameterField.getName()).toLowerCase();
			}

			Type parameterType;

			if (parameterField.isAnnotationPresent(Typed.class)) {
				String parameterTypeName = parameterField.getAnnotation(Typed.class).value();

				parameterType = GenericsUtilities.getType(parameterTypeName, "java.lang", "java.util");
			} else {
				parameterType = parameterField.getGenericType();
			}

			boolean required = parameterField.isAnnotationPresent(Required.class);

			logger.debug("found ", required ? "required" : "optional", " parameter;",
				" name: '", parameterName, "', type: ", parameterType);

			this.parameterFields.put(parameterName, parameterField);

			Integer argumentIndex = null;

			if (functionArguments != null) {
				if (functionArguments.contains(parameterName)) {
					argumentIndex = functionArguments.indexOf(parameterName);
					unknownFunctionArguments.remove(parameterName);
				} else if (required) {
					throw new AluminumException("required parameter '", parameterName, "' is missing ",
						"from function argument list");
				}
			}

			parameterInformation.add(
				new ActionParameterInformation(parameterName, parameterType, required, argumentIndex));
		}

		if (!unknownFunctionArguments.isEmpty()) {
			throw new AluminumException("unknown function argument parameters: ", unknownFunctionArguments);
		}

		return parameterInformation;
	}

	public ActionInformation getInformation() {
		return information;
	}

	public Class<? extends Action> getActionClass() {
		return actionClass;
	}

	public Action create(Map<String, ActionParameter> parameters, Context context) throws AluminumException {
		validateNesting(context);

		Action action =
			getConfiguration().getConfigurationElementFactory().instantiate(actionClass.getName(), Action.class);

		logger.debug("created action ", action);

		setParameters(action, new HashMap<String, ActionParameter>(parameters), context);
		logger.debug("set all parameters");

		injectFields(action);

		logger.debug("injected fields");

		return action;
	}

	private void validateNesting(Context context) throws AluminumException {
		if (actionClass.isAnnotationPresent(ValidInside.class)) {
			Action action = TemplateInformation.from(context).getCurrentAction();
			Class<?> requiredAncestorType = actionClass.getAnnotation(ValidInside.class).value();

			while ((action != null) && !requiredAncestorType.isAssignableFrom(action.getClass())) {
				action = action.getParent();
			}

			if (action == null) {
				throw new AluminumException("'", information.getName(), "' actions are only valid inside actions ",
					"that are assignable to type ", requiredAncestorType.getSimpleName());
			}
		}
	}

	private void setParameters(
			Action action, Map<String, ActionParameter> parameters, Context context) throws AluminumException {
		for (ActionParameterInformation parameterInformation: information.getParameterInformation()) {
			String parameterName = parameterInformation.getName();

			if (parameters.containsKey(parameterName)) {
				ActionParameter parameter = parameters.remove(parameterName);
				Object parameterValue;

				Field parameterField = parameterFields.get(parameterName);

				if (parameterField.getType() == ActionParameter.class) {
					parameterValue = parameter;
				} else {
					parameterValue = parameter.getValue(parameterInformation.getType(), context);
				}

				logger.debug("setting parameter '", parameterName, "' (value: ", parameterValue, ")");

				ReflectionUtilities.setFieldValue(action, parameterField.getName(), parameterValue);
			} else if (parameterInformation.isRequired()) {
				throw new AluminumException("missing required parameter '", parameterName, "'");
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
				throw new AluminumException("unknown parameters: ", parameters.keySet());
			}
		}
	}
}