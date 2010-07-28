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
package com.googlecode.aluminumproject.libraries.functions;

import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.configuration.ConfigurationParameters;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.libraries.AbstractLibraryElement;
import com.googlecode.aluminumproject.utilities.ReflectionUtilities;
import com.googlecode.aluminumproject.utilities.StringUtilities;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * A function factory that creates a {@link StaticMethodInvokingFunction function that invokes a static method}.
 *
 * @author levi_h
 */
public class StaticMethodInvokingFunctionFactory extends AbstractLibraryElement implements FunctionFactory {
	private Method method;

	private FunctionInformation information;

	/**
	 * Creates a static method invoking function factory.
	 *
	 * @param method the static method that will be invoked by the created function
	 */
	public StaticMethodInvokingFunctionFactory(Method method) {
		this.method = method;
	}

	@Override
	public void initialise(Configuration configuration, ConfigurationParameters parameters) {
		super.initialise(configuration, parameters);

		information = new FunctionInformation(getFunctionName(), method.getReturnType(), getArgumentInformation());
	}

	private String getFunctionName() {
		String functionName = "";

		Class<com.googlecode.aluminumproject.annotations.FunctionInformation> informationClass =
			com.googlecode.aluminumproject.annotations.FunctionInformation.class;

		if (method.isAnnotationPresent(informationClass)) {
			functionName = method.getAnnotation(informationClass).name();
		}

		if (functionName.equals("")) {
			functionName = method.getName();
		}

		logger.debug("using function name '", functionName, "' for method ", method);

		return functionName;
	}

	private List<FunctionArgumentInformation> getArgumentInformation() {
		Annotation[][] parameterAnnotations = method.getParameterAnnotations();
		Class<?>[] parameterTypes = method.getParameterTypes();

		List<FunctionArgumentInformation> argumentInformation =
			new ArrayList<FunctionArgumentInformation>(parameterTypes.length);

		for (int i = 0; i < parameterTypes.length; i++) {
			String argumentName = null;

			if (i < parameterAnnotations.length) {
				int j = 0;

				while ((j < parameterAnnotations[i].length) && (argumentName == null)) {
					Annotation annotation = parameterAnnotations[i][j];

					if (annotation instanceof com.googlecode.aluminumproject.annotations.FunctionArgumentInformation) {
						com.googlecode.aluminumproject.annotations.FunctionArgumentInformation informationAnnotation =
							(com.googlecode.aluminumproject.annotations.FunctionArgumentInformation) annotation;

						argumentName = informationAnnotation.name();
					} else {
						j++;
					}
				}

				if ((argumentName != null) && argumentName.equals("")) {
					argumentName = null;
				}
			}

			Class<?> argumentType = parameterTypes[i];

			logger.debug("argument ", i, "; ",
				(argumentName == null) ? "" : StringUtilities.join("name: '", argumentName, "', "),
				"type: ", argumentType.getName());

			argumentInformation.add(new FunctionArgumentInformation(argumentName, argumentType));
		}

		return argumentInformation;
	}

	public FunctionInformation getInformation() {
		return information;
	}

	public Function create(List<FunctionArgument> arguments, Context context) throws FunctionException {
		return new StaticMethodInvokingFunction(method, createParameters(arguments, context));
	}

	private Object[] createParameters(List<FunctionArgument> arguments, Context context) throws FunctionException {
		List<FunctionArgumentInformation> argumentInformation = information.getArgumentInformation();

		if (arguments.size() != argumentInformation.size()) {
			throw new FunctionException("expected ", argumentInformation.size(), " arguments, got ", arguments.size());
		}

		Object[] parameters = new Object[argumentInformation.size()];

		for (int i = 0; i < parameters.length; i++) {
			parameters[i] = arguments.get(i).getValue(
				ReflectionUtilities.wrapPrimitiveType(argumentInformation.get(i).getType()), context);
		}

		return parameters;
	}
}