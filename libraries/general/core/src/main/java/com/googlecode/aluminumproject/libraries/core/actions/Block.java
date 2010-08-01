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

import com.googlecode.aluminumproject.annotations.ActionParameterInformation;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.libraries.actions.AbstractAction;
import com.googlecode.aluminumproject.libraries.actions.ActionBody;
import com.googlecode.aluminumproject.libraries.actions.ActionException;
import com.googlecode.aluminumproject.utilities.Utilities;
import com.googlecode.aluminumproject.writers.Writer;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores its body (a block), so that it can be used later (by the {@link BlockContents block contents} action). Blocks
 * are {@link Context context}-specific and need to have a unique name within a context.
 *
 * @author levi_h
 * @see Include
 * @see IncludeLocal
 */
public class Block extends AbstractAction {
	private String name;

	/**
	 * Creates a <em>block</em> action.
	 */
	public Block() {}

	/**
	 * Sets the name of the block to store.
	 *
	 * @param name the name of the block
	 */
	@ActionParameterInformation(required = true)
	public void setName(String name) {
		this.name = name;
	}

	public void execute(Context context, Writer writer) throws ActionException {
		Map<String, Object> templateInformation = Utilities.typed(context.getImplicitObject(ALUMINUM_IMPLICIT_OBJECT));

		if (!templateInformation.containsKey("blocks")) {
			templateInformation.put("blocks", new HashMap<String, ActionBody>());
		}

		Map<String, ActionBody> blocks = Utilities.typed(templateInformation.get("blocks"));

		if (blocks.containsKey(name)) {
			throw new ActionException("duplicate block: '", name, "'");
		} else {
			logger.debug("storing block '", name, "'");

			blocks.put(name, getBody().copy());
		}
	}
}