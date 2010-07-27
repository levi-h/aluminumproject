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
package com.googlecode.aluminumproject.annotations;

import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.libraries.actions.Action;
import com.googlecode.aluminumproject.libraries.actions.ActionContribution;
import com.googlecode.aluminumproject.libraries.functions.Function;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to indicate that a field in an {@link Action action}, {@link ActionContribution action contribution}, or {@link
 * Function function} is expected to be injected by its factory.
 * <p>
 * At the very least, factories should allow the injection of the following types:
 * <ul>
 * <li>{@link Configuration The current configuration};
 * <li>The factory that created the action, action contribution, or function.
 * </ul>
 *
 * @author levi_h
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Injected {}