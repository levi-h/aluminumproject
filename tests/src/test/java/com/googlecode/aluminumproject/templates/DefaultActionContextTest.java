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
package com.googlecode.aluminumproject.templates;

import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.configuration.ConfigurationParameters;
import com.googlecode.aluminumproject.configuration.TestConfiguration;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.DefaultContext;
import com.googlecode.aluminumproject.interceptors.ActionInterceptor;
import com.googlecode.aluminumproject.interceptors.InterceptionException;
import com.googlecode.aluminumproject.libraries.actions.ActionContributionFactory;
import com.googlecode.aluminumproject.libraries.actions.ActionException;
import com.googlecode.aluminumproject.libraries.actions.ActionFactory;
import com.googlecode.aluminumproject.libraries.actions.ActionParameter;
import com.googlecode.aluminumproject.libraries.actions.TestAction;
import com.googlecode.aluminumproject.libraries.actions.TestActionContributionFactory;
import com.googlecode.aluminumproject.libraries.actions.TestActionFactory;
import com.googlecode.aluminumproject.libraries.actions.TestActionParameter;
import com.googlecode.aluminumproject.writers.StringWriter;
import com.googlecode.aluminumproject.writers.TestWriter;
import com.googlecode.aluminumproject.writers.Writer;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("all")
@Test(groups = {"core", "fast"})
public class DefaultActionContextTest {
	private Configuration configuration;

	private ActionDescriptor actionDescriptor;
	private ActionFactory actionFactory;

	private Context context;
	private Writer writer;

	private DefaultActionContext actionContext;

	@BeforeMethod
	public void createActionContext() {
		configuration = new TestConfiguration(new ConfigurationParameters());

		actionDescriptor = new ActionDescriptor("test", "test");
		actionFactory = new TestActionFactory();

		context = new DefaultContext();
		writer = new TestWriter();

		actionContext = new DefaultActionContext(configuration, actionDescriptor, actionFactory, context, writer);
	}

	public void configurationShouldBeConfigurationOfConstructor() {
		assert actionContext.getConfiguration() == configuration;
	}

	public void actionDescriptorShouldBeActionDescriptorOfConstructor() {
		assert actionContext.getActionDescriptor() == actionDescriptor;
	}

	public void actionFactoryShouldBeActionFactoryOfConstructor() {
		assert actionContext.getActionFactory() == actionFactory;
	}

	public void contextShouldBeContextOfConstructor() {
		assert actionContext.getContext() == context;
	}

	public void initialWriterShouldBeWriterOfConstructor() {
		assert actionContext.getWriter() == writer;
	}

	public void writerShouldBeReplaceable() {
		Writer writer = new StringWriter();

		actionContext.setWriter(writer);
		assert actionContext.getWriter() == writer;
	}

	public void parametersShouldInitiallyBeEmpty() {
		Map<String, ActionParameter> parameters = actionContext.getParameters();
		assert parameters != null;
		assert parameters.isEmpty();
	}

	public void parametersShouldBeAddable() {
		ActionParameter parameter = new TestActionParameter("test");

		actionContext.addParameter("test", parameter);

		Map<String, ActionParameter> parameters = actionContext.getParameters();
		assert parameters != null;
		assert parameters.size() == 1;

		assert parameters.containsKey("test");
		assert parameters.get("test") == parameter;
	}

	@Test(dependsOnMethods = "phaseShouldBeChangeable", expectedExceptions = ActionException.class)
	public void addingParameterAfterActionHasBeenCreatedShouldCauseException() {
		actionContext.setPhase(ActionPhase.CREATION);
		actionContext.setAction(new TestAction());

		actionContext.addParameter("test", new TestActionParameter("test"));
	}

	public void contributionFactoriesShouldInitiallyBeEmpty() {
		Map<ActionContributionDescriptor, ActionContributionFactory> actionContributionFactories =
			actionContext.getActionContributionFactories();
		assert actionContributionFactories != null;
		assert actionContributionFactories.isEmpty();
	}

	public void actionContributionsShouldBeAddable() {
		ActionContributionDescriptor descriptor =
			new ActionContributionDescriptor("test", "test", new TestActionParameter("test"));
		ActionContributionFactory actionContributionFactory = new TestActionContributionFactory();

		actionContext.addActionContribution(descriptor, actionContributionFactory);

		Map<ActionContributionDescriptor, ActionContributionFactory> actionContributionFactories =
			actionContext.getActionContributionFactories();
		assert actionContributionFactories != null;
		assert actionContributionFactories.size() == 1;

		assert actionContributionFactories.containsKey(descriptor);
		assert actionContributionFactories.get(descriptor) == actionContributionFactory;
	}

