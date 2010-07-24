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
package com.googlecode.aluminumproject.configuration;

import static com.googlecode.aluminumproject.configuration.DefaultConfiguration.CACHE_CLASS;
import static com.googlecode.aluminumproject.configuration.DefaultConfiguration.CONFIGURATION_ELEMENT_FACTORY_CLASS;
import static com.googlecode.aluminumproject.configuration.DefaultConfiguration.CONFIGURATION_ELEMENT_PACKAGES;
import static com.googlecode.aluminumproject.configuration.DefaultConfiguration.CONVERTER_REGISTRY_CLASS;
import static com.googlecode.aluminumproject.configuration.DefaultConfiguration.EXPRESSION_FACTORY_PACKAGES;
import static com.googlecode.aluminumproject.configuration.DefaultConfiguration.LIBRARY_PACKAGES;
import static com.googlecode.aluminumproject.configuration.DefaultConfiguration.PARSER_PACKAGES;
import static com.googlecode.aluminumproject.configuration.DefaultConfiguration.SERIALISER_PACKAGES;
import static com.googlecode.aluminumproject.configuration.DefaultConfiguration.TEMPLATE_ELEMENT_FACTORY_CLASS;
import static com.googlecode.aluminumproject.configuration.DefaultConfiguration.TEMPLATE_FINDER_FACTORY_CLASS;
import static com.googlecode.aluminumproject.configuration.DefaultConfiguration.TEMPLATE_STORE_FINDER_FACTORY_CLASS;
import static com.googlecode.aluminumproject.utilities.ReflectionUtilities.getPackageName;

import com.googlecode.aluminumproject.annotations.Named;
import com.googlecode.aluminumproject.cache.test.TestCache;
import com.googlecode.aluminumproject.configuration.test.TestConfigurationElementFactory;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.converters.DefaultConverterRegistry;
import com.googlecode.aluminumproject.converters.test.TestConverterRegistry;
import com.googlecode.aluminumproject.expressions.Expression;
import com.googlecode.aluminumproject.expressions.ExpressionException;
import com.googlecode.aluminumproject.expressions.ExpressionFactory;
import com.googlecode.aluminumproject.expressions.ExpressionOccurrence;
import com.googlecode.aluminumproject.expressions.test.IgnoredExpressionFactory;
import com.googlecode.aluminumproject.expressions.test.TestExpressionFactory;
import com.googlecode.aluminumproject.libraries.Library;
import com.googlecode.aluminumproject.libraries.LibraryException;
import com.googlecode.aluminumproject.libraries.LibraryInformation;
import com.googlecode.aluminumproject.libraries.actions.ActionContributionFactory;
import com.googlecode.aluminumproject.libraries.actions.ActionFactory;
import com.googlecode.aluminumproject.libraries.functions.FunctionFactory;
import com.googlecode.aluminumproject.libraries.test.IgnoredLibrary;
import com.googlecode.aluminumproject.libraries.test.TestLibrary;
import com.googlecode.aluminumproject.parsers.ParseException;
import com.googlecode.aluminumproject.parsers.Parser;
import com.googlecode.aluminumproject.parsers.test.IgnoredParser;
import com.googlecode.aluminumproject.parsers.test.TestParser;
import com.googlecode.aluminumproject.resources.ClassPathTemplateFinderFactory;
import com.googlecode.aluminumproject.resources.MemoryTemplateStoreFinderFactory;
import com.googlecode.aluminumproject.resources.TemplateStoreFinderFactory;
import com.googlecode.aluminumproject.resources.test.TestTemplateFinderFactory;
import com.googlecode.aluminumproject.resources.test.TestTemplateStoreFinderFactory;
import com.googlecode.aluminumproject.serialisers.SerialisationException;
import com.googlecode.aluminumproject.serialisers.Serialiser;
import com.googlecode.aluminumproject.serialisers.test.IgnoredSerialiser;
import com.googlecode.aluminumproject.serialisers.test.TestSerialiser;
import com.googlecode.aluminumproject.templates.DefaultTemplateElementFactory;
import com.googlecode.aluminumproject.templates.Template;
import com.googlecode.aluminumproject.templates.test.TestTemplateElementFactory;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

