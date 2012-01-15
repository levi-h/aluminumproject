/*
 * Copyright 2010-2012 Aluminum project
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
package com.googlecode.aluminumproject.libraries.xml.actions;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.annotations.Required;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.libraries.actions.AbstractAction;
import com.googlecode.aluminumproject.libraries.xml.model.SelectionContext;
import com.googlecode.aluminumproject.writers.NullWriter;
import com.googlecode.aluminumproject.writers.Writer;

import java.util.List;

@SuppressWarnings("javadoc")
public class Select extends AbstractAction implements SelectionContextContainer {
	private @Required com.googlecode.aluminumproject.libraries.xml.model.Element element;

	private @Required String expression;
	private SelectionContext context;

	public Select() {
		context = new SelectionContext();
	}

	public SelectionContext getSelectionContext() {
		return context;
	}

	public void execute(Context context, Writer writer) throws AluminumException {
		getBody().invoke(context, new NullWriter());

		List<?> results = element.select(expression, this.context);

		writer.write((results.size() == 1) ? results.get(0) : results);
	}
}