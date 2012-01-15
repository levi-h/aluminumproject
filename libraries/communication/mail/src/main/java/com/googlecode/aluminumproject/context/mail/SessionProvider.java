/*
 * Copyright 2011-2012 Aluminum project
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
package com.googlecode.aluminumproject.context.mail;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.context.Context;

import javax.mail.Session;

/**
 * Provides a {@link Session mail session}.
 */
public interface SessionProvider {
	/**
	 * Determines which session should be used in a certain context.
	 *
	 * @param context the current context
	 * @return the session to use
	 * @throws AluminumException when no session can be provided
	 */
	Session provide(Context context) throws AluminumException;
}