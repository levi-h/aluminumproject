/*
 * Copyright 2009-2011 Levi Hoogenberg
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

import static com.googlecode.aluminumproject.utilities.GenericsUtilities.getName;
import static com.googlecode.aluminumproject.utilities.GenericsUtilities.getType;
import static com.googlecode.aluminumproject.utilities.GenericsUtilities.getTypeArgument;

import com.googlecode.aluminumproject.AluminumException;

import java.io.Serializable;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.List;
import java.util.Map;

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

	@Test(expectedExceptions = AluminumException.class)
	public void requestingAbstractTypeArgumentShouldCauseException() {
		getTypeArgument(Interface.class, Interface.class, 0);
	}

	public void nameOfClassShouldBeObtainable() {
		String name = getName(String.class);
		assert name != null;
		assert name.equals("java.lang.String");
	}

	public void nameOfArrayShouldBeObtainable() {
		String name = getName(Object[][].class);
		assert name != null;
		assert name.equals("java.lang.Object[][]");
	}

	@Test(dependsOnMethods = "genericArrayTypeShouldBeCreatable")
	public void nameOfGenericArrayShouldBeObtainable() {
		String name = getName(getType("java.util.List<java.lang.String>[]"), "java.lang", "java.util");
		assert name != null;
		assert name.equals("List<String>[]");
	}

	public void nameOfPrimitiveTypeShouldBeObtainable() {
		String name = getName(Integer.TYPE);
		assert name != null;
		assert name.equals("int");
	}

	@Test(dependsOnMethods = "wildcardTypeWithoutBoundsShouldBeCreatable")
	public void nameOfWildcardTypeWithoutBoundsShouldBeObtainable() {
		String name = getName(getType("?"));
		assert name != null;
		assert name.equals("?");
	}

	public void nameOfWildcardTypeWithSingleUpperBoundShouldBeObtainable() {
		String name = getName(getType("? extends java.lang.Number"), "java.lang");
		assert name != null;
		assert name.equals("? extends Number");
	}

	@Test(dependsOnMethods = "wildcardTypeWithMultipleUpperBoundsShouldBeCreatable")
	public void nameOfWildcardTypeWithMultipleUpperBoundsShouldBeObtainable() {
		String name = getName(getType("? extends java.lang.Number & java.io.Serializable"), "java.lang");
		assert name != null;
		assert name.equals("? extends Number & java.io.Serializable");
	}

	@Test(dependsOnMethods = "wildcardTypeWithLowerBoundShouldBeCreatable")
	public void nameOfWildcardTypeWithLowerBoundsShouldBeObtainable() {
		String name = getName(getType("? super java.lang.Boolean"));
		assert name != null;
		assert name.equals("? super java.lang.Boolean");
	}

	@Test(dependsOnMethods = "parameterisedTypeWithSingleParameterShouldBeCreatable")
	public void nameOfParameterisedTypeWithSingleParameterShouldBeObtainable() {
		String name = getName(getType("java.util.Collection<java.lang.Integer>"));
		assert name != null;
		assert name.equals("java.util.Collection<java.lang.Integer>");
	}

	@Test(dependsOnMethods = "parameterisedTypeWithMultipleParametersShouldBeCreatable")
	public void nameOfParameterisedTypeWithMultipleParametersShouldBeObtainable() {
		String name = getName(getType("java.util.Map<java.lang.String, java.lang.Integer>"));
		assert name != null;
		assert name.equals("java.util.Map<java.lang.String, java.lang.Integer>");
	}

	public void genericArrayTypeShouldBeCreatable() {
		Type type = getType("java.lang.Comparable<?>[]");
		assert type instanceof GenericArrayType;
		assert ((GenericArrayType) type).getGenericComponentType() instanceof ParameterizedType;
	}

	public void wildcardTypeWithoutBoundsShouldBeCreatable() {
		Type type = getType("?");
		assert type instanceof WildcardType;

		WildcardType wildcardType = (WildcardType) type;

		Type[] lowerBounds = wildcardType.getLowerBounds();
		assert lowerBounds != null;
		assert lowerBounds.length == 0;

		Type[] upperBounds = wildcardType.getUpperBounds();
		assert upperBounds != null;
		assert upperBounds.length == 1;
		assert upperBounds[0] == Object.class;
	}

	public void wildcardTypeWithSingleUpperBoundShouldBeCreatable() {
		Type type = getType("? extends java.lang.Number");
		assert type instanceof WildcardType;

		WildcardType wildcardType = (WildcardType) type;

		Type[] lowerBounds = wildcardType.getLowerBounds();
		assert lowerBounds != null;
		assert lowerBounds.length == 0;

		Type[] upperBounds = wildcardType.getUpperBounds();
		assert upperBounds != null;
		assert upperBounds.length == 1;
		assert upperBounds[0] == Number.class;
	}

	public void wildcardTypeWithMultipleUpperBoundsShouldBeCreatable() {
		Type type = getType("? extends java.lang.Comparable & java.io.Serializable");
		assert type instanceof WildcardType;

		WildcardType wildcardType = (WildcardType) type;

		Type[] lowerBounds = wildcardType.getLowerBounds();
		assert lowerBounds != null;
		assert lowerBounds.length == 0;

		Type[] upperBounds = wildcardType.getUpperBounds();
		assert upperBounds != null;
		assert upperBounds.length == 2;
		assert upperBounds[0] == Comparable.class;
		assert upperBounds[1] == Serializable.class;
	}

	public void wildcardTypeWithLowerBoundShouldBeCreatable() {
		Type type = getType("? super java.lang.Long");
		assert type instanceof WildcardType;

		WildcardType wildcardType = (WildcardType) type;

		Type[] lowerBounds = wildcardType.getLowerBounds();
		assert lowerBounds != null;
		assert lowerBounds.length == 1;
		assert lowerBounds[0] == Long.class;

		Type[] upperBounds = wildcardType.getUpperBounds();
		assert upperBounds != null;
		assert upperBounds.length == 0;
	}

	public void parameterisedTypeWithSingleParameterShouldBeCreatable() {
		Type type = getType("java.util.List<java.lang.Integer>");
		assert type instanceof ParameterizedType;

		ParameterizedType parameterisedType = (ParameterizedType) type;
		assert parameterisedType.getOwnerType() == null;
		assert parameterisedType.getRawType() == List.class;

		Type[] arguments = parameterisedType.getActualTypeArguments();
		assert arguments != null;
		assert arguments.length == 1;
		assert arguments[0] == Integer.class;
	}

	public void parameterisedTypeWithMultipleParametersShouldBeCreatable() {
		Type type = getType("Map<String, List<Long>>", "java.lang", "java.util");
		assert type instanceof ParameterizedType;

		ParameterizedType parameterisedType = (ParameterizedType) type;
		assert parameterisedType.getOwnerType() == null;
		assert parameterisedType.getRawType() == Map.class;

		Type[] arguments = parameterisedType.getActualTypeArguments();
		assert arguments != null;
		assert arguments.length == 2;
		assert arguments[0] == String.class;
		assert arguments[1] instanceof ParameterizedType;

		ParameterizedType subParameterisedType = (ParameterizedType) arguments[1];
		assert subParameterisedType.getOwnerType() == null;
		assert subParameterisedType.getRawType() == List.class;

		Type[] subarguments = subParameterisedType.getActualTypeArguments();
		assert subarguments != null;
		assert subarguments.length == 1;
		assert subarguments[0] == Long.class;
	}

	public void classShouldBeObtainableUsingFullyQualifiedName() {
		assert getType("java.lang.String") == String.class;
	}

	public void classShouldBeObtainableUsingSimpleNameWhenDefaultPackagesAreSupplied() {
		assert getType("String", "java.lang") == String.class;
	}

	public void primitiveTypeShouldBeObtainable() {
		assert getType("boolean") == Boolean.TYPE;
	}

	@Test(expectedExceptions = AluminumException.class)
	public void obtainingNonexistentTypeShouldCauseException() {
		getType("java.util.Collection<int>");
	}
}