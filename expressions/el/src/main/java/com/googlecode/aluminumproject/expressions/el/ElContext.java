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
package com.googlecode.aluminumproject.expressions.el;

import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.context.Context;

import java.util.HashMap;
import java.util.Map;

import javax.el.ArrayELResolver;
import javax.el.BeanELResolver;
import javax.el.CompositeELResolver;
import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.ListELResolver;
import javax.el.MapELResolver;
import javax.el.ResourceBundleELResolver;
import javax.el.ValueExpression;
import javax.el.VariableMapper;

/**
 * An EL context that is based upon a {@link Context context}.
 * <p>
 * The following EL resolvers, which are all read-only, are used:
 * <ul>
 * <li>An {@link ImplicitObjectElResolver implicit object EL resolver};
 * <li>A {@link ContextVariableElResolver context variable EL resolver};
 * <li>An {@link ArrayELResolver array EL resolver};
 * <li>A {@link ListELResolver list EL resolver};
 * <li>A {@link MapELResolver map EL resolver};
 * <li>A {@link ResourceBundleELResolver resource bundle EL resolver}.
 * <li>A {@link BeanELResolver bean EL resolver};
 * </ul>
 *
 * @author levi_h
 */
public class ElContext extends ELContext {
	private Context context;

	private Configuration configuration;

	private ELResolver elResolver;
	private javax.el.FunctionMapper functionMapper;
	private VariableMapper variableMapper;

	/**
	 * Creates an EL context.
	 *
	 * @param context the context to use when finding variables
	 * @param configuration the configuration used
	 */
	public ElContext(Context context, Configuration configuration) {
		this.context = context;

		this.configuration = configuration;

		elResolver = createElResolver();
		functionMapper = createFunctionMapper();
		variableMapper = createVariableMapper();
	}

	private ELResolver createElResolver() {
		CompositeELResolver elResolver = new CompositeELResolver();

		elResolver.add(new ImplicitObjectElResolver());
		elResolver.add(new ContextVariableElResolver());
		elResolver.add(new ArrayELResolver(true));
		elResolver.add(new ListELResolver(true));
		elResolver.add(new MapELResolver(true));
		elResolver.add(new ResourceBundleELResolver());
		elResolver.add(new BeanELResolver(true));

		return elResolver;
	}

	private javax.el.FunctionMapper createFunctionMapper() {
		return new FunctionMapper(context, configuration);
	}

	private VariableMapper createVariableMapper() {
		return new VariableMapper() {
			private Map<String, ValueExpression> expressions = new HashMap<String, ValueExpression>();

			@Override
			public ValueExpression resolveVariable(String name) {
				return expressions.get(name);
			}

			@Override
			public ValueExpression setVariable(String name, ValueExpression expression) {
				return expressions.put(name, expression);
			}
		};
	}

	/**
	 * Returns the context that will be used to resolve variables.
	 *
	 * @return the wrapped context
	 */
	protected Context getContext() {
		return context;
	}

	@Override
	public ELResolver getELResolver() {
		return elResolver;
	}

	@Override
	public javax.el.FunctionMapper getFunctionMapper() {
		return functionMapper;
	}

	@Override
	public VariableMapper getVariableMapper() {
		return variableMapper;
	}
}