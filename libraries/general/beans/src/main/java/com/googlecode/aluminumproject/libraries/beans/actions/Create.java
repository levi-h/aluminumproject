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
import com.googlecode.aluminumproject.annotations.Required;
import com.googlecode.aluminumproject.annotations.UsableAsFunction;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.libraries.actions.AbstractAction;
import com.googlecode.aluminumproject.utilities.ReflectionUtilities;
import com.googlecode.aluminumproject.writers.Writer;

import java.lang.reflect.Type;

@SuppressWarnings("javadoc")
@UsableAsFunction(argumentParameters = "type")
public class Create extends AbstractAction {
	private @Required Type type;

	public void execute(Context context, Writer writer) throws AluminumException {
		if (type instanceof Class) {
			Class<?> beanClass = (Class<?>) type;
			Object bean = ReflectionUtilities.instantiate(beanClass);

			logger.debug("created bean ", bean, " of type ", beanClass.getSimpleName());

			writer.write(bean);
		} else {
			throw new AluminumException("can't create bean of type ", type, ": it is not a class");
		}
	}
}