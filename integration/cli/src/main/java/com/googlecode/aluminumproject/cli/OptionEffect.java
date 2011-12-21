/*
 * Copyright 2009-2011 Levi Hoogenberg
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
package com.googlecode.aluminumproject.cli;

import com.googlecode.aluminumproject.AluminumException;

import joptsimple.OptionSet;
import joptsimple.OptionSpec;

/**
 * The effect of an option on a {@link Command command}.
 * <p>
 * In general, option effects shouldn't take any immediate acion when they are applied; other options could have
 * influence on the behaviour of the command. Therefore, option effects should rather change the command's state, so
 * that the command can decide what to do with the given options when it is executed.
 *
 * @author levi_h
 * @param <T> the type of the option argument(s) (if any)
 */
public interface OptionEffect<T> {
	/**
	 * Applies this option effect to the command that accepts the option.
	 *
	 * @param options the set of supplied options
	 * @param option the option that causes this effect
	 * @throws AluminumException when this option effect can't be applied
	 */
	void apply(OptionSet options, OptionSpec<T> option) throws AluminumException;
}