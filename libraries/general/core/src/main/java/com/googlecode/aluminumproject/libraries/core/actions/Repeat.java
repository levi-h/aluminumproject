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

import com.googlecode.aluminumproject.annotations.Required;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.ContextException;
import com.googlecode.aluminumproject.libraries.actions.AbstractAction;
import com.googlecode.aluminumproject.libraries.actions.ActionException;
import com.googlecode.aluminumproject.writers.Writer;
import com.googlecode.aluminumproject.writers.WriterException;

/**
 * Invokes its body a number of times.
 * <p>
 * The body is invoked in its own scope (prefixed {@value #REPEAT_SCOPE}); information about the current loop will be
 * available in that scope.
 *
 * @author levi_h
 */
public class Repeat extends AbstractAction {
	private @Required int count;

	private String informationName;

	/**
	 * Creates a <em>repeat</em> action.
	 */
	public Repeat() {
		informationName = DEFAULT_INFORMATION_NAME;
	}

	public void execute(Context context, Writer writer) throws ActionException, ContextException, WriterException {
		String scope = context.addScope(REPEAT_SCOPE, false);

		for (int index = 0; index < count; index++) {
			context.setVariable(scope, informationName, new RepeatInformation(index, count));

			logger.debug("repeating body, ", index, "/", count);

			getBody().invoke(context, writer);
		}

		context.removeScope(REPEAT_SCOPE);
	}

	private static class RepeatInformation implements LoopInformation {
		private int index;
		private int count;

		public RepeatInformation(int index, int count) {
			this.index = index;
			this.count = count;
		}

		public int getIndex() {
			return index;
		}

		public int getCount() {
			return count;
		}

		public boolean isFirst() {
			return index == 0;
		}

		public boolean isLast() {
			return index + 1 == count;
		}
	}

	/** The name prefixes of the scopes that will be used for <em>repeat</em> actions. */
	public final static String REPEAT_SCOPE = "repeat";

	/** The name of the variable that will contain information about the current loop when none is given. */
	public final static String DEFAULT_INFORMATION_NAME = "loop";
}