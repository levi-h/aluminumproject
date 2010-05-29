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

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;

@SuppressWarnings("all")
@Test(groups = {"libraries", "libraries-core", "slow"})
public class WriteTest extends CoreLibraryTest {
	public void valueShouldBeWritten() {
		String output = processTemplate("write", new DefaultContext());
		assert output != null;
		assert output.equals("dimensions");
	}

	public void valueFromBodyShouldBeWritten() {
		DefaultContext context = new DefaultContext();

		Map<String, String> synonyms = new HashMap<String, String>();
		synonyms.put("room", "space");
		synonyms.put("hour", "time");
		context.setVariable("synonyms", synonyms);

		String output = processTemplate("write-with-body", context);
		assert output != null;
		assert output.equals("space and time");
	}

	@Test(expectedExceptions = TemplateException.class)
	public void omittingValueShouldCauseException() {
		processTemplate("write-without-value", new DefaultContext());
	}
}