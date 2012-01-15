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
package com.googlecode.aluminumproject.utilities;

import com.googlecode.aluminumproject.AluminumException;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Offers utility methods related to reflection.
 */
public class ReflectionUtilities {
	private ReflectionUtilities() {}

	/**
	 * Finds a class by name and instantiates it.
	 *
	 * @param <T> the type of the new bean
	 * @param beanClassName the name of the class to instantiate
	 * @param type the type of the bean class (can be a supertype of the actual bean class)
	 * @param loader the class loader to use
	 * @return a new instance of the class with the given name, cast to the given type
	 * @throws AluminumException when no class with the given name can be found, when the bean class is not assignable
	 *                           to the specified type, or when the bean class can't be initialised
	 */
	public static <T> T instantiate(String beanClassName, Class<T> type, ClassLoader loader) throws AluminumException {
		Class<?> beanClass;

		try {
			beanClass = loader.loadClass(beanClassName);
		} catch (ClassNotFoundException exception) {
			throw new AluminumException(exception, "can't find bean class '", beanClassName, "'");
		}

		if (type.isAssignableFrom(beanClass)) {
			return type.cast(instantiate(beanClass));
		} else {
			throw new AluminumException("class ", beanClassName, " is not of type ", type.getName());
		}
	}

	/**
	 * Instantiates a class.
	 *
	 * @param <T> the type of the new object
	 * @param beanClass the class to instantiate
	 * @return a new instance of the given class
	 * @throws AluminumException when the class can't be instantiated
	 */
	public static <T> T instantiate(Class<T> beanClass) throws AluminumException {
		try {
			return beanClass.newInstance();
		} catch (Exception exception) {
			throw new AluminumException(exception, "can't instantiate ", beanClass);
		}
	}

	/**
	 * Returns the name of a package in which a type resides.
	 *
	 * @param type the type to determine the package name of
	 * @return the name of {@code type}'s package, the empty string if {@code type} is in the default package
	 */
	public static String getPackageName(Class<?> type) {
		String typeName = type.getName();

		int lastSeparator = typeName.lastIndexOf('.');

		return (lastSeparator == -1) ? "" : typeName.substring(0, lastSeparator);
	}

	/**
	 * Determines the wrapper type for a primitive type.
	 *
	 * @param type the type to wrap
	 * @return the wrapped type for primitives (e.g. {@code Boolean.class} for {@code Boolean.TYPE}), the type itself
	 *         for non-primitive types
	 */
	public static Class<?> wrapPrimitiveType(Class<?> type) {
		Class<?> wrappedType;

		if (type == Boolean.TYPE) {
			wrappedType = Boolean.class;
		} else if (type == Byte.TYPE) {
			wrappedType = Byte.class;
		} else if (type == Character.TYPE) {
			wrappedType = Character.class;
		} else if (type == Short.TYPE) {
			wrappedType = Short.class;
		} else if (type == Integer.TYPE) {
			wrappedType = Integer.class;
		} else if (type == Long.TYPE) {
			wrappedType = Long.class;
		} else if (type == Float.TYPE) {
			wrappedType = Float.class;
		} else if (type == Double.TYPE) {
			wrappedType = Double.class;
		} else {
			wrappedType = type;
		}

		return wrappedType;
	}

	/**
	 * Determines whether a type is abstract or not.
	 *
	 * @param type the type of which needs to be found out whether it's abstract or not
	 * @return {@code true} if {@code type} is abstract, {@code false} otherwise
	 */
	public static boolean isAbstract(Class<?> type) {
		return Modifier.isAbstract(type.getModifiers());
	}

	/**
	 * Obtains the value of a field from a certain bean.
	 *
	 * @param bean the bean whose field value should be returned
	 * @param fieldName the name of the field to retrieve
	 * @return the value of the field with the specified name from the given bean
	 * @throws AluminumException when the field value can't be got
	 */
	public static Object getFieldValue(Object bean, String fieldName) throws AluminumException {
		Field field = getAccessibleField(bean, fieldName);

		try {
			return field.get(bean);
		} catch (IllegalArgumentException exception) {
			throw new AluminumException(exception, "can't get value of field '", field, "' from ", bean);
		} catch (IllegalAccessException exception) {
			throw new AluminumException(exception, "may not get value of field '", field, "' from ", bean);
		}
	}

