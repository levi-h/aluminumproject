/*
 * Copyright 2009-2012 Aluminum project
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
package com.googlecode.aluminumproject.templates;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.expressions.ExpressionFactory;
import com.googlecode.aluminumproject.utilities.Logger;
import com.googlecode.aluminumproject.writers.Writer;

import java.util.Map;

/**
 * The default {@link ExpressionElement expression element} implementation.
 */
public class DefaultExpressionElement extends AbstractTemplateElement implements ExpressionElement {
	private ExpressionFactory expressionFactory;
	private String text;

	private final Logger logger;

	/**
	 * Creates a default expression element.
	 *
	 * @param expressionFactory the expression factory that will create the expression that gets evaluated
	 * @param text the expression text
	 * @param libraryUrlAbbreviations the expression element's library URL abbreviations
	 * @param lineNumber the line number of the expression element
	 */
	public DefaultExpressionElement(ExpressionFactory expressionFactory, String text,
			Map<String, String> libraryUrlAbbreviations, int lineNumber) {
		super(libraryUrlAbbreviations, lineNumber);

		this.expressionFactory = expressionFactory;
		this.text = text;

		logger = Logger.get(getClass());
	}

	public String getText() {
		return text;
	}

	public void processAsCurrent(Context context, Writer writer) throws AluminumException {
		logger.debug("creating expression '", text, "' using ", expressionFactory);

		Object result = expressionFactory.create(text, context).evaluate(context);

		logger.debug("writing ", result);

		writer.write(result);
	}
}