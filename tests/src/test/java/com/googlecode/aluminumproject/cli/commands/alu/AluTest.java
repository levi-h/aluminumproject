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
package com.googlecode.aluminumproject.cli.commands.alu;

import com.googlecode.aluminumproject.cli.AbstractCommandTest;
import com.googlecode.aluminumproject.parsers.aluscript.AluScriptParser;
import com.googlecode.aluminumproject.utilities.environment.EnvironmentUtilities;

import java.util.Properties;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("all")
@Test(groups = {"cli", "fast"})
public class AluTest extends AbstractCommandTest {
	private Alu alu;

	private String workingDirectory;

	@BeforeClass
	public void replaceWorkingDirectory() {
		workingDirectory = System.getProperty("user.dir");

		System.setProperty("user.dir",
			Thread.currentThread().getContextClassLoader().getResource("templates/cli").getPath());
	}

	@AfterClass
	public void restoreWorkingDirectory() {
		System.setProperty("user.dir", workingDirectory);
	}

	@BeforeMethod
	public void createCommand() {
		alu = new Alu();
	}

	public void supplyingTemplateShouldHaveItProcessed() {
		Execution execution = executeCommand(alu, "-p", "aluscript", "hello.alu");
		assert execution.wasSuccessful();
		assert execution.hadOutput();
		assert execution.getOutput().equals("Hello!");
		assert !execution.hadErrors();
	}

	@Test(dependsOnMethods = "supplyingTemplateShouldHaveItProcessed")
	public void aluScriptParserShouldBeDefaultParser() {
		Execution execution = executeCommand(alu, "hello.alu");
		assert execution.wasSuccessful();
		assert execution.hadOutput();
		assert execution.getOutput().equals("Hello!");
		assert !execution.hadErrors();
	}

	@Test(dependsOnMethods = "supplyingTemplateShouldHaveItProcessed")
	public void argumentsShouldBeAvailableInContextVariable() {
		Execution execution = executeCommand(alu, "hello-with-arguments.alu", "birds", "flowers");
		assert execution.wasSuccessful();
		assert execution.hadOutput();
		assert execution.getOutput().equals("Hello, birds!\nHello, flowers!");
		assert !execution.hadErrors();
	}

	public void notSupplyingTemplateShouldResultInHelpMessage() {
		Execution execution = executeCommand(alu);
		assert execution.wasSuccessful();
		assert execution.hadOutput();
		assert execution.getOutput().contains("Usage: alu [options] [template file] [arguments]");
		assert !execution.hadErrors();
	}

	public void supplyingNonexistentParserShouldResultInErrorMessage() {
		Execution execution = executeCommand(alu, "-p", "xlm", "hello.xml");
		assert !execution.wasSuccessful();
		assert !execution.hadOutput();
		assert execution.hadErrors();
		assert execution.getErrors().equals("can't process template 'hello.xml' (unknown parser: xlm)");
	}

	@Test(dependsOnMethods = "aluScriptParserShouldBeDefaultParser")
	public void templateEngineShouldBeConfigurableUsingPropertySet() {
		Properties configurationPropertySet = new Properties();
		configurationPropertySet.setProperty(AluScriptParser.TEMPLATE_EXTENSION, "alu");

		EnvironmentUtilities.getPropertySetContainer().writePropertySet("alu", configurationPropertySet);

		try {
			Execution execution = executeCommand(alu, "hello");
			assert execution.wasSuccessful();
			assert execution.hadOutput();
			assert execution.getOutput().equals("Hello!");
			assert !execution.hadErrors();
		} finally {
			EnvironmentUtilities.getPropertySetContainer().removePropertySet("alu");
		}
	}
}