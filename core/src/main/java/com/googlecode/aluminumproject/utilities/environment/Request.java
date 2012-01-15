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
package com.googlecode.aluminumproject.utilities.environment;

import com.googlecode.aluminumproject.AluminumException;

/**
 * A request to a server by a {@link WebClient web client}.
 */
public interface Request {
	/**
	 * Adds a request header.
	 *
	 * @param name the name of the header to add
	 * @param value the value of the header to add
	 */
	void addHeader(String name, String value);

	/**
	 * Adds a request parameter.
	 *
	 * @param name the name of the parameter to add
	 * @param value the value of the parameter to add
	 */
	void addParameter(String name, String value);

	/**
	 * Sends this request and returns the server's response.
	 *
	 * @return the the server's response to this request
	 * @throws AluminumException when something goes wrong while sending this request
	 */
	Response getResponse() throws AluminumException;
}