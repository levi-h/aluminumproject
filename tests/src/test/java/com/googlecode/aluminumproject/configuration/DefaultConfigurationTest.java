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
package com.googlecode.aluminumproject.configuration;

import static com.googlecode.aluminumproject.configuration.DefaultConfiguration.CACHE_CLASS;
import static com.googlecode.aluminumproject.configuration.DefaultConfiguration.CONFIGURATION_ELEMENT_FACTORY_CLASS;
import static com.googlecode.aluminumproject.configuration.DefaultConfiguration.CONFIGURATION_ELEMENT_PACKAGES;
import static com.googlecode.aluminumproject.configuration.DefaultConfiguration.CONTEXT_ENRICHER_PACKAGES;
import static com.googlecode.aluminumproject.configuration.DefaultConfiguration.CONVERTER_REGISTRY_CLASS;
import static com.googlecode.aluminumproject.configuration.DefaultConfiguration.EXPRESSION_FACTORY_PACKAGES;
import static com.googlecode.aluminumproject.configuration.DefaultConfiguration.LIBRARY_PACKAGES;
import static com.googlecode.aluminumproject.configuration.DefaultConfiguration.PARSER_PACKAGES;
import static com.googlecode.aluminumproject.configuration.DefaultConfiguration.SERIALISER_PACKAGES;
import static com.googlecode.aluminumproject.configuration.DefaultConfiguration.TEMPLATE_ELEMENT_FACTORY_CLASS;
import static com.googlecode.aluminumproject.configuration.DefaultConfiguration.TEMPLATE_FINDER_CLASS;
import static com.googlecode.aluminumproject.configuration.DefaultConfiguration.TEMPLATE_STORE_FINDER_CLASS;
import static com.googlecode.aluminumproject.configuration.DefaultConfiguration.TYPE_FINDER_CLASS;
import static com.googlecode.aluminumproject.utilities.ReflectionUtilities.getPackageName;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.annotations.Named;
import com.googlecode.aluminumproject.cache.TestCache;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.ContextEnricher;
import com.googlecode.aluminumproject.context.IgnoredContextEnricher;
import com.googlecode.aluminumproject.context.TestContextEnricher;
import com.googlecode.aluminumproject.converters.DefaultConverterRegistry;
import com.googlecode.aluminumproject.converters.TestConverterRegistry;
import com.googlecode.aluminumproject.expressions.Expression;
import com.googlecode.aluminumproject.expressions.ExpressionFactory;
import com.googlecode.aluminumproject.expressions.ExpressionOccurrence;
import com.googlecode.aluminumproject.expressions.IgnoredExpressionFactory;
import com.googlecode.aluminumproject.expressions.TestExpressionFactory;
import com.googlecode.aluminumproject.finders.ClassPathTemplateFinder;
import com.googlecode.aluminumproject.finders.DefaultTypeFinder;
import com.googlecode.aluminumproject.finders.InMemoryTemplateStoreFinder;
import com.googlecode.aluminumproject.finders.TestTemplateFinder;
import com.googlecode.aluminumproject.finders.TestTemplateStoreFinder;
import com.googlecode.aluminumproject.finders.TestTypeFinder;
import com.googlecode.aluminumproject.libraries.IgnoredLibrary;
import com.googlecode.aluminumproject.libraries.Library;
import com.googlecode.aluminumproject.libraries.LibraryInformation;
import com.googlecode.aluminumproject.libraries.TestLibrary;
import com.googlecode.aluminumproject.libraries.actions.ActionContributionFactory;
import com.googlecode.aluminumproject.libraries.actions.ActionFactory;
import com.googlecode.aluminumproject.libraries.functions.FunctionFactory;
import com.googlecode.aluminumproject.parsers.IgnoredParser;
import com.googlecode.aluminumproject.parsers.Parser;
import com.googlecode.aluminumproject.parsers.TestParser;
import com.googlecode.aluminumproject.serialisers.IgnoredSerialiser;
import com.googlecode.aluminumproject.serialisers.Serialiser;
import com.googlecode.aluminumproject.serialisers.TestSerialiser;
import com.googlecode.aluminumproject.templates.DefaultTemplateElementFactory;
import com.googlecode.aluminumproject.templates.Template;
import com.googlecode.aluminumproject.templates.TestTemplateElementFactory;
import com.googlecode.aluminumproject.utilities.ReflectionUtilities;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
@Test(groups = {"core", "fast"})
public class DefaultConfigurationTest {
	private Configuration configuration;

