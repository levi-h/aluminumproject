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
package com.googlecode.aluminumproject.libraries.xml.actions;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.annotations.Ignored;
import com.googlecode.aluminumproject.annotations.Injected;
import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.libraries.actions.AbstractAction;
import com.googlecode.aluminumproject.templates.ActionDescriptor;
import com.googlecode.aluminumproject.writers.Writer;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import nu.xom.Attribute;
import nu.xom.Element;
import nu.xom.Node;
import nu.xom.Text;

@SuppressWarnings("javadoc")
abstract class AbstractElement extends AbstractAction {
	protected @Injected ActionDescriptor descriptor;

	private @Injected Configuration configuration;

	private @Ignored Map<String, String> namespaces;

	private @Ignored Map<String, Map<String, String>> attributes;

	private @Ignored List<Node> children;
	private @Ignored boolean moreChildrenAllowed;

	protected AbstractElement() {
		namespaces = new LinkedHashMap<String, String>();

		attributes = new LinkedHashMap<String, Map<String, String>>();

		children = new LinkedList<Node>();
		moreChildrenAllowed = true;
	}

	protected abstract String getElementName();

	protected void addChild(Element child) throws AluminumException {
		addChildNode(child);
	}

	protected void addText(String text) throws AluminumException {
		addChildNode(new Text(text));
	}

	public void setText(String text) throws AluminumException {
		addText(text);

		moreChildrenAllowed = false;
	}

	private void addChildNode(Node child) throws AluminumException {
		if (!moreChildrenAllowed) {
			throw new AluminumException("can't add ", child, ": the element does not allow any more children");
		}

		children.add(child);
	}

	protected void addNamespace(String prefix, String url) throws AluminumException {
		if (namespaces.containsKey(prefix)) {
			throw new AluminumException("duplicate namespace prefix: '", prefix, "'");
		}

		namespaces.put(prefix, url);
	}

	protected String findNamespaceUrl(String prefix) {
		String namespaceUrl;

		if (namespaces.containsKey(prefix)) {
			namespaceUrl = namespaces.get(prefix);
		} else {
			AbstractElement parent = findAncestorOfType(AbstractElement.class);

			namespaceUrl = (parent == null) ? null : parent.findNamespaceUrl(prefix);
		}

		return namespaceUrl;
	}

	protected void addAttribute(String prefix, String name, String value) {
		if (!attributes.containsKey(prefix)) {
			attributes.put(prefix, new LinkedHashMap<String, String>());
		}

		attributes.get(prefix).put(name, value);
	}

	public void execute(Context context, Writer writer) throws AluminumException {
		Element element = createElement();
		addNamespaceDeclarations(element);

		getBody().invoke(context, new TextWriter(this, configuration.getConverterRegistry()));

		addAttributes(element);

		addChildren(element);

		AbstractElement parent = findAncestorOfType(AbstractElement.class);

		if (parent == null) {
			writer.write(new XomElement(element));
		} else {
			parent.addChild(element);
		}
	}

	private Element createElement() {
		Element element;

		String prefix = descriptor.getLibraryUrlAbbreviation();
		String namespaceUrl = findNamespaceUrl(prefix);

		if (namespaceUrl == null) {
			element = new Element(getElementName());
		} else {
			element = new Element(String.format("%s:%s", prefix, getElementName()), namespaceUrl);
		}

		return element;
	}

	private void addNamespaceDeclarations(Element element) {
		for (Map.Entry<String, String> namespace: namespaces.entrySet()) {
			element.addNamespaceDeclaration(namespace.getKey(), namespace.getValue());
		}
	}

	private void addAttributes(Element element) throws AluminumException {
		for (Map.Entry<String, Map<String, String>> prefixedAttribute: attributes.entrySet()) {
			String namespacePrefix = prefixedAttribute.getKey();

			if (namespacePrefix == null) {
				for (Map.Entry<String, String> attribute: prefixedAttribute.getValue().entrySet()) {
					String attributeName = attribute.getKey();
					String attributeValue = attribute.getValue();

					element.addAttribute(new Attribute(attributeName, attributeValue));
				}
			} else {
				String namespaceUrl = findNamespaceUrl(namespacePrefix);

				if (namespaceUrl == null) {
					throw new AluminumException("can't find namespace with prefix '", namespacePrefix, "'");
				} else {
					for (Map.Entry<String, String> attribute: prefixedAttribute.getValue().entrySet()) {
						String attributeName = String.format("%s:%s", namespacePrefix, attribute.getKey());
						String attributeValue = attribute.getValue();

						element.addAttribute(new Attribute(attributeName, namespaceUrl, attributeValue));
					}
				}
			}
		}
	}

	private void addChildren(Element element) {
		for (Node child: children) {
			element.appendChild(child);
		}
	}
}