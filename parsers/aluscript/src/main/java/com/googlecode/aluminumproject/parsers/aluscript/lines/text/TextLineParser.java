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
package com.googlecode.aluminumproject.parsers.aluscript.lines.text;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.expressions.ExpressionFactory;
import com.googlecode.aluminumproject.expressions.ExpressionOccurrence;
import com.googlecode.aluminumproject.parsers.aluscript.AluScriptContext;
import com.googlecode.aluminumproject.parsers.aluscript.AluScriptSettings;
import com.googlecode.aluminumproject.parsers.aluscript.instructions.ExpressionInstruction;
import com.googlecode.aluminumproject.parsers.aluscript.instructions.Instruction;
import com.googlecode.aluminumproject.parsers.aluscript.instructions.NewlineInstruction;
import com.googlecode.aluminumproject.parsers.aluscript.instructions.TextInstruction;
import com.googlecode.aluminumproject.parsers.aluscript.lines.AbstractLineParser;
import com.googlecode.aluminumproject.utilities.ParserUtilities;

import java.util.Collections;
import java.util.SortedMap;

/**
 * Parses lines that contain text and/or expressions. It executes {@link TextInstruction text} and {@link
 * ExpressionInstruction expression} instructions for each text or expression found and, with the default {@link
 * AluScriptSettings settings}, finishes by executing a {@link NewlineInstruction newline instruction}.
 *
 * @author levi_h
 */
public class TextLineParser extends AbstractLineParser {
	/**
	 * Creates a text line parser.
	 */
	public TextLineParser() {}

	public boolean handles(String line) {
		return true;
	}

	public void parseLine(String line, AluScriptContext context) throws AluminumException {
		context.setLevel(getLevel(line));

		line = removeIndentation(line);

		Configuration configuration = context.getConfiguration();

		SortedMap<ExpressionOccurrence, ExpressionFactory> expressionOccurrences =
			ParserUtilities.getExpressionOccurrences(line, configuration);

		for (ExpressionOccurrence expressionOccurrence: expressionOccurrences.keySet()) {
			ExpressionFactory expressionFactory = expressionOccurrences.get(expressionOccurrence);
			String text = line.substring(expressionOccurrence.getBeginIndex(), expressionOccurrence.getEndIndex());

			Instruction instruction;

			if (expressionFactory == null) {
				instruction = new TextInstruction("text", text);
			} else {
				instruction = new ExpressionInstruction(expressionFactory, text);
			}

			instruction.execute(Collections.<String, String>emptyMap(), context);
		}

		if (context.getSettings().isAutomaticNewlines()) {
			new NewlineInstruction().execute(Collections.<String, String>emptyMap(), context);
		}
	}
}