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
package com.googlecode.aluminumproject.expressions;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.annotations.Ignored;
import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.configuration.DefaultConfiguration;
import com.googlecode.aluminumproject.context.Context;

import java.util.Collections;
import java.util.List;

/**
 * An expression factory that will be ignored by the {@link DefaultConfiguration default configuration}.
 */
@Ignored
public class IgnoredExpressionFactory implements ExpressionFactory {
	/**
	 * Creates an ignored expression factory.
	 */
	public IgnoredExpressionFactory() {}

	public void initialise(Configuration configuration) {}

	public void disable() {}

	public List<ExpressionOccurrence> findExpressions(String text) {
		return Collections.emptyList();
	}

	public Expression create(String value, Context context) throws AluminumException {
		throw new AluminumException("can't create expression");
	}
}