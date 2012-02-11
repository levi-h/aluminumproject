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
package com.googlecode.aluminumproject.libraries.actions;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.annotations.Ignored;
import com.googlecode.aluminumproject.annotations.Required;
import com.googlecode.aluminumproject.annotations.Typed;
import com.googlecode.aluminumproject.annotations.UsableAsFunction;
import com.googlecode.aluminumproject.annotations.ValidInside;
import com.googlecode.aluminumproject.configuration.ConfigurationParameters;
import com.googlecode.aluminumproject.configuration.TestConfiguration;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.DefaultContext;
import com.googlecode.aluminumproject.templates.TemplateInformation;
import com.googlecode.aluminumproject.writers.Writer;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
@Test(groups = {"core", "fast"})
public class DefaultActionFactoryTest {
	public void unannotatedActionShouldResultInHumanisedClassNameAsActionName() {
		ActionFactory actionFactory = new DefaultActionFactory(TestAction.class);
		actionFactory.initialise(new TestConfiguration(new ConfigurationParameters()));

		String actionName = actionFactory.getInformation().getName();
		assert actionName != null;
		assert actionName.equals("test action");
	}

	public void annotatedActionShouldResultInAnnotationAttributeAsActionName() {
		ActionFactory actionFactory = new DefaultActionFactory(AnnotatedAction.class);
		actionFactory.initialise(new TestConfiguration(new ConfigurationParameters()));

		String actionName = actionFactory.getInformation().getName();
		assert actionName != null;
		assert actionName.equals("test");
	}

	public void unannotatedParameterShouldBeRecognised() {
		ActionFactory actionFactory = new DefaultActionFactory(TestAction.class);
		actionFactory.initialise(new TestConfiguration(new ConfigurationParameters()));

		List<ActionParameterInformation> parameterInformation =
			actionFactory.getInformation().getParameterInformation();
		assert parameterInformation != null;
		assert parameterInformation.size() == 1;
	}

	public void annotatedParameterShouldBeRecognised() {
		ActionFactory actionFactory = new DefaultActionFactory(AnnotatedAction.class);
		actionFactory.initialise(new TestConfiguration(new ConfigurationParameters()));

		List<ActionParameterInformation> parameterInformation =
			actionFactory.getInformation().getParameterInformation();
		assert parameterInformation != null;
		assert parameterInformation.size() == 1;
	}

	public void parameterOfTypeActionParameterShouldBeRecognised() {
		ActionFactory actionFactory = new DefaultActionFactory(AnnotatedAction.class);
		actionFactory.initialise(new TestConfiguration(new ConfigurationParameters()));

		List<ActionParameterInformation> parameterInformation =
			actionFactory.getInformation().getParameterInformation();
		assert parameterInformation != null;
		assert parameterInformation.size() == 1;
	}

	@Test(dependsOnMethods = "unannotatedParameterShouldBeRecognised")
	public void unannotatedParameterShouldResultInHumanisedFieldNameAsParameterName() {
		ActionFactory actionFactory = new DefaultActionFactory(TestAction.class);
		actionFactory.initialise(new TestConfiguration(new ConfigurationParameters()));

		String parameterName = actionFactory.getInformation().getParameterInformation().get(0).getName();
		assert parameterName != null;
		assert parameterName.equals("description");
	}

	@Test(dependsOnMethods = "annotatedParameterShouldBeRecognised")
	public void annotatedParameterShouldResultInAnnotationAttributeAsParameterName() {
		ActionFactory actionFactory = new DefaultActionFactory(AnnotatedAction.class);
		actionFactory.initialise(new TestConfiguration(new ConfigurationParameters()));

		String parameterName = actionFactory.getInformation().getParameterInformation().get(0).getName();
		assert parameterName != null;
		assert parameterName.equals("desc");
	}

	@Test(dependsOnMethods = {"unannotatedParameterShouldBeRecognised", "annotatedParameterShouldBeRecognised"})
	public void parameterTypeShouldBecomeFieldTypeByDefault() {
		ActionFactory unannotatedActionFactory = new DefaultActionFactory(TestAction.class);
		unannotatedActionFactory.initialise(new TestConfiguration(new ConfigurationParameters()));

		Type unannotatedParameterType =
			unannotatedActionFactory.getInformation().getParameterInformation().get(0).getType();
		assert unannotatedParameterType == String.class;

		ActionFactory annotatedActionFactory = new DefaultActionFactory(AnnotatedAction.class);
		annotatedActionFactory.initialise(new TestConfiguration(new ConfigurationParameters()));

		Type annotatedParameterType =
			annotatedActionFactory.getInformation().getParameterInformation().get(0).getType();
		assert annotatedParameterType == String.class;
	}

