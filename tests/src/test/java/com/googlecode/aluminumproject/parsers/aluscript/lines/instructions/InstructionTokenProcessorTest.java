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
package com.googlecode.aluminumproject.parsers.aluscript.lines.instructions;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.utilities.text.Splitter;

import java.util.List;

import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
@Test(groups = {"parsers", "parsers-aluscript", "fast"})
public class InstructionTokenProcessorTest {
	public void instructionWithNameShouldBeAccepted() {
		Instruction instruction = createInstruction("@newline");
		assert instruction.getNamePrefix() == null;
		assert instruction.getName() != null;
		assert instruction.getName().equals("newline");

		List<InstructionParameter> parameters = instruction.getParameters();
		assert parameters != null;
		assert parameters.isEmpty();
	}

	public void instructionWithPrefixedNameShouldBeAccepted() {
		Instruction instruction = createInstruction("@c.template");
		assert instruction.getNamePrefix() != null;
		assert instruction.getNamePrefix().equals("c");
		assert instruction.getName() != null;
		assert instruction.getName().equals("template");

		List<InstructionParameter> parameters = instruction.getParameters();
		assert parameters != null;
		assert parameters.isEmpty();
	}

	public void instructionWithParameterShouldBeAccepted() {
		Instruction instruction = createInstruction("@library(c: http://aluminumproject.googlecode.com/core)");
		assert instruction.getNamePrefix() == null;
		assert instruction.getName() != null;
		assert instruction.getName().equals("library");

		List<InstructionParameter> parameters = instruction.getParameters();
		assert parameters != null;
		assert parameters.size() == 1;

		InstructionParameter parameter = parameters.get(0);
		assert parameter != null;
		assert parameter.getNamePrefix() == null;
		assert parameter.getName() != null;
		assert parameter.getName().equals("c");
		assert parameter.getValue() != null;
		assert parameter.getValue().equals("http://aluminumproject.googlecode.com/core");
	}

	public void instructionWithMultipleParametersShouldBeAccepted() {
		Instruction instruction = createInstruction("@c.each(elements: ${templateScope.animals}, c.if: ${printZoo})");
		assert instruction.getNamePrefix() != null;
		assert instruction.getNamePrefix().equals("c");
		assert instruction.getName() != null;
		assert instruction.getName().equals("each");

		List<InstructionParameter> parameters = instruction.getParameters();
		assert parameters != null;
		assert parameters.size() == 2;

		InstructionParameter firstParameter = parameters.get(0);
		assert firstParameter != null;
		assert firstParameter.getNamePrefix() == null;
		assert firstParameter.getName() != null;
		assert firstParameter.getName().equals("elements");
		assert firstParameter.getValue() != null;
		assert firstParameter.getValue().equals("${templateScope.animals}");

		InstructionParameter secondParameter = parameters.get(1);
		assert secondParameter != null;
		assert secondParameter.getNamePrefix() != null;
		assert secondParameter.getNamePrefix().equals("c");
		assert secondParameter.getName() != null;
		assert secondParameter.getName().equals("if");
		assert secondParameter.getValue() != null;
		assert secondParameter.getValue().equals("${printZoo}");
	}

	@Test(dependsOnMethods = "instructionWithNameShouldBeAccepted", expectedExceptions = AluminumException.class)
	public void textBeforeInstructionShouldCauseException() {
		createInstruction("  @newline");
	}

	@Test(expectedExceptions = AluminumException.class)
	public void incompleteInstructionShouldCauseException() {
		createInstruction("@c.template(");
	}

	@Test(dependsOnMethods = "instructionWithParameterShouldBeAccepted", expectedExceptions = AluminumException.class)
	public void textAfterParametersShouldCauseException() {
		createInstruction("@library(c: http://aluminumproject.googlecode.com/core) ");
	}

	private Instruction createInstruction(String text) throws AluminumException {
		InstructionTokenProcessor processor = new InstructionTokenProcessor();

		new Splitter(processor.getSeparatorPatterns()).split(text, processor);

		return processor.getInstruction();
	}
}