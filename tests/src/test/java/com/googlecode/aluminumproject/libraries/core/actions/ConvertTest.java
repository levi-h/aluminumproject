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

import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.DefaultContext;
import com.googlecode.aluminumproject.libraries.core.CoreLibraryTest;
import com.googlecode.aluminumproject.templates.TemplateException;

import org.testng.annotations.Test;

@SuppressWarnings("all")
@Test(groups = {"libraries", "libraries-core", "slow"})
public class ConvertTest extends CoreLibraryTest {
	public void convertedValueShouldBeWritten() {
		Context context = new DefaultContext();
		context.setVariable("value", "5");

		processTemplate("convert", context);

		assert context.getVariableNames(Context.TEMPLATE_SCOPE).contains("converted-value");

		Object convertedValue = context.getVariable(Context.TEMPLATE_SCOPE, "converted-value");
		assert convertedValue instanceof Integer;
		assert ((Integer) convertedValue).intValue() == 5;
	}

	@Test(expectedExceptions = TemplateException.class)
	public void tryingIllegalConversionShouldCauseException() {
		processTemplate("convert-illegal");
	}
}