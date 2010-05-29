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
import com.googlecode.aluminumproject.parsers.aluscript.test.TestAluScriptContext;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("all")
@Test(groups = {"parsers", "parsers-aluscript", "fast"})
public class LibraryInstructionTest {
	private Instruction instruction;

	private TestAluScriptContext context;

	@BeforeMethod
	public void createInstructionAndContext() {
		instruction = new LibraryInstruction();

		context = new TestAluScriptContext();
	}

	public void executingInstructionShouldAddLibraryUrlAbbreviationToContext() {
		instruction.execute(Collections.singletonMap("test", "http://aluminumproject.googlecode.com/test"), context);

		Map<String, String> libraryUrlAbbreviations = context.getLibraryUrlAbbreviations();
		assert libraryUrlAbbreviations != null;
		assert libraryUrlAbbreviations.size() == 1;
		assert libraryUrlAbbreviations.containsKey("test");
		assert libraryUrlAbbreviations.get("test").equals("http://aluminumproject.googlecode.com/test");
	}

	public void executingInstructionWithMultipleParametersShouldAddLibraryUrlsAbbreviationToContext() {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("t", "http://aluminumproject.googlecode.com/test");
		parameters.put("test", "http://aluminumproject.googlecode.com/test");

		instruction.execute(parameters, context);

		Map<String, String> libraryUrlAbbreviations = context.getLibraryUrlAbbreviations();
		assert libraryUrlAbbreviations != null;
		assert libraryUrlAbbreviations.size() == 2;
		assert libraryUrlAbbreviations.containsKey("t");
		assert libraryUrlAbbreviations.get("t").equals("http://aluminumproject.googlecode.com/test");
		assert libraryUrlAbbreviations.containsKey("test");
		assert libraryUrlAbbreviations.get("test").equals("http://aluminumproject.googlecode.com/test");
	}

	@Test(expectedExceptions = AluScriptException.class)
	public void executingInstructionWithoutParametersShouldCauseException() {
		instruction.execute(Collections.<String, String>emptyMap(), context);
	}
}