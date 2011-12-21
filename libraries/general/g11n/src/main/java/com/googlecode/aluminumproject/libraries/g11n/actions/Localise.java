/*
 * Copyright 2010-2011 Levi Hoogenberg
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
package com.googlecode.aluminumproject.libraries.g11n.actions;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.annotations.Named;
import com.googlecode.aluminumproject.annotations.Required;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.g11n.GlobalisationContext;
import com.googlecode.aluminumproject.context.g11n.ResourceBundleProvider;
import com.googlecode.aluminumproject.libraries.actions.AbstractAction;
import com.googlecode.aluminumproject.writers.Writer;

import java.util.Collections;
import java.util.ResourceBundle;

/**
 * Writes a localised resource from the current {@link GlobalisationContext globalisation context}'s {@link
 * ResourceBundleProvider resource bundle provider}'s {@link ResourceBundle resource bundle}.
 * <p>
 * If no resource can be found for a given key, the action will throw an exception. There are two ways to change this
 * behaviour: either by providing a <em>default</em> resource or by using the <em>allow missing key</em> parameter,
 * which will cause the key to be wrapped in two pairs of question marks and used as resource.
 *
 * @author levi_h
 */
public class Localise extends AbstractAction {
	private @Required String key;

	@Named("default")
	private Object defaultResource;

	private boolean allowMissingKey;

	/**
	 * Creates a <em>localise</em> action.
	 */
	public Localise() {
		defaultResource = NO_DEFAULT;
	}

	public void execute(Context context, Writer writer) throws AluminumException {
		Object resource;

		ResourceBundle resourceBundle = GlobalisationContext.from(context).getResourceBundleProvider().provide(context);

		logger.debug("checking for key '", key, "' in resource bundle ", resourceBundle);

		if (Collections.list(resourceBundle.getKeys()).contains(key)) {
			resource = resourceBundle.getObject(key);
		} else if (defaultResource != NO_DEFAULT) {
			resource = defaultResource;
		} else if (allowMissingKey) {
			resource = String.format("??%s??", key);
		} else {
			throw new AluminumException("can't find localised resource with key '", key, "'");
		}

		logger.debug("writing localised resource ", resource);

		writer.write(resource);
	}

	private final static Object NO_DEFAULT = new Object();
}