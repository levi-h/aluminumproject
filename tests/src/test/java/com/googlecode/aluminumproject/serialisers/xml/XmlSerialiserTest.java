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
package com.googlecode.aluminumproject.serialisers.xml;

import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.configuration.ConfigurationParameters;
import com.googlecode.aluminumproject.configuration.TestConfiguration;
import com.googlecode.aluminumproject.converters.ConverterRegistry;
import com.googlecode.aluminumproject.converters.DefaultConverterRegistry;
import com.googlecode.aluminumproject.expressions.ExpressionFactory;
import com.googlecode.aluminumproject.expressions.TestExpressionFactory;
import com.googlecode.aluminumproject.libraries.Library;
import com.googlecode.aluminumproject.libraries.TestLibrary;
import com.googlecode.aluminumproject.libraries.actions.ActionParameter;
import com.googlecode.aluminumproject.libraries.actions.ConstantActionParameter;
import com.googlecode.aluminumproject.libraries.actions.ExpressionActionParameter;
import com.googlecode.aluminumproject.libraries.core.CoreLibrary;
import com.googlecode.aluminumproject.resources.MemoryTemplateStoreFinderFactory;
import com.googlecode.aluminumproject.resources.TemplateStoreFinderFactory;
import com.googlecode.aluminumproject.serialisers.ElementNameTranslator;
import com.googlecode.aluminumproject.serialisers.Serialiser;
import com.googlecode.aluminumproject.templates.ActionContributionDescriptor;
import com.googlecode.aluminumproject.templates.ActionDescriptor;
import com.googlecode.aluminumproject.templates.DefaultTemplateElementFactory;
import com.googlecode.aluminumproject.templates.TemplateBuilder;
import com.googlecode.aluminumproject.templates.TemplateElementFactory;
import com.googlecode.aluminumproject.utilities.resources.MemoryResourceStoreFinder;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("all")
@Test(groups = {"serialisers", "serialisers-xml", "fast"})
public class XmlSerialiserTest {
	private TemplateBuilder templateBuilder;

	private Configuration configuration;

	@BeforeMethod
	public void createTemplateBuilder() {
		templateBuilder = new TemplateBuilder();
	}

	public void emptyTemplateShouldResultInEmptyDocument() {
		createConfiguration();

		assert getTemplateText().equals("");
	}

	public void actionElementWithoutChildrenShouldResultInCombinedOpenAndCloseTag() {
		createConfiguration();

		ActionDescriptor actionDescriptor = new ActionDescriptor("test", "test");
		Map<String, ActionParameter> parameters = Collections.emptyMap();
		List<ActionContributionDescriptor> contributionDescriptors = Collections.emptyList();
		Map<String, String> libraryUrlAbbreviations =
			Collections.singletonMap("test", "http://aluminumproject.googlecode.com/test");

		templateBuilder.addTemplateElement(configuration.getTemplateElementFactory().createActionElement(
			actionDescriptor, parameters, contributionDescriptors, libraryUrlAbbreviations));
		templateBuilder.restoreCurrentTemplateElement();

		assert getTemplateText().equals("<test:test xmlns:test=\"http://aluminumproject.googlecode.com/test\"/>");
	}

	@Test(dependsOnMethods = "textElementShouldResultInText")
	public void actionElementWithChildrenShouldResultInSeparateOpenAndCloseTags() {
		createConfiguration();

		ActionDescriptor actionDescriptor = new ActionDescriptor("test", "test");
		Map<String, ActionParameter> parameters = Collections.emptyMap();
		List<ActionContributionDescriptor> contributionDescriptors = Collections.emptyList();
		Map<String, String> libraryUrlAbbreviations =
			Collections.singletonMap("test", "http://aluminumproject.googlecode.com/test");

		templateBuilder.addTemplateElement(configuration.getTemplateElementFactory().createActionElement(
			actionDescriptor, parameters, contributionDescriptors, libraryUrlAbbreviations));
		templateBuilder.addTemplateElement(configuration.getTemplateElementFactory().createTextElement(
			"text", Collections.<String, String>emptyMap()));
		templateBuilder.restoreCurrentTemplateElement();
		templateBuilder.restoreCurrentTemplateElement();

		assert getTemplateText().equals(
			"<test:test xmlns:test=\"http://aluminumproject.googlecode.com/test\">text</test:test>");
	}

