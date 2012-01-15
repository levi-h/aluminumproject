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

import com.googlecode.aluminumproject.AluminumException;

/**
 * Provides a means to access web resources.
 */
public interface WebClient {
	/**
	 * Creates a request for a certain resource. After the request is created, it can be further configured; no
	 * interaction with the server takes place until the request is actually {@link Request#getResponse() sent}.
	 *
	 * @param method the request method to use
	 * @param url the URL of the resource to request
	 * @return the new request
	 * @throws AluminumException when the request can't be created
	 */
	Request createRequest(String method, String url) throws AluminumException;
}