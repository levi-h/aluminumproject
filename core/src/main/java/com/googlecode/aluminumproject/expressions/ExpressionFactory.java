/*
 * Copyright 2009-2012 Levi Hoogenberg
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
package com.googlecode.aluminumproject.expressions;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.configuration.ConfigurationElement;
import com.googlecode.aluminumproject.context.Context;

import java.util.List;

/**
 * A factory for {@link Expression expressions}. A {@link Configuration configuration} can contain more than one
 * expression factory; each one is used to recognise and create expressions with a specific syntax (or <i>expression
 * language</i>).
 */
public interface ExpressionFactory extends ConfigurationElement {
	/**
	 * Recognises expressions in a text.
	 *
	 * @param text the text to find expressions in
	 * @return all occurrences of expressions in the given text
	 */
	List<ExpressionOccurrence> findExpressions(String text);

	/**
	 * Creates an expression from a textual value.
	 *
	 * @param value the expression value to parse
	 * @param context the context in which the expression will be evaluated
	 * @return the new expression
	 * @throws AluminumException when the expression can't be created
	 */
	Expression create(String value, Context context) throws AluminumException;
}