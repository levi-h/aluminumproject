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
package com.googlecode.aluminumproject.libraries.ds.functions;

import com.googlecode.aluminumproject.utilities.UtilityException;

import java.util.Comparator;

import org.testng.annotations.Test;

@SuppressWarnings("all")
@Test(groups = {"libraries", "libraries-ds", "fast"})
public class ComparatorsTest {
	public void naturalOrderComparatorShouldBeUsable() {
		Comparator<Object> naturalOrderComparator = Comparators.naturalOrder();
		assert naturalOrderComparator.compare(1, 2) < 0;
		assert naturalOrderComparator.compare(1, 1) == 0;
		assert naturalOrderComparator.compare(2, 1) > 0;
	}

	@Test(expectedExceptions = UtilityException.class)
	public void naturalOrderComparatorShouldNotSupportNonComparables() {
		Comparators.naturalOrder().compare(Integer.class, Long.class);
	}

	public void propertyComparatorShouldBeUsable() {
		Comparator<Object> classNameComparator = Comparators.byProperty("simpleName");
		assert classNameComparator.compare(Integer.class, Long.class) < 0;
		assert classNameComparator.compare(Double.class, Double.class) == 0;
		assert classNameComparator.compare(Short.class, Long.class) > 0;
	}

	@Test(dependsOnMethods = "naturalOrderComparatorShouldBeUsable")
	public void comparatorsShouldBeReversible() {
		Comparator<Object> naturalOrderComparator = Comparators.reverse(Comparators.naturalOrder());
		assert naturalOrderComparator.compare(2, 1) < 0;
		assert naturalOrderComparator.compare(1, 1) == 0;
		assert naturalOrderComparator.compare(1, 2) > 0;
	}

	@Test(expectedExceptions = UtilityException.class)
	public void comparingObjectsOfDifferentTypesShouldCauseException() {
		Comparators.naturalOrder().compare('l', 81);
	}

	public void nullsShouldTakePrecedenceOverNonNulls() {
		Comparator<Object> comparator = Comparators.naturalOrder();
		assert comparator.compare(null, "") < 0;
		assert comparator.compare(null, null) == 0;
		assert comparator.compare("", null) > 0;
	}
}