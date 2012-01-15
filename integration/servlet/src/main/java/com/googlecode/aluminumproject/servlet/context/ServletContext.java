/*
 * Copyright 2009-2012 Aluminum project
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
package com.googlecode.aluminumproject.servlet.context;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.context.AbstractContext;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.DefaultScope;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The context that is used in a servlet environment. It contains four scopes, in order of priority:
 * <ul>
 * <li>The template scope (named {@link Context#TEMPLATE_SCOPE "template"});
 * <li>The request scope (named {@value #REQUEST_SCOPE});
 * <li>The session scope (named {@value #SESSION_SCOPE});
 * <li>The application scope (named {@value #APPLICATION_SCOPE}).
 * </ul>
 * It has a number of implicit objects:
 * <ul>
 * <li>The {@link ServletContext application} (named {@value #APPLICATION_IMPLICIT_OBJECT});
 * <li>The {@link HttpServletRequest request object} (named {@value #REQUEST_IMPLICIT_OBJECT});
 * <li>The {@link HttpServletResponse response object} (named {@value #RESPONSE_IMPLICIT_OBJECT}).
 * </ul>
 */
public class ServletContext extends AbstractContext {
	private javax.servlet.ServletContext application;

	private HttpServletRequest request;
	private HttpServletResponse response;

	/**
	 * Creates a servlet context.
	 *
	 * @param application the current application
	 * @param request the current request object
	 * @param response the current response object
	 */
	public ServletContext(javax.servlet.ServletContext application,
			HttpServletRequest request, HttpServletResponse response) {
		this(null, application, request, response);
	}

	/**
	 * Creates a servlet subcontext.
	 *
	 * @param parent the parent scope
	 * @param application the current application
	 * @param request the current request
	 * @param response the current response
	 */
	protected ServletContext(ServletContext parent, javax.servlet.ServletContext application,
			HttpServletRequest request, HttpServletResponse response) {
		super(parent, new ApplicationScope(APPLICATION_SCOPE, application), new SessionScope(SESSION_SCOPE, request),
			new RequestScope(REQUEST_SCOPE, request), new DefaultScope(TEMPLATE_SCOPE));

		this.application = application;

		this.request = request;
		this.response = response;

		addImplicitObjects();
	}

	/**
	 * Returns the application.
	 *
	 * @return the current application
	 */
	protected javax.servlet.ServletContext getApplication() {
		return application;
	}

	/**
	 * Returns the request object.
	 *
	 * @return the current request object
	 */
	protected HttpServletRequest getRequest() {
		return request;
	}

	/**
	 * Returns the response object.
	 *
	 * @return the current response object
	 */
	protected HttpServletResponse getResponse() {
		return response;
	}

	/**
	 * Adds the implicit objects of this context: the application, the request, and the response.
	 */
	protected void addImplicitObjects() {
		addImplicitObject(APPLICATION_IMPLICIT_OBJECT, application);
		addImplicitObject(REQUEST_IMPLICIT_OBJECT, request);
		addImplicitObject(RESPONSE_IMPLICIT_OBJECT, response);
	}

	@Override
	public AbstractContext createSubcontext() throws AluminumException {
		return new ServletContext(this, application, request, response);
	}

	/** The name of the application scope. */
	public final static String APPLICATION_SCOPE = "application";

	/** The name of the request scope. */
	public final static String REQUEST_SCOPE = "request";

	/** The name of the session scope. */
	public final static String SESSION_SCOPE = "session";

	/** The name of the implicit object that contains the application. */
	public final static String APPLICATION_IMPLICIT_OBJECT = "application";

	/** The name of the implicit object that contains the request object. */
	public final static String REQUEST_IMPLICIT_OBJECT = "request";

	/** The name of the implicit object that contains the response object. */
	public final static String RESPONSE_IMPLICIT_OBJECT = "response";
}