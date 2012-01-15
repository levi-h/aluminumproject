/*
 * Copyright 2009-2012 Levi Hoogenberg
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
package com.googlecode.aluminumproject.parsers;

import com.googlecode.aluminumproject.templates.TemplateElementFactory;

/**
 * Translates names used in templates to names as they are expected by a {@link TemplateElementFactory template element
 * factory}.
 */
public interface TemplateNameTranslator {
	/**
	 * Translates an action name.
	 *
	 * @param name the action name as it is used in the template
	 * @return the translated action name
	 */
	String translateActionName(String name);

	/**
	 * Translates an action parameter name.
	 *
	 * @param name the action parameter name as it is used in the template
	 * @return the translated action parameter name
	 */
	String translateActionParameterName(String name);

	/**
	 * Translates an action contribution name.
	 *
	 * @param name the action contribution name as it is used in the template
	 * @return the translated action contribution name
	 */
	String translateActionContributionName(String name);
}