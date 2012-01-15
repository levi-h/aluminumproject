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
package com.googlecode.aluminumproject.libraries.functions;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.annotations.Named;
import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.libraries.AbstractLibraryElement;
import com.googlecode.aluminumproject.utilities.StringUtilities;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * A function factory that creates a {@link StaticMethodInvokingFunction function that invokes a static method}.
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
	public void initialise(Configuration configuration) throws AluminumException {
		super.initialise(configuration);

		information =
			new FunctionInformation(getFunctionName(), method.getGenericReturnType(), getArgumentInformation());
	}

	private String getFunctionName() {
		String functionName;

		if (method.isAnnotationPresent(Named.class)) {
			functionName = method.getAnnotation(Named.class).value();
		} else {
			functionName = method.getName();
		}

		logger.debug("using function name '", functionName, "' for method ", method);

		return functionName;
	}

	private List<FunctionArgumentInformation> getArgumentInformation() {
		Annotation[][] parameterAnnotations = method.getParameterAnnotations();
		Type[] parameterTypes = method.getGenericParameterTypes();

		List<FunctionArgumentInformation> argumentInformation =
			new ArrayList<FunctionArgumentInformation>(parameterTypes.length);

		for (int i = 0; i < parameterTypes.length; i++) {
			String argumentName = null;

			if (i < parameterAnnotations.length) {
				int j = 0;

				while ((j < parameterAnnotations[i].length) && (argumentName == null)) {
					Annotation annotation = parameterAnnotations[i][j];

					if (annotation instanceof Named) {
						argumentName = ((Named) annotation).value();
					} else {
						j++;
					}
				}
			}

			Type argumentType = parameterTypes[i];

			logger.debug("argument ", i, "; ",
				(argumentName == null) ? "" : StringUtilities.join("name: '", argumentName, "', "),
				"type: ", argumentType);

			argumentInformation.add(new FunctionArgumentInformation(argumentName, argumentType));
		}

		return argumentInformation;
	}

	public FunctionInformation getInformation() {
		return information;
	}

	public Function create(List<FunctionArgument> arguments, Context context) throws AluminumException {
		return new StaticMethodInvokingFunction(method, createParameters(arguments, context));
	}

	private Object[] createParameters(List<FunctionArgument> arguments, Context context) throws AluminumException {
		List<FunctionArgumentInformation> argumentInformation = information.getArgumentInformation();

		if (arguments.size() != argumentInformation.size()) {
			throw new AluminumException("expected ", argumentInformation.size(), " arguments, got ", arguments.size());
		}

		Object[] parameters = new Object[argumentInformation.size()];

		for (int i = 0; i < parameters.length; i++) {
			parameters[i] = arguments.get(i).getValue(argumentInformation.get(i).getType(), context);
		}

		return parameters;
	}
}