	@AfterMethod
	public void closeConfiguration() {
		if (configuration != null) {
			configuration.close();

			configuration = null;
		}
	}

	public void typeFinderShouldDefaultToDefaultTypeFinder() {
		configuration = new DefaultConfiguration();

		assert configuration.getTypeFinder() instanceof DefaultTypeFinder;
	}

	public void typeFinderShouldBeConfigurable() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(TYPE_FINDER_CLASS, TestTypeFinder.class.getName());

		configuration = new DefaultConfiguration(parameters);

		assert configuration.getTypeFinder() instanceof TestTypeFinder;
	}

	@Test(dependsOnMethods = "typeFinderShouldBeConfigurable")
	public void configurationShoulInitialiseTypeFinder() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(TYPE_FINDER_CLASS, TestTypeFinder.class.getName());

		configuration = new DefaultConfiguration(parameters);

		assert ((TestTypeFinder) configuration.getTypeFinder()).getConfiguration() == configuration;
	}

	@Test(dependsOnMethods = "configurationShoulInitialiseTypeFinder")
	public void configurationShouldDisableTypeFinderWhenItIsClosed() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(TYPE_FINDER_CLASS, TestTypeFinder.class.getName());

		Configuration configuration = new DefaultConfiguration(parameters);

		TestTypeFinder typeFinder = (TestTypeFinder) configuration.getTypeFinder();

		configuration.close();

		assert typeFinder.getConfiguration() == null;
	}

	public void configurationElementFactoryShouldDefaultToDefaultConfigurationElementFactory() {
		configuration = new DefaultConfiguration();

		assert configuration.getConfigurationElementFactory() instanceof DefaultConfigurationElementFactory;
	}

	public void configurationElementFactoryShouldBeConfigurable() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(CONFIGURATION_ELEMENT_FACTORY_CLASS, TestConfigurationElementFactory.class.getName());

		configuration = new DefaultConfiguration(parameters);

		assert configuration.getConfigurationElementFactory() instanceof TestConfigurationElementFactory;
	}

	@Test(dependsOnMethods = "configurationElementFactoryShouldBeConfigurable")
	public void configurationShouldInitialiseConfigurationElementFactory() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(CONFIGURATION_ELEMENT_FACTORY_CLASS, TestConfigurationElementFactory.class.getName());

		configuration = new DefaultConfiguration(parameters);

		TestConfigurationElementFactory configurationElementFactory =
			(TestConfigurationElementFactory) configuration.getConfigurationElementFactory();
		assert configurationElementFactory.getConfiguration() == configuration;
	}

	@Test(dependsOnMethods = "configurationShouldInitialiseConfigurationElementFactory")
	public void configurationShouldDisableConfigurationElementFactoryWhenItIsClosed() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(CONFIGURATION_ELEMENT_FACTORY_CLASS, TestConfigurationElementFactory.class.getName());

		Configuration configuration = new DefaultConfiguration(parameters);

		TestConfigurationElementFactory configurationElementFactory =
			(TestConfigurationElementFactory) configuration.getConfigurationElementFactory();

		configuration.close();

		assert configurationElementFactory.getConfiguration() == null;
	}

	public void converterRegistryShouldDefaultToDefaultConverterRegistry() {
		configuration = new DefaultConfiguration();

		assert configuration.getConverterRegistry() instanceof DefaultConverterRegistry;
	}

	public void converterRegistryShouldBeConfigurable() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(CONVERTER_REGISTRY_CLASS, TestConverterRegistry.class.getName());

		configuration = new DefaultConfiguration(parameters);

		assert configuration.getConverterRegistry() instanceof TestConverterRegistry;
	}

	@Test(dependsOnMethods = "converterRegistryShouldBeConfigurable")
	public void configurationShouldInitialiseConverterRegistry() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(CONVERTER_REGISTRY_CLASS, TestConverterRegistry.class.getName());

		configuration = new DefaultConfiguration(parameters);

		assert ((TestConverterRegistry) configuration.getConverterRegistry()).getConfiguration() == configuration;
	}

	@Test(dependsOnMethods = "configurationShouldInitialiseConverterRegistry")
	public void configurationShouldDisableConverterRegistryWhenItIsClosed() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(CONVERTER_REGISTRY_CLASS, TestConverterRegistry.class.getName());

		Configuration configuration = new DefaultConfiguration(parameters);

		TestConverterRegistry converterRegistry = (TestConverterRegistry) configuration.getConverterRegistry();

		configuration.close();

		assert converterRegistry.getConfiguration() == null;
	}

	public void templateElementFactoryShouldDefaultToDefaultTemplateElementFactory() {
		configuration = new DefaultConfiguration();

		assert configuration.getTemplateElementFactory() instanceof DefaultTemplateElementFactory;
	}

	public void templateElementFactoryShouldBeConfigurable() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(TEMPLATE_ELEMENT_FACTORY_CLASS, TestTemplateElementFactory.class.getName());

		configuration = new DefaultConfiguration(parameters);

		assert configuration.getTemplateElementFactory() instanceof TestTemplateElementFactory;
	}

	@Test(dependsOnMethods = "templateElementFactoryShouldBeConfigurable")
	public void configurationShouldInitialiseTemplateElementFactory() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(TEMPLATE_ELEMENT_FACTORY_CLASS, TestTemplateElementFactory.class.getName());

		configuration = new DefaultConfiguration(parameters);

		TestTemplateElementFactory templateElementFactory =
			(TestTemplateElementFactory) configuration.getTemplateElementFactory();
		assert templateElementFactory.getConfiguration() == configuration;
	}

	@Test(dependsOnMethods = "configurationShouldInitialiseTemplateElementFactory")
	public void configurationShouldDisableTemplateElementFactoryWhenItIsClosed() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(TEMPLATE_ELEMENT_FACTORY_CLASS, TestTemplateElementFactory.class.getName());

		Configuration configuration = new DefaultConfiguration(parameters);

		TestTemplateElementFactory templateElementFactory =
			(TestTemplateElementFactory) configuration.getTemplateElementFactory();

		configuration.close();

		assert templateElementFactory.getConfiguration() == null;
	}

	public void templateFinderShouldDefaultToClassPathTemplateFinder() {
		configuration = new DefaultConfiguration();

		assert configuration.getTemplateFinder() instanceof ClassPathTemplateFinder;
	}

	public void templateFinderShouldBeConfigurable() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(TEMPLATE_FINDER_CLASS, TestTemplateFinder.class.getName());

		configuration = new DefaultConfiguration(parameters);

		assert configuration.getTemplateFinder() instanceof TestTemplateFinder;
	}

	@Test(dependsOnMethods = "templateFinderShouldBeConfigurable")
	public void configurationShouldInitialiseTemplateFinder() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(TEMPLATE_FINDER_CLASS, TestTemplateFinder.class.getName());

		configuration = new DefaultConfiguration(parameters);

		assert ((TestTemplateFinder) configuration.getTemplateFinder()).getConfiguration() == configuration;
	}

	@Test(dependsOnMethods = "configurationShouldInitialiseTemplateFinder")
	public void configurationShouldDisableTemplateFinderWhenItIsClosed() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(TEMPLATE_FINDER_CLASS, TestTemplateFinder.class.getName());

		Configuration configuration = new DefaultConfiguration(parameters);

		TestTemplateFinder templateFinder = (TestTemplateFinder) configuration.getTemplateFinder();

		configuration.close();

		assert templateFinder.getConfiguration() == null;
	}

	public void templateStoreFinderShouldDefaultToInMemoryTemplateStoreFinder() {
		configuration = new DefaultConfiguration();

		assert configuration.getTemplateStoreFinder() instanceof InMemoryTemplateStoreFinder;
	}

	public void templateStoreFinderShouldBeConfigurable() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(TEMPLATE_STORE_FINDER_CLASS, TestTemplateStoreFinder.class.getName());

		configuration = new DefaultConfiguration(parameters);

		assert configuration.getTemplateStoreFinder() instanceof TestTemplateStoreFinder;
	}

	@Test(dependsOnMethods = "templateStoreFinderShouldBeConfigurable")
	public void configurationShouldInitialiseTemplateStoreFinder() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(TEMPLATE_STORE_FINDER_CLASS, TestTemplateStoreFinder.class.getName());

		configuration = new DefaultConfiguration(parameters);

		assert ((TestTemplateStoreFinder) configuration.getTemplateStoreFinder()).getConfiguration() == configuration;
	}

	@Test(dependsOnMethods = "configurationShouldInitialiseTemplateStoreFinder")
	public void configurationShouldDisableTemplateStoreFinderWhenItIsClosed() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(TEMPLATE_STORE_FINDER_CLASS, TestTemplateStoreFinder.class.getName());

		Configuration configuration = new DefaultConfiguration(parameters);

		TestTemplateStoreFinder templateStoreFinder = (TestTemplateStoreFinder) configuration.getTemplateStoreFinder();

		configuration.close();

		assert templateStoreFinder.getConfiguration() == null;
	}

	public void cacheShouldDefaultToNull() {
		configuration = new DefaultConfiguration();

		assert configuration.getCache() == null;
	}

	public void cacheShouldBeConfigurable() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(CACHE_CLASS, TestCache.class.getName());

		configuration = new DefaultConfiguration(parameters);

		assert configuration.getCache() instanceof TestCache;
	}

	public void configurationShouldInitialiseCache() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(CACHE_CLASS, TestCache.class.getName());

		configuration = new DefaultConfiguration(parameters);

		assert ((TestCache) configuration.getCache()).getConfiguration() == configuration;
	}

	@Test(dependsOnMethods = "configurationShouldInitialiseCache")
	public void configurationShouldDisableCacheWhenItIsClosed() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(CACHE_CLASS, TestCache.class.getName());

		Configuration configuration = new DefaultConfiguration(parameters);

		TestCache cache = (TestCache) configuration.getCache();

		configuration.close();

		assert cache.getConfiguration() == null;
	}

	public void librariesShouldNotBeNull() {
		configuration = new DefaultConfiguration();

		assert configuration.getLibraries() != null;
	}

	public static class ExternalLibrary implements Library {
		public void initialise(Configuration configuration) {}

		public void disable() {}

		public LibraryInformation getInformation() {
			return new LibraryInformation("http://aluminumproject.googlecode.com/external", "ext", "test");
		}

		public List<ActionFactory> getActionFactories() {
			return Collections.emptyList();
		}

		public ActionFactory getDynamicActionFactory(String name) throws AluminumException {
			throw new AluminumException("the external library does not support dynamic actions");
		}

		public List<ActionContributionFactory> getActionContributionFactories() {
			return Collections.emptyList();
		}

		public ActionContributionFactory getDynamicActionContributionFactory(String name) throws AluminumException {
			throw new AluminumException("the external library does not support dynamic action contributions");
		}

		public List<FunctionFactory> getFunctionFactories() {
			return Collections.emptyList();
		}

		public FunctionFactory getDynamicFunctionFactory(String name) throws AluminumException {
			throw new AluminumException("the external library does not support dynamic functions");
		}
	}

	@Test(dependsOnMethods = "librariesShouldNotBeNull")
	public void librariesOutsideLibraryPackagesShouldNotBeFound() {
		configuration = new DefaultConfiguration();

		assert findLibraryOfType(configuration, ExternalLibrary.class) == null;
	}

	@Test(dependsOnMethods = "librariesShouldNotBeNull")
	public void ignoredLibrariesShouldNotBeFound() {
		configuration = new DefaultConfiguration();

		assert findLibraryOfType(configuration, IgnoredLibrary.class) == null;
	}

	@Test(dependsOnMethods = "librariesShouldNotBeNull")
	public void librariesInsideLibraryPackagesShouldBeFound() {
		configuration = new DefaultConfiguration();

		assert findLibraryOfType(configuration, TestLibrary.class) != null;
	}

	@Test(dependsOnMethods = "librariesInsideLibraryPackagesShouldBeFound")
	public void configurationShouldInitialiseLibraries() {
		configuration = new DefaultConfiguration();

		assert findLibraryOfType(configuration, TestLibrary.class).getConfiguration() == configuration;
	}

	@Test(dependsOnMethods = "configurationShouldInitialiseLibraries")
	public void configurationShouldDisableLibrariesWhenItIsClosed() {
		Configuration configuration = new DefaultConfiguration();

		TestLibrary library = findLibraryOfType(configuration, TestLibrary.class);

		configuration.close();

		assert library.getConfiguration() == null;
	}

	@Test(dependsOnMethods = "librariesShouldNotBeNull")
	public void libraryPackagesShouldBeConfigurable() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(LIBRARY_PACKAGES, getPackageName(ExternalLibrary.class));

		configuration = new DefaultConfiguration(parameters);

		assert findLibraryOfType(configuration, ExternalLibrary.class) != null;
	}

	@Test(dependsOnMethods = "librariesShouldNotBeNull")
	public void libraryPackagesShouldBeExtendedByConfigurationElementPackages() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(CONFIGURATION_ELEMENT_PACKAGES, getPackageName(ExternalLibrary.class));

		configuration = new DefaultConfiguration(parameters);

		assert findLibraryOfType(configuration, ExternalLibrary.class) != null;
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
		configuration = new DefaultConfiguration();

		assert configuration.getParsers() != null;
	}

	@Named("external")
	public static class ExternalParser implements Parser {
		public void initialise(Configuration configuration) {}

		public void disable() {}

		public Template parseTemplate(String name) throws AluminumException {
			throw new AluminumException("can't parse template '", name, "'");
		}
	}

	@Test(dependsOnMethods = "parsersShouldNotBeNull")
	public void parsersOutsideParserPackagesShouldNotBeFound() {
		configuration = new DefaultConfiguration();

		assert findParserOfType(configuration, ExternalParser.class) == null;
	}

	@Test(dependsOnMethods = "parsersShouldNotBeNull")
	public void ignoredParsersShouldNotBeFound() {
		configuration = new DefaultConfiguration();

		assert findParserOfType(configuration, IgnoredParser.class) == null;
	}

	@Test(dependsOnMethods = "parsersShouldNotBeNull")
	public void parsersInsideParserPackagesShouldBeFound() {
		configuration = new DefaultConfiguration();

		assert findParserOfType(configuration, TestParser.class) != null;
	}

	@Test(dependsOnMethods = "parsersInsideParserPackagesShouldBeFound")
	public void parsersShouldBeNamedAfterTheirPackages() {
		configuration = new DefaultConfiguration();

		Map<String, Parser> parsers = configuration.getParsers();
		assert parsers.containsKey("parsers");
		assert parsers.get("parsers") == findParserOfType(configuration, TestParser.class);
	}

	@Test(dependsOnMethods = "parsersShouldNotBeNull")
	public void parserPackagesShouldBeConfigurable() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(PARSER_PACKAGES, getPackageName(ExternalParser.class));

		configuration = new DefaultConfiguration(parameters);

		assert findParserOfType(configuration, ExternalParser.class) != null;
	}

	@Test(dependsOnMethods = "parsersShouldNotBeNull")
	public void parserPackagesShouldBeExtendedByConfigurationElementPackages() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(CONFIGURATION_ELEMENT_PACKAGES, getPackageName(ExternalParser.class));

		configuration = new DefaultConfiguration(parameters);

		assert findParserOfType(configuration, ExternalParser.class) != null;
	}

	@Test(dependsOnMethods = "parserPackagesShouldBeConfigurable")
	public void parserNamesShouldBeOverridable() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(PARSER_PACKAGES, getPackageName(ExternalParser.class));

		configuration = new DefaultConfiguration(parameters);
		Map<String, Parser> parsers = configuration.getParsers();

		assert parsers.containsKey("external");
		assert parsers.get("external") == findParserOfType(configuration, ExternalParser.class);
	}

	@Test(dependsOnMethods = "parsersInsideParserPackagesShouldBeFound")
	public void configurationShouldInitialiseParsers() {
		configuration = new DefaultConfiguration();

		assert findParserOfType(configuration, TestParser.class).getConfiguration() == configuration;
	}

	@Test(dependsOnMethods = "parsersInsideParserPackagesShouldBeFound")
	public void configurationShouldDisableParsersWhenItIsClosed() {
		Configuration configuration = new DefaultConfiguration();

		TestParser parser = findParserOfType(configuration, TestParser.class);

		configuration.close();

		assert parser.getConfiguration() == null;
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
		configuration = new DefaultConfiguration();

		assert configuration.getSerialisers() != null;
	}

	@Named("external")
	public static class ExternalSerialiser implements Serialiser {
		public void initialise(Configuration configuration) {}

		public void disable() {}

		public void serialiseTemplate(Template template, String name) throws AluminumException {
			throw new AluminumException("can't serialise template '", name, "'");
		}
	}

	@Test(dependsOnMethods = "serialisersShouldNotBeNull")
	public void serialisersOutsideSerialiserPackagesShouldNotBeFound() {
		configuration = new DefaultConfiguration();

		assert findSerialiserOfType(configuration, ExternalSerialiser.class) == null;
	}

	@Test(dependsOnMethods = "serialisersShouldNotBeNull")
	public void ignoredSerialisersShouldNotBeFound() {
		configuration = new DefaultConfiguration();

		assert findSerialiserOfType(configuration, IgnoredSerialiser.class) == null;
	}

	@Test(dependsOnMethods = "serialisersShouldNotBeNull")
	public void serialisersInsideSerialiserPackagesShouldBeFound() {
		configuration = new DefaultConfiguration();

		assert findSerialiserOfType(configuration, TestSerialiser.class) != null;
	}

	@Test(dependsOnMethods = "serialisersInsideSerialiserPackagesShouldBeFound")
	public void serialisersShouldBeNamedAfterTheirPackages() {
		configuration = new DefaultConfiguration();

		Map<String, Serialiser> serialisers = configuration.getSerialisers();
		assert serialisers.containsKey("serialisers");
		assert serialisers.get("serialisers") == findSerialiserOfType(configuration, TestSerialiser.class);
	}

	@Test(dependsOnMethods = "serialisersShouldNotBeNull")
	public void serialiserPackagesShouldBeConfigurable() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(SERIALISER_PACKAGES, getPackageName(ExternalSerialiser.class));

		configuration = new DefaultConfiguration(parameters);

		assert findSerialiserOfType(configuration, ExternalSerialiser.class) != null;
	}

	@Test(dependsOnMethods = "serialisersShouldNotBeNull")
	public void serialiserPackagesShouldBeExtendedByConfigurationElementPackages() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(CONFIGURATION_ELEMENT_PACKAGES, getPackageName(ExternalSerialiser.class));

		configuration = new DefaultConfiguration(parameters);

		assert findSerialiserOfType(configuration, ExternalSerialiser.class) != null;
	}

	@Test(dependsOnMethods = "serialiserPackagesShouldBeConfigurable")
	public void serialiserNamesShouldBeOverridable() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(SERIALISER_PACKAGES, getPackageName(ExternalSerialiser.class));

		configuration = new DefaultConfiguration(parameters);

		Map<String, Serialiser> serialisers = configuration.getSerialisers();
		assert serialisers.containsKey("external");
		assert serialisers.get("external") == findSerialiserOfType(configuration, ExternalSerialiser.class);
	}

	@Test(dependsOnMethods = "serialisersInsideSerialiserPackagesShouldBeFound")
	public void configurationShouldInitialiseSerialisers() {
		configuration = new DefaultConfiguration();

		assert findSerialiserOfType(configuration, TestSerialiser.class).getConfiguration() == configuration;
	}

	@Test(dependsOnMethods = "configurationShouldInitialiseSerialisers")
	public void configurationShouldDisableSerialisersWhenItIsClosed() {
		Configuration configuration = new DefaultConfiguration();

		TestSerialiser serialiser = findSerialiserOfType(configuration, TestSerialiser.class);

		configuration.close();

		assert serialiser.getConfiguration() == null;
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

	public void contextEnrichersShouldNotBeNull() {
		configuration = new DefaultConfiguration();

		assert configuration.getContextEnrichers() != null;
	}

	public static class ExternalContextEnricher implements ContextEnricher {
		public void initialise(Configuration configuration) {}

		public void disable() {}

		public void beforeTemplate(Context context) {}

		public void afterTemplate(Context context) {}
	}

	@Test(dependsOnMethods = "contextEnrichersShouldNotBeNull")
	public void contextEnrichersOutsideContextEnricherPackagesShouldNotBeFound() {
		configuration = new DefaultConfiguration();

		assert findContextEnricherOfType(configuration, ExternalContextEnricher.class) == null;
	}

	@Test(dependsOnMethods = "contextEnrichersShouldNotBeNull")
	public void ignoredContextEnrichersShouldNotBeFound() {
		configuration = new DefaultConfiguration();

		assert findContextEnricherOfType(configuration, IgnoredContextEnricher.class) == null;
	}

	@Test(dependsOnMethods = "contextEnrichersShouldNotBeNull")
	public void contextEnrichersInsideContextEnricherPackagesShouldBeFound() {
		configuration = new DefaultConfiguration();

		assert findContextEnricherOfType(configuration, TestContextEnricher.class) != null;
	}

	@Test(dependsOnMethods = "contextEnrichersInsideContextEnricherPackagesShouldBeFound")
	public void configurationShouldInitialiseContextEnrichers() {
		configuration = new DefaultConfiguration();

		assert findContextEnricherOfType(configuration, TestContextEnricher.class).getConfiguration() == configuration;
	}

	@Test(dependsOnMethods = "configurationShouldInitialiseContextEnrichers")
	public void configurationShouldDisableContextEnrichersWhenItIsClosed() {
		Configuration configuration = new DefaultConfiguration();

		TestContextEnricher contextEnricher = findContextEnricherOfType(configuration, TestContextEnricher.class);

		configuration.close();

		assert contextEnricher.getConfiguration() == null;
	}

	@Test(dependsOnMethods = "contextEnrichersShouldNotBeNull")
	public void contextEnricherPackagesShouldBeConfigurable() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(CONTEXT_ENRICHER_PACKAGES,
			ReflectionUtilities.getPackageName(ExternalContextEnricher.class));

		configuration = new DefaultConfiguration(parameters);

		assert findContextEnricherOfType(configuration, ExternalContextEnricher.class) != null;
	}

	@Test(dependsOnMethods = "contextEnrichersShouldNotBeNull")
	public void contextEnricherPackagesShouldBeExtendedByConfigurationElementPackages() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(CONFIGURATION_ELEMENT_PACKAGES,
			ReflectionUtilities.getPackageName(ExternalContextEnricher.class));

		configuration = new DefaultConfiguration(parameters);

		assert findContextEnricherOfType(configuration, ExternalContextEnricher.class) != null;
	}

	private static <T extends ContextEnricher> T findContextEnricherOfType(
			Configuration configuration, Class<T> contextEnricherType) {
		Iterator<ContextEnricher> contextEnrichers = configuration.getContextEnrichers().iterator();
		T contextEnricherOfRequestedType = null;

		while (contextEnrichers.hasNext() && (contextEnricherOfRequestedType == null)) {
			ContextEnricher contextEnricher = contextEnrichers.next();
			assert contextEnricher != null;

			if (contextEnricherType.isAssignableFrom(contextEnricher.getClass())) {
				contextEnricherOfRequestedType = contextEnricherType.cast(contextEnricher);
			}
		}

		return contextEnricherOfRequestedType;
	}

	public void expressionFactoriesShouldNotBeNull() {
		configuration = new DefaultConfiguration();

		assert configuration.getExpressionFactories() != null;
	}

	public static class ExternalExpressionFactory implements ExpressionFactory {
		public void initialise(Configuration configuration) {}

		public void disable() {}

		public List<ExpressionOccurrence> findExpressions(String text) {
			return Collections.emptyList();
		}

		public Expression create(String value, Context context) throws AluminumException {
			throw new AluminumException("can't create expression");
		}
	}

	@Test(dependsOnMethods = "expressionFactoriesShouldNotBeNull")
	public void expressionFactoriesOutsideExpressionFactoryPackagesShouldNotBeFound() {
		configuration = new DefaultConfiguration();

		assert findExpressionFactoryOfType(configuration, ExternalExpressionFactory.class) == null;
	}

	@Test(dependsOnMethods = "expressionFactoriesShouldNotBeNull")
	public void ignoredExpressionFactoriesShouldNotBeFound() {
		configuration = new DefaultConfiguration();

		assert findExpressionFactoryOfType(configuration, IgnoredExpressionFactory.class) == null;
	}

	@Test(dependsOnMethods = "expressionFactoriesShouldNotBeNull")
	public void expressionFactoriesInsideExpressionFactoryPackagesShouldBeFound() {
		configuration = new DefaultConfiguration();

		assert findExpressionFactoryOfType(configuration, TestExpressionFactory.class) != null;
	}

	@Test(dependsOnMethods = "expressionFactoriesInsideExpressionFactoryPackagesShouldBeFound")
	public void configurationShouldInitialiseExpressionFactories() {
		configuration = new DefaultConfiguration();

		TestExpressionFactory expressionFactory =
			findExpressionFactoryOfType(configuration, TestExpressionFactory.class);
		assert expressionFactory.getConfiguration() == configuration;
	}

	@Test(dependsOnMethods = "configurationShouldInitialiseExpressionFactories")
	public void configurationShouldDisableExpressionFactoriesWhenItIsClosed() {
		Configuration configuration = new DefaultConfiguration();

		TestExpressionFactory expressionFactory =
			findExpressionFactoryOfType(configuration, TestExpressionFactory.class);

		configuration.close();

		assert expressionFactory.getConfiguration() == null;
	}

	@Test(dependsOnMethods = "expressionFactoriesShouldNotBeNull")
	public void expressionFactoryPackagesShouldBeConfigurable() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(EXPRESSION_FACTORY_PACKAGES, getPackageName(ExternalExpressionFactory.class));

		configuration = new DefaultConfiguration(parameters);

		assert findExpressionFactoryOfType(configuration, ExternalExpressionFactory.class) != null;
	}

	@Test(dependsOnMethods = "expressionFactoriesShouldNotBeNull")
	public void expressionFactoryPackagesShouldBeExtendedByConfigurationElementPackages() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(CONFIGURATION_ELEMENT_PACKAGES, getPackageName(ExternalExpressionFactory.class));

		configuration = new DefaultConfiguration(parameters);

		assert findExpressionFactoryOfType(configuration, ExternalExpressionFactory.class) != null;
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

	@Test(expectedExceptions = AluminumException.class)
	public void closingAlreadyClosedConfigurationShouldCauseException() {
		Configuration configuration = new DefaultConfiguration();
		configuration.close();
		configuration.close();
	}

	@Test(expectedExceptions = AluminumException.class)
	public void tryingToObtainConfigurationElementAfterClosingConfigurationShouldCauseException() {
		Configuration configuration = new DefaultConfiguration();
		configuration.close();

		configuration.getCache();
	}
}