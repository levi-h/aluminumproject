/*
 * Copyright 2010-2012 Levi Hoogenberg
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
import com.googlecode.aluminumproject.libraries.actions.ActionParameter;
import com.googlecode.aluminumproject.libraries.actions.DynamicallyParameterisable;
import com.googlecode.aluminumproject.writers.Writer;

import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings("javadoc")
public class DynamicElement extends AbstractElement implements DynamicallyParameterisable {
	private @Ignored Map<String, ActionParameter> parameters;

	public DynamicElement() {
		parameters = new LinkedHashMap<String, ActionParameter>();
	}

	@Override
	protected String getElementName() {
		return descriptor.getName();
	}

	public void setParameter(String name, ActionParameter parameter) {
		parameters.put(name, parameter);
	}

	@Override
	public void execute(Context context, Writer writer) throws AluminumException {
		for (String name: parameters.keySet()) {
			addAttribute(null, name, (String) parameters.get(name).getValue(String.class, context));
		}

		super.execute(context, writer);
	}
}