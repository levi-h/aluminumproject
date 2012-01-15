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

import com.googlecode.aluminumproject.AluminumException;
import com.googlecode.aluminumproject.annotations.Ignored;
import com.googlecode.aluminumproject.configuration.Configuration;
import com.googlecode.aluminumproject.context.Context;
import com.googlecode.aluminumproject.finders.TypeFinder.TypeFilter;

import java.util.HashMap;
import java.util.List;
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
 * <li>A {@link BeanELResolver bean EL resolver}.
 * </ul>
 * <p>
 * It is possible to add custom EL resolvers by supplying configuration parameters that are named {@value
 * #EL_RESOLVER_WITHOUT_BASE_PACKAGES} or {@value #EL_RESOLVER_WITH_BASE_PACKAGES}: the EL resolvers that are
 * encountered in these comma-separated lists of packages and are not annotated with {@link Ignored &#64;Ignored} are
 * added at the head and tail of the resolver list, respectively.
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
	 * @throws AluminumException when the custom EL resolvers can't be determined
	 */
	public ElContext(Context context, Configuration configuration) throws AluminumException {
		this.context = context;

		this.configuration = configuration;

		elResolver = createElResolver();
		functionMapper = createFunctionMapper();
		variableMapper = createVariableMapper();

		putContext(getClass(), this);
	}

	private ELResolver createElResolver() throws AluminumException {
		CompositeELResolver elResolver = new CompositeELResolver();

		addCustomElResolvers(elResolver, EL_RESOLVER_WITHOUT_BASE_PACKAGES);
		elResolver.add(new ImplicitObjectElResolver());
		elResolver.add(new ContextVariableElResolver());
		elResolver.add(new ArrayELResolver(true));
		elResolver.add(new ListELResolver(true));
		elResolver.add(new MapELResolver(true));
		elResolver.add(new ResourceBundleELResolver());
		elResolver.add(new BeanELResolver(true));
		addCustomElResolvers(elResolver, EL_RESOLVER_WITH_BASE_PACKAGES);

		return elResolver;
	}

	private void addCustomElResolvers(
			CompositeELResolver elResolver, String elResolverPackages) throws AluminumException {
		for (String elResolverPackage: configuration.getParameters().getValues(elResolverPackages)) {
			List<Class<?>> elResolverClasses = configuration.getTypeFinder().find(new TypeFilter() {
				public boolean accepts(Class<?> type) {
					return ELResolver.class.isAssignableFrom(type) && !type.isAnnotationPresent(Ignored.class);
				}
			}, elResolverPackage);

			for (Class<?> elResolverClass: elResolverClasses) {
				elResolver.add(configuration.getConfigurationElementFactory().instantiate(
					elResolverClass.getName(), ELResolver.class));
			}
		}
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
	 * Returns the context that will be used to resolve variables and implicit objects.
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

	/**
	 * The name of the configuration parameter that contains a comma-separated list of packages that should be scanned
	 * for custom EL resolvers that provide a base object.
	 */
	public static final String EL_RESOLVER_WITHOUT_BASE_PACKAGES = "expressions.el.resolvers_without_base.packages";

	/**
	 * The name of the configuration parameter that contains a comma-separated list of packages that should be scanned
	 * for custom EL resolvers that operate on a base object.
	 */
	public static final String EL_RESOLVER_WITH_BASE_PACKAGES = "expressions.el.resolvers_with_base.packages";
}