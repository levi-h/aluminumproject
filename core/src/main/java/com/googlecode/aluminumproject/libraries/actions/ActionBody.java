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
package com.googlecode.aluminumproject.libraries.actions;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.writers.Writer;

/**
 * The body of an {@link Action action}.
 *
 * @author levi_h
 */
public interface ActionBody {
	/**
	 * Copies this action body, so that it can be invoked later.
	 *
	 * @return a copy of this action body
	 */
	ActionBody copy();

	/**
	 * Invokes this body.
	 *
	 * @param context the context to operate in
	 * @param writer the writer to use
	 * @throws AluminumException when something goes wrong while invoking this body
	 */
	void invoke(Context context, Writer writer) throws AluminumException;
}