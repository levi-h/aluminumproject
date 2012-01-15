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

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * A web client that can be used in tests. After it has been created (or {@link EnvironmentUtilities#getWebClient()
 * obtained}), a number of {@link Resource resources} can be added to it; created requests use this resource collection
 * to formulate a response.
 */
public class TestWebClient implements WebClient {
	private List<Resource> resources;

	/**
	 * Creates a test web client.
	 */
	public TestWebClient() {
		resources = new LinkedList<Resource>();
	}

	/**
	 * Adds a resource to this web client.
	 *
	 * @param resource the resource to add
	 */
	public void addResource(Resource resource) {
		resources.add(resource);
	}

	/**
	 * Removes all of this web client's resources.
	 */
	public void removeResources() {
		resources.clear();
	}

	public Request createRequest(String method, String url) throws AluminumException {
		return new TestRequest(method, url, resources);
	}

	/**
	 * A test request.
	 */
	public static class TestRequest implements Request {
		private String method;
		private String url;

		private Map<String, List<String>> headers;
		private Map<String, List<String>> parameters;

		private List<Resource> resources;

		private TestRequest(String method, String url, List<Resource> resources) {
			this.method = method;
			this.url = url;

			headers = new HashMap<String, List<String>>();
			parameters = new HashMap<String, List<String>>();

			this.resources = resources;
		}

		/**
		 * Returns the method of this request.
		 *
		 * @return the request method used
		 */
		public String getMethod() {
			return method;
		}

		/**
		 * Returns the URL of this request.
		 *
		 * @return the request URL used
		 */
		public String getUrl() {
			return url;
		}

		public void addHeader(String name, String value) {
			if (!headers.containsKey(name)) {
				headers.put(name, new LinkedList<String>());
			}

			headers.get(name).add(value);
		}

		/**
		 * Returns all of the headers of this request.
		 *
		 * @return all request headers
		 */
		public Map<String, List<String>> getHeaders() {
			return headers;
		}

		public void addParameter(String name, String value) {
			if (!parameters.containsKey(name)) {
				parameters.put(name, new LinkedList<String>());
			}

			parameters.get(name).add(value);
		}

		/**
		 * Returns all of the parameters of this request.
		 *
		 * @return all request parameters
		 */
		public Map<String, List<String>> getParameters() {
			return parameters;
		}

		public Response getResponse() {
			Iterator<Resource> it = resources.iterator();
			Resource resource = null;

			while (it.hasNext() && (resource == null)) {
				resource = it.next();

				if (!resource.isRequestedBy(this)) {
					resource = null;
				}
			}

			return (resource == null) ? new TestResponse(404) : resource.createResponse();
		}
	}

	/**
	 * A test response.
	 */
	public static class TestResponse implements Response {
		private int statusCode;
		private Map<String, List<String>> headers;
		private byte[] body;

		/**
		 * Creates a test response.
		 *
		 * @param statusCode the status code of the response
		 */
		public TestResponse(int statusCode) {
			this.statusCode = statusCode;
			headers = new HashMap<String, List<String>>();
			body = new byte[0];
		}

		public int getStatusCode() {
			return statusCode;
		}

		public Map<String, List<String>> getHeaders() {
			return headers;
		}

		/**
		 * Adds a response header.
		 *
		 * @param name the name of the header to add
		 * @param value the value of the header to add
		 */
		public void addHeader(String name, String value) {
			if (!headers.containsKey(name)) {
				headers.put(name, new LinkedList<String>());
			}

			headers.get(name).add(value);
		}

		public byte[] getBody() {
			return body;
		}

		/**
		 * Sets the body of this response.
		 *
		 * @param body the response body to use
		 */
		public void setBody(byte[] body) {
			this.body = body;
		}
	}

	/**
	 * A resource.
	 */
	public static interface Resource {
		/**
		 * Determines whether a certain request is a request for this resource.
		 *
		 * @param request the request to check
		 * @return {@code true} if this resource is requested by the given request, {@code false} otherwise
		 */
		boolean isRequestedBy(TestRequest request);

		/**
		 * Creates a response for this resource.
		 *
		 * @return the new response
		 */
		Response createResponse();
	}
}