	@Test(dependsOnMethods = {
		"parameterOfTypeActionParameterShouldBeRecognised",
		"parameterTypeShouldBecomeFieldTypeByDefault"
	})
	public void annotatedParameterShouldResultInAnnotationAttributeAsParameterType() {
		ActionFactory actionFactory = new DefaultActionFactory(ParameterAction.class);
		actionFactory.initialise(new TestConfiguration(new ConfigurationParameters()));

		assert actionFactory.getInformation().getParameterInformation().get(0).getType() == String.class;
	}

	@Test(dependsOnMethods = "unannotatedParameterShouldBeRecognised")
	public void unannotatedParameterShouldBeOptional() {
		ActionFactory actionFactory = new DefaultActionFactory(TestAction.class);
		actionFactory.initialise(new TestConfiguration(new ConfigurationParameters()));

		assert !actionFactory.getInformation().getParameterInformation().get(0).isRequired();
	}

	@Test(dependsOnMethods = "annotatedParameterShouldBeRecognised")
	public void annotatedParameterShouldTakeBeingRequiredFromAnnotationAttribute() {
		ActionFactory actionFactory = new DefaultActionFactory(AnnotatedAction.class);
		actionFactory.initialise(new TestConfiguration(new ConfigurationParameters()));

		assert actionFactory.getInformation().getParameterInformation().get(0).isRequired();
	}

	@Test(dependsOnMethods = "unannotatedParameterShouldBeOptional")
	public void omittingOptionalParametersShouldBePossible() {
		ActionFactory actionFactory = new DefaultActionFactory(TestAction.class);
		actionFactory.initialise(new TestConfiguration(new ConfigurationParameters()));

		actionFactory.create(Collections.<String, ActionParameter>emptyMap(), new DefaultContext());
	}

	@Test(expectedExceptions = AluminumException.class,
		dependsOnMethods = "annotatedParameterShouldTakeBeingRequiredFromAnnotationAttribute")
	public void omittingRequiredParametersShouldCauseException() {
		ActionFactory actionFactory = new DefaultActionFactory(AnnotatedAction.class);
		actionFactory.initialise(new TestConfiguration(new ConfigurationParameters()));

		actionFactory.create(Collections.<String, ActionParameter>emptyMap(), new DefaultContext());
	}

	@Test(dependsOnMethods = "unannotatedParameterShouldResultInHumanisedFieldNameAsParameterName")
	public void supplyingOptionalParametersShouldCreateAction() {
		ActionFactory actionFactory = new DefaultActionFactory(TestAction.class);
		actionFactory.initialise(new TestConfiguration(new ConfigurationParameters()));

		Map<String, ActionParameter> parameters =
			Collections.<String, ActionParameter>singletonMap("description", new TestActionParameter("Test action."));

		assert actionFactory.create(parameters, new DefaultContext()) instanceof TestAction;
	}

	public static class ParameterAction extends AbstractAction {
		@SuppressWarnings("unused")
		private @Typed("String") ActionParameter descriptionParameter;

		public void execute(Context context, Writer writer) {}
	}

	@Test(dependsOnMethods = "annotatedParameterShouldResultInAnnotationAttributeAsParameterType")
	public void supplyingOptionalParametersOfTypeActionParameterShouldCreateAction() {
		ActionFactory actionFactory = new DefaultActionFactory(ParameterAction.class);
		actionFactory.initialise(new TestConfiguration(new ConfigurationParameters()));

		Map<String, ActionParameter> parameters = new HashMap<String, ActionParameter>();
		parameters.put("description parameter", new TestActionParameter("Test action."));

		assert actionFactory.create(parameters, new DefaultContext()) instanceof ParameterAction;
	}

	@Test(dependsOnMethods = "annotatedParameterShouldResultInAnnotationAttributeAsParameterName")
	public void supplyingRequiredParametersWithAnnotatedNamesShouldCreateAction() {
		ActionFactory actionFactory = new DefaultActionFactory(AnnotatedAction.class);
		actionFactory.initialise(new TestConfiguration(new ConfigurationParameters()));

		Map<String, ActionParameter> parameters =
			Collections.<String, ActionParameter>singletonMap("desc", new TestActionParameter("Test action."));

		assert actionFactory.create(parameters, new DefaultContext()) instanceof AnnotatedAction;
	}

