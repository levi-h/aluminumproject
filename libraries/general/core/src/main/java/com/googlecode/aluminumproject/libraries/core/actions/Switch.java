/*
 * Copyright 2013 Aluminum project
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

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.annotations.Ignored;
import com.googlecode.aluminumproject.annotations.Required;
import com.googlecode.aluminumproject.annotations.ValidInside;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.libraries.actions.AbstractAction;
import com.googlecode.aluminumproject.libraries.actions.ActionBody;
import com.googlecode.aluminumproject.writers.NullWriter;
import com.googlecode.aluminumproject.writers.Writer;

@SuppressWarnings("javadoc")
public class Switch extends AbstractAction {
	private @Ignored ActionBody caseBody;
	private @Ignored ActionBody defaultBody;

	public void execute(Context context, Writer writer) throws AluminumException {
		getBody().invoke(context, new NullWriter());

		ActionBody optionBody = (caseBody == null) ? defaultBody : caseBody;

		if (optionBody != null) {
			optionBody.invoke(context, writer);
		}
	}

	@ValidInside(Switch.class)
	private static abstract class SwitchOption extends AbstractAction {
		protected abstract boolean isConditionMet();

		protected abstract boolean isDefaultOption();

		public void execute(Context context, Writer writer) throws AluminumException {
			if (isConditionMet()) {
				Switch switchAction = findAncestorOfType(Switch.class);

				if (isDefaultOption()) {
					if (switchAction.defaultBody == null) {
						switchAction.defaultBody = getBody();
					} else {
						throw new AluminumException("switch actions may have only one default option");
					}
				} else if (switchAction.caseBody == null) {
					switchAction.caseBody = getBody();
				}
			}
		}
	}

	public static class Case extends SwitchOption {
		private @Required boolean condition;

		protected boolean isConditionMet() {
			return condition;
		}

		protected boolean isDefaultOption() {
			return false;
		}
	}

	public static class Default extends SwitchOption {
		protected boolean isConditionMet() {
			return true;
		}

		protected boolean isDefaultOption() {
			return true;
		}
	}
}
