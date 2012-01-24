/*
 * Copyright 2012 Aluminum project
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
package com.googlecode.aluminumproject.libraries.functions;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.DefaultContext;
import com.googlecode.aluminumproject.libraries.actions.AbstractAction;
import com.googlecode.aluminumproject.libraries.actions.ActionInformation;
import com.googlecode.aluminumproject.libraries.actions.ActionParameterInformation;
import com.googlecode.aluminumproject.writers.Writer;

import java.util.Collections;
import java.util.List;

import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
@Test(groups = {"core", "fast"})
public class ActionExecutingFunctionTest {
	@Test(expectedExceptions = AluminumException.class)
	public void callingFunctionWithActionThatDoesNotWriteAnythingShouldCauseException() {
		AbstractAction action = new AbstractAction() {
			public void execute(Context context, Writer writer) {}
		};
		ActionInformation actionInformation =
			new ActionInformation("test", Collections.<ActionParameterInformation>emptyList(), false, String.class);

		new ActionExecutingFunction(action, actionInformation).call(new DefaultContext());
	}

	public void callingFunctionWithActionThatWritesSingleObjectShouldResultInSaidObject() {
		AbstractAction action = new AbstractAction() {
			public void execute(Context context, Writer writer) {
				writer.write("only");
			}
		};
		ActionInformation actionInformation =
			new ActionInformation("test", Collections.<ActionParameterInformation>emptyList(), false, String.class);

		Object result = new ActionExecutingFunction(action, actionInformation).call(new DefaultContext());
		assert result instanceof String;
		assert result.equals("only");
	}

	public void callingFunctionWithActionThatWritesMultipleObjectsShouldResultInList() {
		AbstractAction action = new AbstractAction() {
			public void execute(Context context, Writer writer) {
				writer.write("first");
				writer.write("second");
			}
		};
		ActionInformation actionInformation =
			new ActionInformation("test", Collections.<ActionParameterInformation>emptyList(), false, String.class);

		Object result = new ActionExecutingFunction(action, actionInformation).call(new DefaultContext());
		assert result instanceof List;

		List<?> list = (List<?>) result;
		assert list.size() == 2;
		assert list.get(0).equals("first");
		assert list.get(1).equals("second");
	}
}