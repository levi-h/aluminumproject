/*
 * Copyright 2010-2011 Levi Hoogenberg
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
package com.googlecode.aluminumproject.context.g11n;

import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.context.ContextException;
import com.googlecode.aluminumproject.context.DefaultContext;

import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("all")
@Test(groups = {"libraries", "libraries-g11n", "slow"})
public class NameBasedResourceBundleProviderTest {
	private Context context;

	@BeforeMethod
	public void createContext() {
		LocaleProvider localeProvider = new ConstantLocaleProvider(Locale.getDefault());
		ResourceBundleProvider resourceBundleProvider = new TestResourceBundleProvider();

		GlobalisationContext globalisationContext =
			new GlobalisationContext(localeProvider, resourceBundleProvider, null, null);

		context = new DefaultContext();
		context.addImplicitObject(GlobalisationContext.GLOBALISATION_CONTEXT_IMPLICIT_OBJECT, globalisationContext);
	}

	public void propertyResourceBundlesShouldBeProvided() {
		ResourceBundle resourceBundle = new NameBasedResourceBundleProvider("aluminum").provide(context);
		assert resourceBundle != null;
		assert Collections.list(resourceBundle.getKeys()).contains("version");
		assert resourceBundle.getString("version").equals("test");
	}

	public static class EuropeanCapitalCities extends ResourceBundle {
		private Map<String, String> capitals;

		public EuropeanCapitalCities() {
			// apologies if your favourite European country is missing from this list
			// you can always send a feature request, of course

			capitals = new LinkedHashMap<String, String>();
			capitals.put("Austria", "Vienna");
			capitals.put("Belgium", "Brussels");
			capitals.put("Bulgaria", "Sofia");
			capitals.put("Czech Republic", "Prague");
			capitals.put("Denmark", "Copenhagen");
			capitals.put("England", "London");
			capitals.put("Finland", "Helsinki");
			capitals.put("France", "Paris");
			capitals.put("Germany", "Berlin");
			capitals.put("Greece", "Athens");
			capitals.put("Hungary", "Budapest");
			capitals.put("Iceland", "Reykjavik");
			capitals.put("Ireland", "Dublin");
			capitals.put("Italy", "Rome");
			capitals.put("Netherlands", "Amsterdam");
			capitals.put("Norway", "Oslo");
			capitals.put("Poland", "Warsaw");
			capitals.put("Portugal", "Lisbon");
			capitals.put("Romania", "Bucharest");
			capitals.put("Russia", "Moscow");
			capitals.put("Scotland", "Edinburgh");
			capitals.put("Serbia", "Belgrade");
			capitals.put("Spain", "Madrid");
			capitals.put("Sweden", "Stockholm");
			capitals.put("Switzerland", "Bern");
			capitals.put("Turkey", "Ankara");
		}

		public Enumeration<String> getKeys() {
			return Collections.enumeration(capitals.keySet());
		}

		protected String handleGetObject(String key) {
			return capitals.get(key);
		}
	}

	public void classResourceBundleShouldBeProvided() {
		ResourceBundle resourceBundle =
			new NameBasedResourceBundleProvider(EuropeanCapitalCities.class.getName()).provide(context);
		assert resourceBundle != null;
		assert Collections.list(resourceBundle.getKeys()).contains("France");
		assert resourceBundle.getString("France").equals("Paris");
	}

	@Test(expectedExceptions = ContextException.class)
	public void requestingResourceBundleWithUnknownBaseNameShouldCauseException() {
		new NameBasedResourceBundleProvider("nonexistent").provide(context);
	}
}