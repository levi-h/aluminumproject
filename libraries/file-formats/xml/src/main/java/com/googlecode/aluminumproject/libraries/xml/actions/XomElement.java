/*
 * Copyright 2010-2012 Levi Hoogenberg
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
import com.googlecode.aluminumproject.libraries.xml.model.Element;
import com.googlecode.aluminumproject.libraries.xml.model.SelectionContext;
import com.googlecode.aluminumproject.writers.Writer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nu.xom.Comment;
import nu.xom.Document;
import nu.xom.Node;
import nu.xom.Nodes;
import nu.xom.Serializer;
import nu.xom.XPathContext;
import nu.xom.XPathException;
import nu.xom.xslt.XSLException;
import nu.xom.xslt.XSLTransform;

@SuppressWarnings("javadoc")
class XomElement implements Element {
	private nu.xom.Element element;

	public XomElement(nu.xom.Element element) {
		this.element = (element.getDocument() == null) ? new Document(element).getRootElement() : element;
	}

	public List<?> select(String expression, SelectionContext context) throws AluminumException {
		Nodes nodes;

		try {
			XPathContext xPathContext = new XPathContext();

			for (Map.Entry<String, String> namespace: context.getNamespaces().entrySet()) {
				xPathContext.addNamespace(namespace.getKey(), namespace.getValue());
			}

			nodes = element.query(expression, xPathContext);
		} catch (XPathException exception) {
			throw new AluminumException(exception, "can't execute query");
		}

		List<Object> results = new ArrayList<Object>(nodes.size());

		for (int i = 0; i < nodes.size(); i++) {
			Node node = nodes.get(i);

			if (node instanceof nu.xom.Element) {
				results.add(new XomElement((nu.xom.Element) node));
			} else if ((node instanceof nu.xom.Attribute) || (node instanceof nu.xom.Namespace)
					|| (node instanceof nu.xom.Text) || (node instanceof Comment)) {
				results.add(node.getValue());
			} else {
				throw new AluminumException("unsupported node type: ", node.getClass().getSimpleName());
			}
		}

		return results;
	}

	public List<?> transform(Element styleSheet) throws AluminumException {
		if (!(styleSheet instanceof XomElement)) {
			throw new AluminumException("can't use style sheet ", styleSheet);
		}

		Nodes nodes;

		try {
			Document inputDocument = new Document(new nu.xom.Element(element));
			Document styleSheetDocument = new Document(new nu.xom.Element(((XomElement) styleSheet).element));

			nodes = new XSLTransform(styleSheetDocument).transform(inputDocument);
		} catch (XSLException exception) {
			throw new AluminumException(exception, "can't transform document");
		}

		List<Object> results = new ArrayList<Object>(nodes.size());

		for (int i = 0; i < nodes.size(); i++) {
			Node node = nodes.get(i);

			if (node instanceof nu.xom.Element) {
				results.add(new XomElement((nu.xom.Element) node));
			} else if ((node instanceof nu.xom.Text)) {
				results.add(node.getValue());
			} else {
				throw new AluminumException("unsupported node type: ", node.getClass().getSimpleName());
			}
		}

		return results;
	}

	public void writeDocument(Writer writer, int indentation) throws AluminumException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		Serializer serializer = new Serializer(out);
		serializer.setLineSeparator("\n");
		serializer.setIndent(indentation);

		try {
			serializer.write(element.getDocument());
		} catch (IOException exception) {
			throw new AluminumException(exception, "can't write document");
		}

		writer.write(new String(out.toByteArray()).trim());
	}
}