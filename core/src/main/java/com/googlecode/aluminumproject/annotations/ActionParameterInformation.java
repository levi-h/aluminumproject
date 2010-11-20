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
package com.googlecode.aluminumproject.annotations;

import com.googlecode.aluminumproject.libraries.actions.Action;
import com.googlecode.aluminumproject.libraries.actions.ActionParameter;
import com.googlecode.aluminumproject.libraries.actions.DefaultActionFactory;
import com.googlecode.aluminumproject.utilities.GenericsUtilities;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Type;

/**
 * Contains information about the parameter of an {@link Action action}. When put on a setter, {@link
 * DefaultActionFactory default action factories} will use the information to fill {@link
 * com.googlecode.aluminumproject.libraries.actions.ActionParameterInformation action parameter information}.
 *
 * @author levi_h
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ActionParameterInformation {
	/**
	 * The name of the parameter. If not given, the property name will be used as parameter name.
	 */
	String name() default "";

	/**
	 * The type of the parameter. If not given, the property type will be used as parameter type (most of the time, this
	 * will be what is needed anyway - this attribute is especially useful for parameters of the type {@link
	 * ActionParameter}).
	 * <p>
	 * Note that the type of this attribute is {@code String}, since {@link Type types} are not permitted. The attribute
	 * value will be converted to a type by {@link GenericsUtilities#getType(String, String...) the getType utility
	 * method} (with {@code java.lang} and {@code java.util} as default packages).
	 */
	String type() default "";

	/**
	 * Whether the parameter is required or not. Parameters are optional by default.
	 */
	boolean required() default false;
}