	@Test(dependsOnMethods = {
		"actionElementWithoutChildrenShouldResultInCombinedOpenAndCloseTag",
		"actionElementWithChildrenShouldResultInSeparateOpenAndCloseTags"
	})
	public void libraryUrlAbbreviationsShouldNotBeRepeatedInChildTags() {
		createConfiguration();

		ActionDescriptor actionDescriptor = new ActionDescriptor("test", "test");
		Map<String, ActionParameter> parameters = Collections.emptyMap();
		List<ActionContributionDescriptor> contributionDescriptors = Collections.emptyList();
		Map<String, String> libraryUrlAbbreviations =
			Collections.singletonMap("test", "http://aluminumproject.googlecode.com/test");

		templateBuilder.addTemplateElement(configuration.getTemplateElementFactory().createActionElement(
			actionDescriptor, parameters, contributionDescriptors, libraryUrlAbbreviations));
		templateBuilder.addTemplateElement(configuration.getTemplateElementFactory().createActionElement(
			actionDescriptor, parameters, contributionDescriptors, libraryUrlAbbreviations));
		templateBuilder.restoreCurrentTemplateElement();
		templateBuilder.restoreCurrentTemplateElement();

		assert getTemplateText().equals(
			"<test:test xmlns:test=\"http://aluminumproject.googlecode.com/test\"><test:test/></test:test>");
	}

	@Test(dependsOnMethods = "actionElementWithoutChildrenShouldResultInCombinedOpenAndCloseTag")
	public void versionedLibraryUrlsShouldBeUsable() {
		createConfiguration();

		ActionDescriptor actionDescriptor = new ActionDescriptor("test", "test");
		Map<String, ActionParameter> parameters = Collections.emptyMap();
		List<ActionContributionDescriptor> contributionDescriptors = Collections.emptyList();
		Map<String, String> libraryUrlAbbreviations =
			Collections.singletonMap("test", "http://aluminumproject.googlecode.com/test/test");

		templateBuilder.addTemplateElement(configuration.getTemplateElementFactory().createActionElement(
			actionDescriptor, parameters, contributionDescriptors, libraryUrlAbbreviations));
		templateBuilder.restoreCurrentTemplateElement();

		assert getTemplateText().equals("<test:test xmlns:test=\"http://aluminumproject.googlecode.com/test/test\"/>");
	}

	@Test(dependsOnMethods = "actionElementWithoutChildrenShouldResultInCombinedOpenAndCloseTag")
	public void parametersShouldResultInAttributes() {
		createConfiguration();

		ActionDescriptor actionDescriptor = new ActionDescriptor("test", "test");
		Map<String, ActionParameter> parameters = Collections.<String, ActionParameter>singletonMap(
			"description", new ConstantActionParameter("test", configuration.getConverterRegistry()));
		List<ActionContributionDescriptor> contributionDescriptors = Collections.emptyList();
		Map<String, String> libraryUrlAbbreviations =
			Collections.singletonMap("test", "http://aluminumproject.googlecode.com/test");

		templateBuilder.addTemplateElement(configuration.getTemplateElementFactory().createActionElement(
			actionDescriptor, parameters, contributionDescriptors, libraryUrlAbbreviations));
		templateBuilder.restoreCurrentTemplateElement();

		assert getTemplateText().equals(
			"<test:test description=\"test\" xmlns:test=\"http://aluminumproject.googlecode.com/test\"/>");
	}

	public void contributionsShouldResultInAttributes() {
		createConfiguration();

		ActionDescriptor actionDescriptor = new ActionDescriptor("test", "test");
		Map<String, ActionParameter> parameters = Collections.emptyMap();
		List<ActionContributionDescriptor> contributions = Arrays.asList(new ActionContributionDescriptor("c", "if",
			new ExpressionActionParameter(getExpressionFactory(), "[proceed]", configuration.getConverterRegistry())));

		Map<String, String> libraryUrlAbbreviations = new LinkedHashMap<String, String>();
		libraryUrlAbbreviations.put("c", "http://aluminumproject.googlecode.com/core");
		libraryUrlAbbreviations.put("test", "http://aluminumproject.googlecode.com/test");

		templateBuilder.addTemplateElement(configuration.getTemplateElementFactory().createActionElement(
			actionDescriptor, parameters, contributions, libraryUrlAbbreviations));
		templateBuilder.restoreCurrentTemplateElement();

		assert getTemplateText().equals("<test:test c:if=\"[proceed]\" " +
			"xmlns:c=\"http://aluminumproject.googlecode.com/core\" " +
			"xmlns:test=\"http://aluminumproject.googlecode.com/test\"/>");
	}

	public static class UpperCaseElementNameTranslator implements ElementNameTranslator {
		public String translateActionName(String name) {
			return name.toUpperCase();
		}

		public String translateActionParameterName(String name) {
			return name.toUpperCase();
		}

