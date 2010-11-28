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
package com.googlecode.aluminumproject.libraries.html;

import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.configuration.ConfigurationException;
import com.googlecode.aluminumproject.libraries.AbstractLibrary;
import com.googlecode.aluminumproject.libraries.LibraryInformation;
import com.googlecode.aluminumproject.libraries.html.actions.TagFactory;
import com.googlecode.aluminumproject.utilities.ReflectionUtilities;
import com.googlecode.aluminumproject.utilities.environment.EnvironmentUtilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Allows users to generate HTML documents.
 *
 * @author levi_h
 */
public class HtmlLibrary extends AbstractLibrary {
	private LibraryInformation information;

	/**
	 * Creates an HTML library.
	 */
	public HtmlLibrary() {
		super(ReflectionUtilities.getPackageName(HtmlLibrary.class));
	}

	@Override
	public void initialise(Configuration configuration) throws ConfigurationException {
		super.initialise(configuration);

		String url = "http://aluminumproject.googlecode.com/html";
		String version = EnvironmentUtilities.getVersion();

		information = new LibraryInformation(url, version);

		addTagFactories();
	}

	private void addTagFactories() throws ConfigurationException {
		Properties tagInformation = new Properties();

		try {
			InputStream tagInformationStream =
				Thread.currentThread().getContextClassLoader().getResourceAsStream("html4.properties");

			try {
				tagInformation.load(tagInformationStream);
			} finally {
				tagInformationStream.close();
			}
		} catch (IOException exception) {
			throw new ConfigurationException(exception, "can't read HTML 4 tag information");
		}

		Map<String, String[]> attributeGroups = new HashMap<String, String[]>();
		List<String> tagNames = new LinkedList<String>();

		Enumeration<?> propertyNames = tagInformation.propertyNames();

		while (propertyNames.hasMoreElements()) {
			String propertyName = (String) propertyNames.nextElement();
			String property = tagInformation.getProperty(propertyName);

			if (property.equals("attribute group")) {
				attributeGroups.put(propertyName,
					tagInformation.getProperty(String.format("%s.attributes", propertyName)).split(", "));
			} else if (property.equals("tag")) {
				tagNames.add(propertyName);
			}
		}

		for (String tagName: tagNames) {
			List<String> attributes = new LinkedList<String>();

			for (String attribute: tagInformation.getProperty(String.format("%s.attributes", tagName)).split(", ")) {
				if (attributeGroups.containsKey(attribute)) {
					Collections.addAll(attributes, attributeGroups.get(attribute));
				} else {
					attributes.add(attribute);
				}
			}

			boolean open = Boolean.parseBoolean(tagInformation.getProperty(String.format("%s.open", tagName), "false"));

			addActionFactory(new TagFactory(tagName, attributes, open));
		}
	}

	public LibraryInformation getInformation() {
		return information;
	}

}