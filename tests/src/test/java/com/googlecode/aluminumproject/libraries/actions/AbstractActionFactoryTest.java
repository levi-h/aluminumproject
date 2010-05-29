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
package com.googlecode.aluminumproject.libraries.actions;

import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.configuration.ConfigurationParameters;
import com.googlecode.aluminumproject.configuration.test.TestConfiguration;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.DefaultContext;
import com.googlecode.aluminumproject.converters.ConverterRegistry;
import com.googlecode.aluminumproject.converters.DefaultConverterRegistry;
import com.googlecode.aluminumproject.libraries.Library;
import com.googlecode.aluminumproject.libraries.test.TestLibrary;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("all")
@Test(groups = {"core", "fast"})
public class AbstractActionFactoryTest {
	private AbstractActionFactory actionFactoryWithDynamicParameters;
	private AbstractActionFactory actionFactoryWithoutDynamicParameters;

	private ConverterRegistry converterRegistry;

	private Context context;

	@BeforeMethod
	public void createActionFactoriesConverterRegistryAndContext() {
		class ActionFactory extends AbstractActionFactory {
			private boolean dynamicallyParameterisable;

			public ActionFactory(boolean dynamicallyParameterisable) {
				this.dynamicallyParameterisable = dynamicallyParameterisable;
			}

			public ActionInformation getInformation() {
				return new ActionInformation("abstract", Arrays.asList(
					new ActionParameterInformation("required", Object.class, true),
					new ActionParameterInformation("optional", Object.class, false)
				), dynamicallyParameterisable);
			}

			public void initialise(Configuration configuration, ConfigurationParameters parameters) {}

			public Action create(Map<String, ActionParameter> parameters, Context context) {
				return null;
			}
		}

		actionFactoryWithDynamicParameters = new ActionFactory(true);
		actionFactoryWithoutDynamicParameters = new ActionFactory(false);

		ConfigurationParameters parameters = new ConfigurationParameters();

		converterRegistry = new DefaultConverterRegistry();
		converterRegistry.initialise(new TestConfiguration(parameters), parameters);

		context = new DefaultContext();
	}

	public void libraryShouldBeStored() {
		Library library = new TestLibrary();
		actionFactoryWithDynamicParameters.setLibrary(library);
		assert actionFactoryWithDynamicParameters.getLibrary() == library;
	}

	@Test(expectedExceptions = ActionException.class)
	public void omittingRequiredParameterShouldCauseException() {
		actionFactoryWithDynamicParameters.checkRequiredParameters(new HashMap<String, ActionParameter>());
	}

	public void supplyingRequiredParametersShouldPassParameterCheck() {
		Map<String, ActionParameter> parameters = new HashMap<String, ActionParameter>();
		parameters.put("required", new ConstantActionParameter("", converterRegistry));

		actionFactoryWithoutDynamicParameters.checkRequiredParameters(parameters);
	}

	@Test(expectedExceptions = ActionException.class)
	public void supplyingSuperfluousParameterShouldCauseException() {
		Map<String, ActionParameter> parameters = new HashMap<String, ActionParameter>();
		parameters.put("superfluous", new ConstantActionParameter("", converterRegistry));

		actionFactoryWithoutDynamicParameters.checkSuperfluousParameters(parameters);
	}

	public void superfluousParametersShouldPassParameterCheckWhenUsingDynamicParameters() {
		Map<String, ActionParameter> parameters = new HashMap<String, ActionParameter>();
		parameters.put("superfluous", new ConstantActionParameter("", converterRegistry));

		actionFactoryWithDynamicParameters.checkSuperfluousParameters(parameters);
	}

	@Test(expectedExceptions = ActionException.class)
	public void gettingMissingRequiredVariableShouldCauseException() {
		Map<String, ActionParameter> parameters = new HashMap<String, ActionParameter>();

		actionFactoryWithoutDynamicParameters.getParameter(parameters, context, "required", Object.class);
	}

	public void gettingMissingOptionalVariableShouldResultInNull() {
		Map<String, ActionParameter> parameters = new HashMap<String, ActionParameter>();

		actionFactoryWithoutDynamicParameters.getParameter(parameters, context, "optional", Object.class);
	}

	public void gettingVariableShouldResultInConvertedValue() {
		Map<String, ActionParameter> parameters = new HashMap<String, ActionParameter>();
		parameters.put("v", new ConstantActionParameter("5", converterRegistry));

		Integer variable = actionFactoryWithDynamicParameters.getParameter(parameters, context, "v", Integer.class);
		assert variable != null;
		assert variable.intValue() == 5;
	}

	public void removingVariableShouldResultInConvertedValue() {
		Map<String, ActionParameter> parameters = new HashMap<String, ActionParameter>();
		parameters.put("v", new ConstantActionParameter("5", converterRegistry));

		Integer variable = actionFactoryWithDynamicParameters.removeParameter(parameters, context, "v", Integer.class);
		assert variable != null;
		assert variable.intValue() == 5;
	}

	public void removingVariableShouldRemoveParameter() {
		Map<String, ActionParameter> parameters = new HashMap<String, ActionParameter>();
		parameters.put("v", new ConstantActionParameter("5", converterRegistry));

		actionFactoryWithDynamicParameters.removeParameter(parameters, context, "v", Integer.class);

		assert parameters.isEmpty();
	}
}