		public String translateActionContributionName(String name) {
			return name.toUpperCase();
		}
	}

	@Test(dependsOnMethods = {"parametersShouldResultInAttributes", "contributionsShouldResultInAttributes"})
	public void elementNameTranslatorShouldBeConfigurable() {
		ConfigurationParameters configurationParameters = new ConfigurationParameters();
		configurationParameters.addParameter(
			XmlSerialiser.ELEMENT_NAME_TRANSLATOR_CLASS, UpperCaseElementNameTranslator.class.getName());

		createConfiguration(configurationParameters);

		ActionDescriptor actionDescriptor = new ActionDescriptor("TEST", "test");

		Map<String, ActionParameter> parameters = new HashMap<String, ActionParameter>();
		parameters.put("description", new ConstantActionParameter("test", configuration.getConverterRegistry()));

		List<ActionContributionDescriptor> contributions = new LinkedList<ActionContributionDescriptor>();
		contributions.add(new ActionContributionDescriptor("C", "if",
			new ExpressionActionParameter(getExpressionFactory(), "[proceed]", configuration.getConverterRegistry())));

		Map<String, String> libraryUrlAbbreviations = new LinkedHashMap<String, String>();
		libraryUrlAbbreviations.put("C", "http://aluminumproject.googlecode.com/core");
		libraryUrlAbbreviations.put("TEST", "http://aluminumproject.googlecode.com/test");

		templateBuilder.addTemplateElement(configuration.getTemplateElementFactory().createActionElement(
			actionDescriptor, parameters, contributions, libraryUrlAbbreviations));
		templateBuilder.restoreCurrentTemplateElement();

		assert getTemplateText().equals("<TEST:TEST DESCRIPTION=\"test\" C:IF=\"[proceed]\"" +
			" xmlns:C=\"http://aluminumproject.googlecode.com/core\" " +
			"xmlns:TEST=\"http://aluminumproject.googlecode.com/test\"/>");
	}

	public void textElementShouldResultInText() {
		createConfiguration();

		templateBuilder.addTemplateElement(configuration.getTemplateElementFactory().createTextElement(
			"text", Collections.<String, String>emptyMap()));
		templateBuilder.restoreCurrentTemplateElement();

		assert getTemplateText().equals("text");
	}

	public void expressionElementShouldResultInExpressionText() {
		createConfiguration();

		templateBuilder.addTemplateElement(configuration.getTemplateElementFactory().createExpressionElement(
			getExpressionFactory(), "[name]", Collections.<String, String>emptyMap()));
		templateBuilder.restoreCurrentTemplateElement();

		assert getTemplateText().equals("[name]");
	}

	private void createConfiguration() {
		createConfiguration(new ConfigurationParameters());
	}

	private void createConfiguration(ConfigurationParameters parameters) {
		TestConfiguration configuration = new TestConfiguration(parameters);

		ConverterRegistry converterRegistry = new DefaultConverterRegistry();
		converterRegistry.initialise(configuration);
		configuration.setConverterRegistry(converterRegistry);

		TemplateElementFactory templateElementFactory = new DefaultTemplateElementFactory();
		templateElementFactory.initialise(configuration);
		configuration.setTemplateElementFactory(templateElementFactory);

		Library coreLibrary = new CoreLibrary();
		coreLibrary.initialise(configuration);
		configuration.addLibrary(coreLibrary);

		Library testLibrary = new TestLibrary();
		testLibrary.initialise(configuration);
		configuration.addLibrary(testLibrary);

		TemplateStoreFinderFactory templateStoreFinderFactory = new MemoryTemplateStoreFinderFactory();
		templateStoreFinderFactory.initialise(configuration);
		configuration.setTemplateStoreFinderFactory(templateStoreFinderFactory);

		Serialiser serialiser = new XmlSerialiser();
		serialiser.initialise(configuration);
		configuration.addSerialiser("xml", serialiser);

		ExpressionFactory expressionFactory = new TestExpressionFactory();
		expressionFactory.initialise(configuration);
		configuration.addExpressionFactory(expressionFactory);

		this.configuration = configuration;
	}

	private ExpressionFactory getExpressionFactory() {
		return configuration.getExpressionFactories().get(0);
	}

	private String getTemplateText() {
		configuration.getSerialisers().get("xml").serialiseTemplate(templateBuilder.build(), "template");

		MemoryResourceStoreFinder templateStoreFinder =
			(MemoryResourceStoreFinder) configuration.getTemplateStoreFinderFactory().createTemplateStoreFinder();

		String templateText = new String(templateStoreFinder.getResource("template"));
		templateStoreFinder.removeResource("template");

		return templateText;
	}
}