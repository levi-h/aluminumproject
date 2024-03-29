/*
 * Copyright 2012 Aluminum project
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
package com.googlecode.aluminumproject.libraries.beans.actions;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.annotations.Injected;
import com.googlecode.aluminumproject.annotations.Required;
import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.libraries.actions.AbstractAction;
import com.googlecode.aluminumproject.utilities.ReflectionUtilities;
import com.googlecode.aluminumproject.writers.Writer;

@SuppressWarnings("javadoc")
public class SetProperty extends AbstractAction {
	private @Required Object bean;
	private @Required String name;
	private Object value;

	private @Injected Configuration configuration;

	public SetProperty() {
		value = NO_VALUE;
	}

	public void execute(Context context, Writer writer) throws AluminumException {
		if (bean == null) {
			throw new AluminumException("can't set properties on null beans");
		}

		Object parameterOrBodyValue = (value == NO_VALUE) ? getBodyObject(Object.class, context, writer) : value;
		Class<?> propertyType = ReflectionUtilities.getPropertyType(bean.getClass(), name);

		Object value = configuration.getConverterRegistry().convert(parameterOrBodyValue, propertyType);

		logger.debug("setting property '", name, "' with value ", value, " on bean ", bean);

		ReflectionUtilities.setProperty(bean, Object.class, name, value);
	}

	private final static Object NO_VALUE = new Object();
}