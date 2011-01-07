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
package com.googlecode.aluminumproject.context;

import java.util.Map;
import java.util.Set;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("all")
@Test(groups = {"core", "fast"})
public class AbstractContextTest {
	private AbstractContext context;

	private static class TestContext extends AbstractContext {
		public TestContext() {
			super(new DefaultScope(Context.TEMPLATE_SCOPE), new DefaultScope(BLOCK_SCOPE));
		}

		private TestContext(TestContext parent) {
			super(parent, new DefaultScope(Context.TEMPLATE_SCOPE));
		}

		@Override
		public TestContext createSubcontext() {
			return new TestContext(this);
		}

		private static final String BLOCK_SCOPE = "block";
	}

	@BeforeMethod
	public void createContext() {
		context = new TestContext();
	}

	@Test(expectedExceptions = ContextException.class)
	public void creatingAbstractContextWithoutTemplateScopeShouldCauseException() {
		new AbstractContext() {};
	}

	@Test(expectedExceptions = ContextException.class)
	public void creatingAbstractContextWithDuplicateScopeNamesShouldCauseException() {
		new AbstractContext(new DefaultScope(Context.TEMPLATE_SCOPE), new DefaultScope(Context.TEMPLATE_SCOPE)) {};
	}

	public void scopeNamesShouldIncludeAllScopes() {
		Set<String> scopeNames = context.getScopeNames();
		assert scopeNames != null;
		assert scopeNames.size() == 2;
		assert scopeNames.contains(Context.TEMPLATE_SCOPE);
		assert scopeNames.contains(TestContext.BLOCK_SCOPE);
	}

	public static final String NEW_SCOPE = "new";

	@Test(dependsOnMethods = "scopeNamesShouldIncludeAllScopes")
	public void scopeNamesShouldIncludeAddedScope() {
		context.addScope(NEW_SCOPE, true);

		assert context.getScopeNames().contains(NEW_SCOPE);
	}

	@Test(expectedExceptions = ContextException.class)
	public void addingScopeWithUniqueNameShouldCauseExceptionForExistingScope() {
		context.addScope(Context.TEMPLATE_SCOPE, true);
	}

	public void addingScopeWithoutUniqueNameShouldBePossibleForExistingScope() {
		context.addScope(Context.TEMPLATE_SCOPE, false);
	}

	@Test(dependsOnMethods = "addingScopeWithoutUniqueNameShouldBePossibleForExistingScope")
	public void nameOfExistingScopeShouldEqualRequestedName() {
		assert !context.addScope(NEW_SCOPE, false).equals(context.addScope(NEW_SCOPE, false));
	}

	@Test(expectedExceptions = ContextException.class)
	public void removingScopeThatWasNeverAddedShouldCauseException() {
		context.removeScope(NEW_SCOPE);
	}

	@Test(expectedExceptions = ContextException.class)
	public void removingTemplateScopeShouldCauseException() {
		context.removeScope(Context.TEMPLATE_SCOPE);
	}

	@Test(dependsOnMethods = "scopeNamesShouldIncludeAddedScope")
	public void scopeNamesShouldNotIncludeRemovedScope() {
		context.addScope(NEW_SCOPE, true);
		context.removeScope(NEW_SCOPE);

		assert !context.getScopeNames().contains(NEW_SCOPE);
	}

	@Test(expectedExceptions = ContextException.class)
	public void gettingVariableNamesFromNonexistentScopeShouldCauseException() {
		context.getVariableNames("nonexistent");
	}

	public void variableNamesShouldIncludeNewVariables() {
		context.setVariable(Context.TEMPLATE_SCOPE, "name", "value");

		Set<String> variableNames = context.getVariableNames(Context.TEMPLATE_SCOPE);
		assert variableNames != null;
		assert variableNames.size() == 1;
		assert variableNames.contains("name");
	}

	public void variableNamesShouldNotIncludeRemovedVariable() {
		context.setVariable(Context.TEMPLATE_SCOPE, "name", "value");
		context.removeVariable(Context.TEMPLATE_SCOPE, "name");

		Set<String> variableNames = context.getVariableNames(Context.TEMPLATE_SCOPE);
		assert variableNames != null;
		assert variableNames.isEmpty();
	}

