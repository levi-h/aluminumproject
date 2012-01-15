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
import com.googlecode.aluminumproject.converters.ConverterRegistry;
import com.googlecode.aluminumproject.writers.AbstractWriter;

@SuppressWarnings("javadoc")
class TextWriter extends AbstractWriter {
	private AbstractElement element;

	private ConverterRegistry converterRegistry;

	public TextWriter(AbstractElement element, ConverterRegistry converterRegistry) {
		this.element = element;

		this.converterRegistry = converterRegistry;
	}

	public void write(Object object) throws AluminumException {
		checkOpen();

		String text = (String) ((object instanceof String) ? object : converterRegistry.convert(object, String.class));

		if (text == null) {
			throw new AluminumException("can't write null values");
		} else {
			element.addText(text);
		}
	}
}