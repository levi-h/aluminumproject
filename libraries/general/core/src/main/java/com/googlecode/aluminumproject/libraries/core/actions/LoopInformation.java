/*
 * Copyright 2010 Levi Hoogenberg
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
package com.googlecode.aluminumproject.libraries.core.actions;

/**
 * Provides information about a loop in a looping action.
 *
 * @author levi_h
 * @see Each
 * @see Repeat
 */
public interface LoopInformation {
	/**
	 * Returns the index of the loop.
	 *
	 * @return the current loop index (zero-based)
	 */
	int getIndex();

	/**
	 * Returns the number of loops. Note that this might be an expensive operation.
	 *
	 * @return the number of times that the looping action will loop
	 */
	int getCount();

	/**
	 * Determines whether this is the first loop.
	 *
	 * @return {@code true} if the loop is the first one, {@code false} otherwise
	 */
	boolean isFirst();

	/**
	 * Determines whether this is the last loop.
	 *
	 * @return {@code true} if the loop is the last one, {@code false} otherwise
	 */
	boolean isLast();
}