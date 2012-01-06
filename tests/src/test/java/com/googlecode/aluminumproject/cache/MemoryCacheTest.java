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
package com.googlecode.aluminumproject.cache;

import com.googlecode.aluminumproject.configuration.ConfigurationParameters;
import com.googlecode.aluminumproject.configuration.TestConfiguration;
import com.googlecode.aluminumproject.templates.Template;
import com.googlecode.aluminumproject.templates.TemplateBuilder;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
@Test(groups = {"core", "fast"})
public class MemoryCacheTest {
	private Cache cache;

	@BeforeMethod
	public void createCache() {
		cache = new MemoryCache();
		cache.initialise(new TestConfiguration(new ConfigurationParameters()));
	}

	public void storedTemplateShouldBeFindable() {
		Template template = new TemplateBuilder().build();
		Cache.Key key = new Cache.Key("test", null);

		cache.storeTemplate(key, template);

		assert cache.findTemplate(key) == template;
	}

	@Test(dependsOnMethods = "storedTemplateShouldBeFindable")
	public void storingTemplateUnderExistingKeyShouldReplaceTemplate() {
		Cache.Key key = new Cache.Key("test", null);

		Template initialTemplate = new TemplateBuilder().build();
		cache.storeTemplate(key, initialTemplate);

		Template replacement = new TemplateBuilder().build();
		cache.storeTemplate(key, replacement);

		assert cache.findTemplate(key) == replacement;
	}

	public void findingUnknownTemplateShouldResultInNull() {
		assert cache.findTemplate(new Cache.Key("unknown", null)) == null;
	}

	public void disablingCacheShouldRemoveStoredTemplates() {
		Cache.Key key = new Cache.Key("test", null);

		cache.storeTemplate(key, new TemplateBuilder().build());
		cache.disable();

		assert cache.findTemplate(key) == null;
	}
}