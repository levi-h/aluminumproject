/*
 * Copyright 2009-2012 Levi Hoogenberg
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
package com.googlecode.aluminumproject.parsers.aluscript.lines.comments;

import com.googlecode.aluminumproject.parsers.aluscript.AluScriptContext;
import com.googlecode.aluminumproject.parsers.aluscript.lines.AbstractLineParser;

/**
 * Parses lines that contain a comment. Comments start with a {@literal #} symbol.
 */
public class CommentLineParser extends AbstractLineParser {
	/**
	 * Creates a comment line parser.
	 */
	public CommentLineParser() {}

	public boolean handles(String line) {
		return removeIndentation(line).startsWith("#");
	}

	public void parseLine(String line, AluScriptContext context) {
		logger.debug("ignoring comment (", removeIndentation(line), ")");
	}
}