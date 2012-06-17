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
package com.googlecode.aluminumproject.libraries.common.actions;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.annotations.Ignored;
import com.googlecode.aluminumproject.annotations.Injected;
import com.googlecode.aluminumproject.annotations.Required;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.libraries.Library;
import com.googlecode.aluminumproject.libraries.LibraryInformation;
import com.googlecode.aluminumproject.libraries.actions.AbstractAction;
import com.googlecode.aluminumproject.libraries.actions.DefaultActionFactory;
import com.googlecode.aluminumproject.libraries.functions.Function;
import com.googlecode.aluminumproject.libraries.functions.FunctionArgument;
import com.googlecode.aluminumproject.libraries.functions.FunctionFactory;
import com.googlecode.aluminumproject.writers.NullWriter;
import com.googlecode.aluminumproject.writers.Writer;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("javadoc")
public class CallFunction extends AbstractAction {
	private @Required String name;
	private @Ignored List<FunctionArgument> arguments;

	private @Injected DefaultActionFactory factory;

	public CallFunction() {
		arguments = new LinkedList<FunctionArgument>();
	}

	protected void addArgument(FunctionArgument argument) {
		arguments.add(argument);
	}

	protected Library getLibrary() {
		return factory.getLibrary();
	}

	public void execute(Context context, Writer writer) throws AluminumException {
		getBody().invoke(context, new NullWriter());

		logger.debug("calling function");

		writer.write(createFunction(context).call(context));
	}

	private Function createFunction(Context context) throws AluminumException {
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

			if (libraryInformation.isSupportingDynamicFunctions()) {
				functionFactory = library.getDynamicFunctionFactory(name);
			} else {
				throw new AluminumException("can't find factory for function '", name, "'");
			}
		}

		return functionFactory.create(arguments, context);
	}
}