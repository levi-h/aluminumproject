/*
 * Copyright 2012 Aluminum project
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

@SuppressWarnings("javadoc")
public class SelectionContext {
	private Map<String, String> namespaces;

	public SelectionContext() {
		namespaces = new HashMap<String, String>();
	}

	public void addNamespace(String prefix, String url) throws AluminumException {
		if (namespaces.containsKey(prefix)) {
			throw new AluminumException("duplicate namespace prefix: '", prefix, "'");
		} else {
			namespaces.put(prefix, url);
		}
	}

	public Map<String, String> getNamespaces() {
		return Collections.unmodifiableMap(namespaces);
	}
}