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
package com.googlecode.aluminumproject.libraries.xml.actions;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.annotations.ValidInside;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.libraries.actions.AbstractDynamicallyParameterisableAction;
import com.googlecode.aluminumproject.libraries.actions.ActionParameter;
import com.googlecode.aluminumproject.writers.Writer;

import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings("javadoc")
@ValidInside(AbstractElement.class)
public class ProcessingInstruction extends AbstractDynamicallyParameterisableAction {
	private String target;

	private boolean addToDocument;

	public ProcessingInstruction() {
		addToDocument = true;
	}

	public void execute(Context context, Writer writer) throws AluminumException {
		Map<String, String> pseudoAttributes = new LinkedHashMap<String, String>();

		Map<String, ActionParameter> dynamicParameters = getDynamicParameters();

		for (String name: dynamicParameters.keySet()) {
			pseudoAttributes.put(name, (String) dynamicParameters.get(name).getValue(String.class, context));
		}

		findAncestorOfType(AbstractElement.class).addProcessingInstruction(target, pseudoAttributes, addToDocument);
	}
}