	@Test(dependsOnMethods = "phaseShouldBeChangeable", expectedExceptions = ActionException.class)
	public void addingActionContributionAfterContributionsHaveBeenMadeShouldCauseException() {
		actionContext.setPhase(ActionPhase.CREATION);

		actionContext.addActionContribution(
			new ActionContributionDescriptor("test", "test", new TestActionParameter("test")),
			new TestActionContributionFactory());
	}

	@Test(expectedExceptions = ActionException.class)
	public void replacingActionShouldCauseException() {
		actionContext.setAction(new TestAction());
		actionContext.setAction(new TestAction());
	}

	public void interceptorsShouldInitiallyBeEmpty() {
		List<ActionInterceptor> contributionInterceptors = actionContext.getInterceptors(ActionPhase.CONTRIBUTION);
		assert contributionInterceptors != null;
		assert contributionInterceptors.isEmpty();

		List<ActionInterceptor> creationInterceptors = actionContext.getInterceptors(ActionPhase.CREATION);
		assert creationInterceptors != null;
		assert creationInterceptors.isEmpty();

		List<ActionInterceptor> executionInterceptors = actionContext.getInterceptors(ActionPhase.EXECUTION);
		assert executionInterceptors != null;
		assert executionInterceptors.isEmpty();
	}

	public void interceptorsShouldBeAddable() {
		ActionInterceptor interceptor = new ActionInterceptor() {
			public Set<ActionPhase> getPhases() {
				return EnumSet.allOf(ActionPhase.class);
			}

			public void intercept(ActionContext actionContext) {}
		};

		actionContext.addInterceptor(interceptor);

		List<ActionInterceptor> contributionInterceptors = actionContext.getInterceptors(ActionPhase.CONTRIBUTION);
		assert contributionInterceptors != null;
		assert contributionInterceptors.size() == 1;
		assert contributionInterceptors.contains(interceptor);

		List<ActionInterceptor> creationInterceptors = actionContext.getInterceptors(ActionPhase.CREATION);
		assert creationInterceptors != null;
		assert creationInterceptors.size() == 1;
		assert creationInterceptors.contains(interceptor);

		List<ActionInterceptor> executionInterceptors = actionContext.getInterceptors(ActionPhase.EXECUTION);
		assert executionInterceptors != null;
		assert executionInterceptors.size() == 1;
		assert executionInterceptors.contains(interceptor);

	}

	@Test(dependsOnMethods = "phaseShouldBeChangeable", expectedExceptions = ActionException.class)
	public void addingInterceptorForPastPhaseShouldCauseException() {
		actionContext.setPhase(ActionPhase.EXECUTION);

		actionContext.addInterceptor(new ActionInterceptor() {
			public Set<ActionPhase> getPhases() {
				return EnumSet.of(ActionPhase.CREATION, ActionPhase.EXECUTION);
			}

			public void intercept(ActionContext actionContext) {}
		});
	}

	public void initialPhaseShouldBeNull() {
		assert actionContext.getPhase() == null;
	}

	public void phaseShouldBeChangeable() {
		actionContext.setPhase(ActionPhase.CONTRIBUTION);

		assert actionContext.getPhase() == ActionPhase.CONTRIBUTION;
	}

	@Test(dependsOnMethods = {"interceptorsShouldBeAddable", "phaseShouldBeChangeable"})
	public void proceedingActionContextShouldRunNextInterceptor() {
		actionContext.addInterceptor(new ActionInterceptor() {
			public Set<ActionPhase> getPhases() {
				return EnumSet.of(ActionPhase.EXECUTION);
			}

			public void intercept(ActionContext actionContext) throws InterceptionException {
				actionContext.getContext().setVariable("run", true);

				actionContext.proceed();
			}
		});
		actionContext.setPhase(ActionPhase.EXECUTION);

		actionContext.proceed();

		assert context.getVariableNames(Context.TEMPLATE_SCOPE).contains("run");
		assert context.getVariable(Context.TEMPLATE_SCOPE, "run") instanceof Boolean;
		assert ((Boolean) context.getVariable(Context.TEMPLATE_SCOPE, "run")).booleanValue();
	}

	@Test(dependsOnMethods = {"interceptorsShouldInitiallyBeEmpty", "phaseShouldBeChangeable"})
	public void proceedingActionContextWithoutInterceptorsLeftShouldHaveNoEffect() {
		actionContext.setPhase(ActionPhase.CREATION);
		actionContext.proceed();
	}
}