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
package com.googlecode.aluminumproject.templates;

/**
 * The life cycle of an action.
 */
public enum ActionPhase {
	/**
	 * The first phase, in which action contributions are made.
	 */
	CONTRIBUTION,

	/**
	 * The second phase, in which the action is constructed and gets its parent and body set.
	 */
	CREATION,

	/**
	 * The third and final phase, in which the action is executed.
	 */
	EXECUTION;
}