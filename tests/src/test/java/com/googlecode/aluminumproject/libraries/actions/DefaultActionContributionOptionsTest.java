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
import com.googlecode.aluminumproject.configuration.TestConfiguration;
import com.googlecode.aluminumproject.context.DefaultContext;
import com.googlecode.aluminumproject.converters.ConverterRegistry;
import com.googlecode.aluminumproject.converters.DefaultConverterRegistry;
import com.googlecode.aluminumproject.interceptors.ActionInterceptor;
import com.googlecode.aluminumproject.interceptors.ActionSkipper;
import com.googlecode.aluminumproject.templates.ActionContext;
import com.googlecode.aluminumproject.templates.ActionPhase;
import com.googlecode.aluminumproject.templates.DefaultActionContext;
import com.googlecode.aluminumproject.writers.ListWriter;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("all")
@Test(groups = {"core", "fast"})
public class DefaultActionContributionOptionsTest {
	private DefaultActionContributionOptions actionContributionOptions;
	private DefaultActionContext actionContext;

	private ConverterRegistry converterRegistry;

	@BeforeMethod
	public void createActionContributionOptions() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		Configuration configuration = new TestConfiguration(parameters);

		actionContributionOptions = new DefaultActionContributionOptions();
		actionContext = new DefaultActionContext(configuration,
			new TestActionFactory(), new DefaultContext(), new ListWriter());

		converterRegistry = new DefaultConverterRegistry();
		converterRegistry.initialise(configuration, parameters);
	}

	public void notSettingParametersShouldLeaveThemEmpty() {
		actionContributionOptions.apply(actionContext);

		Map<String, ActionParameter> parameters = actionContext.getParameters();
		assert parameters != null;
		assert parameters.isEmpty();
	}

	public void interceptorsShouldBeEmptyByDefault() {
		actionContributionOptions.apply(actionContext);

		for (ActionPhase phase: ActionPhase.values()) {
			List<ActionInterceptor> interceptorsForPhase = actionContext.getInterceptors(phase);
			assert interceptorsForPhase != null;
			assert interceptorsForPhase.isEmpty();
		}
	}

	public void parametersShouldBeAddedToParameterList() {
		ConstantActionParameter parameter = new ConstantActionParameter("value", converterRegistry);
		actionContributionOptions.setParameter("name", parameter);

		ConstantActionParameter otherParameter = new ConstantActionParameter("value", converterRegistry);
		actionContributionOptions.setParameter("other-name", otherParameter);

		actionContributionOptions.apply(actionContext);

		Map<String, ActionParameter> parameters = actionContext.getParameters();
		assert parameters != null;
		assert parameters.size() == 2;

		assert parameters.containsKey("name");
		assert parameters.get("name") == parameter;

		assert parameters.containsKey("other-name");
		assert parameters.get("other-name") == otherParameter;
	}

	@Test(dependsOnMethods = "interceptorsShouldBeEmptyByDefault")
	public void skippingActionShouldAddActionSkipper() {
		actionContributionOptions.skipAction();
		actionContributionOptions.apply(actionContext);

		List<ActionInterceptor> executionInterceptors = actionContext.getInterceptors(ActionPhase.EXECUTION);
		assert executionInterceptors != null;
		assert executionInterceptors.size() == 1;
		assert executionInterceptors.get(0) instanceof ActionSkipper;
	}

	@Test(dependsOnMethods = "interceptorsShouldBeEmptyByDefault")
	public void inteceptorShouldBeAddable() {
		ActionInterceptor interceptor = new ActionInterceptor() {
			public Set<ActionPhase> getPhases() {
				return EnumSet.allOf(ActionPhase.class);
			}

			public void intercept(ActionContext actionContext) {}
		};

		actionContributionOptions.addInterceptor(interceptor);
		actionContributionOptions.apply(actionContext);

		List<ActionInterceptor> executionInterceptors = actionContext.getInterceptors(ActionPhase.CREATION);
		assert executionInterceptors != null;
		assert executionInterceptors.size() == 1;
		assert executionInterceptors.get(0) == interceptor;
	}
}