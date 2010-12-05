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

import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.libraries.actions.AbstractAction;
import com.googlecode.aluminumproject.libraries.actions.ActionException;
import com.googlecode.aluminumproject.writers.Writer;
import com.googlecode.aluminumproject.writers.WriterException;

import java.io.IOException;
import java.io.StringReader;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.ParsingException;

/**
 * Parses its body text as an XML document and writes the resulting {@link Element root element}.
 *
 * @author levi_h
 */
public class Parse extends AbstractAction {
	/**
	 * Creates a <em>parse</em> action.
	 */
	public Parse() {}

	public void execute(Context context, Writer writer) throws ActionException, WriterException {
		String text = getBodyText(context, writer);

		logger.debug("body text is '", text, "', now trying to parse");

		Document document;

		try {
			document = new Builder().build(new StringReader(text));
		} catch (ParsingException exception) {
			throw new ActionException(exception, "can't parse '", text, "'");
		} catch (IOException exception) {
			throw new ActionException(exception, "can't parse '", text, "'");
		}

		logger.debug("parsed body text, result: ", document);

		writer.write(new XomElement(document.getRootElement()));
	}
}