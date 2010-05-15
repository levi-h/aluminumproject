/*
 * Copyright 2009-2010 Levi Hoogenberg
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
package com.googlecode.aluminumproject.serialisers.xml;

import com.googlecode.aluminumproject.libraries.actions.ActionContributionFactory;
import com.googlecode.aluminumproject.libraries.actions.ActionParameter;
import com.googlecode.aluminumproject.serialisers.ElementNameTranslator;
import com.googlecode.aluminumproject.templates.ActionElement;
import com.googlecode.aluminumproject.templates.Template;
import com.googlecode.aluminumproject.templates.TemplateElement;

import java.io.PrintWriter;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

/**
 * Serialises {@link ActionElement action elements}.
 *
 * @author levi_h
 */
public class ActionElementSerialiser implements TemplateElementSerialiser<ActionElement> {
	/**
	 * Creates an action element serialiser.
	 */
	public ActionElementSerialiser() {}

	public void writeBeforeChildren(Template template,
			ActionElement actionElement, ElementNameTranslator elementNameTranslator, PrintWriter writer) {
		String actionName = actionElement.getFactory().getInformation().getName();
		String translatedActionName = elementNameTranslator.translateActionName(actionName);

		String actionLibraryUrl = actionElement.getFactory().getLibrary().getInformation().getUrl();

		writer.print("<");
		writer.print(getQualifiedName(actionElement, translatedActionName, actionLibraryUrl));

		for (Map.Entry<String, ActionParameter> parameter: actionElement.getParameters().entrySet()) {
			writer.print(" ");
			writer.print(elementNameTranslator.translateActionParameterName(parameter.getKey()));
			writer.print("=\"");
			writer.print(parameter.getValue().getText());
			writer.print("\"");
		}

		for (ActionContributionFactory contributionFactory: actionElement.getContributionFactories().keySet()) {
			String contributionName = contributionFactory.getInformation().getName();
			String translatedContributionName = elementNameTranslator.translateActionContributionName(contributionName);

			String contributionLibraryUrl = contributionFactory.getLibrary().getInformation().getUrl();

			writer.print(" ");
			writer.print(getQualifiedName(actionElement, translatedContributionName, contributionLibraryUrl));
			writer.print("=\"");
			writer.print(actionElement.getContributionFactories().get(contributionFactory).getText());
			writer.print("\"");
		}

		TemplateElement parentElement = template.getParent(actionElement);

		Map<String, String> parentNamespaces = (parentElement == null)
			? Collections.<String, String>emptyMap() : actionElement.getLibraryUrlAbbreviations();

		for (Map.Entry<String, String> namespace: actionElement.getLibraryUrlAbbreviations().entrySet()) {
			if (!parentNamespaces.containsKey(namespace.getKey())) {
				String abbreviation = namespace.getKey();

				writer.print(" xmlns");

				if (!abbreviation.equals("")) {
					writer.print(":");
					writer.print(abbreviation);
				}

				writer.print("=\"");
				writer.print(namespace.getValue());
				writer.print("\"");
			}
		}

		if (template.getChildren(actionElement).isEmpty()) {
			writer.print("/");
		}

		writer.print(">");
	}

	public void writeAfterChildren(Template template,
			ActionElement actionElement, ElementNameTranslator elementNameTranslator, PrintWriter writer) {
		if (!template.getChildren(actionElement).isEmpty()) {
			String actionName = actionElement.getFactory().getInformation().getName();
			String translatedActionName = elementNameTranslator.translateActionName(actionName);

			String actionLibraryUrl = actionElement.getFactory().getLibrary().getInformation().getUrl();

			writer.print("</");
			writer.print(getQualifiedName(actionElement, translatedActionName, actionLibraryUrl));
			writer.print(">");
		}
	}

	private String getQualifiedName(ActionElement actionElement, String name, String libraryUrl) {
		String libraryUrlAbbreviation = null;

		Map<String, String> libraryUrlAbbreviations = actionElement.getLibraryUrlAbbreviations();
		Iterator<String> it = libraryUrlAbbreviations.keySet().iterator();

		while ((libraryUrlAbbreviation == null) && it.hasNext()) {
			String abbreviation = it.next();

			if (libraryUrl.equals(libraryUrlAbbreviations.get(abbreviation))) {
				libraryUrlAbbreviation = abbreviation;
			}
		}

		return ((libraryUrlAbbreviation == null) || libraryUrlAbbreviation.equals(""))
			? name : String.format("%s:%s", libraryUrlAbbreviation, name);
	}
}