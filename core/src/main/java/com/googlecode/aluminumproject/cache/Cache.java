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
package com.googlecode.aluminumproject.cache;

import com.googlecode.aluminumproject.configuration.ConfigurationElement;
import com.googlecode.aluminumproject.templates.Template;
import com.googlecode.aluminumproject.utilities.Utilities;

import java.io.Serializable;

/**
 * A cache for {@link Template templates}. Templates are stored under a {@link Key key}: the combination of the name of
 * the template and the name of the parser that produced it.
 *
 * @author levi_h
 */
public interface Cache extends ConfigurationElement {
	/**
	 * Stores a template under a certain key. When a template had already been stored under the given key, it is
	 * replaced.
	 *
	 * @param key the key to store the template under
	 * @param template the template to store
	 * @throws CacheException when the cache is not available
	 */
	void storeTemplate(Key key, Template template) throws CacheException;

	/**
	 * Finds a template by its key. When this cache does not contain the given key, {@code null} is returned.
	 *
	 * @param key the key to find a template by
	 * @return the template with the given key or {@code null} if no template was stored under the key given
	 * @throws CacheException when the cache is not available
	 */
	Template findTemplate(Key key) throws CacheException;

	/**
	 * The key that cached templates are stored under.
	 *
	 * @author levi_h
	 */
	public static class Key implements Serializable {
		private String name;
		private String parser;

		/**
		 * Creates a cache key.
		 *
		 * @param name the name of the template
		 * @param parser the name of the parser that was used to create the template
		 */
		public Key(String name, String parser) {
			this.name = name;
			this.parser = parser;
		}

		@Override
		public int hashCode() {
			return 53 * System.identityHashCode(name) + 53 * System.identityHashCode(parser) + 43;
		}

		@Override
		public boolean equals(Object object) {
			return (object.getClass() == Key.class) && equals((Key) object);
		}

		private boolean equals(Key key) {
			return Utilities.equals(name, key.name) && Utilities.equals(parser, key.parser);
		}

		@Override
		public String toString() {
			return String.format("Cache key, template: %s, parser: %s", name, parser);
		}

		private final static long serialVersionUID = 20090524L;
	}
}