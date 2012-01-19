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
package com.googlecode.aluminumproject.utilities;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.templates.Template;
import com.googlecode.aluminumproject.templates.TemplateElement;
import com.googlecode.aluminumproject.templates.TemplateInformation;

/**
 * Offers utility methods related to templates and their elements.
 */
public class TemplateUtilities {
	private TemplateUtilities() {}

	/**
	 * Finds the template that contains a certain template element.
	 *
	 * @param templateElement the template element to find the template for
	 * @param context the context to search in
	 * @return the template that contains the given template element
	 * @throws AluminumException when no template that contains the specified template element is available in the given
	 *                           context
	 */
	public static Template findTemplate(TemplateElement templateElement, Context context) throws AluminumException {
		Template template = null;

		do {
			template = TemplateInformation.from(context).getTemplate();

			if (!template.contains(templateElement)) {
				template = null;
			}

			context = context.getParent();
		} while ((context != null) && (template == null));

		if (template == null) {
			throw new AluminumException("can't find template that contains ", templateElement);
		} else {
			return template;
		}
	}
}