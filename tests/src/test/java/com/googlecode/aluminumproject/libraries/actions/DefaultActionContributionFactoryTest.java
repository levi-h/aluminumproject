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

import com.googlecode.aluminumproject.configuration.ConfigurationParameters;
import com.googlecode.aluminumproject.configuration.TestConfiguration;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("all")
@Test(groups = {"core", "fast"})
public class DefaultActionContributionFactoryTest {
	private ActionContributionFactory unannotatedActionContributionFactory;
	private ActionContributionFactory annotatedActionContributionFactory;

	@BeforeMethod
	public void createActionContributionFactories() {
		TestConfiguration configuration = new TestConfiguration(new ConfigurationParameters());

		unannotatedActionContributionFactory = new DefaultActionContributionFactory(TestActionContribution.class);
		unannotatedActionContributionFactory.initialise(configuration);

		annotatedActionContributionFactory = new DefaultActionContributionFactory(AnnotatedActionContribution.class);
		annotatedActionContributionFactory.initialise(configuration);
	}

	public void unannotatedActionContributionShouldResultInHumanisedClassNameAsName() {
		String name = unannotatedActionContributionFactory.getInformation().getName();
		assert name != null;
		assert name.equals("test action contribution");
	}

	public void annotatedActionContributionShouldResultInAnnotationAttributeAsName() {
		String name = annotatedActionContributionFactory.getInformation().getName();
		assert name != null;
		assert name.equals("annotated test");
	}

	public void unannotatedActionContributionShouldResultInObjectAsParameterType() {
		assert unannotatedActionContributionFactory.getInformation().getParameterType() == Object.class;
	}

	public void annotatedActionContributionShouldResultInAnnotationAttributeAsParameterType() {
		assert annotatedActionContributionFactory.getInformation().getParameterType() == String.class;
	}

	public void actionContributionShouldBeCreatable() {
		assert unannotatedActionContributionFactory.create() instanceof TestActionContribution;
		assert annotatedActionContributionFactory.create() instanceof AnnotatedActionContribution;
	}
}