	/**
	 * Sets the value of a field on a certain bean.
	 *
	 * @param bean the bean whose field value should be set
	 * @param fieldName the name of the field to set
	 * @param value the new value for the field
	 * @throws AluminumException when the field value can't be set
	 */
	public static void setFieldValue(Object bean, String fieldName, Object value) throws AluminumException {
		Field field = getAccessibleField(bean, fieldName);

		try {
			field.set(bean, value);
		} catch (IllegalArgumentException exception) {
			throw new AluminumException(exception, "can't set value of field '", field, "' on ", bean);
		} catch (IllegalAccessException exception) {
			throw new AluminumException(exception, "may not set value of field '", field, "' on ", bean);
		}
	}

	private static Field getAccessibleField(Object bean, String name) throws AluminumException {
		Field field;

		try {
			field = getField(bean.getClass(), name);

			if (field == null) {
				throw new AluminumException("can't find field named '", name, "' on ", bean);
			}

			if (!field.isAccessible()) {
				field.setAccessible(true);
			}
		} catch (SecurityException exception) {
			throw new AluminumException(exception, "may not access field '", name, "' on ", bean);
		}

		return field;
	}

	private static Field getField(Class<?> beanClass, String name) throws SecurityException {
		Field field;

		try {
			field = beanClass.getDeclaredField(name);
		} catch (NoSuchFieldException exception) {
			field = null;
		}

		if (field == null) {
			Class<?>[] interfaces = beanClass.getInterfaces();
			int i = 0;

			while ((i < interfaces.length) && (field == null)) {
				field = getField(interfaces[i++], name);
			}
		}

		if (field == null) {
			Class<?> superclass = beanClass.getSuperclass();

			if (superclass != null) {
				field = getField(superclass, name);
			}
		}

		return field;
	}

	/**
	 * Determines whether a method is a <i>getter</i>, in the JavaBeans sense of the word.
	 *
	 * @param method the method to examine
	 * @return {@code true} if the method is a getter, {@code false} otherwise
	 * @throws AluminumException if no information about the method's declaring class can be found
	 */
	public static boolean isGetter(Method method) throws AluminumException {
		PropertyDescriptor[] propertyDescriptors = getPropertyDescriptors(method.getDeclaringClass());
		int i = 0;

		while ((i < propertyDescriptors.length) && !method.equals(propertyDescriptors[i].getReadMethod())) {
			i++;
		}

		return i < propertyDescriptors.length;
	}

	/**
	 * Reads a property value from a bean.
	 *
	 * @param <T> the type of the property
	 * @param bean the bean to get the property value from
	 * @param propertyType the expected type of the property
	 * @param propertyName the name of the property
	 * @return the value of the property on the given bean
	 * @throws AluminumException when the property is of a different type than was expected, when the property is
	 *                           write-only, or when something goes wrong while getting the value
	 */
	public static <T> T getProperty(Object bean, Class<T> propertyType, String propertyName) throws AluminumException {
		Method getter = getPropertyDescriptor(bean.getClass(), propertyType, propertyName).getReadMethod();

		if (getter == null) {
			throw new AluminumException("property '", propertyName, "' is write-only");
		} else {
			if (!getter.isAccessible()) {
				getter.setAccessible(true);
			}

			Object property;

			try {
				property = getter.invoke(bean);
			} catch (Exception exception) {
				throw new AluminumException(exception,
					"can't invoke getter of property '", propertyName, "' on ", bean);
			}

			try {
				return propertyType.cast(property);
			} catch (ClassCastException exception) {
				throw new AluminumException(exception, "can't cast ", property, " to ", propertyType);
			}
		}
	}

	/**
	 * Reads a (possibly nested) property value from a bean.
	 *
	 * @param <T> the type of the last property in the chain
	 * @param bean the first bean in the chain
	 * @param propertyType the expected type of the property
	 * @param propertyPath the path to the property
	 * @return the value of the property
	 * @throws AluminumException when the property is of a different type than was expected, when of the properties is
	 *                           write-only, or when something goes wrong while getting the value
	 */
	public static <T> T getNestedProperty(
			Object bean, Class<T> propertyType, String propertyPath) throws AluminumException {
		Object value = bean;

		String[] propertyNames = propertyPath.split("\\.");
		int i = 0;

		while ((value != null) && (i < propertyNames.length)) {
			value = getProperty(value, Object.class, propertyNames[i]);

			i++;
		}

		try {
			return propertyType.cast(value);
		} catch (ClassCastException exception) {
			throw new AluminumException(exception, "can't cast ", value, " to ", propertyType);
		}
	}

