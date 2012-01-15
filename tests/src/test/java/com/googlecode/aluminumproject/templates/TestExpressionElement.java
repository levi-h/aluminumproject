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
package com.googlecode.aluminumproject.templates;

import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.writers.Writer;

import java.util.Collections;
import java.util.Map;

/**
 * An expression element that can be used in tests.
 */
public class TestExpressionElement implements ExpressionElement {
	/**
	 * Creates a test expression element.
	 */
	public TestExpressionElement() {}

	public Map<String, String> getLibraryUrlAbbreviations() {
		return Collections.emptyMap();
	}

	public String getText() {
		return "";
	}

	public void process(Context context, Writer writer) {}
}