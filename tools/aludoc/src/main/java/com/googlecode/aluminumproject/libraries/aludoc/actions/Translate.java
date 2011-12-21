/*
 * Copyright 2011 Levi Hoogenberg
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
package com.googlecode.aluminumproject.libraries.aludoc.actions;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.annotations.Required;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.g11n.GlobalisationContext;
import com.googlecode.aluminumproject.libraries.Library;
import com.googlecode.aluminumproject.libraries.actions.AbstractAction;
import com.googlecode.aluminumproject.writers.Writer;

import java.util.Collections;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Finds a translation for a message key by looking in both the library messages resource bundle and a common messages
 * resource bundle and writes it away.
 *
 * @author levi_h
 */
public class Translate extends AbstractAction {
	private @Required String key;
	private @Required Library library;

	/**
	 * Creates a <em>translate</em> action.
	 */
	public Translate() {}

	public void execute(Context context, Writer writer) throws AluminumException {
		Locale locale = GlobalisationContext.from(context).getLocaleProvider().provide(context);

		String message = findMessage(library.getInformation().getPreferredUrlAbbreviation(), locale);

		if (message == null) {
			message = findMessage("common", locale);
		}

		if (message == null) {
			message = "-";
		}

		logger.debug("message for key '", key, "': '", message, "'");

		writer.write(message);
	}

	private String findMessage(String baseName, Locale locale) throws AluminumException {
		baseName = String.format("aludoc/%s", baseName);

		logger.debug("trying to find message with key '", key, "' in messages resource bundle '", baseName, "'");

		ResourceBundle messageBundle;

		try {
			messageBundle = ResourceBundle.getBundle(baseName, locale);
		} catch (MissingResourceException exception) {
			throw new AluminumException(exception, "can't find messages resource bundle '", baseName, "'",
				" for locale ", locale);
		}

		return Collections.list(messageBundle.getKeys()).contains(key) ? messageBundle.getString(key) : null;
	}
}