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
package com.googlecode.aluminumproject.utilities;

import static com.googlecode.aluminumproject.utilities.GenericsUtilities.getTypeArgument;

import java.util.List;

import org.testng.annotations.Test;

@SuppressWarnings("all")
@Test(groups = {"utilities", "fast"})
public class GenericsUtilitiesTest {
	public static interface Interface<A, B> {}
	public static class InterfaceImplementation implements Interface<String, Integer> {}

	public void typeArgumentOfInterfaceImplementationShouldBeObtainable() {
		assert getTypeArgument(InterfaceImplementation.class, Interface.class, 0) == String.class;
		assert getTypeArgument(InterfaceImplementation.class, Interface.class, 1) == Integer.class;
	}

	public static class ArrayImplementation implements Interface<String[], String[][]> {}

	@Test(dependsOnMethods = "typeArgumentOfInterfaceImplementationShouldBeObtainable")
	public void arrayTypeShouldBeSupported() {
		assert getTypeArgument(ArrayImplementation.class, Interface.class, 0) == String[].class;
		assert getTypeArgument(ArrayImplementation.class, Interface.class, 1) == String[][].class;
	}

	public static interface Subinterface<B, A> extends Interface<A, B> {}
	public static class SubinterfaceImplementation implements Subinterface<String, Double> {}

	public void typeArgumentOfSubinterfaceImplementationShouldBeObtainable() {
		assert getTypeArgument(SubinterfaceImplementation.class, Interface.class, 0) == Double.class;
		assert getTypeArgument(SubinterfaceImplementation.class, Interface.class, 1) == String.class;
	}

	public static class VariableSubinterfaceImplementation<C extends Number, D> implements Subinterface<C, D> {}

	@Test(dependsOnMethods = "typeArgumentOfSubinterfaceImplementationShouldBeObtainable")
	public void variableTypesShouldBeSupported() {
		assert getTypeArgument(VariableSubinterfaceImplementation.class, Subinterface.class, 0) == Number.class;
		assert getTypeArgument(VariableSubinterfaceImplementation.class, Subinterface.class, 1) == Object.class;
	}

	public static class Superclass<A, B> {}
	public static class Subclass extends Superclass<String, Double> {}

	public void typeArgumentOfSubclassShouldBeObtainable() {
		assert getTypeArgument(Subclass.class, Superclass.class, 0) == String.class;
		assert getTypeArgument(Subclass.class, Superclass.class, 1) == Double.class;
	}

	public static class ParameterisedSubclass<T extends String>
		extends Superclass<List<?>, ThreadLocal<? extends String>> {}

	@Test(dependsOnMethods = "typeArgumentOfSubclassShouldBeObtainable")
	public void parameterisedTypeShouldBeSupported() {
		assert getTypeArgument(ParameterisedSubclass.class, Superclass.class, 0) == List.class;
		assert getTypeArgument(ParameterisedSubclass.class, Superclass.class, 1) == ThreadLocal.class;
	}

	public static class SubclassOfInterfaceImplementation extends InterfaceImplementation {}
	public static class SubclassOfSubclass extends Subclass {}

	public void typeArgumentsShouldBeInheritedByAncestors() {
		assert getTypeArgument(SubclassOfInterfaceImplementation.class, Interface.class, 0) == String.class;
		assert getTypeArgument(SubclassOfInterfaceImplementation.class, Interface.class, 1) == Integer.class;

		assert getTypeArgument(SubclassOfSubclass.class, Superclass.class, 0) == String.class;
		assert getTypeArgument(SubclassOfSubclass.class, Superclass.class, 1) == Double.class;
	}

	@Test(expectedExceptions = UtilityException.class)
	public void requestingAbstractTypeArgumentShouldCauseException() {
		getTypeArgument(Interface.class, Interface.class, 0);
	}
}