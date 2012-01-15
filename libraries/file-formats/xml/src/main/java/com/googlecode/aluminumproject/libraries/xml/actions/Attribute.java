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
import com.googlecode.aluminumproject.annotations.Required;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.libraries.actions.AbstractAction;
import com.googlecode.aluminumproject.writers.Writer;

@SuppressWarnings("javadoc")
public class Attribute extends AbstractAction {
	private String prefix;
	private @Required String name;
	private @Required String value;

	public void execute(Context context, Writer writer) throws AluminumException {
		AbstractElement element = findAncestorOfType(AbstractElement.class);

		if (element == null) {
			throw new AluminumException("attributes should be nested inside an element");
		} else {
			element.addAttribute(prefix, name, value);
		}
	}
}