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
package com.googlecode.aluminumproject.libraries.actions;

import com.googlecode.aluminumproject.annotations.Ignored;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.libraries.TestLibrary;
import com.googlecode.aluminumproject.writers.Writer;

/**
 * An action contribution that can be used in tests. It will be ignored when adding all action contributions from a
 * package, since its {@link TestActionContributionFactory factory} is added to the {@link TestLibrary test library}.
 */
@Ignored
public class TestActionContribution implements ActionContribution {
	/**
	 * Creates a test action contribution.
	 */
	public TestActionContribution() {}

	public boolean canBeMadeTo(ActionFactory actionFactory) {
		return true;
	}

	public void make(Context context, Writer writer, ActionParameter parameter, ActionContributionOptions options) {}
}