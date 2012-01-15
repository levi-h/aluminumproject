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
import com.googlecode.aluminumproject.writers.Writer;

import java.io.IOException;
import java.io.StringReader;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.ParsingException;

@SuppressWarnings("javadoc")
public class Parse extends AbstractAction {
	public void execute(Context context, Writer writer) throws AluminumException {
		String text = getBodyText(context, writer);

		logger.debug("body text is '", text, "', now trying to parse");

		Document document;

		try {
			document = new Builder().build(new StringReader(text));
		} catch (ParsingException exception) {
			throw new AluminumException(exception, "can't parse '", text, "'");
		} catch (IOException exception) {
			throw new AluminumException(exception, "can't parse '", text, "'");
		}

		logger.debug("parsed body text, result: ", document);

		writer.write(new XomElement(document.getRootElement()));
	}
}