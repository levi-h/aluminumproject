/*
 * Copyright 2010-2011 Levi Hoogenberg
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

import com.googlecode.aluminumproject.converters.ConverterException;
import com.googlecode.aluminumproject.converters.ConverterRegistry;
import com.googlecode.aluminumproject.writers.AbstractWriter;
import com.googlecode.aluminumproject.writers.Writer;
import com.googlecode.aluminumproject.writers.WriterException;

/**
 * A {@link Writer writer} that adds text nodes to {@link AbstractElement elements}. When a non-{@link String string} is
 * written, it will be converted first.
 *
 * @author levi_h
 */
class TextWriter extends AbstractWriter {
	private AbstractElement element;

	private ConverterRegistry converterRegistry;

	/**
	 * Creates a text writer.
	 *
	 * @param element the element to add the text nodes to
	 * @param converterRegistry the converter registry to use when something other than text is being written
	 */
	public TextWriter(AbstractElement element, ConverterRegistry converterRegistry) {
		this.element = element;

		this.converterRegistry = converterRegistry;
	}

	public void write(Object object) throws WriterException {
		checkOpen();

		String text;

		if (object instanceof String) {
			text = (String) object;
		} else {
			try {
				text = (String) converterRegistry.convert(object, String.class);
			} catch (ConverterException exception) {
				throw new WriterException("can't convert ", object, " to a string");
			}
		}

		if (text == null) {
			throw new WriterException("can't write null values");
		} else {
			element.addText(text);
		}
	}
}