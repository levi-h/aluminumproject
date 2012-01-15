/*
 * Copyright 2010-2012 Aluminum project
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
package com.googlecode.aluminumproject.utilities.finders;

import com.googlecode.aluminumproject.utilities.finders.FieldFinder.FieldFilter;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.List;

import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
@Test(groups = {"utilities", "fast"})
public class FieldFinderTest {
	@Test(expectedExceptions = IllegalArgumentException.class)
	public void notSupplyingTypeShouldCauseException() {
		FieldFinder.find(new FieldFilter() {
			public boolean accepts(Field field) {
				return true;
			}
		}, null);
	}

	public void fieldsOfClassShouldBeFindable() throws NoSuchFieldException {
		List<Field> textualFields = FieldFinder.find(new FieldFilter() {
			public boolean accepts(Field field) {
				return field.getType() == String.class;
			}
		}, Car.class);

		assert textualFields != null;
		assert textualFields.size() == 1;
		assert textualFields.contains(Car.class.getDeclaredField("manufacturer"));
	}

	public void fieldsOfSuperclassShouldBeFindable() throws NoSuchFieldException {
		List<Field> numericFields = FieldFinder.find(new FieldFilter() {
			public boolean accepts(Field field) {
				return field.getType() == Integer.TYPE;
			}
		}, Car.class);

		assert numericFields != null;
		assert numericFields.size() == 1;
		assert numericFields.contains(Vehicle.class.getDeclaredField("maximumSpeed"));
	}

	public void fieldsOfImplementedInterfaceShouldBeFindable() throws NoSuchFieldException {
		List<Field> constants = FieldFinder.find(new FieldFilter() {
			public boolean accepts(Field field) {
				return Modifier.isStatic(field.getModifiers());
			}
		}, Car.class);

		assert constants != null;
		assert constants.size() == 1;
		assert constants.contains(TrafficParticipant.class.getField("MINIMUM_SPEEDING_FINE_AMOUNT"));
	}

	private static class Vehicle {
		@SuppressWarnings("unused")
		private int maximumSpeed;
	}

	private static interface TrafficParticipant {
		@SuppressWarnings("unused")
		BigDecimal MINIMUM_SPEEDING_FINE_AMOUNT = new BigDecimal("25");
	}

	private static class Car extends Vehicle implements TrafficParticipant {
		@SuppressWarnings("unused")
		private String manufacturer;
	}
}