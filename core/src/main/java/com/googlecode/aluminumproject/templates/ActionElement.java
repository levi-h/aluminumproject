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
package com.googlecode.aluminumproject.templates;

import com.googlecode.aluminumproject.libraries.actions.Action;
import com.googlecode.aluminumproject.libraries.actions.ActionParameter;

import java.util.List;
import java.util.Map;

/**
 * A template element that executes an {@link Action action}.
 *
 * @author levi_h
 */
public interface ActionElement extends TemplateElement {
	/**
	 * Returns a descriptor of the action.
	 *
	 * @return the action descriptor of this action element
	 */
	ActionDescriptor getDescriptor();

	/**
	 * Returns the parameters that will be applied to the action.
	 *
	 * @return the parameters of this action element
	 */
	Map<String, ActionParameter> getParameters();

	/**
	 * Returns the descriptors of the contributions that will be made to the action.
	 *
	 * @return the contribution descriptors of this action element
	 */
	List<ActionContributionDescriptor> getContributionDescriptors();
}