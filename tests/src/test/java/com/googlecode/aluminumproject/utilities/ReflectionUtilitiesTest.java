/*
 * Copyright 2009-2012 Levi Hoogenberg
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

import com.googlecode.aluminumproject.AluminumException;

import java.awt.Point;
import java.text.Collator;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javassist.ClassPool;
import javassist.Loader;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
@Test(groups = {"utilities", "fast"})
public class ReflectionUtilitiesTest {
	private ClassLoader contextClassLoader;

	@BeforeMethod
	public void getContextClassLoader() {
		contextClassLoader = Thread.currentThread().getContextClassLoader();
	}

	public void instantiationShouldWorkWithActualType() {
		Map<?, ?> map = ReflectionUtilities.instantiate("java.util.HashMap", HashMap.class, contextClassLoader);
		assert map != null;
		assert map instanceof HashMap;
	}

	@Test(dependsOnMethods = "instantiationShouldWorkWithActualType")
	public void instantiationShouldWorkWithSupertype() {
		Map<?, ?> map = ReflectionUtilities.instantiate("java.util.HashMap", Map.class, contextClassLoader);
		assert map != null;
		assert map instanceof HashMap;
	}

	public void instantiationShouldWorkWithCustomClassLoader() {
		ClassLoader classLoader = new Loader(ClassPool.getDefault());

		ClassPool.getDefault().makeClass("com.googlecode.aluminumproject.Test");

		Object test = ReflectionUtilities.instantiate("com.googlecode.aluminumproject.Test", Object.class, classLoader);
		assert test != null;
		assert test.getClass().getClassLoader() == classLoader;
	}

	@Test(expectedExceptions = AluminumException.class)
	public void instantiatingNonexistingClassShouldFail() {
		ReflectionUtilities.instantiate("Nonexisting", Object.class, contextClassLoader);
	}

	@Test(expectedExceptions = AluminumException.class)
	public void instantiationWithUnassignableTypeShouldFail() {
		ReflectionUtilities.instantiate("java.util.HashMap", Collection.class, contextClassLoader);
	}

	@Test(expectedExceptions = AluminumException.class)
	public void instantiatingUninstantiableClassShouldCauseException() {
		ReflectionUtilities.instantiate(Float.class);
	}

	public void instantiatingInstantiableClassShouldWork() {
		assert ReflectionUtilities.instantiate(Object.class) != null;
	}

	public void gettingPackageNameShouldWork() {
		String packageName;

		packageName = ReflectionUtilities.getPackageName(getClass());
		assert packageName != null;
		assert packageName.equals("com.googlecode.aluminumproject.utilities");

		packageName = ReflectionUtilities.getPackageName(String.class);
		assert packageName != null;
		assert packageName.equals("java.lang");
	}

	public void primitiveTypesShouldBeWrapped() {
		assert ReflectionUtilities.wrapPrimitiveType(Boolean.TYPE) == Boolean.class;
		assert ReflectionUtilities.wrapPrimitiveType(Byte.TYPE) == Byte.class;
		assert ReflectionUtilities.wrapPrimitiveType(Character.TYPE) == Character.class;
		assert ReflectionUtilities.wrapPrimitiveType(Short.TYPE) == Short.class;
		assert ReflectionUtilities.wrapPrimitiveType(Integer.TYPE) == Integer.class;
		assert ReflectionUtilities.wrapPrimitiveType(Long.TYPE) == Long.class;
		assert ReflectionUtilities.wrapPrimitiveType(Float.TYPE) == Float.class;
		assert ReflectionUtilities.wrapPrimitiveType(Double.TYPE) == Double.class;
	}

	public void nonPrimitiveTypesShouldNotBeWrapped() {
		assert ReflectionUtilities.wrapPrimitiveType(Object.class) == Object.class;
		assert ReflectionUtilities.wrapPrimitiveType(String.class) == String.class;
	}

	public void abstractClassesShouldBeRecognisedAsBeingAbstract() {
		assert ReflectionUtilities.isAbstract(AbstractMap.class);
	}

	public void concreteClassesShouldBeRecognisedAsNotBeingAbstract() {
		assert !ReflectionUtilities.isAbstract(HashMap.class);
	}

	public void interfacesShouldBeRecognisedAsBeingAbstract() {
		assert ReflectionUtilities.isAbstract(Map.class);
	}

	@Test(expectedExceptions = AluminumException.class)
	public void gettingValueOfUnknownFieldShouldCauseException() {
		ReflectionUtilities.getFieldValue(new Object(), "name");
	}

	public void gettingFieldValueShouldGiveCorrectValue() {
		Object fieldValue = ReflectionUtilities.getFieldValue(new Point(), "x");
		assert fieldValue instanceof Integer;
		assert ((Integer) fieldValue).intValue() == 0;
	}

	@Test(expectedExceptions = AluminumException.class)
	public void settingFieldValueWithWrongTypeShouldCauseException() {
		ReflectionUtilities.setFieldValue(new Point(), "x", 0L);
	}

	@Test(expectedExceptions = AluminumException.class)
	public void settingValueOfUnknownFieldShouldCauseException() {
		ReflectionUtilities.setFieldValue(new Point(), "z", 4);
	}

	@Test(expectedExceptions = AluminumException.class)
	public void settingValueOfFinalFieldShouldCauseException() {
		ReflectionUtilities.setFieldValue("", "CASE_INSENSITIVE_ORDER", Collections.reverseOrder());
	}

	public void settingFieldValueShouldUpdateBean() {
		Point point = new Point();

		ReflectionUtilities.setFieldValue(point, "x", 3);
		assert point.x == 3;
	}

	public void gettersShouldBeRecognisedAsSuch() throws NoSuchMethodException {
		assert ReflectionUtilities.isGetter(String.class.getMethod("getBytes"));
	}

	public void nonGettersShouldBeRecognisedAsSuch() throws NoSuchMethodException {
		assert !ReflectionUtilities.isGetter(Collection.class.getMethod("size"));
	}

	@Test(expectedExceptions = AluminumException.class)
	public void gettingPropertyWithWrongTypeShouldCauseException() {
		ReflectionUtilities.getProperty(getClass(), Integer.class, "name");
	}

	@Test(expectedExceptions = AluminumException.class)
	public void gettingUnknownPropertyShouldCauseException() {
		ReflectionUtilities.getProperty(new Object(), Object.class, "name");
	}

	@Test(expectedExceptions = AluminumException.class)
	public void gettingWriteOnlyPropertyShouldCauseException() {
		ReflectionUtilities.getProperty(new StringBuilder(), Integer.TYPE, "length");
	}

	public void gettingPropertyShouldGiveCorrectValue() {
		String property = ReflectionUtilities.getProperty(getClass(), String.class, "simpleName");
		assert property != null;
		assert property.equals("ReflectionUtilitiesTest");
	}

	@Test(expectedExceptions = AluminumException.class)
	public void gettingNestedPropertyWithWrongTypeShouldCauseException() {
		ReflectionUtilities.getNestedProperty(10, Integer.class, "class.name");
	}

	@Test(dependsOnMethods = "gettingUnknownPropertyShouldCauseException", expectedExceptions = AluminumException.class)
	public void exceptionInChainWhileGettingNestedPropertyShouldBePropagated() {
		ReflectionUtilities.getNestedProperty("", Object.class, "class.count");
	}

	public void gettingNestedPropertyShouldGiveCorrectValue() {
		String nestedProperty = ReflectionUtilities.getNestedProperty(10, String.class, "class.simpleName");
		assert nestedProperty != null;
		assert nestedProperty.equals("Integer");
	}

	public void encounteringNullInChainWhileGettingNestedPropertyShouldResultInNull() {
		assert ReflectionUtilities.getNestedProperty(getClass(), String.class, "declaringClass.name")== null;
	}

	public void settersShouldBeRecognisedAsSuch() throws NoSuchMethodException {
		assert ReflectionUtilities.isSetter(Thread.class.getMethod("setPriority", Integer.TYPE));
	}

	public void nonSettersShouldBeRecognisedAsSuch() throws NoSuchMethodException {
		assert !ReflectionUtilities.isSetter(System.class.getMethod("setProperty", String.class, String.class));
	}

	@Test(expectedExceptions = AluminumException.class)
	public void settingPropertyWithWrongTypeShouldCauseException() {
		ReflectionUtilities.setProperty(new StringBuilder(), Long.TYPE, "length", 12L);
	}

	@Test(expectedExceptions = AluminumException.class)
	public void settingUnknownPropertyShouldCauseException() {
		ReflectionUtilities.setProperty(new Object(), Object.class, "name", "object");
	}

	@Test(expectedExceptions = AluminumException.class)
	public void settingReadOnlyPropertyShouldCauseException() {
		ReflectionUtilities.setProperty(getClass(), String.class, "simpleName", "ReflectionUtilitiesTest");
	}

	public void settingPropertyShouldUpdateBean() {
		Collator collator = Collator.getInstance();

		ReflectionUtilities.setProperty(collator, Integer.TYPE, "strength", Collator.SECONDARY);
		assert collator.getStrength() == Collator.SECONDARY;
	}

	public void gettingPropertyNameShouldWorkUsingGetter() throws NoSuchMethodException {
		String propertyName = ReflectionUtilities.getPropertyName(Thread.class.getMethod("isAlive"));
		assert propertyName != null;
		assert propertyName.equals("alive");
	}

	public void gettingPropertyNameShouldWorkUsingSetter() throws NoSuchMethodException {
		String propertyName = ReflectionUtilities.getPropertyName(Thread.class.getMethod("setDaemon", Boolean.TYPE));
		assert propertyName != null;
		assert propertyName.equals("daemon");
	}
}