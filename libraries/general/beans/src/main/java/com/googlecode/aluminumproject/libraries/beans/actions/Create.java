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
import com.googlecode.aluminumproject.annotations.Ignored;
import com.googlecode.aluminumproject.annotations.Required;
import com.googlecode.aluminumproject.annotations.UsableAsFunction;
import com.googlecode.aluminumproject.annotations.ValidInside;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.libraries.actions.AbstractAction;
import com.googlecode.aluminumproject.utilities.ReflectionUtilities;
import com.googlecode.aluminumproject.writers.NullWriter;
import com.googlecode.aluminumproject.writers.Writer;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("javadoc")
@UsableAsFunction(argumentParameters = "type")
public class Create extends AbstractAction {
	private @Required Type type;
	private @Ignored List<Object> arguments;

	public Create() {
		arguments = new LinkedList<Object>();
	}

	public void execute(Context context, Writer writer) throws AluminumException {
		if (type instanceof Class) {
			getBody().invoke(context, new NullWriter());

			Class<?> beanClass = (Class<?>) type;
			Object bean = ReflectionUtilities.instantiate(beanClass, arguments.toArray());

			logger.debug("created bean ", bean, " of type ", beanClass.getSimpleName());

			writer.write(bean);
		} else {
			throw new AluminumException("can't create bean of type ", type, ": it is not a class");
		}
	}

	@ValidInside(Create.class)
	public static class Argument extends AbstractAction {
		private Object value;

		public Argument() {
			value = NO_VALUE;
		}

		public void execute(Context context, Writer writer) throws AluminumException {
			findAncestorOfType(Create.class).arguments.add((value == NO_VALUE)
				? getBodyObject(Object.class, context, writer) : value);
		}

		private final static Object NO_VALUE = new Object();
	}
}