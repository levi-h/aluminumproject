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
package com.googlecode.aluminumproject.converters;

import static com.googlecode.aluminumproject.configuration.DefaultConfiguration.CONFIGURATION_ELEMENT_PACKAGES;
import static com.googlecode.aluminumproject.utilities.ReflectionUtilities.getPackageName;

import com.googlecode.aluminumproject.Logger;
import com.googlecode.aluminumproject.annotations.Ignored;
import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.configuration.ConfigurationElementFactory;
import com.googlecode.aluminumproject.configuration.ConfigurationException;
import com.googlecode.aluminumproject.configuration.ConfigurationParameters;
import com.googlecode.aluminumproject.utilities.GenericsUtilities;
import com.googlecode.aluminumproject.utilities.ReflectionUtilities;
import com.googlecode.aluminumproject.utilities.Utilities;
import com.googlecode.aluminumproject.utilities.UtilityException;
import com.googlecode.aluminumproject.utilities.finders.TypeFinder;
import com.googlecode.aluminumproject.utilities.finders.TypeFinder.TypeFilter;

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
 * When a default converter registry is asked to {@link #getConverter(Class, Class) get} a converter, it will try to
 * find the most specific one. This means that when a converter for a string is requested and the registry contains
 * converters for both strings and objects, the converter that converts strings will be returned, even though both would
 * be able to convert values of the requested type.
 *
 * @author levi_h
 */
public class DefaultConverterRegistry implements ConverterRegistry {
	private Configuration configuration;

	private Set<Converter<?>> converters;

	/** The logger to use. */
	protected final Logger logger;

	/**
	 * Creates a default converter registry.
	 */
	public DefaultConverterRegistry() {
		logger = Logger.get(getClass());
	}

	public void initialise(
			Configuration configuration, ConfigurationParameters parameters) throws ConfigurationException {
		this.configuration = configuration;

		converters = new HashSet<Converter<?>>();

		Set<String> converterPackages = new HashSet<String>();

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
	 * @throws ConfigurationException when one of the converters can't be registered
	 */
	protected void registerConverters(String packageName) throws ConfigurationException {
		logger.debug("registering all converters in package ", packageName);

		List<Class<?>> converterClasses;

		try {
			converterClasses = TypeFinder.find(new TypeFilter() {
				public boolean accepts(Class<?> type) {
					return Converter.class.isAssignableFrom(type) && !ReflectionUtilities.isAbstract(type)
						&& !type.isAnnotationPresent(Ignored.class);
				}
			}, packageName);
		} catch (UtilityException exception) {
			throw new ConfigurationException(exception, "can't find converter classes in package ", packageName);
		}

		ConfigurationElementFactory configurationElementFactory = configuration.getConfigurationElementFactory();

		for (Class<?> converterClass: converterClasses) {
			registerConverter(configurationElementFactory.instantiate(converterClass.getName(), Converter.class));
		}
	}

	public void registerConverter(Converter<?> converter) {
		logger.debug("registering converter ", converter);

		converters.add(converter);
	}

	public <S> Converter<? super S> getConverter(
			Class<S> sourceType, Class<?> targetType) throws ConverterException {
		targetType = ReflectionUtilities.wrapPrimitiveType(targetType);

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
			throw new ConverterException("no converter found that can convert values ",
				"from type ", sourceType.getName(), " to type ", targetType.getName());
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

	public <S, T> T convert(S value, Class<T> targetType) throws ConverterException {
		T convertedValue;

		if (value == null) {
			convertedValue = null;
		} else {
			targetType = Utilities.typed(ReflectionUtilities.wrapPrimitiveType(targetType));

			if (targetType.isInstance(value)) {
				convertedValue = targetType.cast(value);
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