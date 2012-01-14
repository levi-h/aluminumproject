/*
 * Copyright 2012 Levi Hoogenberg
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
import com.googlecode.aluminumproject.annotations.Ignored;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.libraries.actions.AbstractAction;
import com.googlecode.aluminumproject.writers.NullWriter;
import com.googlecode.aluminumproject.writers.Writer;

/**
 * Constructs a {@link com.googlecode.aluminumproject.libraries.xml.model.SelectionContext selection context} and writes
 * it. Namespaces can be added through nested {@link Namespace namespace actions}.
 *
 * @author levi_h
 */
public class SelectionContext extends AbstractAction implements SelectionContextContainer {
	private @Ignored com.googlecode.aluminumproject.libraries.xml.model.SelectionContext selectionContext;

	/**
	 * Creates a selection context.
	 */
	public SelectionContext() {
		selectionContext = new com.googlecode.aluminumproject.libraries.xml.model.SelectionContext();
	}

	public com.googlecode.aluminumproject.libraries.xml.model.SelectionContext getSelectionContext() {
		return selectionContext;
	}

	public void execute(Context context, Writer writer) throws AluminumException {
		getBody().invoke(context, new NullWriter());

		writer.write(selectionContext);
	}
}