/*
 * Copyright 2010 Levi Hoogenberg
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
package com.googlecode.aluminumproject.tools.aludoc;

import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.configuration.ConfigurationException;
import com.googlecode.aluminumproject.configuration.ConfigurationParameters;
import com.googlecode.aluminumproject.configuration.DefaultConfiguration;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.DefaultContext;
import com.googlecode.aluminumproject.context.g11n.GlobalisationContextProvider;
import com.googlecode.aluminumproject.libraries.Library;
import com.googlecode.aluminumproject.libraries.LibraryInformation;
import com.googlecode.aluminumproject.libraries.aludoc.AluDocLibrary;
import com.googlecode.aluminumproject.templates.TemplateException;
import com.googlecode.aluminumproject.templates.TemplateProcessor;
import com.googlecode.aluminumproject.tools.ToolException;
import com.googlecode.aluminumproject.writers.NullWriter;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Generates documentation for {@link Library libraries} by processing an AluDoc template.
 * <p>
 * The {@link Configuration configuration} for the template processor can be modified by {@link
 * #addConfigurationParameter(String, String) adding configuration parameters}. There are two default values:
 * <ul>
 * <li>the default {@link GlobalisationContextProvider#LOCALE locale} is English;
 * <li>the default {@link GlobalisationContextProvider#RESOURCE_BUNDLE_BASE_NAME resource bundle base name} is {@code
 *     "aludoc/messages"}.
 * </ul>
 * <p>
 * There are two methods that generate the documentation: {@link #generate() one that uses a default template}, and
 * {@link #generate(String, String) one that doesn't}. In either case, the following context variables are available:
 * <ul>
 * <li>{@code "libraries"}, a {@link List list} that contains the libraries for which documentation is generated (by
 *     default, this list contains all available libraries, though certain libraries may be {@link
 *     #excludeLibrary(String) excluded});
 * <li>{@code "location"}, the desired output location for the documentation.
 * </ul>
 *
 * @author levi_h
 */
public class AluDoc {
	private File location;

	private Map<String, String> configurationParameters;

	private List<String> excludedLibraryUrls;

	/**
	 * Creates an AluDoc generator.
	 *
	 * @param location the location to generate the AluDoc to
	 */
	public AluDoc(File location) {
		this.location = location;

		configurationParameters = new HashMap<String, String>();
		configurationParameters.put(GlobalisationContextProvider.LOCALE, "en");
		configurationParameters.put(GlobalisationContextProvider.RESOURCE_BUNDLE_BASE_NAME, "aludoc/messages");

		excludedLibraryUrls = new LinkedList<String>();
		excludedLibraryUrls.add(AluDocLibrary.URL);
	}

	/**
	 * Adds (or replaces) a configuration parameter to the configuration that the generator will use.
	 *
	 * @param name the name of the configuration parameter to add
	 * @param value the value of the configuration parameter to add
	 */
	public void addConfigurationParameter(String name, String value) {
		configurationParameters.put(name, value);
	}

	/**
	 * Excludes a library from the list of libraries that will be generated documentation for.
	 *
	 * @param libraryUrl the URL of the library to exclude
	 */
	public void excludeLibrary(String libraryUrl) {
		excludedLibraryUrls.add(libraryUrl);
	}

	/**
	 * Generates documentation using a default template ({@code "aludoc/html/aludoc.alu"}).
	 *
	 * @throws ToolException when the documentation can't be generated
	 */
	public void generate() throws ToolException {
		generate("aludoc/html/aludoc.alu", "aluscript");
	}

	/**
	 * Generates documentation for the added libraries.
	 *
	 * @param templateName the name of the template to process
	 * @param parser the name of the parser to use
	 * @throws ToolException when the documentation can't be generated
	 */
	public void generate(String templateName, String parser) throws ToolException {
		Configuration configuration = createConfiguration();

		Context context = new DefaultContext();
		context.setVariable("libraries", getLibraries(configuration));
		context.setVariable("location", location);

		try {
			new TemplateProcessor(configuration).processTemplate(templateName, parser, context, new NullWriter());
		} catch (TemplateException exception) {
			throw new ToolException(exception, "can't generate documentation");
		}
	}

	private Configuration createConfiguration() throws ToolException {
		ConfigurationParameters parameters = new ConfigurationParameters();

		for (String name: configurationParameters.keySet()) {
			parameters.addParameter(name, configurationParameters.get(name));
		}

		try {
			return new DefaultConfiguration(parameters);
		} catch (ConfigurationException exception) {
			throw new ToolException(exception, "can't create configuration");
		}
	}

	private List<Library> getLibraries(Configuration configuration) throws ToolException {
		List<Library> libraries = new LinkedList<Library>();


		for (Library library: configuration.getLibraries()) {
			LibraryInformation libraryInformation = library.getInformation();

			if (!(excludedLibraryUrls.contains(libraryInformation.getUrl()) ||
					excludedLibraryUrls.contains(libraryInformation.getVersionedUrl()))) {
				libraries.add(library);
			}
		}

		if (libraries.isEmpty()) {
			throw new ToolException("no libraries were left to generate documentation for");
		} else {
			return libraries;
		}
	}
}