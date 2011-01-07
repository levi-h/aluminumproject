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

import static com.googlecode.aluminumproject.context.Context.ALUMINUM_IMPLICIT_OBJECT;

import com.googlecode.aluminumproject.annotations.Required;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.ContextException;
import com.googlecode.aluminumproject.libraries.actions.AbstractAction;
import com.googlecode.aluminumproject.libraries.actions.ActionBody;
import com.googlecode.aluminumproject.libraries.actions.ActionException;
import com.googlecode.aluminumproject.utilities.Utilities;
import com.googlecode.aluminumproject.writers.Writer;
import com.googlecode.aluminumproject.writers.WriterException;

import java.util.Map;

/**
 * Invokes a named {@link ActionBody body} that was stored by a {@link Block block action}. If no block can be found
 * with the given name, then the body of the action itself will be invoked.
 *
 * @author levi_h
 */
public class BlockContents extends AbstractAction {
	private @Required String name;

	/**
	 * Creates a <em>block contents</em> action.
	 */
	public BlockContents() {}

	public void execute(Context context, Writer writer) throws ActionException, ContextException, WriterException {
		ActionBody block = getBlock(context);

		if (block == null) {
			logger.debug("no block named '", name, "' available, invoking body");

			getBody().invoke(context, writer);
		} else {
			logger.debug("invoking block named '", name, "'");

			block.invoke(context, writer);
		}
	}

	private ActionBody getBlock(Context context) {
		ActionBody block = null;

		Map<String, Object> templateInformation = Utilities.typed(context.getImplicitObject(ALUMINUM_IMPLICIT_OBJECT));

		if (templateInformation.containsKey("blocks")) {
			Map<String, ActionBody> blocks = Utilities.typed(templateInformation.get("blocks"));

			if (blocks.containsKey(name)) {
				block = blocks.get(name);
			}
		}

		return block;
	}
}