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
package com.googlecode.aluminumproject.libraries.core.actions;

import com.googlecode.aluminumproject.context.DefaultContext;
import com.googlecode.aluminumproject.libraries.core.CoreLibraryTest;
import com.googlecode.aluminumproject.templates.TemplateException;

import org.testng.annotations.Test;

@SuppressWarnings("all")
@Test(groups = {"libraries", "libraries-core", "slow"})
public class BlockTest extends CoreLibraryTest {
	public void blockShouldBeAvailableWithinSameTemplate() {
		String output = processTemplate("block");
		assert output != null;
		assert output.equals("block");
	}

	public void blockShouldBeAvailableInIncludedTemplate() {
		String output = processTemplate("block-include");
		assert output != null;
		assert output.equals("block");
	}

	public void blockShouldBeAvailableInLocallyIncludedTemplate() {
		String output = processTemplate("block-include-local");
		assert output != null;
		assert output.equals("block");
	}

	@Test(expectedExceptions = TemplateException.class)
	public void namingMultipleBlocksTheSameShouldCauseException() {
		processTemplate("block-duplication");
	}

	public void no_block_default() {
		String output = processTemplate("block-contents");
		assert output != null;
		assert output.equals("default");
	}
}