/*
 * Copyright 2009-2011 Levi Hoogenberg
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

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.parsers.aluscript.AluScriptContext;

/**
 * Parses a single line in an AluScript template.
 *
 * @author levi_h
 */
public interface LineParser {
	/**
	 * Determines whether a certain line is handled by this line parser.
	 *
	 * @param line the line to check
	 * @return {@code true} if the given line can be parsed by this line parser or {@code false} if this line parser
	 *         does not handle it
	 */
	boolean handles(String line);

	/**
	 * Parses a line.
	 *
	 * @param line the line to parse
	 * @param context the context to use
	 * @throws AluminumException when the line can't be parsed
	 */
	void parseLine(String line, AluScriptContext context) throws AluminumException;
}