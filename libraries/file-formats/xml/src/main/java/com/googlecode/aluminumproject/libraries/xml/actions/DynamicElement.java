/*
 * Copyright 2010 Levi Hoogenberg
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

import com.googlecode.aluminumproject.annotations.Ignored;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.ContextException;
import com.googlecode.aluminumproject.libraries.actions.ActionException;
import com.googlecode.aluminumproject.libraries.actions.ActionParameter;
import com.googlecode.aluminumproject.libraries.actions.DynamicallyParameterisable;
import com.googlecode.aluminumproject.writers.Writer;
import com.googlecode.aluminumproject.writers.WriterException;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Creates an XML element that has a name that's equal to its action name. It supports dynamic parameters: for each
 * parameter, an attribute is added to the element.
 *
 * @author levi_h
 */
public class DynamicElement extends AbstractElement implements DynamicallyParameterisable {
	private @Ignored Map<String, ActionParameter> parameters;

	/**
	 * Creates a dynamic element.
	 */
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
	public void execute(Context context, Writer writer) throws ActionException, ContextException, WriterException {
		for (String name: parameters.keySet()) {
			addAttribute(null, name, (String) parameters.get(name).getValue(String.class, context));
		}

		super.execute(context, writer);
	}
}