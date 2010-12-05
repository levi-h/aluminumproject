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
package com.googlecode.aluminumproject.expressions;

import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.libraries.actions.Action;
import com.googlecode.aluminumproject.libraries.actions.ExpressionActionParameter;

/**
 * Produces a value at runtime. An expression can be used as the parameter to an {@link Action action} and inside text.
 *
 * @author levi_h
 * @see ExpressionActionParameter
 */
public interface Expression {
	/**
	 * Evaluates this expression in a certain context.
	 *
	 * @param context the context to evaluate this expression in
	 * @return the value that this expression evaluates to
	 * @throws ExpressionException when this expression can't be evaluated
	 */
	Object evaluate(Context context) throws ExpressionException;
}