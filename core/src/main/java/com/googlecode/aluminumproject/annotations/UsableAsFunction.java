/*
 * Copyright 2012 Aluminum project
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

import com.googlecode.aluminumproject.libraries.actions.Action;
import com.googlecode.aluminumproject.libraries.functions.Function;
import com.googlecode.aluminumproject.utilities.GenericsUtilities;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Type;

/**
 * Indicates that an {@link Action action} can be used as if it were a {@link Function function}.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface UsableAsFunction {
	/**
	 * The result type of the function (as a {@code String}, because {@link Type a type} is not allowed - its value will
	 * be converted by {@link GenericsUtilities#getType(String, String...) the getType method}, with {@code java.lang}
	 * and {@code java.util} as default packages).
	 */
	String resultType() default "Object";

	/**
	 * The order in which the action's parameters are expected as function arguments. Not all of the parameters have to
	 * be included: it won't be possible to set a value for absent optional parameters.
	 */
	String[] argumentParameters() default {};
}