	@Test(expectedExceptions = ContextException.class)
	public void gettingVariableFromNonexistentScopeShouldCauseException() {
		context.getVariable("nonexistent", "name");
	}

	public void newVariableShouldBeRetrievable() {
		context.setVariable(Context.TEMPLATE_SCOPE, "name", "value");

		assert context.getVariable(Context.TEMPLATE_SCOPE, "name").equals("value");
	}

	@Test(dependsOnMethods = "newVariableShouldBeRetrievable", expectedExceptions = ContextException.class)
	public void removedVariableShouldNotBeRetrievable() {
		context.setVariable(Context.TEMPLATE_SCOPE, "name", "value");
		context.removeVariable(Context.TEMPLATE_SCOPE, "name");

		context.getVariable(Context.TEMPLATE_SCOPE, "name");
	}

	@Test(expectedExceptions = ContextException.class)
	public void settingVariableInNonexistentScopeShouldCauseException() {
		context.setVariable("nonexistent", "name", "value");
	}

	@Test(expectedExceptions = ContextException.class)
	public void removingVariableFromNonexistentScopeShouldCauseException() {
		context.removeVariable("nonexistent", "name");
	}

	@Test(expectedExceptions = ContextException.class)
	public void removingNonexistentVariableShouldCauseException() {
		context.removeVariable(Context.TEMPLATE_SCOPE, "nonexistent");
	}

	@Test(dependsOnMethods = {"scopeNamesShouldIncludeAddedScope", "newVariableShouldBeRetrievable"})
	public void settingVariableWithoutScopeShouldSetItInInnermostScope() {
		context.addScope(NEW_SCOPE, true);

		context.setVariable("name", "value");

		assert context.getVariable(NEW_SCOPE, "name").equals("value");
	}

	@Test(dependsOnMethods = "settingVariableWithoutScopeShouldSetItInInnermostScope",
		expectedExceptions = ContextException.class)
	public void removingVariableWithoutScopeShouldRemoveItFromInnermostScope() {
		context.addScope(NEW_SCOPE, true);

		context.setVariable("name", "value");
		context.removeVariable("name");

		context.getVariable(NEW_SCOPE, "name");
	}

	@Test(dependsOnMethods = {"scopeNamesShouldIncludeAddedScope", "removingNonexistentVariableShouldCauseException"},
		expectedExceptions = ContextException.class)
	public void removingNonexistentVariableWithoutScopeShouldCauseException() {
		context.addScope(NEW_SCOPE, true);

		context.removeVariable("nonexistent");
	}

	@Test(expectedExceptions = ContextException.class)
	public void findingNonexistentVariableShouldCauseException() {
		context.findVariable("nonexistent");
	}

	@Test(dependsOnMethods = "newVariableShouldBeRetrievable")
	public void variableShouldBeFindableInScopeWithHighestPriority() {
		context.setVariable(TestContext.BLOCK_SCOPE, "name", "value");

		assert context.findVariable("name").equals("value");
	}

	@Test(dependsOnMethods = "newVariableShouldBeRetrievable")
	public void variableShouldBeFindableInDefaultScope() {
		context.setVariable(Context.TEMPLATE_SCOPE, "name", "value");

		assert context.findVariable("name").equals("value");
	}

	@Test(dependsOnMethods = {
		"variableShouldBeFindableInDefaultScope",
		"variableShouldBeFindableInScopeWithHighestPriority"
	})
	public void findingVariableShouldPreferScopeWithHigherPriority() {
		context.setVariable(Context.TEMPLATE_SCOPE, "priority", "low");
		context.setVariable(TestContext.BLOCK_SCOPE, "priority", "high");

		assert context.findVariable("priority").equals("high");
	}

	@Test(dependsOnMethods = {
		"variableShouldBeFindableInDefaultScope",
		"subcontextShouldHaveParent"
	})
	public void variableShouldBeFindableInParentContext() {
		context.setVariable(Context.TEMPLATE_SCOPE, "name", "value");

		assert context.createSubcontext().findVariable("name").equals("value");
	}

	@Test(dependsOnMethods = "variableShouldBeFindableInParentContext")
	public void findingVariableShouldPreferNearestAncestor() {
		AbstractContext subcontext = context.createSubcontext();

		context.setVariable(Context.TEMPLATE_SCOPE, "name", "context");
		subcontext.setVariable(Context.TEMPLATE_SCOPE, "name", "subcontext");

		assert subcontext.createSubcontext().findVariable("name").equals("subcontext");
	}

