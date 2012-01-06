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
package com.googlecode.aluminumproject.parsers.aluscript.lines.text;

import com.googlecode.aluminumproject.parsers.aluscript.TestAluScriptContext;
import com.googlecode.aluminumproject.parsers.aluscript.lines.LineParser;
import com.googlecode.aluminumproject.templates.ExpressionElement;
import com.googlecode.aluminumproject.templates.TemplateElement;
import com.googlecode.aluminumproject.templates.TextElement;

import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
@Test(groups = {"parsers", "parsers-aluscript", "fast"})
public class TextLineParserTest {
	private LineParser lineParser;

	private TestAluScriptContext context;

	@BeforeMethod
	public void createLineParser() {
		lineParser = new TextLineParser();

		context = new TestAluScriptContext();
		context.getSettings().setAutomaticNewlines(false);
	}

	public void anyLineShouldBeHandled() {
		assert lineParser.handles("line");
		assert lineParser.handles("\t\t\tindented line");
	}

	public void parsingLineWithTextShouldAddTextElementToContext() {
		lineParser.parseLine("text", context);

		List<TemplateElement> templateElements = context.getTemplateElements();
		assert templateElements.size() == 1;
		assert templateElements.get(0) instanceof TextElement;
	}

	public void parsingLineWithExpressionShouldAddExpressionElementToContext() {
		lineParser.parseLine("<<expression>>", context);

		List<TemplateElement> templateElements = context.getTemplateElements();
		assert templateElements.size() == 1;
		assert templateElements.get(0) instanceof ExpressionElement;
	}

	@Test(dependsOnMethods = {
		"parsingLineWithTextShouldAddTextElementToContext",
		"parsingLineWithExpressionShouldAddExpressionElementToContext"
	})
	public void parsingLineWithTextAndExpressionShouldAddTemplateElementsForBothToContext() {
		lineParser.parseLine("text<<expression>>", context);

		List<TemplateElement> templateElements = context.getTemplateElements();
		assert templateElements.size() == 2;
		assert templateElements.get(0) instanceof TextElement;
		assert templateElements.get(1) instanceof ExpressionElement;
	}

	@Test(dependsOnMethods = "parsingLineWithTextAndExpressionShouldAddTemplateElementsForBothToContext")
	public void automaticNewlinesSettingShouldAddTextElementContainingNewlineToContext() {
		context.getSettings().setAutomaticNewlines(true);

		lineParser.parseLine("text<<expression>>text", context);

		List<TemplateElement> templateElements = context.getTemplateElements();
		assert templateElements.size() == 4;
		assert templateElements.get(0) instanceof TextElement;
		assert templateElements.get(1) instanceof ExpressionElement;
		assert templateElements.get(2) instanceof TextElement;
		assert templateElements.get(3) instanceof TextElement;
		assert ((TextElement) templateElements.get(3)).getText().equals("\n");
	}
}