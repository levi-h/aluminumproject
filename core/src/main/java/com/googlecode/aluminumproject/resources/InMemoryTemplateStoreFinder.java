/*
 * Copyright 2011 Levi Hoogenberg
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
package com.googlecode.aluminumproject.resources;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * A template store finder that finds {@link ByteArrayOutputStream in-memory output streams}. After templates have
 * been stored they can be {@link #get(String) retrieved} and {@link #remove(String) removed}.
 *
 * @author levi_h
 */
public class InMemoryTemplateStoreFinder extends AbstractTemplateStoreFinder {
	private Map<String, ByteArrayOutputStream> templates;

	/**
	 * Creates an in-memory template store finder.
	 */
	public InMemoryTemplateStoreFinder() {
		templates = new HashMap<String, ByteArrayOutputStream>();
	}

	public OutputStream find(String name) {
		if (!templates.containsKey(name)) {
			templates.put(name, new ByteArrayOutputStream());
		}

		return templates.get(name);
	}

	/**
	 * Returns the contents of a template.
	 *
	 * @param name the name of the requested template
	 * @return the contents of the template with the given name or {@code null} if no template could be found for
	 *         the specified name
	 */
	public byte[] get(String name) {
		return templates.containsKey(name) ? templates.get(name).toByteArray() : null;
	}

	/**
	 * Removes a template. If no template was stored with the given name, this is a no-op.
	 *
	 * @param name the name of the template to remove
	 */
	public void remove(String name) {
		templates.remove(name);
	}
}