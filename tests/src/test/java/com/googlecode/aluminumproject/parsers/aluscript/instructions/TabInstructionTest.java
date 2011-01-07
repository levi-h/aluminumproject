/*
 * Copyright 2010 Levi Hoogenberg
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

import com.googlecode.aluminumproject.parsers.aluscript.AluScriptException;
import com.googlecode.aluminumproject.parsers.aluscript.TestAluScriptContext;
import com.googlecode.aluminumproject.templates.TemplateElement;
import com.googlecode.aluminumproject.templates.TextElement;

import java.util.Collections;
import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("all")
@Test(groups = {"parsers", "parsers-aluscript", "fast"})
public class TabInstructionTest {
	private Instruction instruction;

	private TestAluScriptContext context;

	@BeforeMethod
	public void createInstructionAndContext() {
		instruction = new TabInstruction();

		context = new TestAluScriptContext();
	}

	public void executingInstructionShouldAddTextElementContainingTabToContext() {
		instruction.execute(Collections.<String, String>emptyMap(), context);

		List<TemplateElement> templateElements = context.getTemplateElements();
		assert templateElements.size() == 1;
		assert templateElements.get(0) instanceof TextElement;
		assert ((TextElement) templateElements.get(0)).getText().equals("\t");
	}

	@Test(expectedExceptions = AluScriptException.class)
	public void supplyingParametersToInstructionShouldCauseException() {
		instruction.execute(Collections.singletonMap("hard", "true"), context);
	}
}