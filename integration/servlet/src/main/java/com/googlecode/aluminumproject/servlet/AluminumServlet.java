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
package com.googlecode.aluminumproject.servlet;

import com.googlecode.aluminumproject.Aluminum;
import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.configuration.ConfigurationParameters;
import com.googlecode.aluminumproject.configuration.DefaultConfiguration;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.servlet.context.ServletContext;
import com.googlecode.aluminumproject.utilities.Logger;
import com.googlecode.aluminumproject.utilities.Utilities;
import com.googlecode.aluminumproject.writers.ResponseWriter;
import com.googlecode.aluminumproject.writers.Writer;

import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A servlet that uses Aluminum to serve web content.
 * <p>
 * Upon {@link #init() initialisation}, a new {@link Aluminum template engine} is being created. Both its configuration
 * and the configuration parameters can be configured by overriding the {@link
 * #createConfiguration(ConfigurationParameters) createConfiguration} and {@link #createConfigurationParameters()
 * createConfigurationParameters} methods. The default implementation creates a {@link DefaultConfiguration default
 * configuration} that uses context and init parameters as configuration parameters.
 * <p>
 * The template engine is reused for each request. The name of the template that will be executed is equal to
 * (depending on how the servlet is mapped in the deployment descriptor) either the servlet path or the path info,
 * exluding the leading slash. This means that when the Aluminum servlet is added to a local application that's deployed
 * under context path {@code /app} and mapped to {@code /aluminum/*}, a request to {@code
 * http://localhost/app/aluminum/hello.xml} will result in the template name {@code hello.xml}.
 * <p>
 * The name of the parser that will be used is gotten by looking at the extension of the template name. So in the
 * previous example, the template would be parsed with the {@code xml} parser. To change this process, override the
 * {@link #determineParser(String) determineParser} method.
 * <p>
 * Templates operate in a context that's designed for a servlet environment: the {@link ServletContext servlet context}.
 * It gives access to the application and the request and response objects, and provides a couple of extra scopes. The
 * {@link #createContext() createContext method} can be overridden if another context is desired.
 * <p>
 * The writer that is used by the template engine is the {@link ResponseWriter response writer}, that writes byte arrays
 * (for binary output) or strings (for textual output) to the servlet output stream. When a different writer is needed,
 * the {@link #createWriter() createWriter method} can be overridden by subclasses.
 *
 * @author levi_h
 */
public class AluminumServlet extends HttpServlet {
	private Aluminum aluminum;

	private HttpServletRequest request;
	private HttpServletResponse response;

	private final Logger logger;

	/**
	 * Creates an aluminum servlet.
	 */
	public AluminumServlet() {
		logger = Logger.get(getClass());
	}

	@Override
	public void init() throws ServletException {
		logger.debug("initialising aluminum servlet (", getServletConfig().getServletName(), ")");

		try {
			ConfigurationParameters configurationParameters = createConfigurationParameters();
			logger.debug("using configuration parameters: ", configurationParameters);

			Configuration configuration = createConfiguration(configurationParameters);
			logger.debug("using configuration ", configuration);

			aluminum = new Aluminum(configuration);
		} catch (AluminumException exception) {
			throw new ServletException("can't initialise aluminum servlet", exception);
		}
	}

	/**
	 * Creates the configuration that should be used. This implementation creates a {@link DefaultConfiguration default
	 * configuration}.
	 *
	 * @param parameters the configuration parameters to use
	 * @return the configuration that will be used to create the template engine with
	 * @throws AluminumException when the configuration can't be created
	 */
	protected Configuration createConfiguration(ConfigurationParameters parameters) throws AluminumException {
		return new DefaultConfiguration(parameters);
	}

	/**
	 * Creates the parameters that are used by the configuration. This method makes both the context and the init
	 * parameters available.
	 *
	 * @return the parameters for the configuration
	 * @throws AluminumException when the configuration parameters can't be created
	 */
	protected ConfigurationParameters createConfigurationParameters() throws AluminumException {
		ConfigurationParameters parameters = new ConfigurationParameters();

		logger.debug("adding context parameters");

		Enumeration<?> contextParameters = getServletContext().getInitParameterNames();

		while (contextParameters.hasMoreElements()) {
			String name = (String) contextParameters.nextElement();

			parameters.addParameter(name, getServletContext().getInitParameter(name));
		}

		logger.debug("adding init parameters");

		Enumeration<?> initParameters = getServletConfig().getInitParameterNames();

		while (initParameters.hasMoreElements()) {
			String name = (String) initParameters.nextElement();

			parameters.addParameter(name, getServletConfig().getInitParameter(name));
		}

		return parameters;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		this.request = request;
		this.response = response;

		try {
			String name = Utilities.withDefault(request.getPathInfo(), request.getServletPath()).substring(1);
			logger.debug("template name: '", name, "'");

			String parser = determineParser(name);
			logger.debug("using parser '", parser, "'");

			Context context = createContext();
			logger.debug("using context ", context);

			Writer writer = createWriter();
			logger.debug("using writer ", writer);

			aluminum.processTemplate(name, parser, context, writer);
		} catch (AluminumException exception) {
			throw new ServletException("can't process template", exception);
		} finally {
			this.request = null;
			this.response = null;
		}
	}

	/**
	 * Returns the current request object.
	 *
	 * @return the request object or {@code null} if this method is called outside of the {@link
	 *         #doGet(HttpServletRequest, HttpServletResponse) doGet method}
	 */
	protected HttpServletRequest getRequest() {
		return request;
	}

	/**
	 * Returns the current response object.
	 *
	 * @return the response object or {@code null} if this method is called outside of the {@link
	 *         #doGet(HttpServletRequest, HttpServletResponse) doGet method}
	 */
	protected HttpServletResponse getResponse() {
		return response;
	}

	/**
	 * Determines the name of the parser that should be used for a certain request. This implementation returns the
	 * extension of the template path.
	 *
	 * @param name the name of the template that will be processed
	 * @return the name of the parser that should be used when processing the template
	 * @throws AluminumException when the parser can't be determined
	 */
	protected String determineParser(String name) throws AluminumException {
		String[] templatePath = name.split("/");

		if (templatePath.length == 0) {
			throw new AluminumException("can't determine template path from '", name, "'");
		} else {
			String templateName = templatePath[templatePath.length - 1];

			int positionOfDot = templateName.lastIndexOf(".");

			if (positionOfDot == -1) {
				throw new AluminumException("can't determine parser for template '", templateName, "'");
			}

			return templateName.substring(positionOfDot + 1);
		}
	}

	/**
	 * Creates the context that will be used when processing a template. Each request, a new context will be created.
	 * This implementation will return a new {@link ServletContext servlet context}.
	 *
	 * @return the context to use
	 * @throws AluminumException when the context can't be created
	 */
	protected Context createContext() throws AluminumException {
		return new ServletContext(getServletContext(), getRequest(), getResponse());
	}

	/**
	 * Creates the writer that will be used when processing a template. A new writer will be created for each request.
	 * This method will return a new {@link ResponseWriter response writer}.
	 *
	 * @return the writer to use
	 * @throws AluminumException when the writer can't be created
	 */
	protected Writer createWriter() throws AluminumException {
		return new ResponseWriter(getResponse());
	}

	@Override
	public void destroy() {
		logger.debug("destroying aluminum servlet (", getServletConfig().getServletName(), ")");

		try {
			aluminum.stop();
		} catch (AluminumException exception) {
			throw new RuntimeException("can't stop template engine", exception);
		}
	}

	private final static long serialVersionUID = 20111220L;
}