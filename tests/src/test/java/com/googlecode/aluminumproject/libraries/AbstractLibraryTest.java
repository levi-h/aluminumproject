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
package com.googlecode.aluminumproject.libraries;

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.annotations.Ignored;
import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.configuration.ConfigurationParameters;
import com.googlecode.aluminumproject.configuration.TestConfiguration;
import com.googlecode.aluminumproject.libraries.actions.ActionContributionFactory;
import com.googlecode.aluminumproject.libraries.actions.ActionFactory;
import com.googlecode.aluminumproject.libraries.actions.AnnotatedActionContribution;
import com.googlecode.aluminumproject.libraries.actions.ContributingActionFactory;
import com.googlecode.aluminumproject.libraries.actions.IgnoredAction;
import com.googlecode.aluminumproject.libraries.actions.TestAction;
import com.googlecode.aluminumproject.libraries.actions.TestActionContribution;
import com.googlecode.aluminumproject.libraries.actions.TestActionContributionFactory;
import com.googlecode.aluminumproject.libraries.actions.TestActionContributionThatIsUsableAsAction;
import com.googlecode.aluminumproject.libraries.actions.TestActionFactory;
import com.googlecode.aluminumproject.libraries.functions.FunctionFactory;
import com.googlecode.aluminumproject.libraries.functions.TestFunctionFactory;
import com.googlecode.aluminumproject.libraries.functions.TestFunctions;
import com.googlecode.aluminumproject.utilities.ReflectionUtilities;

import java.util.Iterator;
import java.util.List;

import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
@Test(groups = {"core", "fast"})
public class AbstractLibraryTest {
	public void usingPackagesThatDoNotContainActionsShouldResultInEmptyListOfActionFactories() {
		Library library = createLibrary(false, false, ReflectionUtilities.getPackageName(String.class));

		List<ActionFactory> actionFactories = library.getActionFactories();
		assert actionFactories != null;
		assert actionFactories.isEmpty();
	}

	@Test(dependsOnMethods = "usingPackagesThatDoNotContainActionsShouldResultInEmptyListOfActionFactories")
	public void actionFactoriesShouldContainFactoriesForCommonActions() {
		Library library = createLibrary(true, false, ReflectionUtilities.getPackageName(String.class));

		List<ActionFactory> actionFactories = library.getActionFactories();
		assert actionFactories != null;
		assert getActionFactory(actionFactories, "call function") != null;
		assert getActionFactory(actionFactories, "function argument") != null;
	}

	public void usingPackagesThatDoContainActionsShouldResultInFactoryPerAction() {
		Library library = createLibrary(false, false, ReflectionUtilities.getPackageName(TestAction.class));

		List<ActionFactory> actionFactories = library.getActionFactories();
		assert actionFactories != null;
		assert getActionFactory(actionFactories, "test") != null;
		assert getActionFactory(actionFactories, "test action") != null;
	}

	@Test(dependsOnMethods = "usingPackagesThatDoContainActionsShouldResultInFactoryPerAction")
	public void libraryShouldNotContainFactoriesForIgnoredActions() {
		Library library = createLibrary(false, false, ReflectionUtilities.getPackageName(IgnoredAction.class));

		assert getActionFactory(library.getActionFactories(), "ignored action") == null;
	}

	public void usingPackagesThatContainActionContributionsThatAreUsableAsActionsShouldResultInFactoryPerAction() {
		Library library = createLibrary(false, false,
			ReflectionUtilities.getPackageName(TestActionContributionThatIsUsableAsAction.class));

		ActionFactory actionFactory =
			getActionFactory(library.getActionFactories(), "test action contribution that is usable as action");
		assert actionFactory instanceof ContributingActionFactory;
	}

	private ActionFactory getActionFactory(List<ActionFactory> actionFactories, String actionName) {
		Iterator<ActionFactory> actionFactoryIterator = actionFactories.iterator();
		ActionFactory actionFactory = null;

		while (actionFactoryIterator.hasNext() && (actionFactory == null)) {
			actionFactory = actionFactoryIterator.next();

			if (!actionFactory.getInformation().getName().equals(actionName)) {
				actionFactory = null;
			}
		}

		return actionFactory;
	}

	@Test(expectedExceptions = AluminumException.class)
	public void obtainingDynamicActionFactoryWhenDynamicActionsAreNotSupportedShouldCauseException() {
		Library library = createLibrary(false, false, ReflectionUtilities.getPackageName(TestAction.class));

		library.getDynamicActionFactory("test");
	}

	public void dynamicActionFactoriesShouldBeObtainableWhenDynamicActionsAreSupported() {
		Library library = createLibrary(false, true, ReflectionUtilities.getPackageName(TestAction.class));

		assert library.getDynamicActionFactory("test") != null;
	}

