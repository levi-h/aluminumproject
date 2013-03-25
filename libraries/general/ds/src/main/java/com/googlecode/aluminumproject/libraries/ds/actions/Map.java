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
import com.googlecode.aluminumproject.annotations.Required;
import com.googlecode.aluminumproject.annotations.ValidInside;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.libraries.actions.AbstractAction;
import com.googlecode.aluminumproject.writers.NullWriter;
import com.googlecode.aluminumproject.writers.Writer;

import java.util.LinkedHashMap;

@SuppressWarnings("javadoc")
public class Map extends AbstractAction {
	private @Ignored java.util.Map<Object, Object> entries;

	public Map() {
		entries = new LinkedHashMap<Object, Object>();
	}

	public void execute(Context context, Writer writer) throws AluminumException {
		getBody().invoke(context, new NullWriter());

		writer.write(entries);
	}

	@ValidInside(Map.class)
	public static class MapEntry extends AbstractAction {
		private @Required Object key;
		private @Required Object value;

		public void execute(Context context, Writer writer) {
			findAncestorOfType(Map.class).entries.put(key, value);
		}
	}
}