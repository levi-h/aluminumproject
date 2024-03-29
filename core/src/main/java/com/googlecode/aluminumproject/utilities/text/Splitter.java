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
package com.googlecode.aluminumproject.utilities.text;

import com.googlecode.aluminumproject.AluminumException;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Splits text on a number of separators, which are expressed as patterns (regular expressions). Text is scanned from
 * the left to the right; each time a separator is found, a {@link TokenProcessor token processor} is notified of the
 * token before the separator, the separator itself, and the separator pattern that matches the separator. If multiple
 * separator patterns can match, the longest separator will be chosen. The last token will be offered to the token
 * processor with the empty string as separator and {@code null} as separator pattern.
 * <p>
 * A character can be escaped to prevent it from being recognised as (part of) a separator. To escape a character,
 * create a splitter with an escape character and put the escape character in front of each character that should be
 * escaped. Another way to prevent tokens to be identified as separators is quoting them. To do so, create a splitter
 * with one or more {@link QuotationCharacters quotation characters} and surround the token text with the quotation
 * characters.
 */
public class Splitter {
	private List<Pattern> separatorPatterns;

	private char escapeCharacter;
	private List<QuotationCharacters> quotationCharacters;

	/**
	 * Creates a splitter with a number of separator patterns, but without an escape character or quotation characters.
	 *
	 * @param separatorPatterns the separator patterns to use
	 */
	public Splitter(List<String> separatorPatterns) {
		this(separatorPatterns, '\0', Collections.<QuotationCharacters>emptyList());
	}

	/**
	 * Creates a splitter with a number of separator patterns and an escape character, but without quotation characters.
	 *
	 * @param separatorPatterns the separator patterns to use
	 * @param escapeCharacter the escape character to use
	 */
	public Splitter(List<String> separatorPatterns, char escapeCharacter) {
		this(separatorPatterns, escapeCharacter, Collections.<QuotationCharacters>emptyList());
	}

	/**
	 * Creates a splitter with a number of separator patterns and a number of quotation characters, but without an
	 * escape character.
	 *
	 * @param separatorPatterns the separator patterns to use
	 * @param quotationCharacters the quotation characters to use
	 */
	public Splitter(List<String> separatorPatterns, List<QuotationCharacters> quotationCharacters) {
		this(separatorPatterns, '\0', quotationCharacters);
	}

	/**
	 * Creates a splitter.
	 *
	 * @param separatorPatterns the separator patterns to use
	 * @param escapeCharacter the escape character to use
	 * @param quotationCharacters the quotation characters to use
	 */
	public Splitter(List<String> separatorPatterns,
			char escapeCharacter, List<QuotationCharacters> quotationCharacters) {
		this.separatorPatterns = new LinkedList<Pattern>();

		for (String separatorPattern: separatorPatterns) {
			this.separatorPatterns.add(Pattern.compile(separatorPattern));
		}

		this.escapeCharacter = escapeCharacter;
		this.quotationCharacters = quotationCharacters;
	}

	/**
	 * Splits text as explained in the class description and notifies a token processor of each token.
	 *
	 * @param text the text to split
	 * @param tokenProcessor the token processor to notify of each token that can be found
	 * @throws AluminumException when the text to split ends with an escape character, when it contains an unclosed
	 *                           quote character, or when the token processor can't process one of the tokens
	 */
	public void split(String text, TokenProcessor tokenProcessor) throws AluminumException {
		StringBuilder textBuffer = new StringBuilder();
		StringBuilder matchTextBuffer = new StringBuilder();

		fillBuffers(text, textBuffer, matchTextBuffer);

		processTokens(textBuffer, findMatches(matchTextBuffer), tokenProcessor);
	}

	private void fillBuffers(
			String text, StringBuilder textBuffer, StringBuilder matchTextBuffer) throws AluminumException {
		boolean escaping = false;

		QuotationCharacters quotationCharacters = null;

		for (char character: text.toCharArray()) {
			if (escaping) {
				textBuffer.append(character);
				matchTextBuffer.append('\0');

				escaping = false;
			} else if (character == escapeCharacter) {
				escaping = true;
			} else if (quotationCharacters == null) {
				quotationCharacters = findQuotationCharacters(character);

				if ((quotationCharacters == null) || quotationCharacters.keep) {
					textBuffer.append(character);
					matchTextBuffer.append((quotationCharacters == null) ? character : '\0');
				}
			} else {
				if ((character != quotationCharacters.closingCharacter) || quotationCharacters.keep) {
					textBuffer.append(character);
					matchTextBuffer.append('\0');
				}

				if (character == quotationCharacters.closingCharacter) {
					quotationCharacters = null;
				}
			}
		}

		if (escaping) {
			throw new AluminumException("escape character ", escapeCharacter, " does not escape anything");
		} else if (quotationCharacters != null) {
			throw new AluminumException("quotation character ", quotationCharacters.openingCharacter, " is not closed");
		}
	}

