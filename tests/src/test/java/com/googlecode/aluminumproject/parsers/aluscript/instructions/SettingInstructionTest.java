/*
 * Copyright 2013 Aluminum project
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
package com.googlecode.aluminumproject.parsers.aluscript.instructions;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.converters.SingleConverterRegistry;
import com.googlecode.aluminumproject.converters.common.StringToBooleanConverter;
import com.googlecode.aluminumproject.parsers.aluscript.TestAluScriptContext;

import java.util.Collections;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
@Test(groups = {"parsers", "parsers-aluscript", "fast"})
public class SettingInstructionTest {
	private Instruction instruction;

	private TestAluScriptContext context;

	@BeforeMethod
	public void createInstructionAndContext() {
		instruction = new SettingInstruction();

		context = new TestAluScriptContext();
	}

	public void executingInstructionShouldChangeSetting() {
		context.getConfiguration().setConverterRegistry(
			new SingleConverterRegistry<String>(String.class, new StringToBooleanConverter()));

		instruction.execute(Collections.singletonMap("automatic newlines", "false"), context);

		assert !context.getSettings().isAutomaticNewlines();
	}

	@Test(expectedExceptions = AluminumException.class)
	public void tryingToChangeUnknownSettingShouldCauseException() {
		instruction.execute(Collections.singletonMap("nonexistent", "value"), context);
	}
}