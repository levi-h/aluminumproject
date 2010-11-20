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
package com.googlecode.aluminumproject.utilities.text;

import com.googlecode.aluminumproject.utilities.Utilities;
import com.googlecode.aluminumproject.utilities.UtilityException;
import com.googlecode.aluminumproject.utilities.text.Splitter.QuotationCharacters;
import com.googlecode.aluminumproject.utilities.text.Splitter.TokenProcessor;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.testng.annotations.Test;

@SuppressWarnings("all")
@Test(groups = {"utilities", "fast"})
public class SplitterTest {
	public void splittingWithoutSeparatorPatternsShouldResultInSingleToken() {
		List<Token> tokens = split(new Splitter(Collections.<String>emptyList()), "a, b");
		assert tokens.size() == 1;
		assert tokens.get(0).equals(new Token("a, b", "", null));
	}

	public void splittingWithSingleSeparatorPatternShouldSeparateTokens() {
		List<Token> tokens = split(new Splitter(Arrays.asList(", ")), "a, b");
		assert tokens.size() == 2;
		assert tokens.get(0).equals(new Token("a", ", ", ", "));
		assert tokens.get(1).equals(new Token("b", "", null));
	}

	public void splittingWithMultipleSeparatorPatternsShouldSeparateTokens() {
		List<Token> tokens = split(new Splitter(Arrays.asList(", *", " */ *")), "a, b / c, d");
		assert tokens.size() == 4;
		assert tokens.get(0).equals(new Token("a", ", ", ", *"));
		assert tokens.get(1).equals(new Token("b", " / ", " */ *"));
		assert tokens.get(2).equals(new Token("c", ", ", ", *"));
		assert tokens.get(3).equals(new Token("d", "", null));
	}

	@Test(dependsOnMethods = "splittingWithMultipleSeparatorPatternsShouldSeparateTokens")
	public void contiguousSeparatorsShouldResultInEmptyTokens() {
		List<Token> tokens = split(new Splitter(Arrays.asList("\\(", ": ?", "\\)")), "(a: )");
		assert tokens.size() == 4: tokens.size();
		assert tokens.get(0).equals(new Token("", "(", "\\("));
		assert tokens.get(1).equals(new Token("a", ": ", ": ?"));
		assert tokens.get(2).equals(new Token("", ")", "\\)"));
		assert tokens.get(3).equals(new Token("", "", null));
	}

	public void longestSeparatorShouldWinWhenMultipleSeparatorPatternsMatch() {
		List<Token> tokens = split(new Splitter(Arrays.asList(" ", "  ")), "a  b");
		assert tokens.size() == 2;
		assert tokens.get(0).equals(new Token("a", "  ", "  "));
		assert tokens.get(1).equals(new Token("b", "", null));
	}

	@Test(dependsOnMethods = "splittingWithSingleSeparatorPatternShouldSeparateTokens")
	public void escapeCharacterShouldPreventSeparation() {
		List<Token> tokens = split(new Splitter(Arrays.asList(", ?"), '\\'), "a, b\\, c");
		assert tokens.size() == 2;
		assert tokens.get(0).equals(new Token("a", ", ", ", ?"));
		assert tokens.get(1).equals(new Token("b, c", "", null));
	}

	@Test(dependsOnMethods = "escapeCharacterShouldPreventSeparation")
	public void escapeCharacterShouldBeEscapable() {
		List<Token> tokens = split(new Splitter(Arrays.asList(", ?"), '\\'), "a, b\\\\, c");
		assert tokens.size() == 3;
		assert tokens.get(0).equals(new Token("a", ", ", ", ?"));
		assert tokens.get(1).equals(new Token("b\\", ", ", ", ?"));
		assert tokens.get(2).equals(new Token("c", "", null));
	}

	@Test(dependsOnMethods = "escapeCharacterShouldPreventSeparation", expectedExceptions = UtilityException.class)
	public void danglingEscapeCharacterShouldCauseException() {
		split(new Splitter(Collections.<String>emptyList(), '\\'), "a\\");
	}

