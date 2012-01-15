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
package com.googlecode.aluminumproject.context;

/**
 * The default {@link Context context}. It contains a single scope: the {@link Context#TEMPLATE_SCOPE template scope}.
 * <p>
 * Default contexts support subcontexts.
 */
public class DefaultContext extends AbstractContext {
	/**
	 * Creates a default context.
	 */
	public DefaultContext() {
		super(new DefaultScope(Context.TEMPLATE_SCOPE));
	}

	/**
	 * Creates a default subcontext.
	 *
	 * @param parent the parent context
	 */
	protected DefaultContext(DefaultContext parent) {
		super(parent, new DefaultScope(Context.TEMPLATE_SCOPE));
	}

	@Override
	public DefaultContext createSubcontext() {
		return new DefaultContext(this);
	}
}