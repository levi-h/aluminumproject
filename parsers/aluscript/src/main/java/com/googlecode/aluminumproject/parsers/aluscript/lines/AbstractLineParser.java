/*
 * Copyright 2009-2012 Aluminum project
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
package com.googlecode.aluminumproject.parsers.aluscript.lines;

import com.googlecode.aluminumproject.utilities.Logger;

/**
 * Abstract superclass of line parsers.
 */
public abstract class AbstractLineParser implements LineParser {
	/** The logger to use. */
	protected final Logger logger;

	/**
	 * Creates an abstract line parser.
	 */
	protected AbstractLineParser() {
		logger = Logger.get(getClass());
	}

	/**
	 * Determines the nesting level of a line by counting the number of tab characters at the start of the line.
	 *
	 * @param line the (indented) line to determine the nesting level of
	 * @return the nesting level of the given line (&gt;= 0)
	 */
	protected int getLevel(String line) {
		int level = 0;

		while ((level < line.length()) && (line.charAt(level) == '\t')) {
			level++;
		}

		return level;
	}

	/**
	 * Removes all leading tab characters from a line.
	 *
	 * @param indentedLine the line to remove indentation from
	 * @return the given line without its indentation characters
	 */
	protected String removeIndentation(String indentedLine) {
		return indentedLine.replaceFirst("\t+", "");
	}
}