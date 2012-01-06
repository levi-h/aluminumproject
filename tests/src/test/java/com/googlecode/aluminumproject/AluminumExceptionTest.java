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
package com.googlecode.aluminumproject;

import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
@Test(groups = {"core", "fast"})
public class AluminumExceptionTest {
	public void messagePartsShouldBeJoined() {
		boolean valid = false;

		String message = new AluminumException("the template is ", valid ? "valid" : "invalid").getMessage();
		assert message != null;
		assert message.equals("the template is invalid");
	}

	public void causeShouldBeAvailable() {
		Throwable cause = new Exception();

		assert new AluminumException(cause, "test").getCause() == cause;
	}
}