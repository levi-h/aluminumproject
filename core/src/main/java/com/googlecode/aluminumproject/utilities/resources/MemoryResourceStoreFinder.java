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
package com.googlecode.aluminumproject.utilities.resources;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * A resource store finder that finds {@link ByteArrayOutputStream in-memory output streams}. After resources have
 * been stored they can be {@link #getResource(String) retrieved} and {@link #removeResource(String) removed}.
 *
 * @author levi_h
 */
public class MemoryResourceStoreFinder implements ResourceStoreFinder {
	private Map<String, ByteArrayOutputStream> resources;

	/**
	 * Creates an in-memory resource store finder.
	 */
	public MemoryResourceStoreFinder() {
		resources = new HashMap<String, ByteArrayOutputStream>();
	}

	/**
	 * Returns the contents of a resource with a certain name.
	 *
	 * @param name the name of the requested resource
	 * @return the contents of the resource with the given name or {@code null} if no resource could be found for
	 *         the name specified
	 */
	public byte[] getResource(String name) {
		ByteArrayOutputStream out = resources.get(name);

		return (out == null) ? null : out.toByteArray();
	}

	/**
	 * Removes a resource with a certain name. If no resource can be found with the given name, this is a no-op.
	 *
	 * @param name the name of the resource to remove
	 */
	public void removeResource(String name) {
		resources.remove(name);
	}

	public OutputStream find(String name) {
		if (!resources.containsKey(name)) {
			resources.put(name, new ByteArrayOutputStream());
		}

		return resources.get(name);
	}
}