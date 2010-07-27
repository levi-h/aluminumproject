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
package com.googlecode.aluminumproject.libraries.common.actions;

import com.googlecode.aluminumproject.annotations.ActionParameterInformation;
import com.googlecode.aluminumproject.annotations.Injected;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.ContextException;
import com.googlecode.aluminumproject.libraries.Library;
import com.googlecode.aluminumproject.libraries.LibraryException;
import com.googlecode.aluminumproject.libraries.LibraryInformation;
import com.googlecode.aluminumproject.libraries.actions.AbstractAction;
import com.googlecode.aluminumproject.libraries.actions.ActionException;
import com.googlecode.aluminumproject.libraries.actions.DefaultActionFactory;
import com.googlecode.aluminumproject.libraries.functions.Function;
import com.googlecode.aluminumproject.libraries.functions.FunctionArgument;
import com.googlecode.aluminumproject.libraries.functions.FunctionException;
import com.googlecode.aluminumproject.libraries.functions.FunctionFactory;
import com.googlecode.aluminumproject.writers.Writer;
import com.googlecode.aluminumproject.writers.WriterException;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Calls a {@link Function function}. {@link FunctionArgument function arguments} can be added by nesting {@link
 * com.googlecode.aluminumproject.libraries.common.actions.FunctionArgument function argument actions}.
 *
 * @author levi_h
 */
public class CallFunction extends AbstractAction {
	private String name;

	private List<FunctionArgument> arguments;

	private String resultName;
	private String resultScope;

	private @Injected DefaultActionFactory factory;

	/**
	 * Creates a <em>call function</em> action.
	 */
	public CallFunction() {
		arguments = new LinkedList<FunctionArgument>();
	}

	/**
	 * Sets the name of the function to call.
	 *
	 * @param name the function name to use
	 */
	@ActionParameterInformation(required = true)
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Adds an argument to the invoked function.
	 *
	 * @param argument the argument to add
	 */
	protected void addArgument(FunctionArgument argument) {
		arguments.add(argument);
	}

	/**
	 * Sets the name of the variable that will contain the result of the function call.
	 *
	 * @param resultName the result variable's name
	 */
	@ActionParameterInformation(required = true)
	public void setResultName(String resultName) {
		this.resultName = resultName;
	}

	/**
	 * Sets the (optional) scope of the variable that will contain the result of the function call.
	 *
	 * @param resultScope the result variable's scope
	 */
	public void setResultScope(String resultScope) {
		this.resultScope = resultScope;
	}

	/**
	 * Returns the library that contains the function that will be called.
	 *
	 * @return the called function's library
	 */
	protected Library getLibrary() {
		return factory.getLibrary();
	}

	public void execute(Context context, Writer writer) throws ActionException, ContextException, WriterException {
		getBody().invoke(context, writer);

		Object result;

		try {
			logger.debug("calling function");

			result = createFunction(context).call(context);
		} catch (FunctionException exception) {
			throw new ActionException(exception, "can't call function");
		}

		if (resultScope == null) {
			logger.debug("storing function result in  variable '", resultName, "' in the innermost scope");

			context.setVariable(resultName, result);
		} else {
			logger.debug("storing function result in variable '", resultName, "' in the '", resultScope, "' scope");

			context.setVariable(resultScope, resultName, result);
		}
	}

	private Function createFunction(Context context) throws ActionException, FunctionException {
		Library library = getLibrary();

		Iterator<FunctionFactory> it = library.getFunctionFactories().iterator();
		FunctionFactory functionFactory = null;

		while ((it.hasNext()) && (functionFactory == null)) {
			functionFactory = it.next();

			if (!functionFactory.getInformation().getName().equals(name)) {
				functionFactory = null;
			}
		}

		if (functionFactory == null) {
			LibraryInformation libraryInformation = library.getInformation();

			if (libraryInformation.supportsDynamicFunctions()) {
				try {
					functionFactory = library.getDynamicFunctionFactory(name);
				} catch (LibraryException exception) {
					throw new ActionException("can't get dynamic function factory for function '", name, "'");
				}
			} else {
				throw new ActionException("can't find factory for function '", name, "'");
			}
		}

		return functionFactory.create(arguments, context);
	}
}