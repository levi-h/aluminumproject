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
package com.googlecode.aluminumproject.expressions.el;

import static com.googlecode.aluminumproject.configuration.DefaultConfiguration.EXPRESSION_FACTORY_PACKAGES;
import static com.googlecode.aluminumproject.configuration.DefaultConfiguration.LIBRARY_PACKAGES;
import static com.googlecode.aluminumproject.utilities.ReflectionUtilities.getPackageName;

import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.configuration.ConfigurationParameters;
import com.googlecode.aluminumproject.configuration.DefaultConfiguration;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.DefaultContext;
import com.googlecode.aluminumproject.libraries.LibraryInformation;
import com.googlecode.aluminumproject.libraries.TestLibrary;
import com.googlecode.aluminumproject.templates.TemplateElement;
import com.googlecode.aluminumproject.templates.TemplateInformation;
import com.googlecode.aluminumproject.writers.Writer;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
@Test(groups = {"expressions", "expressions-el", "fast"})
public class FunctionMapperTest {
	private Configuration configuration;

	private FunctionMapper functionMapper;

	@BeforeMethod
	public void createConfigurationAndContext() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(LIBRARY_PACKAGES, getPackageName(TestLibrary.class));
		parameters.addParameter(EXPRESSION_FACTORY_PACKAGES, getPackageName(ElExpressionFactory.class));

		configuration = new DefaultConfiguration(parameters);

		Context context = new DefaultContext();

		final Map<String, String> libraryUrlAbbreviations = new HashMap<String, String>();

		LibraryInformation testLibraryInformation = new TestLibrary().getInformation();
		libraryUrlAbbreviations.put("test", testLibraryInformation.getUrl());
		libraryUrlAbbreviations.put("versionedTest", testLibraryInformation.getVersionedUrl());

		TemplateInformation.from(context).addTemplateElement(new TemplateElement() {
			public Map<String, String> getLibraryUrlAbbreviations() {
				return libraryUrlAbbreviations;
			}

			public void process(Context context, Writer writer) {}
		});

		functionMapper = new FunctionMapper(context, configuration);
	}

	@AfterMethod
	public void closeConfiguration() {
		configuration.close();
	}

	public void resolvingFunctionWithUnknownPrefixShouldResultInNull() {
		assert functionMapper.resolveFunction("t", "function") == null;
	}

	public void resolvingFunctionWithUnknownNameShouldResultInNull() {
		assert functionMapper.resolveFunction("test", "function") == null;
	}

	public void resolvingExistingFunctionShouldResultInDelegate() {
		assert functionMapper.resolveFunction("test", "max") != null;
	}

	public void resolvingExistingFunctionWithVersionedAbbreviationShouldResultInDelegate() {
		assert functionMapper.resolveFunction("versionedTest", "max") != null;
	}

	public void resolvingDynamicFunctionShouldResultInDelegate() {
		assert functionMapper.resolveFunction("test", "add1and2and3and4") != null;
	}

	public void resolvingDynamicFunctionWithVersionedAbbreviationShouldResultInDelegate() {
		assert functionMapper.resolveFunction("versionedTest", "add1and2and3and4") != null;
	}

	@Test(dependsOnMethods = {
		"resolvingExistingFunctionShouldResultInDelegate",
		"resolvingDynamicFunctionShouldResultInDelegate"
	})
	public void delegateShouldBeInvocable() throws IllegalAccessException, InvocationTargetException {
		Object result;

		result = functionMapper.resolveFunction("test", "max").invoke(null, 5, 10);
		assert result instanceof Integer;
		assert ((Integer) result).intValue() == 10;

		result = functionMapper.resolveFunction("test", "add1and2and3and4").invoke(null);
		assert result instanceof Integer;
		assert ((Integer) result).intValue() == 10;
	}

	@Test(
		dependsOnMethods = "resolvingExistingFunctionShouldResultInDelegate",
		expectedExceptions = IllegalArgumentException.class
	)
	public void invokingDelegateWithWrongNumberOfArgumentsShouldCauseException()
			throws IllegalAccessException, InvocationTargetException {
		functionMapper.resolveFunction("test", "max").invoke(null, 5);
	}

	@Test(
		dependsOnMethods = "resolvingExistingFunctionShouldResultInDelegate",
		expectedExceptions = InvocationTargetException.class
	)
	public void invokingDelegateWithIncompatibleArgumentsShouldCauseException()
			throws IllegalAccessException, InvocationTargetException {
		functionMapper.resolveFunction("test", "max").invoke(null, "five", "ten");
	}
}