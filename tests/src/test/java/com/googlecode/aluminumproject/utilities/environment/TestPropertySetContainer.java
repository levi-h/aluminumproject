/*
 * Copyright 2010-2011 Levi Hoogenberg
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

import com.googlecode.aluminumproject.utilities.UtilityException;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * The property set container that is used in tests.
 *
 * @author levi_h
 */
public class TestPropertySetContainer implements PropertySetContainer {
	private Map<String, Properties> propertySets;

	/**
	 * Creates a test property set container.
	 */
	public TestPropertySetContainer() {
		propertySets = new HashMap<String, Properties>();
	}

	public boolean containsPropertySet(String name) {
		return propertySets.containsKey(name);
	}

	public Properties readPropertySet(String name) throws UtilityException {
		Properties propertySet = propertySets.get(name);

		if (propertySet == null) {
			throw new UtilityException("property set '", name, "' does not exist");
		}

		return propertySet;
	}

	public void writePropertySet(String name, Properties propertySet) {
		propertySets.put(name, propertySet);
	}

	public void removePropertySet(String name) throws UtilityException {
		if (propertySets.containsKey(name)) {
			propertySets.remove(name);
		} else {
			throw new UtilityException("can't remove property set '", name, "', since it does not exist");
		}
	}
}