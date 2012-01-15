/*
 * Copyright 2009-2012 Aluminum project
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
package com.googlecode.aluminumproject.context;

import java.util.Map;
import java.util.Set;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
@Test(groups = {"core", "fast"})
public class ScopeMapTest {
	private Scope scope;

	private Map<String, Object> map;

	@BeforeMethod
	public void createScopeAndScopeMap() {
		scope = new DefaultScope("default");
		scope.setVariable("name", "value");

		map = new ScopeMap(scope);
	}

	public void keySetShouldContainVariableNames() {
		Set<String> keySet = map.keySet();
		assert keySet.size() == 1;
		assert keySet.contains("name");
	}

	public void variablesShouldBeRetrievable() {
		Object variable = map.get("name");
		assert variable != null;
		assert variable.equals("value");
	}

	public void addingValueToMapShouldAddVariableToScope() {
		map.put("other name", "other value");

		Object variable = scope.getVariable("other name");
		assert variable != null;
		assert variable.equals("other value");
	}

	public void changingEntryShouldChangeVariable() {
		for (Map.Entry<String, Object> entry: map.entrySet()) {
			if (entry.getKey().equals("name")) {
				entry.setValue("new value");
			}
		}

		Object variable = scope.getVariable("name");
		assert variable != null;
		assert variable.equals("new value");
	}

	public void removingValueFromMapShouldRemoveVariableFromScope() {
		map.remove("name");

		assert scope.getVariableNames().isEmpty();
	}
}