	@Test(expectedExceptions = AluminumException.class)
	public void supplyingSuperfluousParametersShouldCauseException() {
		ActionFactory actionFactory = new DefaultActionFactory(TestAction.class);
		actionFactory.initialise(new TestConfiguration(new ConfigurationParameters()));

		Map<String, ActionParameter> parameters =
			Collections.<String, ActionParameter>singletonMap("superfluous", new TestActionParameter("true"));

		actionFactory.create(parameters, new DefaultContext());
	}

	public static class DynamicallyParameterisableAction extends AbstractDynamicallyParameterisableAction {
		private String regularParameter;

		public String getRegularParameter() {
			return regularParameter;
		}

		@Override
		public Map<String, ActionParameter> getDynamicParameters() {
			return super.getDynamicParameters();
		}

		public void execute(Context context, Writer writer) {}
	}

	public void superfluousParametersShouldBeSetAsDynamicParametersWhenActionIsDynamicallyParameterisable() {
		ActionFactory actionFactory = new DefaultActionFactory(DynamicallyParameterisableAction.class);
		actionFactory.initialise(new TestConfiguration(new ConfigurationParameters()));

		Map<String, ActionParameter> parameters =
			Collections.<String, ActionParameter>singletonMap("dynamic", new TestActionParameter("true"));

		Action action = actionFactory.create(parameters, new DefaultContext());
		assert action instanceof DynamicallyParameterisableAction;

		Map<String, ActionParameter> dynamicParameters =
			((DynamicallyParameterisableAction) action).getDynamicParameters();
		assert dynamicParameters != null;
		assert dynamicParameters.size() == 1;
		assert dynamicParameters.containsKey("dynamic");

		Object parameterValue = dynamicParameters.get("dynamic").getValue(String.class, new DefaultContext());
		assert parameterValue instanceof String;
		assert Boolean.parseBoolean((String) parameterValue);
	}

	@Test(dependsOnMethods = {
		"unannotatedParameterShouldResultInHumanisedFieldNameAsParameterName",
		"superfluousParametersShouldBeSetAsDynamicParametersWhenActionIsDynamicallyParameterisable"
	})
	public void regularParametersShouldBePreferredOverDynamicOnes() {
		ActionFactory actionFactory = new DefaultActionFactory(DynamicallyParameterisableAction.class);
		actionFactory.initialise(new TestConfiguration(new ConfigurationParameters()));

		Map<String, ActionParameter> parameters =
			Collections.<String, ActionParameter>singletonMap("regular parameter", new TestActionParameter("value"));

		Action action = actionFactory.create(parameters, new DefaultContext());
		assert action instanceof DynamicallyParameterisableAction;

		DynamicallyParameterisableAction dynamicallyParameterisableAction = (DynamicallyParameterisableAction) action;

		Map<String, ActionParameter> dynamicParameters = dynamicallyParameterisableAction.getDynamicParameters();
		assert dynamicParameters != null;
		assert dynamicParameters.isEmpty();

		String regularParameter = dynamicallyParameterisableAction.getRegularParameter();
		assert regularParameter != null;
		assert regularParameter.equals("value");
	}

	public void unannotatedActionShouldNotBeUsableAsFunction() {
		ActionFactory actionFactory = new DefaultActionFactory(TestAction.class);
		actionFactory.initialise(new TestConfiguration(new ConfigurationParameters()));

		assert actionFactory.getInformation().getResultTypeWhenFunction() == null;
	}

	@Test(dependsOnMethods = {"unannotatedActionShouldNotBeUsableAsFunction", "unannotatedParameterShouldBeRecognised"})
	public void parametersOfActionsThatAreNotUsableAsFunctionsShouldNotHaveArgumentIndices() {
		ActionFactory actionFactory = new DefaultActionFactory(TestAction.class);
		actionFactory.initialise(new TestConfiguration(new ConfigurationParameters()));

		Integer argumentIndex =
			actionFactory.getInformation().getParameterInformation().get(0).getIndexWhenFunctionArgument();
		assert argumentIndex == null;
	}

	@UsableAsFunction(resultType = "String", argumentParameters = "description")
	public static class ActionThatIsUsableAsFunction extends AbstractAction {
		private @Required String description;

