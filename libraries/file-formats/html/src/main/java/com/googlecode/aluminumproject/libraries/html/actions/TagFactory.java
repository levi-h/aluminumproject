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
package com.googlecode.aluminumproject.libraries.html.actions;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.libraries.Library;
import com.googlecode.aluminumproject.libraries.actions.ActionFactory;
import com.googlecode.aluminumproject.libraries.actions.ActionInformation;
import com.googlecode.aluminumproject.libraries.actions.ActionParameter;
import com.googlecode.aluminumproject.libraries.actions.ActionParameterInformation;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("javadoc")
public class TagFactory implements ActionFactory {
	private String name;
	private List<String> attributes;

	private boolean open;

	private ActionInformation information;

	private Library library;

	public TagFactory(String name, List<String> attributes, boolean open) {
		this.name = name;
		this.attributes = attributes;

		this.open = open;
	}

	public void initialise(Configuration configuration) {
		List<ActionParameterInformation> parameterInformation = new LinkedList<ActionParameterInformation>();

		for (String attribute: attributes) {
			parameterInformation.add(new ActionParameterInformation(attribute, String.class, false));
		}

		information = new ActionInformation(name, parameterInformation, false);
	}

	public void disable() {}

	public Library getLibrary() {
		return library;
	}

	public void setLibrary(Library library) {
		this.library = library;
	}

	public ActionInformation getInformation() {
		return information;
	}

	public Tag create(Map<String, ActionParameter> parameters, Context context) throws AluminumException {
		Map<String, String> attributes = new LinkedHashMap<String, String>();

		for (Map.Entry<String, ActionParameter> parameter: parameters.entrySet()) {
			String attributeName = parameter.getKey();

			if (!this.attributes.contains(attributeName)) {
				throw new AluminumException("tag '", name, "' does not support attribute '", attributeName, "'");
			}

			attributes.put(attributeName, (String) parameter.getValue().getValue(String.class, context));
		}

		return new Tag(name, attributes, open);
	}
}