	public void usingPackagesThatDoNotContainActionContributionsShouldResultInEmptyListOfActionContributionFactories() {
		Library library = createLibrary(false, false, ReflectionUtilities.getPackageName(String.class));

		List<ActionContributionFactory> actionContributionFactories = library.getActionContributionFactories();
		assert actionContributionFactories != null;
		assert actionContributionFactories.isEmpty();
	}

	public void usingPackagesThatDoContainActionContributionsShouldResultInFactoryPerActionContribution() {
		Library library = createLibrary(false, false, ReflectionUtilities.getPackageName(AnnotatedActionContribution.class));

		List<ActionContributionFactory> actionContributionFactories = library.getActionContributionFactories();
		assert actionContributionFactories != null;
		assert getActionContributionFactory(actionContributionFactories, "annotated test") != null;
	}

	@Test(dependsOnMethods = "usingPackagesThatDoContainActionContributionsShouldResultInFactoryPerActionContribution")
	public void libraryShouldNotContainFactoriesForIgnoredActionContributions() {
		Library library = createLibrary(false, false, ReflectionUtilities.getPackageName(TestActionContribution.class));

		List<ActionContributionFactory> actionContributionFactories = library.getActionContributionFactories();
		assert getActionContributionFactory(actionContributionFactories, "test action contribution") == null;
	}

	private ActionContributionFactory getActionContributionFactory(
			List<ActionContributionFactory> actionContributionFactories, String name) {
		Iterator<ActionContributionFactory> actionContributionFactoryIterator = actionContributionFactories.iterator();
		ActionContributionFactory actionContributionFactory = null;

		while (actionContributionFactoryIterator.hasNext() && (actionContributionFactory == null)) {
			actionContributionFactory = actionContributionFactoryIterator.next();

			if (!actionContributionFactory.getInformation().getName().equals(name)) {
				actionContributionFactory = null;
			}
		}

		return actionContributionFactory;
	}

	@Test(expectedExceptions = AluminumException.class)
	public void obtainingDynamicActionContributionFactoryWhenDynamicActionsAreNotSupportedShouldCauseException() {
		Library library = createLibrary(false, false, ReflectionUtilities.getPackageName(TestAction.class));

		library.getDynamicActionContributionFactory("test");
	}

	public void dynamicActionContributionFactoriesShouldBeObtainableWhenDynamicActionContributionsAreSupported() {
		Library library = createLibrary(false, true, ReflectionUtilities.getPackageName(TestAction.class));

		assert library.getDynamicActionContributionFactory("test") != null;
	}

	public void usingPackagesThatDoNotContainFunctionsShouldResultInEmptyListOfFunctionFactories() {
		Library library = createLibrary(false, false, ReflectionUtilities.getPackageName(String.class));

		List<FunctionFactory> functionFactories = library.getFunctionFactories();
		assert functionFactories != null;
		assert functionFactories.isEmpty();
	}

	public void usingPackagesThatDoContainFunctionsShouldResultInStaticMethodInvokingFunctionFactoryPerMethod() {
		Library library = createLibrary(false, false, ReflectionUtilities.getPackageName(TestFunctions.class));

		List<FunctionFactory> functionFactories = library.getFunctionFactories();
		assert functionFactories != null;
		assert getFunctionFactory(functionFactories, "minimumValue") != null;
		assert getFunctionFactory(functionFactories, "max") != null;
		assert getFunctionFactory(functionFactories, "divide") != null;
	}

	@Test(dependsOnMethods =
		"usingPackagesThatDoContainFunctionsShouldResultInStaticMethodInvokingFunctionFactoryPerMethod")
	public void libraryShouldNotContainFunctionFactoriesForIgnoredMethods() {
		Library library = createLibrary(false, false, ReflectionUtilities.getPackageName(TestFunctions.class));

		assert getFunctionFactory(library.getFunctionFactories(), "ignored") == null;
	}

	@Test(dependsOnMethods =
		"usingPackagesThatDoContainFunctionsShouldResultInStaticMethodInvokingFunctionFactoryPerMethod")
	public void libraryShouldNotContainFunctionFactoriesForInaccessibleMethods() {
		Library library = createLibrary(false, false, ReflectionUtilities.getPackageName(TestFunctions.class));

		assert getFunctionFactory(library.getFunctionFactories(), "inaccessible") == null;
	}

	private FunctionFactory getFunctionFactory(List<FunctionFactory> functionFactories, String functionName) {
		Iterator<FunctionFactory> functionFactoryIterator = functionFactories.iterator();
		FunctionFactory functionFactory = null;

		while (functionFactoryIterator.hasNext() && (functionFactory == null)) {
			functionFactory = functionFactoryIterator.next();

			if (!functionFactory.getInformation().getName().equals(functionName)) {
				functionFactory = null;
			}
		}

		return functionFactory;
	}

	@Test(expectedExceptions = AluminumException.class)
	public void obtainingDynamicFunctionFactoryWhenDynamicFunctionsAreNotSupportedShouldCauseException() {
		Library library = createLibrary(false, false, ReflectionUtilities.getPackageName(TestAction.class));

		library.getDynamicFunctionFactory("test");
	}

