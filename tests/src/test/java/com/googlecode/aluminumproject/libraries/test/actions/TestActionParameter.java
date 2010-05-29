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
package com.googlecode.aluminumproject.libraries.test.actions;

import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.libraries.actions.ActionException;
import com.googlecode.aluminumproject.libraries.actions.ActionParameter;

/**
 * An action parameter that can be used in tests.
 *
 * @author levi_h
 */
public class TestActionParameter implements ActionParameter {
	private String text;

	/**
	 * Creates a test action parameter.
	 *
	 * @param text the text of the parameter
	 */
	public TestActionParameter(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public <T> T getValue(Class<T> type, Context context) throws ActionException {
		if (type == String.class) {
			return type.cast(text);
		} else {
			throw new ActionException("'", text, "' is not of ", type);
		}
	}
}