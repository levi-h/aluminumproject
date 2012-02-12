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
package com.googlecode.aluminumproject.utilities;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Provides utility methods related to {@link String strings}.
 */
public class StringUtilities {
	private StringUtilities() {}

	/**
	 * Joins parts of an array together to form a string.
	 *
	 * @param parts the parts to join
	 * @return the string that is formed when all given parts are joined together
	 * @throws IllegalArgumentException if {@code parts} is {@code null}
	 */
	public static String join(Object... parts) {
		if (parts == null) {
			throw new IllegalArgumentException("parts should not be null");
		}

		StringBuilder builder = new StringBuilder();

		for (Object part: parts) {
			builder.append(convertPart(part));
		}

		return builder.toString();
	}

	private static Object convertPart(Object part) {
		Object convertedPart;

		if ((part != null) && part.getClass().isArray()) {
			Object[] array = new Object[Array.getLength(part)];

			for (int i = 0; i < array.length; i++) {
				array[i] = convertPart(Array.get(part, i));
			}

			convertedPart = Arrays.toString(array);
		} else {
			convertedPart = part;
		}

		return convertedPart;
	}

	/**
	 * Humanises a camel cased text. Each individual word will be written in lower case (unless it's in upper case in
	 * the input text) and separated with spaces. The first word will be capitalised.
	 *
	 * @param text the text to humanise
	 * @return a humanised version of the given text
	 */
	public static String humanise(String text) {
		List<String> words = new ArrayList<String>();

		Matcher matcher = HUMANISE_PATTERN.matcher(text);

		int previousEnd = 0;

		while (matcher.find()) {
			int start = matcher.start();
			int end = matcher.end();

			addWord(words, text, previousEnd, start);
			addWord(words, text, start, end);

			previousEnd = end;
		}

		addWord(words, text, previousEnd, text.length());

		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < words.size(); i++) {
			String word = words.get(i);

			if (i == 0) {
				builder.append(Character.toUpperCase(word.charAt(0)));
				builder.append(word.substring(1));
			} else {
				builder.append(' ');

				if (Character.isUpperCase(word.charAt(word.length() - 1)) && word.length() > 1) {
					builder.append(word);
				} else {
					builder.append(word.toLowerCase());
				}
			}
		}

		return builder.toString();
	}

	private static void addWord(List<String> words, String text, int start, int end) {
		if (end > start) {
			words.add(text.substring(start, end));
		}
	}

	/**
	 * Turns a text into camel case. Each word (apart from the first one) will be capitalised; after that, all of the
	 * words will be joined.
	 *
	 * @param text the text to turn into camel case
	 * @return a camel-cased version of the given text
	 */
	public static String camelCase(String text) {
		StringBuilder builder = new StringBuilder();

		boolean turnIntoUpperCase = false;

		for (char character: text.toCharArray()) {
			if (character == ' ') {
				turnIntoUpperCase = true;
			} else {
				builder.append(turnIntoUpperCase ? Character.toUpperCase(character) : character);

				turnIntoUpperCase = false;
			}
		}

		return builder.toString();
	}

	private final static Pattern HUMANISE_PATTERN = Pattern.compile("[A-Z]?[a-z]+");
}