@SuppressWarnings("all")
@Test(groups = {"core", "fast"})
public class DefaultConfigurationTest {
	public void configurationElementFactoryShouldDefaultToDefaultConfigurationElementFactory() {
		ConfigurationElementFactory configurationElementFactory =
			new DefaultConfiguration().getConfigurationElementFactory();

		assert configurationElementFactory instanceof DefaultConfigurationElementFactory;
	}

	public void configurationElementFactoryShouldBeConfigurable() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(CONFIGURATION_ELEMENT_FACTORY_CLASS, TestConfigurationElementFactory.class.getName());

		ConfigurationElementFactory configurationElementFactory =
			new DefaultConfiguration(parameters).getConfigurationElementFactory();

		assert configurationElementFactory instanceof TestConfigurationElementFactory;
	}

	@Test(dependsOnMethods = "configurationElementFactoryShouldBeConfigurable")
	public void configurationShouldInitialiseConfigurationElementFactory() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(CONFIGURATION_ELEMENT_FACTORY_CLASS, TestConfigurationElementFactory.class.getName());

		Configuration configuration = new DefaultConfiguration(parameters);

		TestConfigurationElementFactory configurationElementFactory =
			(TestConfigurationElementFactory) configuration.getConfigurationElementFactory();
		assert configurationElementFactory.getConfiguration() == configuration;
	}

	public void converterRegistryShouldDefaultToDefaultConverterRegistry() {
		assert new DefaultConfiguration().getConverterRegistry() instanceof DefaultConverterRegistry;
	}

	public void converterRegistryShouldBeConfigurable() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(CONVERTER_REGISTRY_CLASS, TestConverterRegistry.class.getName());

		assert new DefaultConfiguration(parameters).getConverterRegistry() instanceof TestConverterRegistry;
	}

	@Test(dependsOnMethods = "converterRegistryShouldBeConfigurable")
	public void configurationShouldInitialiseConverterRegistry() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(CONVERTER_REGISTRY_CLASS, TestConverterRegistry.class.getName());

		Configuration configuration = new DefaultConfiguration(parameters);

		assert ((TestConverterRegistry) configuration.getConverterRegistry()).getConfiguration() == configuration;
	}

	public void templateElementFactoryShouldDefaultToDefaultTemplateElementFactory() {
		assert new DefaultConfiguration().getTemplateElementFactory() instanceof DefaultTemplateElementFactory;
	}

	public void templateElementFactoryShouldBeConfigurable() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(TEMPLATE_ELEMENT_FACTORY_CLASS, TestTemplateElementFactory.class.getName());

		assert new DefaultConfiguration(parameters).getTemplateElementFactory() instanceof TestTemplateElementFactory;
	}

	@Test(dependsOnMethods = "templateElementFactoryShouldBeConfigurable")
	public void configurationShouldInitialiseTemplateElementFactory() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(TEMPLATE_ELEMENT_FACTORY_CLASS, TestTemplateElementFactory.class.getName());

		Configuration configuration = new DefaultConfiguration(parameters);

		TestTemplateElementFactory templateElementFactory =
			(TestTemplateElementFactory) configuration.getTemplateElementFactory();
		assert templateElementFactory.getConfiguration() == configuration;
	}

	public void templateFinderFactoryShouldDefaultToClassPathTemplateFinderFactory() {
		assert new DefaultConfiguration().getTemplateFinderFactory() instanceof ClassPathTemplateFinderFactory;
	}

	public void templateFinderFactoryShouldBeConfigurable() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(TEMPLATE_FINDER_FACTORY_CLASS, TestTemplateFinderFactory.class.getName());

		assert new DefaultConfiguration(parameters).getTemplateFinderFactory() instanceof TestTemplateFinderFactory;
	}

	@Test(dependsOnMethods = "templateFinderFactoryShouldBeConfigurable")
	public void configurationShouldInitialiseTemplateFinderFactory() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(TEMPLATE_FINDER_FACTORY_CLASS, TestTemplateFinderFactory.class.getName());

		Configuration configuration = new DefaultConfiguration(parameters);

		TestTemplateFinderFactory templateFinderFactory =
			(TestTemplateFinderFactory) configuration.getTemplateFinderFactory();
		assert templateFinderFactory.getConfiguration() == configuration;
	}

	public void templateStoreFinderFactoryShouldDefaultToMemoryTemplateStoreFinderFactory() {
		assert new DefaultConfiguration().getTemplateStoreFinderFactory() instanceof MemoryTemplateStoreFinderFactory;
	}

	public void templateStoreFinderFactoryShouldBeConfigurable() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(TEMPLATE_STORE_FINDER_FACTORY_CLASS, TestTemplateStoreFinderFactory.class.getName());

		TemplateStoreFinderFactory templateStoreFinderFactory =
			new DefaultConfiguration(parameters).getTemplateStoreFinderFactory();

		assert templateStoreFinderFactory instanceof TestTemplateStoreFinderFactory;
	}

	@Test(dependsOnMethods = "templateStoreFinderFactoryShouldBeConfigurable")
	public void configurationShouldInitialiseTemplateStoreFinderFactory() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(TEMPLATE_STORE_FINDER_FACTORY_CLASS, TestTemplateStoreFinderFactory.class.getName());

		Configuration configuration = new DefaultConfiguration(parameters);

		TestTemplateStoreFinderFactory templateStoreFinderFactory =
			(TestTemplateStoreFinderFactory) configuration.getTemplateStoreFinderFactory();
		assert templateStoreFinderFactory.getConfiguration() == configuration;
	}

	public void cacheShouldDefaultToNull() {
		assert new DefaultConfiguration().getCache() == null;
	}

	public void cacheShouldBeConfigurable() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(CACHE_CLASS, TestCache.class.getName());

		assert new DefaultConfiguration(parameters).getCache() instanceof TestCache;
	}

	public void configurationShouldInitialiseCache() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(CACHE_CLASS, TestCache.class.getName());

		Configuration configuration = new DefaultConfiguration(parameters);

		assert ((TestCache) configuration.getCache()).getConfiguration() == configuration;
	}

	public void librariesShouldNotBeNull() {
		assert new DefaultConfiguration().getLibraries() != null;
	}

	public static class ExternalLibrary implements Library {
		public void initialise(Configuration configuration, ConfigurationParameters parameters) {}

		public LibraryInformation getInformation() {
			return new LibraryInformation("http://aluminumproject.googlecode.com/external", "test", "External library");
		}

		public List<ActionFactory> getActionFactories() {
			return Collections.emptyList();
		}

		public ActionFactory getDynamicActionFactory(String name) throws LibraryException {
			throw new LibraryException("the external library does not support dynamic actions");
		}

		public List<ActionContributionFactory> getActionContributionFactories() {
			return Collections.emptyList();
		}

		public ActionContributionFactory getDynamicActionContributionFactory(String name) throws LibraryException {
			throw new LibraryException("the external library does not support dynamic action contributions");
		}

		public List<FunctionFactory> getFunctionFactories() {
			return Collections.emptyList();
		}

		public FunctionFactory getDynamicFunctionFactory(String name) throws LibraryException {
			throw new LibraryException("the external library does not support dynamic functions");
		}
	}

	@Test(dependsOnMethods = "librariesShouldNotBeNull")
	public void librariesOutsideLibraryPackagesShouldNotBeFound() {
		assert findLibraryOfType(new DefaultConfiguration(), ExternalLibrary.class) == null;
	}

	@Test(dependsOnMethods = "librariesShouldNotBeNull")
	public void ignoredLibrariesShouldNotBeFound() {
		assert findLibraryOfType(new DefaultConfiguration(), IgnoredLibrary.class) == null;
	}

	@Test(dependsOnMethods = "librariesShouldNotBeNull")
	public void librariesInsideLibraryPackagesShouldBeFound() {
		assert findLibraryOfType(new DefaultConfiguration(), TestLibrary.class) != null;
	}

	@Test(dependsOnMethods = "librariesInsideLibraryPackagesShouldBeFound")
	public void configurationShouldInitialiseLibraries() {
		DefaultConfiguration configuration = new DefaultConfiguration();

		assert findLibraryOfType(configuration, TestLibrary.class).getConfiguration() == configuration;
	}

	@Test(dependsOnMethods = "librariesShouldNotBeNull")
	public void libraryPackagesShouldBeConfigurable() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(LIBRARY_PACKAGES, getPackageName(ExternalLibrary.class));

		assert findLibraryOfType(new DefaultConfiguration(parameters), ExternalLibrary.class) != null;
	}

	@Test(dependsOnMethods = "librariesShouldNotBeNull")
	public void libraryPackagesShouldBeExtendedByConfigurationElementPackages() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(CONFIGURATION_ELEMENT_PACKAGES, getPackageName(ExternalLibrary.class));
	}

	private static <T extends Library> T findLibraryOfType(Configuration configuration, Class<T> libraryType) {
		Iterator<Library> libraries = configuration.getLibraries().iterator();
		T libraryOfRequestedType = null;

		while (libraries.hasNext() && (libraryOfRequestedType == null)) {
			Library library = libraries.next();
			assert library != null;

			if (libraryType.isAssignableFrom(library.getClass())) {
				libraryOfRequestedType = libraryType.cast(library);
			}
		}

		return libraryOfRequestedType;
	}

	public void parsersShouldNotBeNull() {
		assert new DefaultConfiguration().getParsers() != null;
	}

	@Named("external")
	public static class ExternalParser implements Parser {
		public void initialise(Configuration configuration, ConfigurationParameters parameters) {}

		public Template parseTemplate(String name) throws ParseException {
			throw new ParseException("can't parse template '", name, "'");
		}
	}

	@Test(dependsOnMethods = "parsersShouldNotBeNull")
	public void parsersOutsideParserPackagesShouldNotBeFound() {
		assert findParserOfType(new DefaultConfiguration(), ExternalParser.class) == null;
	}

	@Test(dependsOnMethods = "parsersShouldNotBeNull")
	public void ignoredParsersShouldNotBeFound() {
		assert findParserOfType(new DefaultConfiguration(), IgnoredParser.class) == null;
	}

	@Test(dependsOnMethods = "parsersShouldNotBeNull")
	public void parsersInsideParserPackagesShouldBeFound() {
		assert findParserOfType(new DefaultConfiguration(), TestParser.class) != null;
	}

	@Test(dependsOnMethods = "parsersInsideParserPackagesShouldBeFound")
	public void parsersShouldBeNamedAfterTheirPackages() {
		Configuration configuration = new DefaultConfiguration();

		assert configuration.getParsers().containsKey("test");
		assert configuration.getParsers().get("test") == findParserOfType(configuration, TestParser.class);
	}

	@Test(dependsOnMethods = "parsersShouldNotBeNull")
	public void parserPackagesShouldBeConfigurable() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(PARSER_PACKAGES, getPackageName(ExternalParser.class));

		assert findParserOfType(new DefaultConfiguration(parameters), ExternalParser.class) != null;
	}

	@Test(dependsOnMethods = "parsersShouldNotBeNull")
	public void parserPackagesShouldBeExtendedByConfigurationElementPackages() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(CONFIGURATION_ELEMENT_PACKAGES, getPackageName(ExternalParser.class));

		assert findParserOfType(new DefaultConfiguration(parameters), ExternalParser.class) != null;
	}

	@Test(dependsOnMethods = "parserPackagesShouldBeConfigurable")
	public void parserNamesShouldBeOverridable() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(PARSER_PACKAGES, getPackageName(ExternalParser.class));

		Configuration configuration = new DefaultConfiguration(parameters);
		Map<String, Parser> parsers = configuration.getParsers();

		assert parsers.containsKey("external");
		assert parsers.get("external") == findParserOfType(configuration, ExternalParser.class);
	}

	@Test(dependsOnMethods = "parsersInsideParserPackagesShouldBeFound")
	public void configurationShouldInitialiseParsers() {
		DefaultConfiguration configuration = new DefaultConfiguration();

		assert findParserOfType(configuration, TestParser.class).getConfiguration() == configuration;
	}

	private static <T extends Parser> T findParserOfType(Configuration configuration, Class<T> parserType) {
		Iterator<Parser> parsers = configuration.getParsers().values().iterator();
		T parserOfRequestedType = null;

		while (parsers.hasNext() && (parserOfRequestedType == null)) {
			Parser parser = parsers.next();
			assert parser != null;

			if (parserType.isAssignableFrom(parser.getClass())) {
				parserOfRequestedType = parserType.cast(parser);
			}
		}

		return parserOfRequestedType;
	}

	public void serialisersShouldNotBeNull() {
		assert new DefaultConfiguration().getSerialisers() != null;
	}

	@Named("external")
	public static class ExternalSerialiser implements Serialiser {
		public void initialise(Configuration configuration, ConfigurationParameters parameters) {}

		public void serialiseTemplate(Template template, String name) throws SerialisationException {
			throw new SerialisationException("can't serialise template '", name, "'");
		}
	}

	@Test(dependsOnMethods = "serialisersShouldNotBeNull")
	public void serialisersOutsideSerialiserPackagesShouldNotBeFound() {
		assert findSerialiserOfType(new DefaultConfiguration(), ExternalSerialiser.class) == null;
	}

	@Test(dependsOnMethods = "serialisersShouldNotBeNull")
	public void ignoredSerialisersShouldNotBeFound() {
		assert findSerialiserOfType(new DefaultConfiguration(), IgnoredSerialiser.class) == null;
	}

	@Test(dependsOnMethods = "serialisersShouldNotBeNull")
	public void serialisersInsideSerialiserPackagesShouldBeFound() {
		assert findSerialiserOfType(new DefaultConfiguration(), TestSerialiser.class) != null;
	}

	@Test(dependsOnMethods = "serialisersInsideSerialiserPackagesShouldBeFound")
	public void serialisersShouldBeNamedAfterTheirPackages() {
		Configuration configuration = new DefaultConfiguration();

		assert configuration.getSerialisers().containsKey("test");
		assert configuration.getSerialisers().get("test") == findSerialiserOfType(configuration, TestSerialiser.class);
	}

	@Test(dependsOnMethods = "serialisersShouldNotBeNull")
	public void serialiserPackagesShouldBeConfigurable() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(SERIALISER_PACKAGES, getPackageName(ExternalSerialiser.class));

		assert findSerialiserOfType(new DefaultConfiguration(parameters), ExternalSerialiser.class) != null;
	}

	@Test(dependsOnMethods = "serialisersShouldNotBeNull")
	public void serialiserPackagesShouldBeExtendedByConfigurationElementPackages() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(CONFIGURATION_ELEMENT_PACKAGES, getPackageName(ExternalSerialiser.class));

		assert findSerialiserOfType(new DefaultConfiguration(parameters), ExternalSerialiser.class) != null;
	}

	@Test(dependsOnMethods = "serialiserPackagesShouldBeConfigurable")
	public void serialiserNamesShouldBeOverridable() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(SERIALISER_PACKAGES, getPackageName(ExternalSerialiser.class));

		Configuration configuration = new DefaultConfiguration(parameters);
		Map<String, Serialiser> serialisers = configuration.getSerialisers();

		assert serialisers.containsKey("external");
		assert serialisers.get("external") == findSerialiserOfType(configuration, ExternalSerialiser.class);
	}

	@Test(dependsOnMethods = "serialisersInsideSerialiserPackagesShouldBeFound")
	public void configurationShouldInitialiseSerialisers() {
		DefaultConfiguration configuration = new DefaultConfiguration();

		assert findSerialiserOfType(configuration, TestSerialiser.class).getConfiguration() == configuration;
	}

	private static <T extends Serialiser> T findSerialiserOfType(Configuration configuration, Class<T> serialiserType) {
		Iterator<Serialiser> serialisers = configuration.getSerialisers().values().iterator();
		T serialiserOfRequestedType = null;

		while (serialisers.hasNext() && (serialiserOfRequestedType == null)) {
			Serialiser serialiser = serialisers.next();
			assert serialiser != null;

			if (serialiserType.isAssignableFrom(serialiser.getClass())) {
				serialiserOfRequestedType = serialiserType.cast(serialiser);
			}
		}

		return serialiserOfRequestedType;
	}

	public void expressionFactoriesShouldNotBeNull() {
		assert new DefaultConfiguration().getExpressionFactories() != null;
	}

	public static class ExternalExpressionFactory implements ExpressionFactory {
		public void initialise(Configuration configuration, ConfigurationParameters parameters) {}

		public List<ExpressionOccurrence> findExpressions(String text) {
			return Collections.emptyList();
		}

		public Expression create(String value, Context context) throws ExpressionException {
			throw new ExpressionException("can't create expression");
		}
	}

	@Test(dependsOnMethods = "expressionFactoriesShouldNotBeNull")
	public void expressionFactoriesOutsideExpressionFactoryPackagesShouldNotBeFound() {
		assert findExpressionFactoryOfType(new DefaultConfiguration(), ExternalExpressionFactory.class) == null;
	}

	@Test(dependsOnMethods = "expressionFactoriesShouldNotBeNull")
	public void ignoredExpressionFactoriesShouldNotBeFound() {
		assert findExpressionFactoryOfType(new DefaultConfiguration(), IgnoredExpressionFactory.class) == null;
	}

	@Test(dependsOnMethods = "expressionFactoriesShouldNotBeNull")
	public void expressionFactoriesInsideExpressionFactoryPackagesShouldBeFound() {
		assert findExpressionFactoryOfType(new DefaultConfiguration(), TestExpressionFactory.class) != null;
	}

	@Test(dependsOnMethods = "expressionFactoriesInsideExpressionFactoryPackagesShouldBeFound")
	public void configurationShouldInitialiseExpressionFactories() {
		DefaultConfiguration configuration = new DefaultConfiguration();

		TestExpressionFactory expressionFactory =
			findExpressionFactoryOfType(configuration, TestExpressionFactory.class);
		assert expressionFactory.getConfiguration() == configuration;
	}

	@Test(dependsOnMethods = "expressionFactoriesShouldNotBeNull")
	public void expressionFactoryPackagesShouldBeConfigurable() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(EXPRESSION_FACTORY_PACKAGES, getPackageName(ExternalExpressionFactory.class));

		ExpressionFactory expressionFactory =
			findExpressionFactoryOfType(new DefaultConfiguration(parameters), ExternalExpressionFactory.class);
		assert expressionFactory != null;
	}

	@Test(dependsOnMethods = "expressionFactoriesShouldNotBeNull")
	public void expressionFactoryPackagesShouldBeExtendedByConfigurationElementPackages() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(CONFIGURATION_ELEMENT_PACKAGES, getPackageName(ExternalExpressionFactory.class));

		ExpressionFactory expressionFactory =
			findExpressionFactoryOfType(new DefaultConfiguration(parameters), ExternalExpressionFactory.class);
		assert expressionFactory != null;
	}

	private static <T extends ExpressionFactory> T findExpressionFactoryOfType(
			Configuration configuration, Class<T> expressionFactoryType) {
		Iterator<ExpressionFactory> expressionFactories = configuration.getExpressionFactories().iterator();
		T expressionFactoryOfRequestedType = null;

		while (expressionFactories.hasNext() && (expressionFactoryOfRequestedType == null)) {
			ExpressionFactory expressionFactory = expressionFactories.next();
			assert expressionFactory != null;

			if (expressionFactoryType.isAssignableFrom(expressionFactory.getClass())) {
				expressionFactoryOfRequestedType = expressionFactoryType.cast(expressionFactory);
			}
		}

		return expressionFactoryOfRequestedType;
	}
}