	@Test(dependsOnMethods = {
		"scopeNamesShouldIncludeAddedScope",
		"findingVariableShouldPreferScopeWithHigherPriority"
	})
	public void newScopeShouldHaveHighestPriority() {
		context.addScope(NEW_SCOPE, true);

		context.setVariable(Context.TEMPLATE_SCOPE, "priority", "low");
		context.setVariable(NEW_SCOPE, "priority", "high");

		assert context.findVariable("priority").equals("high");
	}

	public void aluminumImplicitObjectShouldBeAvailable() {
		Set<String> names = context.getImplicitObjectNames();
		assert names != null;
		assert names.contains(Context.ALUMINUM_IMPLICIT_OBJECT);
		assert context.getImplicitObject(Context.ALUMINUM_IMPLICIT_OBJECT) instanceof Map;
	}

	public void scopesShouldBeAvailableAsImplicitObjects() {
		Set<String> names = context.getImplicitObjectNames();
		assert names != null;
		assert names.contains("templateScope");
		assert names.contains("blockScope");
	}

	@Test(dependsOnMethods = {"scopesShouldBeAvailableAsImplicitObjects", "scopeNamesShouldIncludeAddedScope"})
	public void newScopesShouldBeAvailableAsImplicitObjects() {
		assert !context.getImplicitObjectNames().contains("newScope");

		context.addScope(NEW_SCOPE, true);

		assert context.getImplicitObjectNames().contains("newScope");
	}

	@Test(dependsOnMethods = "scopesShouldBeAvailableAsImplicitObjects")
	public void implicitObjectsShouldBeRetrievable() {
		assert context.getImplicitObject("templateScope") instanceof ScopeMap;
	}

	@Test(expectedExceptions = ContextException.class)
	public void retrievingNonexistentImplicitObjectShouldCauseException() {
		context.getImplicitObject("nonexistent");
	}

	@Test(dependsOnMethods = "implicitObjectsShouldBeRetrievable")
	public void implicitObjectsShouldBeAddable() {
		assert !context.getImplicitObjectNames().contains("lock");

		Object lock = new Object();
		context.addImplicitObject("lock", lock);

		assert context.getImplicitObject("lock") == lock;
	}

	@Test(dependsOnMethods = "implicitObjectsShouldBeAddable", expectedExceptions = ContextException.class)
	public void addingImplicitObjectWithExistingNameShouldCauseException() {
		context.addImplicitObject("lock", new Object());
		context.addImplicitObject("lock", new Object());
	}

	@Test(expectedExceptions = ContextException.class)
	public void addingImplicitObjectWithScopeNameSuffixShouldCauseException() {
		context.addImplicitObject("defaultScope", new DefaultScope("default"));
	}

	@Test(dependsOnMethods = "implicitObjectsShouldBeRetrievable")
	public void implicitObjectsShouldBeRemovable() {
		context.removeImplicitObject("templateScope");

		assert !context.getImplicitObjectNames().contains("templateScope");
	}

	@Test(expectedExceptions = ContextException.class)
	public void removingNonexistentImplicitObjectShouldCauseException() {
		context.removeImplicitObject("nonexistent");
	}

	public void normalContextShouldNotHaveParent() {
		assert context.getParent() == null;
	}

	public void subcontextShouldBeCreatable() {
		assert context.createSubcontext() != null;
	}

	@Test(dependsOnMethods = "subcontextShouldBeCreatable")
	public void subcontextShouldHaveParent() {
		assert context.createSubcontext().getParent() == context;
	}

	@Test(dependsOnMethods = {"scopeNamesShouldIncludeAllScopes", "subcontextShouldBeCreatable"})
	public void subcontextShouldNotShareScopesWithParent() {
		assert !context.createSubcontext().getScopeNames().contains(TestContext.BLOCK_SCOPE);
	}

	@Test(dependsOnMethods = {"implicitObjectsShouldBeAddable", "subcontextShouldBeCreatable"})
	public void subcontextShouldNotShareImplicitObjectsWithParent() {
		AbstractContext subcontext = context.createSubcontext();

		context.addImplicitObject("lock", new Object());

		assert !subcontext.getImplicitObjectNames().contains("lock");
	}
}