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

import java.util.EnumSet;
import java.util.Set;

/**
 * A transition between two {@link InstructionState states} of an {@link InstructionTokenProcessor
 * instruction token processor}.
 */
abstract class InstructionStateTransition {
	private Set<InstructionState> startStates;
	private InstructionState endState;
	private String separatorPattern;

	/**
	 * Creates an instruction state transition.
	 *
	 * @param startState the start state of the transition
	 * @param endState the end state of the transition
	 * @param separatorPattern the separator pattern that may cause the transition
	 */
	protected InstructionStateTransition(InstructionState startState,
			InstructionState endState, String separatorPattern) {
		this(EnumSet.of(startState), endState, separatorPattern);
	}

	/**
	 * Creates an instruction state transition.
	 *
	 * @param startStates the possible start states of the transition
	 * @param endState the end state of the transition
	 * @param separatorPattern the separator pattern that may cause the transition
	 */
	protected InstructionStateTransition(Set<InstructionState> startStates,
			InstructionState endState, String separatorPattern) {
		this.startStates = startStates;
		this.endState = endState;
		this.separatorPattern = separatorPattern;
	}

	/**
	 * Determines whether this instruction state transition is possible when an instruction token processor has a
	 * certain state.
	 *
	 * @param startState the start state to check
	 * @return {@code true} if this instruction state transition is possible with the given start state, {@code false}
	 *         otherwise
	 */
	public boolean containsStartState(InstructionState startState) {
		return startStates.contains(startState);
	}

	/**
	 * Returns the state of an instruction token processor after this instruction state transition.
	 *
	 * @return the end state of this instruction state transition
	 */
	public InstructionState getEndState() {
		return endState;
	}

	/**
	 * Returns the separator pattern that may cause this instruction state transition. The separator pattern may be
	 * {@code null}: in that case, no separator is expected, but rather the end of the text.
	 *
	 * @return this instruction state transition's separator pattern
	 */
	public String getSeparatorPattern() {
		return separatorPattern;
	}

	/**
	 * Performs the actions that are necessary for this instruction state transition on an instruction token processor.
	 *
	 * @param instructionTokenProcessor the instruction token processor to modify
	 * @param token the token to use
	 * @throws AluminumException when something goes wrong while changing the instruction token processor
	 */
	public abstract void makeTransition(InstructionTokenProcessor instructionTokenProcessor, String token)
		throws AluminumException;
}