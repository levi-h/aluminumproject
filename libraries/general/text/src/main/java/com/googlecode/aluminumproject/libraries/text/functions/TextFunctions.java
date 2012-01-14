/*
 * Copyright 2010-2011 Levi Hoogenberg
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

/**
 * Contains a number of text functions. All of the functions allow a {@code null} input text.
 *
 * @author levi_h
 */
@FunctionClass
public class TextFunctions {
	private TextFunctions() {}

	/**
	 * Replaces all occurrences of {@code target} in {@code text} with {@code replacement}.
	 *
	 * @param text the original text
	 * @param target the text to replace
	 * @param replacement the replacement to use
	 * @return the replaced text
	 */
	public static String replace(@Named("text") String text,
			@Named("target") String target, @Named("replacement") String replacement) {
		return (text == null) ? text : text.replace(target, replacement);
	}

	/**
	 * Humanises a text.
	 *
	 * @param text the text to humanise
	 * @return the humanised text
	 * @see StringUtilities#humanise(String)
	 */
	public static String humanise(@Named("text") String text) {
		return (text == null) ? text : StringUtilities.humanise(text);
	}

	/**
	 * Turns a text into lower case.
	 *
	 * @param text the text to turn into lower case
	 * @return the lower-cased text
	 */
	public static String lowerCase(@Named("text") String text) {
		return (text == null) ? text : text.toLowerCase();
	}

	/**
	 * Turns a text into upper case.
	 *
	 * @param text the text to turn into upper case
	 * @return the upper-cased text
	 */
	public static String upperCase(@Named("text") String text) {
		return (text == null) ? text : text.toUpperCase();
	}

	/**
	 * Reverses a text.
	 *
	 * @param text the text to reverse
	 * @return the reversed text
	 */
	public static String reverse(@Named("text") String text) {
		return (text == null) ? text : new StringBuilder(text).reverse().toString();
	}
}