	@Test(dependsOnMethods = "splittingWithSingleSeparatorPatternShouldSeparateTokens")
	public void quotationCharactersShouldPreventSeparation() {
		List<QuotationCharacters> quotationCharacters = Arrays.asList(new QuotationCharacters('(', ')', false));

		List<Token> tokens = split(new Splitter(Arrays.asList(", "), quotationCharacters), "a, (b, c), d");
		assert tokens.size() == 3;
		assert tokens.get(0).equals(new Token("a", ", ", ", "));
		assert tokens.get(1).equals(new Token("b, c", ", ", ", "));
		assert tokens.get(2).equals(new Token("d", "", null));
	}

	@Test(dependsOnMethods = "quotationCharactersShouldPreventSeparation")
	public void multipleQuotationCharactersShouldBeSupported() {
		List<QuotationCharacters> quotationCharacters = Arrays.asList(
			new QuotationCharacters('(', ')', true),
			new QuotationCharacters('[', ']', true)
		);

		List<Token> tokens = split(new Splitter(Arrays.asList(", ?"), quotationCharacters), "a, (b, c), [d, e]");
		assert tokens.size() == 3;
		assert tokens.get(0).equals(new Token("a", ", ", ", ?"));
		assert tokens.get(1).equals(new Token("(b, c)", ", ", ", ?"));
		assert tokens.get(2).equals(new Token("[d, e]", "", null));
	}

	@Test(dependsOnMethods = "quotationCharactersShouldPreventSeparation")
	public void quotationCharactersShouldBeKeepable() {
		List<QuotationCharacters> quotationCharacters = Arrays.asList(new QuotationCharacters('(', ')', true));

		List<Token> tokens = split(new Splitter(Arrays.asList(", "), quotationCharacters), "a, (b, c), d");
		assert tokens.size() == 3;
		assert tokens.get(0).equals(new Token("a", ", ", ", "));
		assert tokens.get(1).equals(new Token("(b, c)", ", ", ", "));
		assert tokens.get(2).equals(new Token("d", "", null));
	}

	public void quotationCharactersShouldBeEscapable() {
		List<QuotationCharacters> quotationCharacters = Arrays.asList(new QuotationCharacters('\'', '\'', false));

		List<Token> tokens = split(new Splitter(Arrays.asList(" "), '\\', quotationCharacters), "a \\'\\ \\'");
		assert tokens.size() == 2;
		assert tokens.get(0).equals(new Token("a", " ", " "));
		assert tokens.get(1).equals(new Token("' '", "", null));
	}

	public void quotationCharactersShouldBeNestable() {
		List<QuotationCharacters> quotationCharacters = Arrays.asList(new QuotationCharacters('(', ')', true));

		List<Token> tokens = split(new Splitter(Arrays.asList(",\\s"), quotationCharacters), "a, (b, (c, d))");
		assert tokens.size() == 2;
		assert tokens.get(0).equals(new Token("a", ", ", ",\\s"));
		assert tokens.get(1).equals(new Token("(b, (c, d))", "", null));
	}

	@Test(dependsOnMethods = "quotationCharactersShouldPreventSeparation", expectedExceptions = UtilityException.class)
	public void notClosingQuotationMarksShouldCauseException() {
		List<QuotationCharacters> quotationCharacters = Arrays.asList(new QuotationCharacters('\'', '\'', false));

		split(new Splitter(Collections.<String>emptyList(), quotationCharacters), "a, 'b");
	}

	private class Token {
		private String token;
		private String separator;
		private String separatorPattern;

		public Token(String token, String separator, String separatorPattern) {
			this.token = token;
			this.separator = separator;
			this.separatorPattern = separatorPattern;
		}

		public boolean equals(Object object) {
			return (object instanceof Token) && equals((Token) object);
		}

		private boolean equals(Token token) {
			return Utilities.equals(this.token, token.token) && Utilities.equals(separator, token.separator)
				&& Utilities.equals(separatorPattern, token.separatorPattern);
		}
	}

	private List<Token> split(Splitter splitter, String text) {
		final List<Token> tokens = new LinkedList<Token>();

		splitter.split(text, new TokenProcessor() {
			public void process(String token, String separator, String separatorPattern) {
				tokens.add(new Token(token, separator, separatorPattern));
			}
		});

		return tokens;
	}
}