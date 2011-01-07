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
package com.googlecode.aluminumproject.parsers.aluscript.lines.instructions;

import com.googlecode.aluminumproject.parsers.aluscript.AluScriptContext;
import com.googlecode.aluminumproject.parsers.aluscript.AluScriptException;
import com.googlecode.aluminumproject.parsers.aluscript.instructions.ActionInstruction;
import com.googlecode.aluminumproject.parsers.aluscript.instructions.Instruction;
import com.googlecode.aluminumproject.parsers.aluscript.lines.AbstractLineParser;
import com.googlecode.aluminumproject.utilities.UtilityException;
import com.googlecode.aluminumproject.utilities.text.Splitter;
import com.googlecode.aluminumproject.utilities.text.Splitter.QuotationCharacters;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Parses lines that contain instructions.
 * <p>
 * Instructions start with an {@literal @} symbol. If the name of the instruction contains a dot, then an {@link
 * ActionInstruction action instruction} will be executed; in other cases, an instruction with the given name will be
 * looked for in the {@link AluScriptContext context}.
 * <p>
 * Both instructions and actions support parameters. Parameters are comma-separated and enclosed in parentheses; their
 * names and values are separated by a colon. Parameter names may contain a dot; for actions, this means that the
 * parameter will be treated as an action contribution.
 * <p>
 * A backslash can be used to escape special characters; a character sequence can be escaped by surrounding it with
 * (either single or double) quotes.
 *
 * @author levi_h
 */
public class InstructionLineParser extends AbstractLineParser {
	/**
	 * Creates an instruction line parser.
	 */
	public InstructionLineParser() {}

	public boolean handles(String line) {
		return removeIndentation(line).startsWith("@");
	}

	public void parseLine(String line, AluScriptContext context) throws AluScriptException {
		context.setLevel(getLevel(line));

		line = removeIndentation(line);

		InstructionTokenProcessor processor = new InstructionTokenProcessor();

		try {
			List<String> separatorPatterns = processor.getSeparatorPatterns();
			char escapeCharacter = '\\';
			List<QuotationCharacters> quotationCharacters =
				Arrays.asList(new QuotationCharacters('\'', '\'', false), new QuotationCharacters('"', '"', false));

			new Splitter(separatorPatterns, escapeCharacter, quotationCharacters).split(line, processor);
		} catch (UtilityException exception) {
			throw new AluScriptException(exception, "can't parse instruction line '", line, "'");
		}

		Instruction instruction = findInstruction(context,
			processor.getInstruction().getNamePrefix(), processor.getInstruction().getName());
		Map<String, String> parameters = convertParameters(processor.getInstruction().getParameters());

		logger.debug("executing ", instruction, " with parameters ", parameters);

		instruction.execute(parameters, context);
	}

	private Instruction findInstruction(AluScriptContext context, String namePrefix, String name) {
		Instruction instruction;

		if (namePrefix == null) {
			instruction = context.findInstruction(name);
		} else {
			instruction = new ActionInstruction(namePrefix, name);
		}

		return instruction;
	}

	private Map<String, String> convertParameters(
			List<InstructionParameter> instructionParameters) throws AluScriptException {
		Map<String, String> parameters = new HashMap<String, String>();

		for (InstructionParameter instructionParameter: instructionParameters) {
			String namePrefix = instructionParameter.getNamePrefix();
			String name = instructionParameter.getName();

			if (namePrefix != null) {
				name = String.format("%s.%s", namePrefix, name);
			}

			if (parameters.containsKey(name)) {
				throw new AluScriptException("duplicate parameter (", name, ")");
			} else {
				parameters.put(name, instructionParameter.getValue());
			}
		}

		return parameters;
	}
}