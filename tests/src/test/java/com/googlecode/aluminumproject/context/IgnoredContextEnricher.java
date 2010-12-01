/*
 * Copyright 2010 Levi Hoogenberg
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

import com.googlecode.aluminumproject.annotations.Ignored;
import com.googlecode.aluminumproject.configuration.Configuration;

/**
 * A context enricher that should not be used when a package is scanned.
 *
 * @author levi_h
 */
@Ignored
public class IgnoredContextEnricher implements ContextEnricher {
	/**
	 * Creates an ignored context enricher.
	 */
	public IgnoredContextEnricher() {}

	public void initialise(Configuration configuration) {}

	public void disable() {}

	public void beforeTemplate(Context context) {}

	public void afterTemplate(Context context) {}
}