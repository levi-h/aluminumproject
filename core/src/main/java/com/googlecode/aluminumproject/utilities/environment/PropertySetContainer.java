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
package com.googlecode.aluminumproject.utilities.environment;

import com.googlecode.aluminumproject.AluminumException;

import java.util.Properties;

/**
 * Contains various property sets. Each property set is identified by its name. Since property sets are
 * environment-specific, a properties container can be obtained using {@link
 * EnvironmentUtilities#getPropertySetContainer() a method in EnvironmentUtilities}.
 */
public interface PropertySetContainer {
	/**
	 * Determines whether this property set container contains a property set with a certain name.
	 *
	 * @param name the name of the property set to check
	 * @return {@code true} if a property set with the given name exists in this property set container, {@code false}
	 *         otherwise
	 */
	boolean containsPropertySet(String name);

	/**
	 * Reads a property set with a certain name. The returned property set is not live: changes don't automatically
	 * propagate back to this container.
	 *
	 * @param name the name of the property set to read
	 * @return the property set with the given name
	 * @throws AluminumException if this property set container does not contain a property set with the given name or
	 *                           when the property set can't be read
	 */
	Properties readPropertySet(String name) throws AluminumException;

	/**
	 * Writes a property set. If this property set container does not contain a property set with the given name, it
	 * will be created; if a property set with the same name does exist, it will be overwritten.
	 *
	 * @param name the name to use for the property set
	 * @param propertySet the property set to write
	 * @throws AluminumException when the property set can't be written
	 */
	void writePropertySet(String name, Properties propertySet) throws AluminumException;

	/**
	 * Removes a property set.
	 *
	 * @param name the name of the property set to remove
	 * @throws AluminumException if this property set container does not contain a property set with the given name
	 */
	void removePropertySet(String name) throws AluminumException;
}