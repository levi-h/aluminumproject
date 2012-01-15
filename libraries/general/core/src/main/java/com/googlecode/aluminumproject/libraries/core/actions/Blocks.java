/*
 * Copyright 2011-2012 Aluminum project
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
import com.googlecode.aluminumproject.annotations.Required;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.libraries.actions.AbstractAction;
import com.googlecode.aluminumproject.libraries.actions.ActionBody;
import com.googlecode.aluminumproject.utilities.Utilities;
import com.googlecode.aluminumproject.writers.Writer;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("javadoc")
public class Blocks {
	private Blocks() {}

	public static class Block extends AbstractAction {
		private @Required String name;

		public void execute(Context context, Writer writer) throws AluminumException {
			Map<String, ActionBody> blocks = getBlocks(context, true);

			if (blocks.containsKey(name)) {
				throw new AluminumException("duplicate block: '", name, "'");
			} else {
				logger.debug("storing block '", name, "'");

				blocks.put(name, getBody().copy());
			}
		}
	}

	public static class BlockContents extends AbstractAction {
		private @Required String name;

		public void execute(Context context, Writer writer) throws AluminumException {
			ActionBody block = findBlock(context, name);

			if (block == null) {
				logger.debug("no block named '", name, "' available, invoking body");

				getBody().invoke(context, writer);
			} else {
				logger.debug("invoking block named '", name, "'");

				block.invoke(context, writer);
			}
		}
	}

	private static ActionBody findBlock(Context context, String name) throws AluminumException {
		Map<String, ActionBody> blocks = null;

		do {
			blocks = getBlocks(context, false);

			context = context.getParent();
		} while ((blocks == null) && (context != null));

		return (blocks == null) ? null : blocks.get(name);
	}

	private static Map<String, ActionBody> getBlocks(Context context, boolean create) throws AluminumException {
		Map<String, ActionBody> blocks;

		if (context.getImplicitObjectNames().contains(BLOCKS)) {
			blocks = Utilities.typed(context.getImplicitObject(BLOCKS));
		} else {
			if (create) {
				blocks = new HashMap<String, ActionBody>();

				context.addImplicitObject(BLOCKS, blocks);
			} else {
				blocks = null;
			}
		}

		return blocks;
	}

	private final static String BLOCKS = Context.RESERVED_IMPLICIT_OBJECT_NAME_PREFIX + ".libraries.core.blocks";
}