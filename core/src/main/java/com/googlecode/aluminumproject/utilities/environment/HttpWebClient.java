/*
 * Copyright 2010-2011 Levi Hoogenberg
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

import com.googlecode.aluminumproject.utilities.UtilityException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * An HTTP {@link WebClient web client} implementation. It supports two methods: {@value #GET_METHOD} and {@value
 * #POST_METHOD}.
 *
 * @author levi_h
 */
public class HttpWebClient implements WebClient {
	private List<String> supportedMethods;

	/**
	 * Creates an HTTP web client.
	 */
	public HttpWebClient() {
		supportedMethods = Arrays.asList(GET_METHOD, POST_METHOD);
	}

	public Request createRequest(String method, String url) throws UtilityException {
		if (!supportedMethods.contains(method)) {
			throw new UtilityException("unsupported method: '", method, "'");
		}

		return new HttpRequest(method, url);
	}

	private static class HttpRequest implements Request {
		private String method;
		private String url;

		private Map<String, List<String>> headers;
		private Map<String, List<String>> parameters;

		public HttpRequest(String method, String url) {
			this.method = method;
			this.url = url;

			headers = new HashMap<String, List<String>>();
			parameters = new HashMap<String, List<String>>();
		}

		private void setMethod(HttpURLConnection connection) throws UtilityException {
			try {
				connection.setRequestMethod(method);
			} catch (ProtocolException exception) {
				throw new UtilityException(exception, "can't set method");
			}
		}

		public void addHeader(String name, String value) {
			if (!headers.containsKey(name)) {
				headers.put(name, new LinkedList<String>());
			}

			headers.get(name).add(value);
		}

		public void addParameter(String name, String value) {
			if (!parameters.containsKey(name)) {
				parameters.put(name, new LinkedList<String>());
			}

			parameters.get(name).add(value);
		}

		public Response getResponse() throws UtilityException {
			HttpURLConnection connection = getConnection();

			setMethod(connection);
			addHeaders(connection);
			setBody(connection);

			try {
				connection.connect();
			} catch (IOException exception) {
				throw new UtilityException(exception, "can't open connection");
			}

			return new HttpResponse(getStatusCode(connection), connection.getHeaderFields(), getBody(connection));
		}

		private HttpURLConnection getConnection() throws UtilityException {
			URLConnection connection;

			try {
				StringBuilder urlBuilder = new StringBuilder(url);

				if (method.equals(GET_METHOD)) {
					String queryString = getQueryString();

					if (queryString.length() > 0) {
						urlBuilder.append("?");
						urlBuilder.append(queryString);
					}
				}

				connection = new URL(urlBuilder.toString()).openConnection();
			} catch (IOException exception) {
				throw new UtilityException(exception, "can't open connection to ", url);
			}

			if (connection instanceof HttpURLConnection) {
				return (HttpURLConnection) connection;
			} else {
				throw new UtilityException("can't open HTTP connection to ", url);
			}
		}

		private void addHeaders(HttpURLConnection connection) {
			for (Map.Entry<String, List<String>> header: headers.entrySet()) {
				String name = header.getKey();

				for (String value: header.getValue()) {
					connection.addRequestProperty(name, value);
				}
			}
		}

		private void setBody(URLConnection connection) throws UtilityException {
			if (method.equals(POST_METHOD)) {
				connection.setDoOutput(true);

				try {
					OutputStream out = connection.getOutputStream();

					try {
						out.write(getQueryString().getBytes("utf-8"));
					} finally {
						out.close();
					}
				} catch (IOException exception) {
					throw new UtilityException(exception, "can't write body");
				}
			}
		}

		private String getQueryString() throws UtilityException {
			StringBuilder queryStringBuilder = new StringBuilder();

			for (Map.Entry<String, List<String>> requestParameter: parameters.entrySet()) {
				if (queryStringBuilder.length() > 0) {
					queryStringBuilder.append("&");
				}

				String name = requestParameter.getKey();

				for (String value: requestParameter.getValue()) {
					String encodedValue;

					try {
						encodedValue = URLEncoder.encode(value, "utf-8");
					} catch (UnsupportedEncodingException exception) {
						throw new UtilityException(exception, "can't encode request parameter '", name, "'");
					}

					queryStringBuilder.append(name);
					queryStringBuilder.append("=");
					queryStringBuilder.append(encodedValue);
				}
			}

			return queryStringBuilder.toString();
		}

		private int getStatusCode(HttpURLConnection connection) throws UtilityException {
			try {
				return connection.getResponseCode();
			} catch (IOException exception) {
				throw new UtilityException(exception, "can't get status code");
			}
		}

		private byte[] getBody(HttpURLConnection connection) throws UtilityException {
			InputStream in;

			try {
				in = connection.getErrorStream();

				if (in == null) {
					in = connection.getInputStream();
				}

				ByteArrayOutputStream out = new ByteArrayOutputStream();

				byte[] buffer = new byte[1024];
				int bytesRead;

				try {
					while ((bytesRead = in.read(buffer)) > 0) {
						out.write(buffer, 0, bytesRead);
					}
				} finally {
					in.close();
				}

				return out.toByteArray();
			} catch (IOException exception) {
				throw new UtilityException(exception, "can't get body");
			}
		}
	}

	private static class HttpResponse implements Response {
		private int statusCode;
		private Map<String, List<String>> headers;
		private byte[] body;

		public HttpResponse(int statusCode, Map<String, List<String>> headers, byte[] body) {
			this.statusCode = statusCode;
			this.headers = headers;
			this.body = body;
		}

		public int getStatusCode() {
			return statusCode;
		}

		public Map<String, List<String>> getHeaders() {
			return headers;
		}

		public byte[] getBody() {
			return body;
		}
	}

	/** The HTTP <em>get</em> method: {@value}. */
	public final static String GET_METHOD = "GET";

	/** The HTTP <em>post</em> method: {@value}. */
	public final static String POST_METHOD = "POST";
}