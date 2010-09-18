/*
 * Copyright 2009-2010 Levi Hoogenberg
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
package com.googlecode.aluminumproject.writers;

/**
 * Writes text to an underlying writer while preserving only a number of contiguous characters of a certain type.
 *
 * @author levi_h
 */
public class PreserveWhitespaceWriter extends AbstractDecorativeWriter {
	private char characterToPreserve;
	private int amount;

	private int count;

	/**
	 * Creates a preserve whitespace writer.
	 *
	 * @param writer the underlying writer
	 * @param type the kind of whitespace to preserve
	 * @param amount the number of characters to preserve
	 */
	public PreserveWhitespaceWriter(Writer writer, WhitespaceType type, int amount) {
		super(writer);

		switch (type) {
			case SPACE:
				characterToPreserve = ' ';

				break;

			case TAB:
				characterToPreserve = '\t';

				break;

			case NEWLINE:
				characterToPreserve = '\n';

				break;
		}

		this.amount = amount;
	}

	public void write(Object object) throws WriterException {
		checkOpen();

		if (object instanceof String) {
			for (char character: ((String) object).toCharArray()) {
				if (character == characterToPreserve) {
					if (count < amount) {
						writeCharacter(character);

						count++;
					}
				} else {
					writeCharacter(character);

					count = 0;
				}
			}
		} else {
			getWriter().write(object);

			count = 0;
		}
	}

	private void writeCharacter(char character) {
		getWriter().write(String.valueOf(character));
	}

	/**
	 * A kind of whitespace.
	 *
	 * @author levi_h
	 */
	public static enum WhitespaceType {
		/** A space. */
		SPACE,

		/** A tab. */
		TAB,

		/** A newline. */
		NEWLINE;
	}
}