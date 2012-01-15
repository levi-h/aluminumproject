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

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayList;
import java.util.Arrays;
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
	 * @throws AluminumException when the type argument can't be worked out
	 */
	public static Class<?> getTypeArgument(
			Class<?> parameterisedType, Class<?> genericType, int index) throws AluminumException {
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
					throw new AluminumException("can't find a parameterised version of ", implementedOrExtendedType);
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
			throw new AluminumException("can't find type argument ", index, " that ", parameterisedType, " uses to ",
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
	 * Returns the name of a {@link Type type}.
	 *
	 * @param type the type to return the name of
	 * @param defaultPackages the packages that do not need to be included in the name
	 * @return the name of the given type
	 */
	public static String getName(Type type, String... defaultPackages) {
		String name;

		if (type instanceof Class) {
			Class<?> classType = (Class<?>) type;

			if (classType.isArray()) {
				name = String.format("%s[]", getName(classType.getComponentType(), defaultPackages));
			} else {
				Package typePackage = classType.getPackage();

				name = ((typePackage != null) && Arrays.asList(defaultPackages).contains(typePackage.getName()))
					? classType.getSimpleName() : classType.getName();
			}
		} else if (type instanceof GenericArrayType) {
			name = String.format("%s[]", getName(((GenericArrayType) type).getGenericComponentType(), defaultPackages));
		} else if (type instanceof WildcardType) {
			WildcardType wildcardType = (WildcardType) type;

			StringBuilder nameBuilder = new StringBuilder("?");

			Type[] lowerBounds = wildcardType.getLowerBounds();

			if (lowerBounds.length > 0) {
				nameBuilder.append(" super ").append(getName(lowerBounds[0], defaultPackages));
			} else {
				Type[] upperBounds = wildcardType.getUpperBounds();

				if (!((upperBounds.length == 1) && (upperBounds[0] == Object.class))) {
					for (Type upperBound: upperBounds) {
						nameBuilder.append(" ");
						nameBuilder.append(nameBuilder.length() == 2 ? "extends" : "&");
						nameBuilder.append(" ").append(getName(upperBound, defaultPackages));
					}
				}
			}

			name = nameBuilder.toString();
		} else if (type instanceof ParameterizedType) {
			ParameterizedType parameterizedType = (ParameterizedType) type;

			StringBuilder nameBuilder = new StringBuilder(getName(parameterizedType.getRawType(), defaultPackages));

			Type[] typeArguments = parameterizedType.getActualTypeArguments();

			for (int i = 0; i < typeArguments.length; i++) {
				nameBuilder.append((i == 0) ? "<" : ", ");
				nameBuilder.append(getName(typeArguments[i], defaultPackages));
			}

			name = nameBuilder.append(">").toString();
		} else {
			name = type.toString();
		}

		return name;
	}

	/**
	 * Finds or creates a {@link Type type}, based on its name. The following types are supported:
	 * <ul>
	 * <li>Primitive types ({@code int}, {@code boolean});
	 * <li>{@link Class Classes} ({@code java.lang.Object});
	 * <li>{@link GenericArrayType Generic array types} ({@code java.util.List<java.lang.String>[]});
	 * <li>{@link ParameterizedType Parameterised types} ({@code java.util.List<java.lang.String>}, {@code
	 *     java.util.Map<java.lang.String, java.lang.Integer>});
	 * <li>{@link WildcardType Wildcard types} ({@code ?}, {@code ? super java.lang.Long}, {@code ? extends
	 *     java.lang.Number}, {@code ? extends java.lang.Number & java.io.Serializable}).
	 * </ul>
	 * Class names are expected to be fully qualified, unless their packages are contained in the {@code
	 * defaultPackages} parameter.
	 *
	 * @param name the name of the type to find or create
	 * @param defaultPackages the packages for which class names don't have to be qualified
	 * @return the type with the given name
	 * @throws AluminumException when the type can't be found or created
	 */
	public static Type getType(String name, String... defaultPackages) throws AluminumException {
		return getType(name, true, defaultPackages);
	}

	private static Type getType(
			String name, boolean allowPrimitive, String... defaultPackages) throws AluminumException {
		Type type;

		if (name.startsWith("?")) {
			type = createWildcardType(name, defaultPackages);
		} else if (name.endsWith("[]")) {
			type = createGenericArrayType(name, defaultPackages);
		} else if (name.contains("<")) {
			type = createParameterisedType(name, defaultPackages);
		} else if (allowPrimitive && PRIMITIVE_TYPES.containsKey(name)) {
			type = PRIMITIVE_TYPES.get(name);
		} else {
			type = getClass(name, defaultPackages);
		}

		return type;
	}

	private static Type createGenericArrayType(String name, String... defaultPackages) throws AluminumException {
		Type componentType = getType(name.substring(0, name.length() - 2), defaultPackages);

		if (componentType instanceof Class) {
			throw new AluminumException("can't create class with component type ", componentType);
		} else {
			return new GenericArrayTypeImpl(componentType);
		}
	}

	private static class GenericArrayTypeImpl implements GenericArrayType {
		private Type genericComponentType;

		public GenericArrayTypeImpl(Type genericComponentType) {
			this.genericComponentType = genericComponentType;
		}

		public Type getGenericComponentType() {
			return genericComponentType;
		}
	}

	private static WildcardType createWildcardType(String name, String... defaultPackages) throws AluminumException {
		Matcher matcher = WILDCARD_TYPE_PATTERN.matcher(name);

		if (matcher.matches()) {
			Type[] boundingTypes;
			boolean upperBound;

			if (matcher.group(1) == null) {
				boundingTypes = new Type[] {
					Object.class
				};
				upperBound = true;
			} else {
				String[] boundingTypesAsText = matcher.group(2).split("\\s*&\\s*");
				boundingTypes = new Type[boundingTypesAsText.length];

				for (int i = 0; i < boundingTypesAsText.length; i++) {
					boundingTypes[i] = getType(boundingTypesAsText[i], false, defaultPackages);
				}

				upperBound = matcher.group(1).equals("extends");
			}

			return new WildcardTypeImpl(boundingTypes, upperBound);
		} else {
			throw new AluminumException("can't create wildcard type with name '", name, "'");
		}
	}

	private static class WildcardTypeImpl implements WildcardType {
		private Type[] bounds;
		private boolean upperBound;

		public WildcardTypeImpl(Type[] bounds, boolean upperBound) {
			this.bounds = bounds;
			this.upperBound = upperBound;
		}

		public Type[] getLowerBounds() {
			return upperBound ? new Type[0] : copyBounds();
		}

		public Type[] getUpperBounds() {
			return upperBound ? copyBounds() : new Type[0];
		}

		private Type[] copyBounds() {
			Type[] copiedBounds = new Type[bounds.length];

			System.arraycopy(bounds, 0, copiedBounds, 0, bounds.length);

			return copiedBounds;
		}
	}

	private static ParameterizedType createParameterisedType(
			String name, String... defaultPackages) throws AluminumException {
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
			throw new AluminumException("can't create parameterised type with name '", name, "'");
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

	private static Class<?> getClass(String className, String... defaultPackages) throws AluminumException {
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
			throw new AluminumException("can't find class with name '", className, "'");
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