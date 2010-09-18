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

/**
 * The occurrence of an {@link Expression expression} in a {@link String string}.
 *
 * @author levi_h
 */
public class ExpressionOccurrence implements Comparable<ExpressionOccurrence> {
	private int beginIndex;
	private int endIndex;

	/**
	 * Creates an expression occurrence.
	 *
	 * @param beginIndex the beginning index of the occurrence (inclusive)
	 * @param endIndex the ending index of the occurrence (exclusive)
	 */
	public ExpressionOccurrence(int beginIndex, int endIndex) {
		this.beginIndex = beginIndex;
		this.endIndex = endIndex;
	}

	/**
	 * Returns the beginninig index of this expression occurrence.
	 *
	 * @return the index at which the expression starts (inclusive)
	 */
	public int getBeginIndex() {
		return beginIndex;
	}

	/**
	 * Returns the ending index of this expression occurrence.
	 *
	 * @return the index at which the expression ends (exclusive)
	 */
	public int getEndIndex() {
		return endIndex;
	}

	public int compareTo(ExpressionOccurrence occurrence) {
		int result = getBeginIndex() - occurrence.getBeginIndex();

		if (result == 0) {
			result = getEndIndex() - occurrence.getEndIndex();
		}

		return result;

	}

	@Override
	public int hashCode() {
		return 59 * beginIndex + 53 * endIndex + 47;
	}

	@Override
	public boolean equals(Object object) {
		return (object instanceof ExpressionOccurrence) && equals((ExpressionOccurrence) object);
	}

	private boolean equals(ExpressionOccurrence occurrence) {
		return (beginIndex == occurrence.beginIndex) && (endIndex == occurrence.endIndex);
	}
}