	public void dynamicFunctionFactoriesShouldBeObtainableWhenDynamicFunctionsAreSupported() {
		Library library = createLibrary(false, true, ReflectionUtilities.getPackageName(TestAction.class));

		assert library.getDynamicFunctionFactory("test") != null;
	}

	@Test(dependsOnMethods = {
		"usingPackagesThatDoNotContainActionsShouldResultInEmptyListOfActionFactories",
		"usingPackagesThatDoNotContainActionContributionsShouldResultInEmptyListOfActionContributionFactories",
		"usingPackagesThatDoNotContainFunctionsShouldResultInEmptyListOfFunctionFactories"
	})
	public void libraryElementsShouldBeInitialisedAndHaveTheirLibrariesInjected() {
		DefaultLibrary library = new DefaultLibrary(false, false, ReflectionUtilities.getPackageName(String.class)) {
			@Override
			public void initialise(Configuration configuration) throws AluminumException {
				super.initialise(configuration);

				addActionFactory(new TestActionFactory());
				addActionContributionFactory(new TestActionContributionFactory());
				addFunctionFactory(new TestFunctionFactory());
			}
		};

		Configuration configuration = new TestConfiguration(new ConfigurationParameters());

		library.initialise(configuration);

		ActionFactory actionFactory = getActionFactory(library.getActionFactories(), "test");
		assert actionFactory != null;
		assert actionFactory instanceof TestActionFactory;
		assert ((TestActionFactory) actionFactory).getConfiguration() == configuration;
		assert ((TestActionFactory) actionFactory).getLibrary() == library;

		ActionContributionFactory actionContributionFactory =
			getActionContributionFactory(library.getActionContributionFactories(), "test");
		assert actionContributionFactory != null;
		assert actionContributionFactory instanceof TestActionContributionFactory;
		assert ((TestActionContributionFactory) actionContributionFactory).getConfiguration() == configuration;
		assert ((TestActionContributionFactory) actionContributionFactory).getLibrary() == library;

		FunctionFactory functionFactory = getFunctionFactory(library.getFunctionFactories(), "test");
		assert functionFactory != null;
		assert functionFactory instanceof TestFunctionFactory;
		assert ((TestFunctionFactory) functionFactory).getConfiguration() == configuration;
		assert ((TestFunctionFactory) functionFactory).getLibrary() == library;
	}

	private Library createLibrary(boolean addCommonElements, boolean dynamicElementsSupported, String... packageNames) {
		Library library = new DefaultLibrary(addCommonElements, dynamicElementsSupported, packageNames);
		library.initialise(new TestConfiguration(new ConfigurationParameters()));

		return library;
	}

	@Ignored
	public static class DefaultLibrary extends AbstractLibrary {
		private boolean addCommonElements;

		private LibraryInformation information;

		public DefaultLibrary(boolean addCommonElements, boolean dynamicElementsSupported, String... packageNames) {
			super(packageNames);

			this.addCommonElements = addCommonElements;

			String url = "http://aluminumproject.googlecode.com/default";
			String version = "test";

			information = new LibraryInformation(url, "def", version,
				dynamicElementsSupported, dynamicElementsSupported, dynamicElementsSupported);
		}

		public LibraryInformation getInformation() {
			return information;
		}

		@Override
		public ActionFactory getDynamicActionFactory(String name) throws AluminumException {
			ActionFactory dynamicActionFactory;

			if (information.isSupportingDynamicActions()) {
				dynamicActionFactory = new TestActionFactory();
				initialiseLibraryElement(dynamicActionFactory);
			} else {
				dynamicActionFactory = super.getDynamicActionFactory(name);
			}

			return dynamicActionFactory;
		}

		public ActionContributionFactory getDynamicActionContributionFactory(String name) throws AluminumException {
			ActionContributionFactory dynamicActionContributionFactory;

			if (information.isSupportingDynamicActionContributions()) {
				dynamicActionContributionFactory = new TestActionContributionFactory();
				initialiseLibraryElement(dynamicActionContributionFactory);
			} else {
				dynamicActionContributionFactory = super.getDynamicActionContributionFactory(name);
			}

			return dynamicActionContributionFactory;
		}

		public FunctionFactory getDynamicFunctionFactory(String name) throws AluminumException {
			FunctionFactory dynamicFunctionFactory;

			if (information.isSupportingDynamicFunctions()) {
				dynamicFunctionFactory = new TestFunctionFactory();
				initialiseLibraryElement(dynamicFunctionFactory);
			} else {
				dynamicFunctionFactory = super.getDynamicFunctionFactory(name);
			}

			return dynamicFunctionFactory;
		}

		@Override
		protected void addCommonActions() {
			if (addCommonElements) {
				super.addCommonActions();
			}
		}
	}
}