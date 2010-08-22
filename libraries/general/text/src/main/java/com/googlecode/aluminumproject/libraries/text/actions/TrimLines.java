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
package com.googlecode.aluminumproject.libraries.text.actions;

import com.googlecode.aluminumproject.annotations.ActionContributionInformation;

/**
 * Trims each line of the body text of the action that it contributes to.
 *
 * @author levi_h
 */
@ActionContributionInformation(parameterType = "com.googlecode.aluminumproject.writers.TrimWriter$TrimType")
public class TrimLines extends AbstractTrim {
	/**
	 * Creates a <em>trim lines</em> action contribution.
	 */
	public TrimLines() {
		super(true);
	}
}