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
package com.googlecode.aluminumproject.cli.commands.alu;

import com.googlecode.aluminumproject.cli.AbstractCommandTest;

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
			Thread.currentThread().getContextClassLoader().getResource("templates/aluscript/cli").getPath());
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
		String[] output = executeCommand(alu, "-p", "aluscript", "hello.alu");
		assert output[0].equals("Hello!");
		assert output[1].equals("");
	}

	@Test(dependsOnMethods = "supplyingTemplateShouldHaveItProcessed")
	public void aluScriptParserShouldBeDefaultParser() {
		String[] output = executeCommand(alu, "hello.alu");
		assert output[0].equals("Hello!");
		assert output[1].equals("");
	}

	@Test(dependsOnMethods = "supplyingTemplateShouldHaveItProcessed")
	public void argumentsShouldBeAvailableInContextVariable() {
		String[] output = executeCommand(alu, "hello-with-arguments.alu", "birds", "flowers");
		assert output[0].equals("Hello, birds!\nHello, flowers!");
		assert output[1].equals("");
	}

	public void notSupplyingTemplateShouldResultInHelpMessage() {
		String[] output = executeCommand(alu);
		assert output[0].contains("Usage: alu [options] [template file] [arguments]");
		assert output[1].equals("");
	}

	public void supplyingNonexistentParserShouldResultInErrorMessage() {
		String[] output = executeCommand(alu, "-p", "xlm", "hello.xml");
		assert output[0].equals("");
		assert output[1].equals("The template 'hello.xml' can't be processed (unknown parser: xlm).");
		assert output[0].equals("");
		assert output[0].equals("");
	}
}