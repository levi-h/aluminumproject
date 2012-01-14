/*
 * Copyright 2012 Levi Hoogenberg
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
package com.googlecode.aluminumproject.libraries.xml.model;

import com.googlecode.aluminumproject.AluminumException;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * The context that is used when {@link Element#select(String, SelectionContext) selecting} from an element.
 *
 * @author levi_h
 */
public class SelectionContext {
	private Map<String, String> namespaces;

	/**
	 * Creates a selection context.
	 */
	public SelectionContext() {
		namespaces = new HashMap<String, String>();
	}

	/**
	 * Adds a namespace to this selection context.
	 *
	 * @param prefix the prefix of the namespace to add
	 * @param url the URL of the namespace to add
	 * @throws AluminumException when this selection context already contains a namespace with the given prefix
	 */
	public void addNamespace(String prefix, String url) throws AluminumException {
		if (namespaces.containsKey(prefix)) {
			throw new AluminumException("duplicate namespace prefix: '", prefix, "'");
		} else {
			namespaces.put(prefix, url);
		}
	}

	/**
	 * Returns a read-only view of the namespaces that were added to this selection context.
	 *
	 * @return the namespaces in this selection context
	 */
	public Map<String, String> getNamespaces() {
		return Collections.unmodifiableMap(namespaces);
	}
}