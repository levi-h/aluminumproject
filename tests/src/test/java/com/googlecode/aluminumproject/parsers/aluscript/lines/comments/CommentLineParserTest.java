/*
 * Copyright 2010-2012 Levi Hoogenberg
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
package com.googlecode.aluminumproject.parsers.aluscript.lines.comments;

import com.googlecode.aluminumproject.parsers.aluscript.TestAluScriptContext;
import com.googlecode.aluminumproject.parsers.aluscript.lines.LineParser;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
@Test(groups = {"parsers", "parsers-aluscript", "fast"})
public class CommentLineParserTest {
	private LineParser lineParser;

	private TestAluScriptContext context;

	@BeforeMethod
	public void createLineParserAndContext() {
		lineParser = new CommentLineParser();

		context = new TestAluScriptContext();
	}

	public void linesThatStartWithHashSymbolShouldBeHandled() {
		assert lineParser.handles("# comment");
	}

	public void indentedLinesThatStartWithHashSymbolShouldBeHandled() {
		assert lineParser.handles("\t\t# comment");
	}

	public void linesThatDoNotStartWithHashSymbolShouldNotBeHandled() {
		assert !lineParser.handles("-- comment");
	}

	@Test(dependsOnMethods = "linesThatStartWithHashSymbolShouldBeHandled")
	public void parsingCommentShouldHaveNoEffect() {
		lineParser.parseLine("# comment", context);
	}
}