	private QuotationCharacters findQuotationCharacters(char character) {
		QuotationCharacters quotationCharacters = null;

		Iterator<QuotationCharacters> it = this.quotationCharacters.iterator();

		while ((quotationCharacters == null) && it.hasNext()) {
			QuotationCharacters currentQuotationCharacters = it.next();

			if (character == currentQuotationCharacters.openingCharacter) {
				quotationCharacters = currentQuotationCharacters;
			}
		}

		return quotationCharacters;
	}

	private List<Match> findMatches(StringBuilder buffer) {
		List<Match> matches = new LinkedList<Match>();

		Match match;
		int i = 0;

		do {
			match = null;

			for (Pattern separatorPattern: separatorPatterns) {
				Matcher matcher = separatorPattern.matcher(buffer);

				if (matcher.find(i)) {
					int startIndex = matcher.start();
					int endIndex = matcher.end();

					if ((match == null) || (startIndex < match.startIndex) ||
							((startIndex == match.startIndex) &&
								((endIndex - startIndex) > (match.endIndex - match.startIndex)))) {
						match = new Match(startIndex, endIndex, separatorPattern.pattern());
					}
				}
			}

			if (match != null) {
				matches.add(match);

				i = match.endIndex;
			}
		} while ((i < buffer.length()) && (match != null));

		return matches;
	}

	private void processTokens(
			StringBuilder buffer, List<Match> matches, TokenProcessor tokenProcessor) throws AluminumException {
		Match match;
		int offset = 0;

		do {
			match = findMatch(matches, offset);

			String token;
			String separator;
			String separatorPattern;

			if (match == null) {
				token = buffer.substring(offset, buffer.length());
				separator = "";
				separatorPattern = null;
			} else {
				token = buffer.substring(offset, match.startIndex);
				separator = buffer.substring(match.startIndex, match.endIndex);
				separatorPattern = match.separatorPattern;

				offset = match.endIndex;
			}

			tokenProcessor.process(token, separator, separatorPattern);
		} while (match != null);
	}

	private Match findMatch(List<Match> matches, int offset) {
		Match match = null;

		for (Match possibleMatch: matches) {
			if ((possibleMatch.startIndex >= offset) &&
					((match == null) || possibleMatch.isSoonerThan(match) || possibleMatch.isLongerThan(match))) {
				match = possibleMatch;
			}
		}

		return match;
	}

	private static class Match {
		private final int startIndex;
		private final int endIndex;
		private final String separatorPattern;

		public Match(int startIndex, int endIndex, String separatorPattern) {
			this.startIndex = startIndex;
			this.endIndex = endIndex;
			this.separatorPattern = separatorPattern;
		}

		public boolean isSoonerThan(Match match) {
			return startIndex < match.startIndex;
		}

		public boolean isLongerThan(Match match) {
			return (startIndex == match.startIndex) && (endIndex > match.endIndex);
		}
	}

	/**
	 * A pair of characters that can be used to prevent a {@link Splitter splitter} from seeing text as (part of) a
	 * separator.
	 */
	public static class QuotationCharacters {
		private final char openingCharacter;
		private final char closingCharacter;

		private final boolean keep;

		/**
		 * Creates a pair of quotation characters.
		 *
		 * @param openingCharacter the character that is used to open the quoted text
		 * @param closingCharacter the character that is used to close the quoted text
		 * @param keep whether the quotation characters should be part of the token that is given to the token processor
		 */
		public QuotationCharacters(char openingCharacter, char closingCharacter, boolean keep) {
			this.openingCharacter = openingCharacter;
			this.closingCharacter = closingCharacter;

			this.keep = keep;
		}
	}

	/**
	 * Will be notified of each token found by a {@link Splitter splitter}.
	 */
	public static interface TokenProcessor {
		/**
		 * Processes a token that is found by a splitter.
		 *
		 * @param token the text before the separator
		 * @param separator the separator that was found (the empty string in case of the last token)
		 * @param separatorPattern the pattern that matches the separator that was found ({@code null} for the last
		 *                         token)
		 * @throws AluminumException when the token can't be processed (e.g. when the separator order is unexpected)
		 */
		void process(String token, String separator, String separatorPattern) throws AluminumException;
	}
}