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
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

	/**
	 * Finds or creates a {@link Type type}, based on its name. The following types are supported:
	 * <ul>
	 * <li>Primitive types ({@code int}, {@code boolean});
	 * <li>{@link Class Classes} ({@code java.lang.Object});
	 * <li>{@link ParameterizedType Parameterised types} ({@code java.util.List<java.lang.String>}, {@code
	 *     java.util.Map<java.lang.String, java.lang.Integer>});
	 * <li>{@link WildcardType Wildcard types} ({@code ?}, {@code ? super java.lang.Long}, {@code ? extends
	 *     java.lang.Number}; but not {@code ? extends java.lang.Number & java.io.Serializable}).
	 * </ul>
	 * Class names are expected to be fully qualified, unless their packages are contained in the {@code
	 * defaultPackages} parameter.
	 *
	 * @param name the name of the type to find or create
	 * @param defaultPackages the packages for which class names don't have to be qualified
	 * @return the type with the given name
	 * @throws UtilityException when the type can't be found or created
	 */
	public static Type getType(String name, String... defaultPackages) throws UtilityException {
		return getType(name, true, defaultPackages);
	}

	private static Type getType(
			String name, boolean allowPrimitive, String... defaultPackages) throws UtilityException {
		Type type;

		if (name.startsWith("?")) {
			type = createWildcardType(name, defaultPackages);
		} else if (name.contains("<")) {
			type = createParameterisedType(name, defaultPackages);
		} else if (allowPrimitive && PRIMITIVE_TYPES.containsKey(name)) {
			type = PRIMITIVE_TYPES.get(name);
		} else {
			type = getClass(name, defaultPackages);
		}

		return type;
	}

	private static WildcardType createWildcardType(String name, String... defaultPackages) throws UtilityException {
		Matcher matcher = WILDCARD_TYPE_PATTERN.matcher(name);

		if (matcher.matches()) {
			Type boundingType;
			boolean upperBound;

			if (matcher.group(1) == null) {
				boundingType = Object.class;
				upperBound = true;
			} else {
				boundingType = getType(matcher.group(2), false, defaultPackages);
				upperBound = matcher.group(1).equals("extends");
			}

			return new WildcardTypeImpl(boundingType, upperBound);
		} else {
			throw new UtilityException("can't create wildcard type with name '", name, "'");
		}
	}

	private static class WildcardTypeImpl implements WildcardType {
		private Type boundingType;
		private boolean upperBound;

		public WildcardTypeImpl(Type boundingType, boolean upperBound) {
			this.boundingType = boundingType;
			this.upperBound = upperBound;
		}

		public Type[] getLowerBounds() {
			return upperBound ? new Type[0] : getBounds();
		}

		public Type[] getUpperBounds() {
			return upperBound ? getBounds() : new Type[0];
		}

		private Type[] getBounds() {
			return new Type[] {
				boundingType
			};
		}
	}

	private static ParameterizedType createParameterisedType(
			String name, String... defaultPackages) throws UtilityException {
		Matcher matcher = PARAMETERISED_TYPE_PATTERN.matcher(name);

		if (matcher.matches()) {
			Type rawType = getType(matcher.group(1), false, defaultPackages);

			String[] argumentNames = matcher.group(2).split(",\\s*");
			List<Type> arguments = new ArrayList<Type>(argumentNames.length);

			for (String argumentName: argumentNames) {
				arguments.add(getType(argumentName, false, defaultPackages));
			}

			return new ParameterisedType(rawType, arguments);
		} else {
			throw new UtilityException("can't create parameterised type with name '", name, "'");
		}
	}

	private static class ParameterisedType implements ParameterizedType {
		private Type rawType;
		private List<Type> arguments;

		public ParameterisedType(Type rawType, List<Type> arguments) {
			this.rawType = rawType;
			this.arguments = arguments;
		}

		public Type getOwnerType() {
			return null;
		}

		public Type getRawType() {
			return rawType;
		}

		public Type[] getActualTypeArguments() {
			return arguments.toArray(new Type[arguments.size()]);
		}
	}

	private static Class<?> getClass(String className, String... defaultPackages) throws UtilityException {
		Class<?> foundClass = null;

		List<String> classNames = new LinkedList<String>();

		if (className.contains(".")) {
			classNames.add(className);
		} else {
			for (String defaultPackage: defaultPackages) {
				classNames.add(String.format("%s.%s", defaultPackage, className));
			}
		}

		Iterator<String> itClassNames = classNames.iterator();

		while ((foundClass == null) && itClassNames.hasNext()) {
			try {
				foundClass = Class.forName(itClassNames.next(), false, Thread.currentThread().getContextClassLoader());
			} catch (ClassNotFoundException exception) {}
		}

		if (foundClass == null) {
			throw new UtilityException("can't find class with name '", className, "'");
		} else {
			return foundClass;
		}
	}

	private final static Pattern WILDCARD_TYPE_PATTERN = Pattern.compile("\\?(?:\\s+(super|extends)\\s+(.+))?");
	private final static Pattern PARAMETERISED_TYPE_PATTERN = Pattern.compile("(.+?)<(.+)>");

	private final static Map<String, Class<?>> PRIMITIVE_TYPES;

	static {
		Map<String, Class<?>> primitiveTypes = new HashMap<String, Class<?>>();
		primitiveTypes.put("boolean", Boolean.TYPE);
		primitiveTypes.put("byte", Byte.TYPE);
		primitiveTypes.put("char", Character.TYPE);
		primitiveTypes.put("short", Short.TYPE);
		primitiveTypes.put("int", Integer.TYPE);
		primitiveTypes.put("long", Long.TYPE);
		primitiveTypes.put("float", Float.TYPE);
		primitiveTypes.put("double", Double.TYPE);

		PRIMITIVE_TYPES = Collections.unmodifiableMap(primitiveTypes);
	}
}