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

import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
@Test(groups = {"utilities", "fast"})
public class UtilitiesTest {
	public void nullShouldEqualNull() {
		assert Utilities.equals(null, null);
	}

	public void nullShouldNotEqualEmptyString() {
		assert !Utilities.equals(null, "");
	}

	public void emptyStringShouldNotEqualNull() {
		assert !Utilities.equals("", null);
	}

	public void emptyStringShouldEqualEmptyString() {
		assert Utilities.equals("", "");
	}

	public void valueShouldBeUsedIfNotNull() {
		assert Utilities.withDefault(Boolean.TRUE, Boolean.FALSE);
	}

	public void defaultValueShouldBeUsedIfValueIsNull() {
		assert !Utilities.withDefault(null, Boolean.FALSE);
	}

	@Test(expectedExceptions = IllegalArgumentException.class)
	public void nullDefaultValueShouldCauseException() {
		Utilities.withDefault(null, null);
	}
}