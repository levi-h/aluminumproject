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

import com.googlecode.aluminumproject.expressions.ExpressionFactory;
import com.googlecode.aluminumproject.parsers.aluscript.AluScriptContext;
import com.googlecode.aluminumproject.parsers.aluscript.AluScriptException;
import com.googlecode.aluminumproject.templates.ExpressionElement;
import com.googlecode.aluminumproject.templates.TemplateElement;
import com.googlecode.aluminumproject.templates.TemplateException;

import java.util.Map;

/**
 * Creates an {@link ExpressionElement expression element} and adds it to the context.
 *
 * @author levi_h
 */
public class ExpressionInstruction extends AbstractInstruction {
	private ExpressionFactory expressionFactory;
	private String text;

	/**
	 * Creates an expression instruction.
	 *
	 * @param expressionFactory the expression factory to create the expression element with
	 * @param text the text to create the expression element with
	 */
	public ExpressionInstruction(ExpressionFactory expressionFactory, String text) {
		super("expression");

		this.expressionFactory = expressionFactory;
		this.text = text;
	}

	public void execute(Map<String, String> parameters, AluScriptContext context) throws AluScriptException {
		if (!parameters.isEmpty()) {
			throw new AluScriptException("text instructions do not allow parameters");
		}

		TemplateElement expressionElement;

		try {
			expressionElement = context.getConfiguration().getTemplateElementFactory().createExpressionElement(
				expressionFactory, text, context.getLibraryUrlAbbreviations());
		} catch (TemplateException exception) {
			throw new AluScriptException(exception, "can't create expression element for expression ", text);
		}

		context.addTemplateElement(expressionElement);
	}
}