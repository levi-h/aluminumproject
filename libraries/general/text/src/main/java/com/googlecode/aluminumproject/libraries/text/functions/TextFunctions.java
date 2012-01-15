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
package com.googlecode.aluminumproject.libraries.text.functions;

import com.googlecode.aluminumproject.annotations.FunctionClass;
import com.googlecode.aluminumproject.annotations.Named;
import com.googlecode.aluminumproject.utilities.StringUtilities;

@FunctionClass
@SuppressWarnings("javadoc")
public class TextFunctions {
	private TextFunctions() {}

	public static String replace(@Named("text") String text,
			@Named("target") String target, @Named("replacement") String replacement) {
		return (text == null) ? text : text.replace(target, replacement);
	}

	public static String humanise(@Named("text") String text) {
		return (text == null) ? text : StringUtilities.humanise(text);
	}

	public static String lowerCase(@Named("text") String text) {
		return (text == null) ? text : text.toLowerCase();
	}

	public static String upperCase(@Named("text") String text) {
		return (text == null) ? text : text.toUpperCase();
	}

	public static String reverse(@Named("text") String text) {
		return (text == null) ? text : new StringBuilder(text).reverse().toString();
	}
}