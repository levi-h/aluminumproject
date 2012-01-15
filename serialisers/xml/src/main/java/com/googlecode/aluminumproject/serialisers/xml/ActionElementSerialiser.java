/*
 * Copyright 2009-2012 Levi Hoogenberg
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

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.libraries.actions.ActionParameter;
import com.googlecode.aluminumproject.serialisers.ElementNameTranslator;
import com.googlecode.aluminumproject.templates.ActionContributionDescriptor;
import com.googlecode.aluminumproject.templates.ActionDescriptor;
import com.googlecode.aluminumproject.templates.ActionElement;
import com.googlecode.aluminumproject.templates.Template;
import com.googlecode.aluminumproject.templates.TemplateElement;

import java.io.PrintWriter;
import java.util.Collections;
import java.util.Map;

/**
 * Serialises {@link ActionElement action elements}.
 */
public class ActionElementSerialiser implements TemplateElementSerialiser<ActionElement> {
	/**
	 * Creates an action element serialiser.
	 */
	public ActionElementSerialiser() {}

	public void writeBeforeChildren(Template template, ActionElement actionElement,
			ElementNameTranslator elementNameTranslator, PrintWriter writer) throws AluminumException {
		ActionDescriptor actionDescriptor = actionElement.getDescriptor();

		writer.print("<");
		writer.print(getQualifiedName(actionDescriptor.getLibraryUrlAbbreviation(),
			elementNameTranslator.translateActionName(actionDescriptor.getName())));

		for (Map.Entry<String, ActionParameter> parameter: actionElement.getParameters().entrySet()) {
			writer.print(" ");
			writer.print(elementNameTranslator.translateActionParameterName(parameter.getKey()));
			writer.print("=\"");
			writer.print(parameter.getValue().getText());
			writer.print("\"");
		}

		for (ActionContributionDescriptor contributionDescriptor: actionElement.getContributionDescriptors()) {
			writer.print(" ");
			writer.print(getQualifiedName(contributionDescriptor.getLibraryUrlAbbreviation(),
				elementNameTranslator.translateActionContributionName(contributionDescriptor.getName())));
			writer.print("=\"");
			writer.print(contributionDescriptor.getParameter().getText());
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

	public void writeAfterChildren(Template template, ActionElement actionElement,
			ElementNameTranslator elementNameTranslator, PrintWriter writer) throws AluminumException {
		if (!template.getChildren(actionElement).isEmpty()) {
			ActionDescriptor actionDescriptor = actionElement.getDescriptor();

			writer.print("</");
			writer.print(getQualifiedName(actionDescriptor.getLibraryUrlAbbreviation(),
				elementNameTranslator.translateActionName(actionDescriptor.getName())));
			writer.print(">");
		}
	}

	private String getQualifiedName(String libraryUrlAbbreviation, String name) {
		return (libraryUrlAbbreviation == null) ? name : String.format("%s:%s", libraryUrlAbbreviation, name);
	}
}