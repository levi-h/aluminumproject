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
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.libraries.actions.AbstractAction;
import com.googlecode.aluminumproject.libraries.xml.model.Element;
import com.googlecode.aluminumproject.writers.Writer;

@SuppressWarnings("javadoc")
public class Format extends AbstractAction {
	private Element document;

	private int indentation;

	public Format() {
		indentation = DEFAULT_INDENTATION;
	}

	public void execute(Context context, Writer writer) throws AluminumException {
		Element document;

		if (this.document == null) {
			logger.debug("no document parameter given, using body object");

			document = getBodyObject(Element.class, context, writer);
		} else {
			document = this.document;
		}

		logger.debug("writing document with ", indentation, " space", (indentation == 1) ? "" : "s");

		document.writeDocument(writer, indentation);
	}

	public final static int DEFAULT_INDENTATION = 4;
}