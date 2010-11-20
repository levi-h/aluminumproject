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
 * Trims text as it is written. It has a normal and a multiline mode. In the normal mode, the text that's written is
 * being trimmed in its entirety. In multiline mode, each line is being trimmed individually.
 *
 * @author levi_h
 */
public class TrimWriter extends AbstractDecorativeWriter {
	private TrimType type;
	private boolean multiline;

	private StringBuilder buffer;
	private boolean nonWhitespaceWritten;

	/**
	 * Creates a trim writer.
	 *
	 * @param writer the underlying writer
	 * @param type how text should be trimmed
	 * @param multiline {@code true} to operate in multiline mode, {@code false} to create a regular trim writer
	 */
	public TrimWriter(Writer writer, TrimType type, boolean multiline) {
		super(writer);

		this.type = type;
		this.multiline = multiline;

		this.buffer = new StringBuilder();
	}

	public void write(Object object) throws WriterException {
		checkOpen();

		if (object instanceof String) {
			for (char c: ((String) object).toCharArray()) {
				if (multiline && ((c == '\r') || (c == '\n'))) {
					clearBuffer();
					writeCharacter(c);

					nonWhitespaceWritten = false;
				} else if (Character.isWhitespace(c)) {
					if (nonWhitespaceWritten) {
						if (type.removeTrailingWhitespace) {
							buffer.append(c);
						} else {
							writeCharacter(c);
						}
					} else {
						if (!type.removeLeadingWhitespace) {
							writeCharacter(c);
						}
					}
				} else {
					writeBuffer();
					writeCharacter(c);

					nonWhitespaceWritten = true;
				}
			}
		} else {
			writeBuffer();
			getWriter().write(object);

			nonWhitespaceWritten = true;
		}
	}

	private void clearBuffer() {
		buffer.delete(0, buffer.length());
	}

	private void writeBuffer() {
		if (buffer.length() > 0) {
			getWriter().write(buffer.toString());
			clearBuffer();
		}
	}

	private void writeCharacter(char character) {
		getWriter().write(String.valueOf(character));
	}

	/**
	 * A way to trim text.
	 *
	 * @author levi_h
	 */
	public static enum TrimType {
		/** Don't remove whitespace. */
		NONE(false, false),

		/** Remove leading whitespace. */
		LEFT(true, false),

		/** Remove trailing whitespace. */
		RIGHT(false, true),

		/** Remove both leading and trailing whitespace. */
		BOTH(true, true);

		private boolean removeLeadingWhitespace;
		private boolean removeTrailingWhitespace;

		private TrimType(boolean removeLeadingWhitespace, boolean removeTrailingWhitespace) {
			this.removeLeadingWhitespace = removeLeadingWhitespace;
			this.removeTrailingWhitespace = removeTrailingWhitespace;
		}
	}
}