		public void execute(Context context, Writer writer) {
			writer.write(description);
		}
	}

	public void annotatedActionShouldBeUsableAsFunction() {
		ActionFactory actionFactory = new DefaultActionFactory(ActionThatIsUsableAsFunction.class);
		actionFactory.initialise(new TestConfiguration(new ConfigurationParameters()));

		assert actionFactory.getInformation().getResultTypeWhenFunction() == String.class;
	}

	@Test(dependsOnMethods = {"annotatedActionShouldBeUsableAsFunction", "annotatedParameterShouldBeRecognised"})
	public void parametersOfActionsThatAreUsableAsFunctionsShouldHaveArgumentIndices() {
		ActionFactory actionFactory = new DefaultActionFactory(ActionThatIsUsableAsFunction.class);
		actionFactory.initialise(new TestConfiguration(new ConfigurationParameters()));

		Integer argumentIndex =
			actionFactory.getInformation().getParameterInformation().get(0).getIndexWhenFunctionArgument();
		assert argumentIndex != null;
		assert argumentIndex.equals(Integer.valueOf(0));
	}

	@UsableAsFunction
	public static class ActionThatIsUsableAsFunctionWithOptionalParameter extends AbstractAction {
		@SuppressWarnings("unused")
		private String description;

		public void execute(Context context, Writer writer) {}
	}

	@Test(dependsOnMethods = {"annotatedActionShouldBeUsableAsFunction", "annotatedParameterShouldBeRecognised"})
	public void omittedParametersFromFunctionArgumentListsShouldNotHaveArgumentIndices() {
		ActionFactory actionFactory = new DefaultActionFactory(ActionThatIsUsableAsFunctionWithOptionalParameter.class);
		actionFactory.initialise(new TestConfiguration(new ConfigurationParameters()));

		Integer argumentIndex =
			actionFactory.getInformation().getParameterInformation().get(0).getIndexWhenFunctionArgument();
		assert argumentIndex == null;
	}

	@Ignored
	@UsableAsFunction
	public static class ActionThatIsUsableAsFunctionWithMissingArgumentParameter extends AbstractAction {
		@SuppressWarnings("unused")
		private @Required String description;

		public void execute(Context context, Writer writer) {}
	}

	@Test(dependsOnMethods = "annotatedActionShouldBeUsableAsFunction", expectedExceptions = AluminumException.class)
	public void omittingRequiredParameterFromFunctionArgumentListShouldCauseException() {
		new DefaultActionFactory(ActionThatIsUsableAsFunctionWithMissingArgumentParameter.class).initialise(
			new TestConfiguration(new ConfigurationParameters()));
	}

	@Ignored
	@UsableAsFunction(argumentParameters = "nonexistent")
	public static class ActionThatIsUsableAsFunctionWithUnknownArgumentParameter extends AbstractAction {
		public void execute(Context context, Writer writer) {}
	}

	@Test(dependsOnMethods = "annotatedActionShouldBeUsableAsFunction", expectedExceptions = AluminumException.class)
	public void includingUnknownParameterInFunctionArgumentListShouldCauseException() {
		new DefaultActionFactory(ActionThatIsUsableAsFunctionWithUnknownArgumentParameter.class).initialise(
			new TestConfiguration(new ConfigurationParameters()));
	}

	@ValidInside(TestAction.class)
	public static class ActionThatIsOnlyValidInsideTestAction extends AbstractAction {
		public void execute(Context context, Writer writer) {}
	}

	@Test(expectedExceptions = AluminumException.class)
	public void omittingRequiredAncestorShouldCauseException() {
		DefaultActionFactory actionFactory = new DefaultActionFactory(ActionThatIsOnlyValidInsideTestAction.class);
		actionFactory.initialise(new TestConfiguration(new ConfigurationParameters()));

		actionFactory.create(Collections.<String, ActionParameter>emptyMap(), new DefaultContext());
	}

	public void supplyingRequiredAncestorShouldCreateAction() {
		DefaultActionFactory actionFactory = new DefaultActionFactory(ActionThatIsOnlyValidInsideTestAction.class);
		actionFactory.initialise(new TestConfiguration(new ConfigurationParameters()));

		Map<String, ActionParameter> parameters = Collections.emptyMap();
		Context context = new DefaultContext();

		TemplateInformation.from(context).addAction(new TestAction());

		assert actionFactory.create(parameters, context) instanceof ActionThatIsOnlyValidInsideTestAction;
	}
}