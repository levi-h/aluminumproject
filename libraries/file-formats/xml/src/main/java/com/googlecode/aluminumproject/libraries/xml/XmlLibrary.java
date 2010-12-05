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
package com.googlecode.aluminumproject.libraries.xml;

import com.googlecode.aluminumproject.libraries.AbstractLibrary;
import com.googlecode.aluminumproject.libraries.LibraryException;
import com.googlecode.aluminumproject.libraries.LibraryInformation;
import com.googlecode.aluminumproject.libraries.actions.ActionContributionFactory;
import com.googlecode.aluminumproject.libraries.actions.ActionFactory;
import com.googlecode.aluminumproject.libraries.actions.DefaultActionContributionFactory;
import com.googlecode.aluminumproject.libraries.actions.DefaultActionFactory;
import com.googlecode.aluminumproject.libraries.xml.actions.DynamicAttribute;
import com.googlecode.aluminumproject.libraries.xml.actions.DynamicElement;
import com.googlecode.aluminumproject.utilities.ReflectionUtilities;
import com.googlecode.aluminumproject.utilities.environment.EnvironmentUtilities;

/**
 * Provides actions that work with XML documents. Internally, the <a href="http://www.xom.nu/">XOM</a> library is used,
 * though users of the XML library shouldn't have to be aware of that; they can work with the types in the {@code model}
 * package.
 *
 * @author levi_h
 */
public class XmlLibrary extends AbstractLibrary {
	private LibraryInformation information;

	/**
	 * Creates an XML library.
	 */
	public XmlLibrary() {
		super(ReflectionUtilities.getPackageName(XmlLibrary.class));

		String url = "http://aluminumproject.googlecode.com/xml";
		String version = EnvironmentUtilities.getVersion();

		information = new LibraryInformation(url, "x", version, true, true, false);
	}

	public LibraryInformation getInformation() {
		return information;
	}

	@Override
	public ActionFactory getDynamicActionFactory(String name) throws LibraryException {
		ActionFactory actionFactory = new DefaultActionFactory(DynamicElement.class);
		initialiseLibraryElement(actionFactory);
		return actionFactory;
	}

	@Override
	public ActionContributionFactory getDynamicActionContributionFactory(String name) {
		ActionContributionFactory actionContributionFactory =
			new DefaultActionContributionFactory(DynamicAttribute.class);
		initialiseLibraryElement(actionContributionFactory);
		return actionContributionFactory;
	}
}