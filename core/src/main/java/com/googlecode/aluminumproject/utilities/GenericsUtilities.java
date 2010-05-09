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

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides utility methods related to generics.
 *
 * @author levi_h
 */
public class GenericsUtilities {
	private GenericsUtilities() {}

	/**
	 * Finds a positioned argument to a generic type (class or interface) for a certain class. In other words: it finds
	 * the class with which a generic interface has been implemented or the class that was used to subclass a generic
	 * class. Some example calls and their return values:
	 * <p>
	 * <code>
	 * getTypeArgument(String.class, Comparable.class, 0);
	 * // String.class: String implements Comparable&lt;String&gt;
	 * <br>
	 * getTypeArgument(ElementType.class, Enum.class, 0);
	 * // ElementType.class: ElementType extends Enum&lt;ElementType&gt;
	 * <br>
	 * getTypeArgument(RuleBasedCollator.class, Comparator.class, 0);
	 * // Object.class: Collator implements Comparator&lt;Object&gt;
	 * </code>
	 *
	 * @param parameterisedType the type of which the type argument should be determined
	 * @param genericType the class extended by or the interface implemented by {@code parameterisedType}
	 * @param index the (zero-based) index that indicates which type argument should be retrieved
	 * @return the type argument at position {@code index} used by {@code parameterisedType} to extend or implement
	 *         {@code genericType}
	 * @throws UtilityException when the type argument can't be worked out
	 */
	public static Class<?> getTypeArgument(
			Class<?> parameterisedType, Class<?> genericType, int index) throws UtilityException {
		Type typeArgument = null;

		List<Class<?>> path = new ArrayList<Class<?>>();
		addToPath(path, parameterisedType, genericType);

		if (!path.isEmpty()) {
			int i = 0;

			while ((i < path.size() - 1) && (!(typeArgument instanceof Class))) {
				Class<?> implementedOrExtendedType = path.get(i);
				Class<?> implementingOrExtendingType = path.get(i + 1);

				ParameterizedType parameterisedImplementedOrExtendedType = null;

				if (implementedOrExtendedType.isInterface()) {
					Type[] genericInterfaces = implementingOrExtendingType.getGenericInterfaces();
					int j = 0;

					while ((j < genericInterfaces.length) && (parameterisedImplementedOrExtendedType == null)) {
						Type genericInterface = genericInterfaces[j];

						if (genericInterface instanceof ParameterizedType) {
							ParameterizedType parameterisedGenericInterface = (ParameterizedType) genericInterface;

							if (parameterisedGenericInterface.getRawType() == implementedOrExtendedType) {
								parameterisedImplementedOrExtendedType = parameterisedGenericInterface;
							}
						}

						j++;
					}
				} else {
					Type genericSuperclass = implementingOrExtendingType.getGenericSuperclass();

					if (genericSuperclass instanceof ParameterizedType) {
						parameterisedImplementedOrExtendedType = (ParameterizedType) genericSuperclass;
					}
				}

				if (parameterisedImplementedOrExtendedType == null) {
					throw new UtilityException("can't find a parameterised version of ", implementedOrExtendedType);
				}

				typeArgument = parameterisedImplementedOrExtendedType.getActualTypeArguments()[index];

				if (typeArgument instanceof TypeVariable) {
					TypeVariable<?> typeVariable = (TypeVariable<?>) typeArgument;

					TypeVariable<?>[] typeParameters = implementingOrExtendingType.getTypeParameters();

					index = 0;

					while (typeParameters[index] != typeVariable) {
						index++;
					}
				}

				i++;
			}
		}

		Class<?> typeClass = getTypeClass(typeArgument);

		if (typeClass == null) {
			throw new UtilityException("can't find type argument ", index, " that ", parameterisedType, " uses to ",
				genericType.isInterface() ? "implement" : "extend", " ", genericType);
		} else {
			return typeClass;
		}
	}

	private static boolean addToPath(List<Class<?>> path, Class<?> startClass, Class<?> stopClass) {
		boolean add = false;

		if (startClass == stopClass) {
			add = true;
		} else if (startClass != null) {
			Class<?>[] interfaces = startClass.getInterfaces();
			int i = 0;

			while ((i < interfaces.length) && !add) {
				add = addToPath(path, interfaces[i], stopClass);

				i++;
			}

			add |= addToPath(path, startClass.getSuperclass(), stopClass);
		}

		if (add) {
			path.add(startClass);
		}

		return add;
	}

	private static Class<?> getTypeClass(Type type) {
		Class<?> typeClass;

		if (type instanceof Class) {
			return (Class<?>) type;
		} else if (type instanceof GenericArrayType) {
			Class<?> componentTypeClass = getTypeClass(((GenericArrayType) type).getGenericComponentType());

			typeClass = Array.newInstance(componentTypeClass, 0).getClass();
		} else if (type instanceof ParameterizedType) {
			typeClass = getTypeClass(((ParameterizedType) type).getRawType());
		} else if (type instanceof TypeVariable) {
			Type[] bounds = ((TypeVariable<?>) type).getBounds();

			typeClass = getTypeClass(bounds[0]);
		} else {
			typeClass = null;
		}

		return typeClass;
	}
}