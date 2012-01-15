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

/**
 * An action that creates or finds an object and makes it available to its body.
 *
 * @param <T> the type of the object that the action contains
 */
public interface ContainerAction<T> extends Action {
	/**
	 * Returns the object that this action contains.
	 *
	 * @return the object that this container action makes available for its children
	 */
	T getContainerObject();
}