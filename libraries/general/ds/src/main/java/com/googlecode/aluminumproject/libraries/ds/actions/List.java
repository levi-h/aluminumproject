/*
 * Copyright 2013 Aluminum project
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
package com.googlecode.aluminumproject.libraries.ds.actions;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.annotations.Ignored;
import com.googlecode.aluminumproject.annotations.ValidInside;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.libraries.actions.AbstractAction;
import com.googlecode.aluminumproject.writers.NullWriter;
import com.googlecode.aluminumproject.writers.Writer;

import java.util.LinkedList;

@SuppressWarnings("javadoc")
public class List extends AbstractAction {
	private @Ignored java.util.List<Object> elements;

	public List() {
		elements = new LinkedList<Object>();
	}

	public void execute(Context context, Writer writer) throws AluminumException {
		getBody().invoke(context, new NullWriter());

		writer.write(elements);
	}

	@ValidInside(List.class)
	public static class ListElement extends AbstractAction {
		private Object value;

		public void execute(Context context, Writer writer) throws AluminumException {
			if (value == null) {
				value = getBodyObject(Object.class, context, writer);
			}

			findAncestorOfType(List.class).elements.add(value);
		}
	}
}