/*
 * Copyright 2010-2012 Aluminum project
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

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.annotations.Ignored;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.writers.Writer;

/**
 * Implements {@link ContainerAction the ContainerAction interface} by obtaining the container object, {@link
 * #getContainerObject() making it available}, and giving child actions the opportunity to use it. The actual {@link
 * #provideContainerObject(Context) provision} of the container objects is taken care of by subclasses.
 *
 * @param <T> the type of the container object
 */
public abstract class AbstractContainerAction<T> extends AbstractAction implements ContainerAction<T> {
	private @Ignored T containerObject;

	/**
	 * Created an abstract container action.
	 */
	protected AbstractContainerAction() {}

	public T getContainerObject() {
		return containerObject;
	}

	public void execute(Context context, Writer writer) throws AluminumException {
		containerObject = provideContainerObject(context);

		logger.debug("created container object ", containerObject, ", now invoking body");

		getBody().invoke(context, writer);
	}

	/**
	 * Creates or finds the container object for child actions to use.
	 *
	 * @param context the context in which this action operates
	 * @return the container object to make available
	 * @throws AluminumException when the container object can't be provided
	 */
	protected abstract T provideContainerObject(Context context) throws AluminumException;
}