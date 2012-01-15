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
import com.googlecode.aluminumproject.annotations.Ignored;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.libraries.actions.AbstractAction;
import com.googlecode.aluminumproject.writers.Writer;

import java.util.Map;

@Ignored
@SuppressWarnings("javadoc")
class Tag extends AbstractAction {
	private String name;
	private Map<String, String> attributes;

	private boolean open;

	public Tag(String name, Map<String, String> attributes, boolean open) {
		this.name = name;
		this.attributes = attributes;

		this.open = open;
	}

	public void execute(Context context, Writer writer) throws AluminumException {
		writer.write("<");
		writer.write(name);

		for (Map.Entry<String, String> attribute: attributes.entrySet()) {
			writer.write(" ");
			writer.write(attribute.getKey());
			writer.write("=\"");
			writer.write(attribute.getValue());
			writer.write("\"");
		}

		writer.write(">");

		if (!open) {
			String bodyText = getBodyText(context, writer).trim();

			if (bodyText.length() > 0) {
				writer.write(bodyText);
			}

			writer.write("</");
			writer.write(name);
			writer.write(">");
		}
	}
}