/*
 * Copyright 2010-2012 Aluminum project
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
package com.googlecode.aluminumproject.parsers.aluscript;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.libraries.actions.ActionParameter;
import com.googlecode.aluminumproject.parsers.aluscript.instructions.Instruction;
import com.googlecode.aluminumproject.parsers.aluscript.instructions.TestInstruction;
import com.googlecode.aluminumproject.templates.ActionContributionDescriptor;
import com.googlecode.aluminumproject.templates.ActionDescriptor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
@Test(groups = {"parsers", "parsers-aluscript", "fast"})
public class AluScriptContextTest {
	private AluScriptContext context;

	@BeforeMethod
	public void createContext() {
		context = new TestAluScriptContext();
	}

	@Test(expectedExceptions = AluminumException.class)
	public void creatingContextWithDuplicateInstructionShouldCauseException() {
		new AluScriptContext(context.getConfiguration(), context.getSettings(),
			Arrays.<Instruction>asList(new TestInstruction(), new TestInstruction()));
	}

	public void instructionsShouldBeFindableByTheirNames() {
		assert context.findInstruction("test") instanceof TestInstruction;
	}

	@Test(expectedExceptions = AluminumException.class)
	public void findingNonexistentInstructionShouldCauseException() {
		context.findInstruction("unknown");
	}

	public void initialNestingLevelShouldBeZero() {
		assert context.getLevel() == 0;
	}

	@Test(dependsOnMethods = "initialNestingLevelShouldBeZero", expectedExceptions = AluminumException.class)
	public void increasingNestingLevelShouldCauseException() {
		context.setLevel(1);
	}

	@Test(dependsOnMethods = {
		"addedLibraryUrlAbbreviationShouldBeAvailableAtCurrentNestingLevel",
		"initialNestingLevelShouldBeZero"
	})
	public void addingActionElementShouldIncrementNestingLevel() {
		ActionDescriptor actionDescriptor = new ActionDescriptor("test", "test");
		Map<String, ActionParameter> parameters = Collections.<String, ActionParameter>emptyMap();
		List<ActionContributionDescriptor> contributionDescriptors =
			Collections.<ActionContributionDescriptor>emptyList();

		context.addLibraryUrlAbbreviation("test", "http://aluminumproject.googlecode.com/test");
		context.addTemplateElement(context.getConfiguration().getTemplateElementFactory().createActionElement(
			actionDescriptor, parameters, contributionDescriptors, context.getLibraryUrlAbbreviations()));

		assert context.getLevel() == 1;
	}

	@Test(dependsOnMethods = {
		"addedLibraryUrlAbbreviationShouldBeAvailableAtCurrentNestingLevel",
		"initialNestingLevelShouldBeZero"
	})
	public void addingNonActionElementShouldNotIncrementNestingLevel() {
		context.addLibraryUrlAbbreviation("test", "http://aluminumproject.googlecode.com/test");

		context.addTemplateElement(context.getConfiguration().getTemplateElementFactory().createTextElement(
			"text", context.getLibraryUrlAbbreviations()));
	}

	public void newContextShouldNotContainLibraryUrlAbbreviations() {
		Map<String, String> libraryUrlAbbreviations = context.getLibraryUrlAbbreviations();
		assert libraryUrlAbbreviations != null;
		assert libraryUrlAbbreviations.isEmpty();
	}

	@Test(dependsOnMethods = "newContextShouldNotContainLibraryUrlAbbreviations")
	public void addedLibraryUrlAbbreviationShouldBeAvailableAtCurrentNestingLevel() {
		context.addLibraryUrlAbbreviation("test", "http://aluminumproject.googlecode.com/test");

		Map<String, String> libraryUrlAbbreviations = context.getLibraryUrlAbbreviations();
		assert libraryUrlAbbreviations != null;
		assert libraryUrlAbbreviations.containsKey("test");

		String testLibraryUrl = libraryUrlAbbreviations.get("test");
		assert testLibraryUrl != null;
		assert testLibraryUrl.equals("http://aluminumproject.googlecode.com/test");
	}
}