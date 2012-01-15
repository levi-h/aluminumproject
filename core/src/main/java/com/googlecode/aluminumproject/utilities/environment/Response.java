/*
 * Copyright 2010-2012 Levi Hoogenberg
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

import java.util.List;
import java.util.Map;

/**
 * The response of a server to a {@link Request request}.
 */
public interface Response {
	/**
	 * Returns the status code of this response.
	 *
	 * @return the status code that was sent by the server
	 */
	int getStatusCode();

	/**
	 * Returns the headers of this response.
	 *
	 * @return the headers that were sent by the server
	 */
	Map<String, List<String>> getHeaders();

	/**
	 * Returns the body of this response.
	 *
	 * @return the response body that was sent by the server
	 */
	byte[] getBody();
}