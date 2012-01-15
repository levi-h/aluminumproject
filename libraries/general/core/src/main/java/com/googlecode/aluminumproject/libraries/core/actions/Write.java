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
package com.googlecode.aluminumproject.libraries.core.actions;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.libraries.actions.AbstractAction;
import com.googlecode.aluminumproject.writers.Writer;

import java.util.Collections;
import java.util.List;

@SuppressWarnings("javadoc")
public class Write extends AbstractAction {
	private Object value;

	public Write() {
		value = NO_VALUE;
	}

	public void execute(Context context, Writer writer) throws AluminumException {
		List<?> values;

		if (value == NO_VALUE) {
			values = getBodyList(context, writer);

			if (values.isEmpty()) {
				throw new AluminumException("the 'write' action needs a value, either as a parameter or in its body");
			}
		} else {
			values = Collections.singletonList(value);
		}

		for (Object value: values) {
			logger.debug("writing value ", value);

			writer.write(value);
		}
	}

	private final static Object NO_VALUE = new Object();
}