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
package com.googlecode.aluminumproject.libraries.xml.actions;

import com.googlecode.aluminumproject.annotations.Injected;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.ContextException;
import com.googlecode.aluminumproject.libraries.actions.AbstractAction;
import com.googlecode.aluminumproject.libraries.actions.ActionException;
import com.googlecode.aluminumproject.templates.ActionDescriptor;
import com.googlecode.aluminumproject.writers.Writer;
import com.googlecode.aluminumproject.writers.WriterException;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import nu.xom.Attribute;
import nu.xom.Element;
import nu.xom.Node;
import nu.xom.Text;

/**
 * Abstract superclass of actions that produce an {@link com.googlecode.aluminumproject.libraries.xml.model.Element XML
 * element}.
 * <p>
 * Abstract element actions can be nested: elements are added to their parents; the root element is sent to the {@link
 * Writer writer}.
 *
 * @author levi_h
 */
abstract class AbstractElement extends AbstractAction {
	/** This action's action descriptor. */
	protected @Injected ActionDescriptor descriptor;

	private Map<String, String> namespaces;

	private Map<String, Map<String, String>> attributes;

	private List<Node> children;

	/**
	 * Creates an abstract element action.
	 */
	protected AbstractElement() {
		namespaces = new LinkedHashMap<String, String>();

		children = new LinkedList<Node>();

		attributes = new LinkedHashMap<String, Map<String, String>>();
	}

	/**
	 * Returns the name of the element.
	 *
	 * @return the element name to use
	 */
	protected abstract String getElementName();

	/**
	 * Adds a child element to the element.
	 *
	 * @param child the element to add
	 */
	protected void addChild(Element child) {
		children.add(child);
	}

	/**
	 * Adds a text element to the element.
	 *
	 * @param text the text to add
	 */
	protected void addText(String text) {
		children.add(new Text(text));
	}

	/**
	 * Adds a namespace declaration to the element.
	 *
	 * @param prefix the prefix of the namespace to add
	 * @param url the URL of the namespace to add
	 * @throws ActionException if the element already contains a namespace with the given prefix
	 */
	protected void addNamespace(String prefix, String url) throws ActionException {
		if (namespaces.containsKey(prefix)) {
			throw new ActionException("duplicate namespace prefix: '", prefix, "'");
		}

		namespaces.put(prefix, url);
	}

	/**
	 * Finds a namespace URL given a prefix, looking through both the namespaces of this action and those of its
	 * ancestors.
	 *
	 * @param prefix the prefix of namespace prefix
	 * @return the URL of the namespace with the given prefix or {@code null} if neither this action nor its ancestors
	 *         contain a namespace with such a prefix
	 */
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

	/**
	 * Adds an attribute to the element.
	 *
	 * @param prefix the namespace prefix of the attribute (may be {@code null})
	 * @param name the name of the attribute to add
	 * @param value the value of the attribute to add
	 */
	protected void addAttribute(String prefix, String name, String value) {
		if (!attributes.containsKey(prefix)) {
			attributes.put(prefix, new LinkedHashMap<String, String>());
		}

		attributes.get(prefix).put(name, value);
	}

	public void execute(Context context, Writer writer) throws ActionException, ContextException, WriterException {
		Element element = createElement();
		addNamespaceDeclarations(element);

		getBody().invoke(context, new TextWriter(this));

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

	private void addAttributes(Element element) throws ActionException {
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
					throw new ActionException("can't find namespace with prefix '", namespacePrefix, "'");
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