	/**
	 * Determines whether a method is a <i>setter</i>, in the JavaBeans sense of the word.
	 *
	 * @param method the method to examine
	 * @return {@code true} if the method is a setter, {@code false} otherwise
	 * @throws AluminumException if no information about the method's declaring class can be found
	 */
	public static boolean isSetter(Method method) throws AluminumException {
		PropertyDescriptor[] propertyDescriptors = getPropertyDescriptors(method.getDeclaringClass());
		int i = 0;

		while ((i < propertyDescriptors.length) && !method.equals(propertyDescriptors[i].getWriteMethod())) {
			i++;
		}

		return i < propertyDescriptors.length;
	}

	/**
	 * Sets a property value on a bean.
	 *
	 * @param <T> the type of the property
	 * @param bean the bean to set the property value on
	 * @param propertyType the expected type of the property
	 * @param propertyName the name of the property
	 * @param propertyValue the value to set
	 * @throws AluminumException when the property is of a different type than was expected, when the property is
	 *                           read-only, or when something goes wrong while setting the value
	 */
	public static <T> void setProperty(
			Object bean, Class<T> propertyType, String propertyName, T propertyValue) throws AluminumException {
		Method setter = getPropertyDescriptor(bean.getClass(), propertyType, propertyName).getWriteMethod();

		if (setter == null) {
			throw new AluminumException("property '", propertyName, "' is read-only");
		} else {
			if (!setter.isAccessible()) {
				setter.setAccessible(true);
			}

			try {
				setter.invoke(bean, propertyValue);
			} catch (Exception exception) {
				throw new AluminumException(exception,
					"can't invoke setter of property '", propertyName, "' on ", bean);
			}
		}
	}

	/**
	 * Determines the name of a property based on its getter or setter.
	 *
	 * @param method the getter or setter of a property
	 * @return the name of the property that the given method is a getter or setter for
	 * @throws AluminumException when the method is neither a getter nor a setter
	 */
	public static String getPropertyName(Method method) throws AluminumException {
		PropertyDescriptor[] propertyDescriptors = getPropertyDescriptors(method.getDeclaringClass());
		int i = 0;

		while ((i < propertyDescriptors.length) && !method.equals(propertyDescriptors[i].getReadMethod())
				&& !method.equals(propertyDescriptors[i].getWriteMethod())) {
			i++;
		}

		if (i == propertyDescriptors.length) {
			throw new AluminumException("method ", method, " is neither a getter nor a setter");
		} else {
			return propertyDescriptors[i].getName();
		}
	}

	/**
	 * Determines the type of a property.
	 *
	 * @param beanClass the bean that contains the property
	 * @param propertyName the name of the property to determine the type of
	 * @return the type of the property with the specified name on the given bean class
	 * @throws AluminumException when the bean class does not have a property with the given name
	 */
	public static Class<?> getPropertyType(Class<?> beanClass, String propertyName) throws AluminumException {
		return getPropertyDescriptor(beanClass, Object.class, propertyName).getPropertyType();
	}

	private static PropertyDescriptor[] getPropertyDescriptors(Class<?> beanClass) throws AluminumException {
		PropertyDescriptor[] propertyDescriptors;

		try {
			propertyDescriptors = Introspector.getBeanInfo(beanClass).getPropertyDescriptors();
		} catch (IntrospectionException exception) {
			throw new AluminumException(exception, "can't get information on ", beanClass.getName());
		}

		return propertyDescriptors;
	}

	private static PropertyDescriptor getPropertyDescriptor(
			Class<?> beanClass, Class<?> expectedPropertyType, String propertyName) throws AluminumException {
		PropertyDescriptor[] propertyDescriptors = getPropertyDescriptors(beanClass);
		int i = 0;

		while ((i < propertyDescriptors.length) && !propertyDescriptors[i].getName().equals(propertyName)) {
			i++;
		}

		if (i == propertyDescriptors.length) {
			throw new AluminumException("can't find property with name '", propertyName, "' on ", beanClass);
		} else {
			PropertyDescriptor propertyDescriptor = propertyDescriptors[i];

			Class<?> actualPropertyType = propertyDescriptor.getPropertyType();

			if (expectedPropertyType.isAssignableFrom(actualPropertyType)
					|| (actualPropertyType.isPrimitive() && (expectedPropertyType == Object.class))) {
				return propertyDescriptor;
			} else {
				throw new AluminumException("property '", propertyName, "' is of type ", actualPropertyType.getName(),
					", not of type ", expectedPropertyType.getName());
			}
		}
	}
}