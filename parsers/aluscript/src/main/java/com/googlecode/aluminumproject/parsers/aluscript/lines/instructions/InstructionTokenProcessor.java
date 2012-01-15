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
package com.googlecode.aluminumproject.parsers.aluscript.lines.instructions;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.utilities.Utilities;
import com.googlecode.aluminumproject.utilities.text.Splitter.TokenProcessor;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Processes tokens that form an {@Instruction instruction}. An instruction token is meant to be used only once; after
 * an instruction has been created, the token processor should no longer be used other than to obtain the new
 * instruction.
 */
class InstructionTokenProcessor implements TokenProcessor {
	private InstructionState state;
	private List<InstructionStateTransition> stateTransitions;

	private StringBuilder tokenBuilder;

	private Instruction instruction;

	/**
	 * Creates an instruction token processor.
	 */
	public InstructionTokenProcessor() {
		state = InstructionState.READY;
		stateTransitions = new LinkedList<InstructionStateTransition>();

		addStateTransitions();

		tokenBuilder = new StringBuilder();
	}

	private void addStateTransitions() {
		final InstructionState AFTER_PARAMETERS = InstructionState.AFTER_PARAMETERS;
		final InstructionState DONE = InstructionState.DONE;
		final InstructionState INSTRUCTION_NAME = InstructionState.INSTRUCTION_NAME;
		final InstructionState INSTRUCTION_NAME_OR_PREFIX = InstructionState.INSTRUCTION_NAME_OR_PREFIX;
		final InstructionState PARAMETER_NAME = InstructionState.PARAMETER_NAME;
		final InstructionState PARAMETER_NAME_OR_PREFIX = InstructionState.PARAMETER_NAME_OR_PREFIX;
		final InstructionState PARAMETER_VALUE = InstructionState.PARAMETER_VALUE;
		final InstructionState READY = InstructionState.READY;

		stateTransitions.add(new InstructionStateTransition(READY, INSTRUCTION_NAME_OR_PREFIX, "@ *") {
			@Override
			public void makeTransition(
					InstructionTokenProcessor instructionTokenProcessor, String token) throws AluminumException {
				if (!token.equals("")) {
					throw new AluminumException("unexpected text before instruction: '", token, "'");
				}

				instructionTokenProcessor.createInstruction();
			}
		});

		stateTransitions.add(new InstructionStateTransition(INSTRUCTION_NAME_OR_PREFIX, INSTRUCTION_NAME, " *\\. *") {
			@Override
			public void makeTransition(
					InstructionTokenProcessor instructionTokenProcessor, String token) throws AluminumException {
				instructionTokenProcessor.getInstruction(false).setNamePrefix(token);
			}
		});

		stateTransitions.add(new InstructionStateTransition(INSTRUCTION_NAME_OR_PREFIX, INSTRUCTION_NAME, " *\\. *") {
			@Override
			public void makeTransition(
					InstructionTokenProcessor instructionTokenProcessor, String token) throws AluminumException {
				instructionTokenProcessor.getInstruction(false).setNamePrefix(token);
			}
		});

		EnumSet<InstructionState> instructionName = EnumSet.of(INSTRUCTION_NAME_OR_PREFIX, INSTRUCTION_NAME);

		stateTransitions.add(new InstructionStateTransition(instructionName, DONE, null) {
			@Override
			public void makeTransition(
					InstructionTokenProcessor instructionTokenProcessor, String token) throws AluminumException {
				instructionTokenProcessor.getInstruction(false).setName(token);
			}
		});

		stateTransitions.add(new InstructionStateTransition(instructionName, PARAMETER_NAME_OR_PREFIX, " *\\( *") {
			@Override
			public void makeTransition(
					InstructionTokenProcessor instructionTokenProcessor, String token) throws AluminumException {
				instructionTokenProcessor.getInstruction(false).setName(token);
			}
		});

		stateTransitions.add(new InstructionStateTransition(PARAMETER_NAME_OR_PREFIX, PARAMETER_NAME, " *\\. *") {
			@Override
			public void makeTransition(
					InstructionTokenProcessor instructionTokenProcessor, String token) throws AluminumException {
				Instruction instruction = instructionTokenProcessor.getInstruction(false);

				instruction.createParameter();
				instruction.getParameter().setNamePrefix(token);
			}
		});

		EnumSet<InstructionState> parameterName = EnumSet.of(PARAMETER_NAME_OR_PREFIX, PARAMETER_NAME);

		stateTransitions.add(new InstructionStateTransition(parameterName, PARAMETER_VALUE, " *: *") {
			@Override
			public void makeTransition(
					InstructionTokenProcessor instructionTokenProcessor, String token) throws AluminumException {
				Instruction instruction = instructionTokenProcessor.getInstruction(false);

				if (instruction.getParameter() == null) {
					instruction.createParameter();
				}

				instruction.getParameter().setName(token);
			}
		});

		stateTransitions.add(new InstructionStateTransition(PARAMETER_VALUE, PARAMETER_NAME_OR_PREFIX, ", *") {
			@Override
			public void makeTransition(
					InstructionTokenProcessor instructionTokenProcessor, String token) throws AluminumException {
				Instruction instruction = instructionTokenProcessor.getInstruction(false);

				instruction.getParameter().setValue(token);
				instruction.addParameter();
			}
		});

		stateTransitions.add(new InstructionStateTransition(PARAMETER_VALUE, AFTER_PARAMETERS, " *\\)") {
			@Override
			public void makeTransition(
					InstructionTokenProcessor instructionTokenProcessor, String token) throws AluminumException {
				Instruction instruction = instructionTokenProcessor.getInstruction(false);

				instruction.getParameter().setValue(token);
				instruction.addParameter();
			}
		});

		stateTransitions.add(new InstructionStateTransition(AFTER_PARAMETERS, DONE, null) {
			@Override
			public void makeTransition(
					InstructionTokenProcessor instructionTokenProcessor, String token) throws AluminumException {
				if (!token.equals("")) {
					throw new AluminumException("unexpected text after parameters: '", token, "'");
				}
			}
		});
	}

