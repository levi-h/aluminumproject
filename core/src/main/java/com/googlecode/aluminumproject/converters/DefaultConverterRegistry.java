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
package com.googlecode.aluminumproject.converters;

import static com.googlecode.aluminumproject.configuration.DefaultConfiguration.CONFIGURATION_ELEMENT_PACKAGES;
import static com.googlecode.aluminumproject.utilities.ReflectionUtilities.getPackageName;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.annotations.Ignored;
import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.configuration.ConfigurationElementFactory;
import com.googlecode.aluminumproject.configuration.ConfigurationParameters;
import com.googlecode.aluminumproject.finders.TypeFinder.TypeFilter;
import com.googlecode.aluminumproject.utilities.GenericsUtilities;
import com.googlecode.aluminumproject.utilities.Injector;
import com.googlecode.aluminumproject.utilities.Logger;
import com.googlecode.aluminumproject.utilities.ReflectionUtilities;
import com.googlecode.aluminumproject.utilities.Utilities;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A default {@link ConverterRegistry converter registry} implementation.
 * <p>
 * The default converter registry keeps a set of {@link Converter converters}, which subclasses are free to add to. Upon
 * initialisation, this set will be filled with all of the converters inside the packages that are listed (separated by
 * commas) in the configuration parameter {@value #CONVERTER_PACKAGES}, or, when no such parameter is added, with all of
 * the converters in the {@code com.googlecode.aluminumproject.converters} package (and subpackages). The converter
 * package list is extended by the packages that are contained in the configuration parameter named {@value
 * com.googlecode.aluminumproject.configuration.DefaultConfiguration#CONFIGURATION_ELEMENT_PACKAGES}.
 * <p>
 * When a default converter registry is asked to {@link #getConverter(Class, Type) get} a converter, it will try to
 * find the most specific one. This means that when a converter for a string is requested and the registry contains
 * converters for both strings and objects, the converter that converts strings will be returned, even though both would
 * be able to convert values of the requested type.
 */
public class DefaultConverterRegistry implements ConverterRegistry {
	private Configuration configuration;

	private Injector injector;

	private Set<Converter<?>> converters;

	/** The logger to use. */
	protected final Logger logger;

	/**
	 * Creates a default converter registry.
	 */
	public DefaultConverterRegistry() {
		converters = new HashSet<Converter<?>>();

		logger = Logger.get(getClass());
	}

	public void initialise(Configuration configuration) throws AluminumException {
		this.configuration = configuration;

		injector = new Injector();
		injector.addValueProvider(new Injector.ClassBasedValueProvider(configuration));

		Set<String> converterPackages = new HashSet<String>();

		ConfigurationParameters parameters = configuration.getParameters();
		Collections.addAll(converterPackages,
			parameters.getValues(CONVERTER_PACKAGES, getPackageName(Converter.class)));
		Collections.addAll(converterPackages, parameters.getValues(CONFIGURATION_ELEMENT_PACKAGES));

		for (String converterPackage: converterPackages) {
			registerConverters(converterPackage);
		}
	}

	/**
	 * Registers converters for all converter classes inside a package that haven't been annotated with {@link Ignored
	 * &#64;Ignored}.
	 *
	 * @param packageName the name of the package that contains the converters to register
	 * @throws AluminumException when one of the converters can't be registered
	 */
	protected void registerConverters(String packageName) throws AluminumException {
		logger.debug("registering all converters in package ", packageName);

		List<Class<?>> converterClasses = configuration.getTypeFinder().find(new TypeFilter() {
			public boolean accepts(Class<?> type) {
				return Converter.class.isAssignableFrom(type) && !ReflectionUtilities.isAbstract(type)
					&& !type.isAnnotationPresent(Ignored.class);
			}
		}, packageName);

		ConfigurationElementFactory configurationElementFactory = configuration.getConfigurationElementFactory();

		for (Class<?> converterClass: converterClasses) {
			registerConverter(configurationElementFactory.instantiate(converterClass.getName(), Converter.class));
		}
	}

	public void disable() {
		converters.clear();
	}

	public void registerConverter(Converter<?> converter) throws AluminumException {
		injector.inject(converter);

		logger.debug("injected fields of converter");

		converters.add(converter);

		logger.debug("registered converter ", converter);
	}

	public <S> Converter<? super S> getConverter(Class<S> sourceType, Type targetType) throws AluminumException {
		if (targetType instanceof Class) {
			targetType = ReflectionUtilities.wrapPrimitiveType((Class<?>) targetType);
		}

		List<Converter<? super S>> convertersForSourceType = new ArrayList<Converter<? super S>>();

		for (Converter<?> converter: converters) {
			Class<?> typeArgument = GenericsUtilities.getTypeArgument(converter.getClass(), Converter.class, 0);

			if (typeArgument.isAssignableFrom(sourceType)) {
				Converter<? super S> typedConverter = Utilities.<Converter<? super S>>typed(converter);

				if (typedConverter.supportsSourceType(sourceType) && converter.supportsTargetType(targetType)) {
					convertersForSourceType.add(typedConverter);
				}
			}
		}

		if (convertersForSourceType.isEmpty()) {
			throw new AluminumException("no converter found that can convert values ",
				"from type ", sourceType.getName(), " to ", targetType);
		} else {
			return Collections.min(convertersForSourceType, new ConverterComparator(sourceType));
		}
	}

	/**
	 * Compares two converters by their source types' distances to a requested source type, where <i>distance<i> is
	 * defined as the number of steps it takes in the type hierarchy to get from a type to the requested source type.
	 */
	private static class ConverterComparator implements Comparator<Converter<?>> {
		private Class<?> sourceType;

		public ConverterComparator(Class<?> sourceType) {
			this.sourceType = sourceType;
		}

		public int compare(Converter<?> a, Converter<?> b) {
			return getDistance(sourceType, GenericsUtilities.getTypeArgument(a.getClass(), Converter.class, 0)) -
				getDistance(sourceType, GenericsUtilities.getTypeArgument(b.getClass(), Converter.class, 0));
		}

		private int getDistance(Class<?> from, Class<?> to) {
			int distance;

			if (from == to) {
				distance = 0;
			} else {
				Class<?> supertype = from.getSuperclass();

				distance = (supertype == null) ? Short.MAX_VALUE : getDistance(supertype, to) + 1;

				for (Class<?> implementedType: from.getInterfaces()) {
					distance = Math.min(distance, getDistance(implementedType, to) + 1);
				}
			}

			return distance;
		}
	}

	public <S> Object convert(S value, Type targetType) throws AluminumException {
		Object convertedValue;

		if (value == null) {
			convertedValue = null;
		} else {
			Class<?> targetClass = null;

			if (targetType instanceof ParameterizedType) {
				targetClass = (Class<?>) ((ParameterizedType) targetType).getRawType();
			} else if (targetType instanceof Class) {
				targetClass = ReflectionUtilities.wrapPrimitiveType((Class<?>) targetType);

				targetType = targetClass;
			}

			if ((targetClass != null) && targetClass.isInstance(value)) {
				convertedValue = value;
			} else {
				Class<S> sourceType = Utilities.typed(value.getClass());

				convertedValue = getConverter(sourceType, targetType).convert(value, targetType);
			}
		}

		return convertedValue;
	}

	/**
	 * The name of the configuration parameter that contains the comma-separated list of packages that will be included
	 * when searching for converters.
	 */
	public final static String CONVERTER_PACKAGES = "converter_registry.default.converter.packages";
}