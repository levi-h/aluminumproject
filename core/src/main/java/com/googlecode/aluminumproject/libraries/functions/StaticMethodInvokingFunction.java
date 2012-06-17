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
import com.googlecode.aluminumproject.context.Context;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * A {@link Function function} that invokes a static method.
 */
public class StaticMethodInvokingFunction implements Function {
	private Method method;
	private Object[] parameters;

	/**
	 * Creates a static method invoking function.
	 *
	 * @param method the method to invoke
	 * @param parameters the parameters to use when invoking the method
	 */
	public StaticMethodInvokingFunction(Method method, Object... parameters) {
		this.method = method;
		this.parameters = parameters;
	}

	public Object call(Context context) throws AluminumException {
		try {
			return method.invoke(null, parameters);
		} catch (IllegalArgumentException exception) {
			throw new AluminumException(exception, "can't invoke static method");
		} catch (IllegalAccessException exception) {
			throw new AluminumException(exception, "may not invoke static method");
		} catch (InvocationTargetException exception) {
			throw new AluminumException(exception, "exception while invoking static method");
		}
	}
}