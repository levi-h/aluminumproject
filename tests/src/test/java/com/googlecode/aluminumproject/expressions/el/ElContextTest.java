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
package com.googlecode.aluminumproject.expressions.el;

import com.googlecode.aluminumproject.configuration.ConfigurationParameters;
import com.googlecode.aluminumproject.configuration.TestConfiguration;
import com.googlecode.aluminumproject.context.DefaultContext;
import com.googlecode.aluminumproject.utilities.ReflectionUtilities;

import java.beans.FeatureDescriptor;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.el.ELContext;
import javax.el.ELResolver;

import org.testng.annotations.Test;

@SuppressWarnings("javadoc")
@Test(groups = {"expressions", "expressions-el", "fast"})
public class ElContextTest {
	public void customElResolversWithoutBaseShouldBeConfigurable() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(ElContext.EL_RESOLVER_WITHOUT_BASE_PACKAGES,
			ReflectionUtilities.getPackageName(DictionaryElResolver.class));

		ElContext elContext = new ElContext(new DefaultContext(), new TestConfiguration(parameters));
		ELResolver elResolver = elContext.getELResolver();

		assert elResolver.getValue(elContext, null, "en_nl") instanceof Dictionary;
	}

	@Test(dependsOnMethods = "customElResolversWithoutBaseShouldBeConfigurable")
	public void customElResolversWithBaseShouldBeConfigurable() {
		ConfigurationParameters parameters = new ConfigurationParameters();
		parameters.addParameter(ElContext.EL_RESOLVER_WITHOUT_BASE_PACKAGES,
			ReflectionUtilities.getPackageName(DictionaryElResolver.class));
		parameters.addParameter(ElContext.EL_RESOLVER_WITH_BASE_PACKAGES,
			ReflectionUtilities.getPackageName(TranslationElResolver.class));

		ElContext elContext = new ElContext(new DefaultContext(), new TestConfiguration(parameters));
		ELResolver elResolver = elContext.getELResolver();

		Object translation = elResolver.getValue(elContext, elResolver.getValue(elContext, null, "nl_en"), "boom");
		assert translation instanceof String;
		assert translation.equals("tree");
	}

	public static class DictionaryElResolver extends ELResolver {
		private Map<String, Map<String, Dictionary>> dictionaries;

		public DictionaryElResolver() {
			dictionaries = new HashMap<String, Map<String, Dictionary>>();

			addTranslation("nl", "en", "boom", "tree");
		}

		private void addTranslation(String sourceLanguage, String targetLanguage, String word, String translation) {
			getDictionary(sourceLanguage, targetLanguage).addTranslation(word, translation);
		}

		private Dictionary getDictionary(String sourceLanguage, String targetLanguage) {
			if (!dictionaries.containsKey(sourceLanguage)) {
				dictionaries.put(sourceLanguage, new HashMap<String, Dictionary>());
			}

			if (!dictionaries.get(sourceLanguage).containsKey(targetLanguage)) {
				dictionaries.get(sourceLanguage).put(targetLanguage, new Dictionary());
			}

			return dictionaries.get(sourceLanguage).get(targetLanguage);
		}

		@Override
		public Object getValue(ELContext context, Object base, Object property) {
			Object value = null;

			if ((base == null) && (property instanceof String)) {
				String languages = (String) property;

				if (languages.matches("[a-z]{2}_[a-z]{2}")) {
					value = getDictionary(languages.substring(0, 2), languages.substring(3, 5));

					context.setPropertyResolved(true);
				}
			}

			return value;
		}

		@Override
		public Class<?> getType(ELContext context, Object base, Object property) {
			return null;
		}

		@Override
		public void setValue(ELContext context, Object base, Object property, Object value) {}

		@Override
		public boolean isReadOnly(ELContext context, Object base, Object property) {
			return true;
		}

		@Override
		public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base) {
			return null;
		}

		@Override
		public Class<?> getCommonPropertyType(ELContext context, Object base) {
			return null;
		}
	}

	public static class TranslationElResolver extends ELResolver {
		@Override
		public Object getValue(ELContext context, Object base, Object property) {
			Object value;

			if (handles(base, property)) {
				value = ((Dictionary) base).translate((String) property);

				context.setPropertyResolved(true);
			} else {
				value = null;
			}

			return value;
		}

		@Override
		public Class<?> getType(ELContext context, Object base, Object property) {
			Class<?> type;

			if (handles(base, property)) {
				type = String.class;

				context.setPropertyResolved(true);
			} else {
				type = null;
			}

			return type;
		}

		@Override
		public void setValue(ELContext context, Object base, Object property, Object value) {}

		@Override
		public boolean isReadOnly(ELContext context, Object base, Object property) {
			boolean readOnly;

			if (handles(base, property)) {
				readOnly = true;

				context.setPropertyResolved(true);
			} else {
				readOnly = false;
			}

			return readOnly;
		}

		@Override
		public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base) {
			return null;
		}

		@Override
		public Class<?> getCommonPropertyType(ELContext context, Object base) {
			return handles(base) ? String.class : null;
		}

		private boolean handles(Object base) {
			return base instanceof Dictionary;
		}

		private boolean handles(Object base, Object property) {
			return (base instanceof Dictionary) && (property instanceof String);
		}
	}

	public static class Dictionary {
		private Map<String, String> translations;

		public Dictionary() {
			translations = new HashMap<String, String>();
		}

		public void addTranslation(String word, String translation) {
			translations.put(word, translation);
		}

		public String translate(String word) {
			return translations.get(word);
		}
	}
}