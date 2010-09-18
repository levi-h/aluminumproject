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
package com.googlecode.aluminumproject.expressions.el;

import com.googlecode.aluminumproject.Logger;
import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.libraries.functions.Function;
import com.googlecode.aluminumproject.templates.Template;
import com.googlecode.aluminumproject.templates.TemplateContext;
import com.googlecode.aluminumproject.utilities.Utilities;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Implements {@link javax.el.FunctionMapper} by creating delegates to {@link Function functions}.
 *
 * @author levi_h
 */
public class FunctionMapper extends javax.el.FunctionMapper {
	private Context context;

	private Configuration configuration;

	private Logger logger;

	/**
	 * Creates a function mapper.
	 *
	 * @param context the context that contains the library URL abbreviations
	 * @param configuration the configuration used
	 */
	public FunctionMapper(Context context, Configuration configuration) {
		this.context = context;
		this.configuration = configuration;

		logger = Logger.get(getClass());
	}

	@Override
	public Method resolveFunction(String prefix, String localName) {
		logger.debug("resolving function with prefix '", prefix, "' and name '", localName, "'");

		Map<String, ?> templateInformation =
			Utilities.typed(context.getImplicitObject(Context.ALUMINUM_IMPLICIT_OBJECT));
		TemplateContext templateContext = (TemplateContext) templateInformation.get(Template.TEMPLATE_CONTEXT_KEY);

		Map<String, String> libraryUrlAbbreviations =
			templateContext.getCurrentTemplateElement().getLibraryUrlAbbreviations();

		Method delegate;

		if (libraryUrlAbbreviations.containsKey(prefix)) {
			String libraryUrl = libraryUrlAbbreviations.get(prefix);

			delegate = FunctionDelegateFactory.findDelegate(configuration, libraryUrl, localName, context);
		} else {
			logger.warn("prefix '", prefix, "' is no library URL abbreviation ",
				"(valid abbreviations are ", libraryUrlAbbreviations, ")");

			delegate = null;
		}

		return delegate;
	}
}