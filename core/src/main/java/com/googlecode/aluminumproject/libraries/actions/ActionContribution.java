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
package com.googlecode.aluminumproject.libraries.actions;

import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.ContextException;
import com.googlecode.aluminumproject.interceptors.ActionInterceptor;
import com.googlecode.aluminumproject.writers.Writer;

/**
 * Contributes to an {@link Action action}.
 * <p>
 * An action contribution has a number of different ways to contribute to an action. With the {@link
 * DefaultActionContributionOptions default options}, the following choices are available:
 * <ul>
 * <li>The {@link Context context} in which the action is executed can be altered;
 * <li>The action's {@link ActionParameter parameters} can be {@link ActionContributionOptions#setParameter(String,
 *     ActionParameter) expanded};
 * <li>The {@link Writer writer} that the action will use can be replaced;
 * <li>The action can be {@link ActionContributionOptions#skipAction() skipped} altogether;
 * <li>An {@link ActionInterceptor interceptor} can be {@link
 *     ActionContributionOptions#addInterceptor(ActionInterceptor) added}.
 * <li>
 * </ul>
 * An action contribution is being created with a {@link ActionParameter parameter}. This parameter can be passed to the
 * action, but it can also be used to base logic on (e.g. whether or not the action should be executed).
 *
 * @author levi_h
 */
public interface ActionContribution {
	/**
	 * Determines whether this action contribution can be made to a certain action.
	 *
	 * @param actionFactory the factory that will create the action
	 * @return {@code true} if this action contribution can be made to the action that's created by the given action
	 *         factory, {@code false} otherwise
	 */
	boolean canBeMadeTo(ActionFactory actionFactory);

	/**
	 * Makes this action contribution.
	 *
	 * @param context the context in which the action will be executed
	 * @param writer the writer that will be used for the action
	 * @param parameter the parameter with which this action contribution was created
	 * @param options the ways in which this action contribution can contribute to the action
	 * @throws ActionException when this action contribution can't be made
	 * @throws ContextException when something goes wrong while using the context
	 */
	void make(Context context, Writer writer,
		ActionParameter parameter, ActionContributionOptions options) throws ActionException, ContextException;
}