	/**
	 * Returns all of the separator patterns of the instruction state transitions that this instruction token processor
	 * delegates to.
	 *
	 * @return all separator patterns that are recognised by this instruction token processor
	 */
	public List<String> getSeparatorPatterns() {
		Set<String> separatorPatterns = new HashSet<String>();

		for (InstructionStateTransition stateTransition: stateTransitions) {
			String separatorPattern = stateTransition.getSeparatorPattern();

			if (separatorPattern != null) {
				separatorPatterns.add(separatorPattern);
			}
		}

		return new LinkedList<String>(separatorPatterns);
	}

	/**
	 * Creates a new instruction.
	 */
	protected void createInstruction() {
		instruction = new Instruction();
	}

	/**
	 * Returns the instruction that was built and filled by this instruction token processor.
	 *
	 * @return this instruction token processor's instruction
	 * @throws AluminumException when the instruction is incomplete
	 */
	public Instruction getInstruction() throws AluminumException {
		return getInstruction(true);
	}

	/**
	 * Returns the instruction that is being built by this instruction token processor.
	 *
	 * @param requireCompleteInstruction whether the instruction should be complete
	 * @return this instruction token processor's instruction
	 * @throws AluminumException when {@code requireCompleteInstruction} is {@code true} and the instruction is
	 *                           incomplete
	 */
	protected Instruction getInstruction(boolean requireCompleteInstruction) throws AluminumException {
		if (requireCompleteInstruction && (state != InstructionState.DONE)) {
			throw new AluminumException("the instruction is incomplete");
		}

		return instruction;
	}

	public void process(String token, String separator, String separatorPattern) throws AluminumException {
		Iterator<InstructionStateTransition> it = stateTransitions.iterator();

		InstructionStateTransition stateTransition = null;

		while (it.hasNext() && (stateTransition == null)) {
			stateTransition = it.next();

			if (!stateTransition.containsStartState(state) ||
					!Utilities.equals(stateTransition.getSeparatorPattern(), separatorPattern)) {
				stateTransition = null;
			}
		}

		tokenBuilder.append(token);

		if (stateTransition == null) {
			tokenBuilder.append(separator);
		} else {
			stateTransition.makeTransition(this, tokenBuilder.toString());

			state = stateTransition.getEndState();

			tokenBuilder.delete(0, tokenBuilder.length());
		}
	}
}