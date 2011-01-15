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

import com.googlecode.aluminumproject.writers.AbstractWriter;
import com.googlecode.aluminumproject.writers.Writer;
import com.googlecode.aluminumproject.writers.WriterException;

/**
 * A {@link Writer writer} that adds text nodes to {@link AbstractElement elements}. It can only write {@link String
 * strings}.
 *
 * @author levi_h
 */
class TextWriter extends AbstractWriter {
	private AbstractElement element;

	/**
	 * Creates a text writer.
	 *
	 * @param element the element to add the text nodes to
	 */
	public TextWriter(AbstractElement element) {
		this.element = element;
	}

	public void write(Object object) throws WriterException {
		checkOpen();

		if (object instanceof String) {
			element.addText((String) object);
		} else {
			throw new WriterException("can't write ", object, " - it is not a string");
		}
	}
}