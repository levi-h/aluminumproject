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

/**
 * Implemented by {@link Action actions} that are created by {@link DefaultActionFactory the default action factory} and
 * support dynamic parameters.
 *
 * @author levi_h
 */
public interface DynamicallyParameterisable {
	/**
	 * Sets a dynamic parameter.
	 *
	 * @param name the name of the parameter
	 * @param parameter the dynamic parameter of the action
	 * @throws ActionException when the parameter can't be used
	 */
	void setParameter(String name, ActionParameter parameter) throws ActionException;
}